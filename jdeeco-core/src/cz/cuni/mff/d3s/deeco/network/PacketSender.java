package cz.cuni.mff.d3s.deeco.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.logging.Log;

/**
 * This class enables sending {@link KnowledgeData} as a byte array
 * fragmented into multiple packets.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 * @see Serializer
 * @see PacketReceiver
 * 
 */
public abstract class PacketSender implements KnowledgeDataSender {

	// We reserver Integer.MIN_VALUE for distinguishing initial packets.
	private static int CURRENT_MESSAGE_ID = Integer.MIN_VALUE;

	public synchronized static int getNextMessageId() {
		CURRENT_MESSAGE_ID++;
		return CURRENT_MESSAGE_ID;
	}

	private final int packetSize;

	/**
	 * Minimum fragment size is at least 12 bytes.
	 * 
	 */
	public PacketSender(int packetSize) {
		// At least 12 because: 4 bytes for the initial frame marker, 4 bytes
		// for message id and 4 bytes for packet count.
		assert packetSize >= 12;
		this.packetSize = packetSize;
	}
	
	@Override
	public void broadcastKnowledgeData(List<? extends KnowledgeData> knowledgeData) {
		sendData(knowledgeData);
	}
	
	public void sendData(Object data) {
		sendData(data, "");
	}

	public void sendData(Object data, String recipient) {
		try {
			byte[][] fragments = fragment(data);

			int messageId = getNextMessageId();
			
			//Log.i(String.format("S: " + "(" + messageId + ")" + Arrays.deepToString(fragments)));
			
			// We need to send the message containing id and the number of
			// packets.that will be sent.
			sendPacket(buildInitialPacket(messageId, getDataLength(fragments)), recipient);
			// Now we can send packets
			for (int i = 0; i < fragments.length; i++) {
				sendPacket(buildPacket(messageId, i, fragments[i]), recipient);
			}
		} catch (IOException e) {
			Log.e("Error while serializing data: " + data);
			e.printStackTrace();
		}
	}

	protected abstract void sendPacket(byte[] packet, String recipient);

	private byte[][] fragment(Object data) throws IOException {
		byte[] serialized = Serializer.serialize(data);
		byte[][] result = new byte[(int) Math.ceil(serialized.length
				/ (double) packetSize)][packetSize];
		int start = 0;
		for (int i = 0; i < result.length; i++) {
			result[i] = Arrays.copyOfRange(serialized, start, start
					+ packetSize);
			start += packetSize;
		}
		return result;
	}

	private byte[] buildInitialPacket(int id, int messageSize) {
		byte[] size = ByteBuffer.allocate(4).putInt(messageSize).array();
		return  buildPacket(id, Integer.MIN_VALUE, size);	
	}

	private byte[] buildPacket(int id, int seqNumber, byte[] packetData) {
		byte[] bId = ByteBuffer.allocate(4).putInt(id).array();
		byte[] bSeqNumber = ByteBuffer.allocate(4).putInt(seqNumber).array();
		byte[] result = new byte[8 + packetData.length];
		for (int i = 0; i < bId.length; i++)
			result[i] = bId[i];
		for (int i = 0; i < bSeqNumber.length; i++)
			result[i + 4] = bSeqNumber[i];
		for (int i = 0; i < packetData.length; i++)
			result[i + 8] = packetData[i];
		return result;
	}
	
	private int getDataLength(byte [][] data) {
		int result = 0;
		for (int i = 0; i < data.length; i++)
			result += data[i].length;
		return result;
	}
}
