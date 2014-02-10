package cz.cuni.mff.d3s.deeco.network;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.DeecoProperties;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;

/**
 * This class enables receiving {@link KnowledgeData} that comes from the
 * network as a byte array fragmented into multiple packets.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 * @see Serializer
 * @see PacketSender
 */
public class PacketReceiver {

	public final static int DEFAULT_MESSAGE_WIPE_PERIOD = 500;
	
	public final static int DEFAULT_MAX_MESSAGE_TIME = 2000;


	private final String host;
	
	private final int packetSize;
	private final Map<Integer, Message> messages;

	private KnowledgeDataReceiver knowledgeDataReceiver;
	private CurrentTimeProvider timeProvider;
	private long lastMessagesWipe = 0;
	
	private final int maxMessageTime;
	private final int messageWipePeriod;

	public PacketReceiver(String host, int packetSize) {
		this.packetSize = packetSize;
		this.messages = new HashMap<Integer, Message>();
		this.host = host;
		
		maxMessageTime = Integer.getInteger(DeecoProperties.MESSAGE_CACHE_DEADLINE, DEFAULT_MAX_MESSAGE_TIME);
		messageWipePeriod = Integer.getInteger(DeecoProperties.MESSAGE_CACHE_WIPE_PERIOD, DEFAULT_MESSAGE_WIPE_PERIOD);
		
		Log.d(String.format("PacketReceiver at %s uses packetSize=%d, maxMessageTime=%d, messageWipePeriod=%d", 
				host, packetSize, maxMessageTime, messageWipePeriod));
		
	}
	
	public PacketReceiver(String host) {
		this(host, Integer.getInteger(DeecoProperties.PACKET_SIZE, PacketSender.DEFAULT_PACKET_SIZE));
	}

	public void setKnowledgeDataReceiver(
			KnowledgeDataReceiver knowledgeDataReceiver) {
		this.knowledgeDataReceiver = knowledgeDataReceiver;
	}
	
	public void setCurrentTimeProvider(CurrentTimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}

	public void packetReceived(byte[] packet, double rssi) {
		Message msg;
		int messageId = getMessageId(packet);

		if (messages.containsKey(messageId)) {
			msg = messages.get(messageId);
		} else {
			msg = new Message(messageId);
			messages.put(messageId, msg);
		}
		if (isInitialPacket(packet)) {
			int messageSize = getMessageSize(packet);
			msg.initialize(messageSize);
		} else {
			int seqNumber = getPacketSeqNumber(packet);
			msg.setData(seqNumber, getPacketData(packet));
		}
		msg.setLastRSSI(rssi);
		if (msg.isComplete() && knowledgeDataReceiver != null) {
			//Log.i(String.format("R: " + "(" + messageId + ")"
			//		+ Arrays.toString(msg.data)));
			Log.d(String.format("PacketReceiver: Message completed at %s with messageid %d", host, messageId));
			
			messages.remove(messageId);
			List<? extends KnowledgeData> kd = msg.getKnowledgeDataList();
			if (kd != null)
				knowledgeDataReceiver.receive(kd);
		}
		clearCachedMessagesIfNecessary();
	}

	//TODO Possibly this should be scheduled as a task in the Scheduler.
	private void clearCachedMessagesIfNecessary() {
		if (timeProvider.getCurrentTime() - lastMessagesWipe >= messageWipePeriod) {
			int origCnt = messages.size();

			Set<Integer> droppedIds = new HashSet<>();
			
			Message message;
			Iterator<Entry<Integer, Message>> it = messages.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, Message> entry = it.next();				
				message = entry.getValue();
				if (message != null && message.isStale()) {
					droppedIds.add(entry.getKey());
					it.remove();					
				}
			}
			lastMessagesWipe = timeProvider.getCurrentTime();
			int currentCnt = messages.size();
			Log.i(String.format("Message wipe at %s removed %d cached packets", host, origCnt - currentCnt));
			Log.d(String.format("PacketReceiver: Message wipe %s dropped messageids %s", host, Arrays.deepToString(droppedIds.toArray())));
		}
		
	}

	// -----------Helper methods-----------

	private int getMessageId(byte[] packet) {
		return ByteBuffer.wrap(Arrays.copyOfRange(packet, 0, 4)).getInt();
	}

	private int getMessageSize(byte[] packet) {
		if (isInitialPacket(packet))
			return ByteBuffer.wrap(Arrays.copyOfRange(packet, 8, 12)).getInt();
		else
			return -1;
	}

	private int getPacketSeqNumber(byte[] packet) {
		return ByteBuffer.wrap(Arrays.copyOfRange(packet, 4, 8)).getInt();
	}

	private boolean isInitialPacket(byte[] packet) {
		return getPacketSeqNumber(packet) == Integer.MIN_VALUE;
	}

	private byte[] getPacketData(byte[] packet) {
		return Arrays.copyOfRange(packet, 8, packet.length);
	}

	// ---------Helper Class--------------

	private class Message {


		private Map<Integer, Object> cache = new HashMap<>();

		private byte[] data;
		private int remainingBytes = 0;
		private boolean isInitialized = false;
		private long creationTime;
		
		private double lastRSSI;
		private int messageId = 0;

		public Message(int messageId) {
			this.messageId = messageId;
			this.creationTime = timeProvider.getCurrentTime();
		}

		public boolean isStale() {
			return timeProvider.getCurrentTime() - creationTime > maxMessageTime;
		}
		
		public void setLastRSSI(double rssi) {
			lastRSSI = rssi;
		}

		public void setData(int seqNumber, byte[] data) {
			if (isInitialized) {
				int cnt = Math.min(Math.max(remainingBytes, 0), data.length);
				remainingBytes -= data.length;
				if (remainingBytes < 0) {
					Log.e(String
							.format("Message %d received more data than expected (by %d bytes).",
									messageId, -remainingBytes));
				}
				System.arraycopy(data, 0, this.data, seqNumber * packetSize, cnt);
			} else {
				cache.put(seqNumber, data);
			}
		}

		public boolean isComplete() {
			return isInitialized && (remainingBytes == 0);
		}

		@SuppressWarnings("unchecked")
		public List<? extends KnowledgeData> getKnowledgeDataList() {
			try {
				if (isComplete()) {
					List<? extends KnowledgeData> result = (List<? extends KnowledgeData>) Serializer.deserialize(data);
					for (KnowledgeData kd : result)
						kd.getMetaData().rssi = lastRSSI;
					return result;
				}
			} catch (IOException | ClassNotFoundException e) {
				Log.e("Error while deserializing data - Corrupted message.", e);
			}
			return null;
		}

		private void initialize(int messageSize) {
			if (!isInitialized) {
				this.remainingBytes = messageSize;
				this.data = new byte[messageSize];
				for (Integer key : cache.keySet()) {
					setData(key, (byte[]) cache.get(key));
				}
				cache.clear();
			} else {
				throw new RuntimeException("Cannot call initialize twice.");
			}
			isInitialized = true;
		}

	}

}
