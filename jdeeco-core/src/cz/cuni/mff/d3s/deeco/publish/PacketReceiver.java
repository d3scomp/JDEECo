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
			//System.out.println("R: " + "(" + messageId + ")" + Arrays.toString(msg.data));
			
			messages.remove(messageId);
			List<? extends KnowledgeData> kd = msg.getKnowledgeDataList();
			if (kd != null)
				knowledgeDataReceiver.receive(kd);
		}
	}
	
	//-----------Helper methods-----------
	
	private int getMessageId(byte [] packet) {			
		return ByteBuffer.wrap(Arrays.copyOfRange(packet, 0, 4)).getInt();	
	}	
	
	private int getMessageSize(byte [] packet) {
		if (isInitialPacket(packet))
			return ByteBuffer.wrap(Arrays.copyOfRange(packet, 8, 12)).getInt();
		else
			return -1;
	}
	
	private int getPacketSeqNumber(byte [] packet) {
		return ByteBuffer.wrap(Arrays.copyOfRange(packet, 4, 8)).getInt();
	}
	
	private boolean isInitialPacket(byte [] packet) {
		return getPacketSeqNumber(packet) == Integer.MIN_VALUE;
	}
	
	private byte [] getPacketData(byte [] packet) {
		return Arrays.copyOfRange(packet, 8, packet.length);
	}

	
	//---------Helper Class--------------
	
	private class Message {

		private Map<Integer, Object> cache = new HashMap<>();

		private byte[] data;
		private int remainingBytes = 0;
		private boolean isInitialized = false;

		private int messageSize = 0;
		private int messageId= 0;	

		public Message(int messageId) {
			this.messageId = messageId;
		}

		public void setData(int seqNumber, byte[] data) {
			if (isInitialized) {
				for (int i = seqNumber * packetSize; i < data.length; i++)
					this.data[i] = data[i];
				remainingBytes -= data.length;
				if (remainingBytes < 0) {
					Log.e(String.format("Message %d received more data than expected (by %d bytes).", messageId, -remainingBytes));
				}
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
					return (List<? extends KnowledgeData>) deserialize(data);
				}
			} catch (IOException | ClassNotFoundException e) {
				Log.e("Error while deserializing data - Corrupted message.");
			}
			return null;
		}

		private void initialize(int messageSize) {
			if (!isInitialized) {
				this.remainingBytes = messageSize;
				this.data = new byte[messageSize];
				this.messageSize= messageSize; 
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
