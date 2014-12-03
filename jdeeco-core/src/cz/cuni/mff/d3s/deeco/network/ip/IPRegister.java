package cz.cuni.mff.d3s.deeco.network.ip;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Encapsulates a collection of IP addresses known by a particular host.
 * 
 * TODO: this class should be renamed properly.
 * 
 * @author Ondrej Kováč <info@vajanko.me>
 */
public class IPRegister {
	private Set<String> register;
	
	/**
	 * Add given IP address to the current {@link IPRegister} if it does not contain yet.
	 * If IP address already exists in the collection it won't be affected. 
	 * @param address IP address to be added.
	 */
	public void add(String ... addresses) {
		addAll(Arrays.asList(addresses));
	}
	/**
	 * Add given collection of IP addresses to the current {@link IPRegister} if it does
	 * not contain yet. If any of IP address already exists the collection won't be
	 * affected.
	 * @param addresses Collection of IP addresses to be added.
	 */
	public void addAll(Collection<String> addresses) {
		register.addAll(addresses);
	}
	/**
	 * Remove given IP address from current {@link IPRegister}
	 * @param address IP address to be removed
	 */
	public void remove(String address) {
		register.remove(address);
	}
	/**
	 * Get collection of all IP addresses currently stored in the {@link IPRegister}
	 * @return
	 */
	public Collection<String> getAddresses() {
		return register;
	}
	/**
	 * Remove all IP addresses from the current {@link IPRegister}
	 */
	public void clear() {
		register.clear();
	}
	/**
	 * Create a new empty instance of {@link IPRegister}
	 */
	public IPRegister() {
		this.register = new HashSet<String>();
	}
	/**
	 * Create a new instance of {@link IPRegister} with initial list of IP entries
	 * @param initialEntries Collection of IP address to be initially added to the {@link IPRegister}
	 */
	public IPRegister(Collection<String> initialEntries) {
		this.register = new HashSet<String>(initialEntries);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return register.toString();
	}
}
