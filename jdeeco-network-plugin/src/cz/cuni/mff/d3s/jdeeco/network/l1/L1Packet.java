package cz.cuni.mff.d3s.jdeeco.network.l1;

import java.nio.ByteBuffer;

/**
 * Packet DTO for L1
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class L1Packet {
	
	private static int HEADER_SIZE = 20; //TotalSize + PayloadSize + SrcNode + StartPos + DataID
	
	public final byte [] payload;			/** payload carried by this packet*/
	public final int srcNode;				/** ID of source data originates from*/
	public final int dataId;				/** ID of data this packet (fragment) belongs to*/
	public final int startPos;				/** in bytes*/
	public final int payloadSize; 			/** in bytes*/
	public final int totalSize; 			/** in bytes*/
	public ReceivedInfo receivedInfo; /** receival additaional information */
	
	public L1Packet(byte[] payload, int srcNode, int dataId, int startPos, int totalSize, ReceivedInfo receivedInfo) {
		this.payload = payload;
		this.srcNode = srcNode;
		this.dataId = dataId;
		this.startPos = startPos;
		this.totalSize = totalSize;
		this.receivedInfo = receivedInfo;
		this.payloadSize = payload.length;
	}
	
	public L1Packet(byte[] payload, int srcNode, int dataId, int startPos, int totalSize) {
		this(payload, srcNode, dataId, startPos, totalSize, null);
	}
	
	/**
	 * Retrieves the byte array representation of the L1 packet.
	 * L0 Packet format:
	 * Number of L1Packets + Size Of the first L1Packet + L1Packet + Size of the second L1Packet + ...
	 * 
	 * @param l1Packet
	 *            packet to be encoded
	 * @return array of bytes encoding this L1 packet
	 */
	public byte[] getBytes() {
		ByteBuffer byteBuffer = ByteBuffer.allocate(HEADER_SIZE + payloadSize);
		byteBuffer.putInt(totalSize);
		byteBuffer.putInt(payloadSize);
		byteBuffer.putInt(startPos);
		byteBuffer.putInt(dataId);
		byteBuffer.putInt(srcNode);
		byteBuffer.put(payload);
		return byteBuffer.array();
	}
	
	public static L1Packet fromBytes(byte [] bytes) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
		int totalSize = byteBuffer.getInt();
		int payloadSize = byteBuffer.getInt();
		int startPos = byteBuffer.getInt();
		int dataId = byteBuffer.getInt();
		int srcNode = byteBuffer.getInt();
		byte [] payload = new byte [payloadSize];
		byteBuffer.get(payload, byteBuffer.position(), payloadSize);
		return new L1Packet(payload, srcNode, dataId, startPos, totalSize);
	}
	
	public int getByteSize() {
		return HEADER_SIZE + payloadSize;
	}
}
