package cz.cuni.mff.d3s.jdeeco.network.address;


/**
 * Represents address for MANET broadcasting.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class MANETBroadcastAddress extends Address {
	public final static MANETBroadcastAddress BROADCAST = new MANETBroadcastAddress("broadcast");
	
	private String address;

	public MANETBroadcastAddress(String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}
}
