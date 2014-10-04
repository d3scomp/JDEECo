package cz.cuni.mff.d3s.deeco.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.DeecoProperties;
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
public class PacketSender implements KnowledgeDataSender {

	public static int DEFAULT_PACKET_SIZE = 1000;
	
	public static int HEADER_SIZE = 12;

	// We reserver Integer.MIN_VALUE for distinguishing initial packets.
	private static int CURRENT_MESSAGE_ID = Integer.MIN_VALUE;

	private final NetworkInterface networkInterface;
	private final int packetSize;
	private final boolean hasMANETNic;
	private final boolean hasIPNic;

	//FIXME: this would not work if distributed on multiple JVMs
	public synchronized static int getNextMessageId() {
		CURRENT_MESSAGE_ID++;
		return CURRENT_MESSAGE_ID;
	}

	/**
	 * Minimum fragment size is at least 12 bytes.
	 * 
	 */
	public PacketSender(NetworkInterface networkInterface, int packetSize, boolean hasMANETNic, boolean hasIPNic) {
		// At least 12 because: 4 bytes for the initial frame marker, 4 bytes
		// for message id and 4 bytes for packet count.
		assert packetSize >= 12;
		this.packetSize = packetSize;
		this.networkInterface = networkInterface;
		this.hasIPNic = hasIPNic;
		this.hasMANETNic = hasMANETNic;
		
		Log.d(String.format("PacketSender at %s uses packetSize = %d", networkInterface.getHostId(), packetSize));
	}
	
	public PacketSender(NetworkInterface hostInterface) {
		this(hostInterface, Integer.getInteger(DeecoProperties.PACKET_SIZE, DEFAULT_PACKET_SIZE), true, true);
	}
	
	public boolean hasMANETNic() {
		return hasMANETNic;
	}
	
	public boolean hasIPNic() {
		return hasIPNic;
	}
	
	@Override
	public void broadcastKnowledgeData(List<? extends KnowledgeData> knowledgeData) {
		sendData(knowledgeData, "");
	}
	
	@Override
	public void sendKnowledgeData(List<? extends KnowledgeData> knowledgeData, String recipient) {
		sendData(knowledgeData, recipient);
	}

	public void sendData(Object data, String recipient) {
		try {
			byte[] serialized = Serializer.serialize(data);

			byte[][] fragments = fragment(serialized, packetSize);

			int messageId = getNextMessageId();
			int dataLength = serialized.length;
			
			Log.d(String.format("PacketSender: Sending MSG at %s with messageid %d and size %d (packets=%d)", networkInterface.getHostId(), messageId, dataLength, fragments.length));			
			
			for (int i = 0; i < fragments.length; i++) {
				sendPacket(buildPacket(messageId, i, dataLength, fragments[i]), recipient);
			}
		} catch (IOException e) {
			Log.e("Error while serializing data: " + data);
			e.printStackTrace();
		}
	}

	protected void sendPacket(byte[] packet, String recipient) {
		networkInterface.sendPacket(packet, recipient);
	}

	byte[][] fragment(byte[] serialized, int packetSize) throws IOException {
		int fragmentSize = packetSize - HEADER_SIZE;
		byte[][] result = new byte[(int) Math.ceil(serialized.length
				/ (double) fragmentSize)][];
		int start = 0;
		for (int i = 0; i < result.length; i++) {	
			int remainingBytes = serialized.length - start;
			int cnt = Math.min(fragmentSize, remainingBytes);
			result[i] = new byte[cnt];
			result[i] = Arrays.copyOfRange(serialized, start, start + cnt);
			start += cnt;
		}
		return result;
	}

	byte[] buildPacket(int id, int seqNumber, int totalSize, byte[] packetData) {
		ByteBuffer bb = ByteBuffer.allocate(HEADER_SIZE+packetData.length);
		bb.putInt(id);
		bb.putInt(seqNumber);
		bb.putInt(totalSize);
		bb.put(packetData);
		return bb.array();
	}
	
	/*private int getDataLength(byte [][] data) {
		int result = 0;
		for (int i = 0; i < data.length; i++)
			result += data[i].length;
		return result;
	}*/
}
