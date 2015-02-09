package cz.cuni.mff.d3s.jdeeco.network.l2;

public class L2Packet {
	
	public final ReceivedInfo receivedInfo;
	
	public L2Packet(ReceivedInfo receivedInfo) {
		this.receivedInfo = receivedInfo;
	}
	
	public L2Packet() {
		this(null);
	}
	
	public byte [] getMarshalledData() {
		//TODO
		return null;
	}
	
	public Object getObject() {
		//TODO
		return null;
	}
	
}
