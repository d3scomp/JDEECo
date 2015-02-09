package cz.cuni.mff.d3s.jdeeco.network.l1;

/**
 * Packet DTO for L1
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class L1Packet {
	public final byte [] payload;			/** payload carried by this packet*/
	public final int srcNode;				/** ID of source data originates from*/
	public final int dataId;				/** ID of data this packet (fragment) belongs to*/
	public final int startPos;				/** in bytes*/
	public final int payloadSize; 			/** in bytes*/
	public final int totalSize; 			/** in bytes*/
	public final L1ReceivedInfo receivedInfo; /** receival additaional information */
	
	public L1Packet(byte[] payload, int srcNode, int dataId, int startPos,
			int payloadSize, int totalSize, L1ReceivedInfo receivedInfo) {
		this.payload = payload;
		this.srcNode = srcNode;
		this.dataId = dataId;
		this.startPos = startPos;
		this.payloadSize = payloadSize;
		this.totalSize = totalSize;
		this.receivedInfo = receivedInfo;
	}
}
