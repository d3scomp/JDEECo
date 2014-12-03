/**
 * 
 */
package cz.cuni.mff.d3s.deeco.network.ip;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Ondrej Kováč <info@vajanko.me>
 */
public class IPData implements Serializable {
	
	private Set<String> addresses; 
	private Object metaData;
	
	public Object getMetaData() {
		return metaData;
	}
	public Collection<String> getAddresses() {
		return addresses;
	}
	public void addAddress(String address) {
		addresses.add(address);
	}
	
	public IPData(Object metaData) {
		this.metaData = metaData;
		this.addresses = new HashSet<String>();
	}
	public IPData(Object metaData, Collection<String> addresses) {
		this.metaData = metaData;
		this.addresses = new HashSet<String>(addresses);
	}
}
