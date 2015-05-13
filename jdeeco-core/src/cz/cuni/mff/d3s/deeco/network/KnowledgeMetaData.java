package cz.cuni.mff.d3s.deeco.network;

import java.io.Serializable;
import java.util.Arrays;


/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * @author Ondřej Štumpf
 *
 */
@SuppressWarnings("serial")
public class KnowledgeMetaData implements Serializable {
	public String componentId;
	public long versionId;
	public String sender;
	//if -1.0 then it was received from the IP interface
	public transient double rssi;
	public long createdAt; 
	public int hopCount;
	
	public byte[] encryptedKey;
	public String encryptedKeyAlgorithm;
	public Integer targetRoleHash;
	public byte[] signature;
	
	public KnowledgeMetaData(String componentId, long versionId, String sender, long createdAt, int hopCount) {
		this(componentId, versionId, sender, createdAt, hopCount, null, null, null, null);
	}

	public KnowledgeMetaData(String componentId, long versionId, String sender, long createdAt, int hopCount, 
			byte[] encryptedKey, String encryptedKeyAlgorithm, Integer targetRoleHash, byte[] signature) {
		super();
		this.componentId = componentId;
		this.versionId = versionId;
		this.sender = sender;
		this.createdAt = createdAt;
		this.hopCount = hopCount;
		this.encryptedKey = encryptedKey;
		this.encryptedKeyAlgorithm = encryptedKeyAlgorithm;
		this.targetRoleHash = targetRoleHash;
		this.signature = signature;
	}
	
	public KnowledgeMetaData clone() {
		return new KnowledgeMetaData(componentId, versionId, sender, createdAt, hopCount, encryptedKey, encryptedKeyAlgorithm, targetRoleHash, signature);
	}
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((componentId == null) ? 0 : componentId.hashCode());
		result = prime * result + (int) (createdAt ^ (createdAt >>> 32));
		result = prime * result + hopCount;
		long temp;
		temp = Double.doubleToLongBits(rssi);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + (int) (versionId ^ (versionId >>> 32));
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
		KnowledgeMetaData other = (KnowledgeMetaData) obj;
		if (componentId == null) {
			if (other.componentId != null)
				return false;
		} else if (!componentId.equals(other.componentId))
			return false;
		if (createdAt != other.createdAt)
			return false;
		if (hopCount != other.hopCount)
			return false;
		if (Double.doubleToLongBits(rssi) != Double
				.doubleToLongBits(other.rssi))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		if (versionId != other.versionId)
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
		if (targetRoleHash == null) {
			if (other.targetRoleHash != null)
				return false;
		} else {
			if (!targetRoleHash.equals(other.targetRoleHash)) 
				return false;
		}
		return true;
	}


	public String getSignatureWithRole() {
		if (targetRoleHash != null) {
			return componentId + "v" + versionId + "_" + targetRoleHash;
		} else {
			return getSignature();
		}
	}
	
	public String getSignature() {
		return componentId + "v" + versionId;
	}	
}
