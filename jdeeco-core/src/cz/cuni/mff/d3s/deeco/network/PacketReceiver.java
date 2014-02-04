package cz.cuni.mff.d3s.deeco.network;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeData;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeDataReceiver;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;

public class PacketReceiver {

	private final static int MESSAGE_WIPE_PERIOD = 1500;

	private final int packetSize;
	private final Map<Integer, Message> messages;

	private KnowledgeDataReceiver knowledgeDataReceiver;
	private CurrentTimeProvider timeProvider;
	private long lastMessagesWipe = 0;

	public PacketReceiver(int packetSize) {
		this.packetSize = packetSize;
		this.messages = new HashMap<Integer, Message>();
	}

	public void setKnowledgeDataReceiver(
			KnowledgeDataReceiver knowledgeDataReceiver) {
		this.knowledgeDataReceiver = knowledgeDataReceiver;
	}
	
	public void setCurrentTimeProvider(CurrentTimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}

	public void packetReceived(byte[] packet) {
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
		if (msg.isComplete() && knowledgeDataReceiver != null) {
			Log.i(String.format("R: " + "(" + messageId + ")"
					+ Arrays.toString(msg.data)));
			messages.remove(messageId);
			List<? extends KnowledgeData> kd = msg.getKnowledgeDataList();
			if (kd != null)
				knowledgeDataReceiver.receive(kd);
		}
		clearCachedMessagesIfNecessary();
	}

	//TODO Possibly this should be scheduled as a task in the Scheduler.
	private void clearCachedMessagesIfNecessary() {
		if (timeProvider.getCurrentTime() - lastMessagesWipe >= MESSAGE_WIPE_PERIOD) {
			Message message;
			Iterator<Entry<Integer, Message>> it = messages.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, Message> entry = it.next();				
				message = entry.getValue();
				if (message != null && message.isStale()) {
					it.remove();
				}
			}
			lastMessagesWipe = timeProvider.getCurrentTime();
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

		private final static int MAX_MESSAGE_TIME = 2000;

		private Map<Integer, Object> cache = new HashMap<>();

		private byte[] data;
		private int remainingBytes = 0;
		private boolean isInitialized = false;
		private long creationTime;

		private int messageId = 0;

		public Message(int messageId) {
			this.messageId = messageId;
			this.creationTime = timeProvider.getCurrentTime();
		}

		public boolean isStale() {
			return timeProvider.getCurrentTime() - creationTime > MAX_MESSAGE_TIME;
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

		public List<? extends KnowledgeData> getKnowledgeDataList() {
			try {
				if (isComplete()) {
					List<? extends KnowledgeData> result = (List<? extends KnowledgeData>) Serializer.deserialize(data);
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
