/**
 * 
 */
package cz.cuni.mff.d3s.deeco.network.connector;

import java.io.Serializable;

/**
 * 
 * @author Ondrej Kováč <info@vajanko.me>
 */
public class DicEntry implements Serializable {
	private Object key;
	private String address;
	
	public Object getKey() { return key; }
	public String getAddress() { return address; }
	
	@Override
	public String toString() {
		return key.toString() + "->" + address;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return key.hashCode() * 1000 + address.hashCode();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DicEntry))
			return super.equals(obj);
		
		DicEntry de = (DicEntry)obj;
		return key.equals(de.key) && address.equals(de.address);
	}
	
	public DicEntry(Object key, String address) {
		this.key = key;
		this.address = address;
	}
}
