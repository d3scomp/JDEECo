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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IPAddress other = (IPAddress) obj;
		return other.ipAddress.equals(ipAddress);
	}
}
