package cz.cuni.mff.d3s.jdeeco.network.l1;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * L1 packet
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class L1Packet {

	public static int HEADER_SIZE = 9; // TotalSize + PayloadSize + SrcNode + StartPos + DataID

	public final byte[] payload;
	/** payload carried by this packet */
	public final byte srcNode;
	/** ID of the source that the data originates from */
	public final int dataId;
	/** data ID this packet (fragment) is part of - 2 bytes*/
	public final int startPos;
	/** this packet payload start position - in bytes - 2 bytes*/
	public final int payloadSize;
	/** this packet payload size - in bytes - 2 bytes*/
	public final int totalSize;
	/** payload total size - in bytes - 2 bytes*/
	public ReceivedInfo receivedInfo;

	/** receival additaional information */

	public L1Packet(byte[] payload, byte srcNode, int dataId, int startPos, int totalSize, ReceivedInfo receivedInfo) {
		this.payload = payload;
		this.srcNode = srcNode;
		this.dataId = dataId;
		this.startPos = startPos;
		this.totalSize = totalSize;
		this.receivedInfo = receivedInfo;
		this.payloadSize = payload.length;
	}

	public L1Packet(byte[] payload, byte srcNode, int dataId, int startPos, int totalSize) {
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
		byteBuffer.put(encodeIntegerInto2Bytes(totalSize));
		byteBuffer.put(encodeIntegerInto2Bytes(payloadSize));
		byteBuffer.put(encodeIntegerInto2Bytes(startPos));
		byteBuffer.put(encodeIntegerInto2Bytes(dataId));
		byteBuffer.put(srcNode);
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
		int totalSize = decodeIntegerFrom2Bytes(Arrays.copyOfRange(bytes, offset, offset+2));
		int payloadSize = decodeIntegerFrom2Bytes(Arrays.copyOfRange(bytes, offset+2, offset+4));
		int startPos = decodeIntegerFrom2Bytes(Arrays.copyOfRange(bytes, offset+4, offset+6));
		int dataId = decodeIntegerFrom2Bytes(Arrays.copyOfRange(bytes, offset+6, offset+8));
		byte srcNode = bytes[offset+8];
		return new L1Packet(Arrays.copyOfRange(bytes, offset+9, offset + payloadSize + 9), srcNode, dataId, startPos, totalSize);
	}

	/**
	 * Calculates the size (in bytes) of this L1 packet
	 * 
	 * @return size in bytes
	 */
	public int getByteSize() {
		return HEADER_SIZE + payloadSize;
	}
	
	private static int decodeIntegerFrom2Bytes(byte [] value) {
		int high = value[1] >= 0 ? value[1] : 256 + value[1];
		int low = value[0] >= 0 ? value[0] : 256 + value[0];
		return low | (high << 8);
	}
	
	private static byte [] encodeIntegerInto2Bytes(int value) {
		byte[] result = new byte[2];
		result[0] = (byte)(value & 0xFF);
		result[1] = (byte)((value >> 8) & 0xFF);
		return result;
	}
}
