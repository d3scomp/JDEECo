package cz.cuni.mff.d3s.jdeeco.network;


/**
 * Represents address for MANET broadcasting.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class MANETBroadcastAddress extends Address {

	public final static MANETBroadcastAddress INSTANCE = new MANETBroadcastAddress();

	private MANETBroadcastAddress() {
		// Prevents instantiation from outside the class
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		return getClass() == obj.getClass();
	}
	
}
