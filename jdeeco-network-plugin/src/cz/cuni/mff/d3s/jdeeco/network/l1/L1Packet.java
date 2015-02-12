package cz.cuni.mff.d3s.jdeeco.network.l1;

import java.nio.ByteBuffer;

/**
 * L1 packet
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class L1Packet {

	public static int HEADER_SIZE = 20; // TotalSize + PayloadSize + SrcNode + StartPos + DataID

	public final byte[] payload;
	/** payload carried by this packet */
	public final int srcNode;
	/** ID of the source that the data originates from */
	public final int dataId;
	/** data ID this packet (fragment) is part of */
	public final int startPos;
	/** this packet payload start position - in bytes */
	public final int payloadSize;
	/** this packet payload size - in bytes */
	public final int totalSize;
	/** payload total size - in bytes */
	public ReceivedInfo receivedInfo;

	/** receival additaional information */

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
	 * Retrieves the byte array representation of the L1 packet. L0 Packet format: Number of L1Packets + Size Of the
	 * first L1Packet + L1Packet + Size of the second L1Packet + ...
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

	/**
	 * Decodes L1 packet from given bytes.
	 * 
	 * @param bytes
	 *            bytes to be decoded
	 * @return L1 packet
	 */
	public static L1Packet fromBytes(byte[] bytes, int offset) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
		byteBuffer.position(offset);
		int totalSize = byteBuffer.getInt();
		int payloadSize = byteBuffer.getInt();
		int startPos = byteBuffer.getInt();
		int dataId = byteBuffer.getInt();
		int srcNode = byteBuffer.getInt();
		byte[] payload = new byte[payloadSize];
		byteBuffer.get(payload, byteBuffer.position(), payloadSize);
		return new L1Packet(payload, srcNode, dataId, startPos, totalSize);
	}

	/**
	 * Calculates the size (in bytes) of this L1 packet
	 * 
	 * @return size in bytes
	 */
	public int getByteSize() {
		return HEADER_SIZE + payloadSize;
	}
}
