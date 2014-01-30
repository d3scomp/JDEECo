package cz.cuni.mff.d3s.deeco.publish;

import static cz.cuni.mff.d3s.deeco.publish.Serializer.deserialize;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeData;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeDataReceiver;
import cz.cuni.mff.d3s.deeco.logging.Log;

public class PacketReceiver {

	private final int packetSize;
	private final Map<Integer, Message> messages;
	
	private KnowledgeDataReceiver knowledgeDataReceiver;

	public PacketReceiver(int packetSize) {
		this.packetSize = packetSize;
		this.messages = new HashMap<Integer, Message>();
	}
	
	

	public void setKnowledgeDataReceiver(KnowledgeDataReceiver knowledgeDataReceiver) {
		this.knowledgeDataReceiver = knowledgeDataReceiver;
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
		if (msg.isComplete() && knowledgeDataReceiver != null) {
			List<? extends KnowledgeData> kd = msg.getKnowledgeDataList();
			if (kd != null)
				knowledgeDataReceiver.receive(kd);
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

		public List<? extends KnowledgeData> getKnowledgeDataList() {
			try {
				if (isComplete()) {
					return (List<? extends KnowledgeData>) deserialize(data);
				}
			} catch (IOException | ClassNotFoundException e) {
				Log.i("Error while deserializing data - Corrupted message.");
			}
			return null;
		}

		private void initialize(int messageSize) {
			this.messageSize = messageSize;
			this.data = new byte[messageSize];
		}

	}

}
