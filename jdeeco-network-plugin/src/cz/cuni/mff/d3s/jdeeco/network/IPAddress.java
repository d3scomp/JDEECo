package cz.cuni.mff.d3s.jdeeco.network;

/**
 * Represents the IP network address.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class IPAddress extends Address {
	
	public final String ipAddress;

	public IPAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
