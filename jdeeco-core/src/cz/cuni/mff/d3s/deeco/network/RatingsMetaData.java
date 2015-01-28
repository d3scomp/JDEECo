package cz.cuni.mff.d3s.deeco.network;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Ratings meta data.
 * @author Ondřej Štumpf
 *
 */
@SuppressWarnings("serial")
public class RatingsMetaData implements Serializable {
	//if -1.0 then it was received from the IP interface
	public transient double rssi;
	public long createdAt; 
	public int hopCount;
	
	public byte[] encryptedKey;
	public String encryptedKeyAlgorithm;
	
	public RatingsMetaData(long createdAt, int hopCount) {
		this(createdAt, hopCount, null, null);
	}
	
	public RatingsMetaData(long createdAt, int hopCount, byte[] encryptedKey, String encryptedKeyAlgorithm) {
		this.createdAt = createdAt;
		this.hopCount = hopCount;
		this.encryptedKey = encryptedKey;
		this.encryptedKeyAlgorithm = encryptedKeyAlgorithm;
	}
	
	public RatingsMetaData clone() {
		return new RatingsMetaData(createdAt, hopCount);
	}
	
	@Override
	public int hashCode() {
		final int prime = 53;
		int result = 1;		
		result = prime * result + (int) (createdAt ^ (createdAt >>> 32));
		result = prime * result + hopCount;
		long temp;
		temp = Double.doubleToLongBits(rssi);
		result = prime * result + (int) (temp ^ (temp >>> 32));	
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RatingsMetaData other = (RatingsMetaData) obj;		
		if (createdAt != other.createdAt)
			return false;
		if (hopCount != other.hopCount)
			return false;
		if (Double.doubleToLongBits(rssi) != Double
				.doubleToLongBits(other.rssi))
			return false;		
		if (encryptedKeyAlgorithm == null) {
			if (other.encryptedKeyAlgorithm != null)
				return false;
		} else {
			if (!encryptedKeyAlgorithm.equals(other.encryptedKeyAlgorithm)) 
				return false;
		}
		if (encryptedKey == null) {
			if (other.encryptedKey != null)
				return false;
		} else {
			if (!Arrays.equals(encryptedKey, other.encryptedKey)) 
				return false;
		}
		return true;
	}
}
