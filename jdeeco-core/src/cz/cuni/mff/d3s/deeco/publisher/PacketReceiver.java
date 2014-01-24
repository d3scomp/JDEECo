package cz.cuni.mff.d3s.deeco.publisher;

import static cz.cuni.mff.d3s.deeco.publisher.Serializer.deserialize;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.logging.Log;

public class PacketReceiver {

	private final int packetSize;
	private final Map<Integer, Message> messages;
	private final IncomingKnowledgeListener incomingKnowledgeListener;

	public PacketReceiver(int packetSize, IncomingKnowledgeListener incomingKnowledgeListener) {
		this.packetSize = packetSize;
		this.messages = new HashMap<Integer, Message>();
		this.incomingKnowledgeListener = incomingKnowledgeListener;
	}

	public void packetReceived(byte[] packet) {
		int messageId;
		Message msg;
		if (isInitialMessage(packet)) {
			messageId = getInitialMessageId(packet);
			int messageSize = getMessageSize(packet);
			if (!messages.containsKey(messageId)) {
				msg = new Message(messageSize);
				messages.put(messageId, msg);
			} else {
				msg = messages.get(messageId);
				msg.setPacketCount(messageSize);
			}
		} else {
			messageId = getMessageId(packet);
			int seqNumber = getMessageSeqNumber(packet);
			if (messages.containsKey(messageId)) {
				msg = messages.get(messageId);
				msg.setData(seqNumber, getData(packet));
			} else {
				msg = new Message(seqNumber, getData(packet));
				messages.put(messageId, msg);
			}
		}
		if (msg.isComplete()) {
			incomingKnowledgeListener.knowledgeReceived(msg.getKnowledgeData());
		}
	}
	
	//-----------Helper methods-----------
	
	private int getMessageId(byte [] packet) {
		return ByteBuffer.wrap(Arrays.copyOfRange(packet, 0, 4)).getInt();
	}
	
	private int getInitialMessageId(byte [] packet) {
		return ByteBuffer.wrap(Arrays.copyOfRange(packet, 4, 8)).getInt();
	}
	
	private int getMessageSize(byte [] packet) {
		return ByteBuffer.wrap(Arrays.copyOfRange(packet, 8, 12)).getInt();
	}
	
	private int getMessageSeqNumber(byte [] packet) {
		return getInitialMessageId(packet);
	}
	
	private boolean isInitialMessage(byte [] packet) {
		return getMessageId(packet) == Integer.MIN_VALUE;
	}
	
	private byte [] getData(byte [] packet) {
		return Arrays.copyOfRange(packet, 8, packet.length);
	}

	
	//---------Helper Class--------------
	
	private class Message {

		private Map<Integer, Object> cache;

		private byte[] data;
		private int messageSize;

		public Message(int messageSize) {
			initialize(messageSize);
		}

		public Message(int seqNumber, byte [] data) {
			this.messageSize = -1;
			this.cache = new HashMap<>();
			setData(seqNumber, data);
		}

		public void setPacketCount(int packetCount) {
			if (this.messageSize == -1) {
				initialize(packetCount);
				for (Integer key : cache.keySet())
					setData(key, (byte[]) cache.get(key));
			}
		}

		public void setData(int seqNumber, byte[] data) {
			if (messageSize != -1) {
				for (int i = seqNumber * packetSize; i < data.length; i++)
					this.data[i] = data[i];
				this.messageSize -= data.length;
			} else {
				cache.put(seqNumber, data);
			}
		}

		public boolean isComplete() {
			return messageSize == 0;
		}

		public KnowledgeData getKnowledgeData() {
			try {
				if (isComplete()) {
					return (KnowledgeData) deserialize(data);
				}
			} catch (IOException | ClassNotFoundException e) {
				Log.e("Error while deserializing data");
			}
			return null;
		}

		private void initialize(int messageSize) {
			this.messageSize = messageSize;
			this.data = new byte[messageSize];
		}

	}

}
