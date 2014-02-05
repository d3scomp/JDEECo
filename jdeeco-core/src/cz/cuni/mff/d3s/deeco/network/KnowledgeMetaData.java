package cz.cuni.mff.d3s.deeco.network;

import java.io.Serializable;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
@SuppressWarnings("serial")
public class KnowledgeMetaData implements Serializable {
	public String componentId;
	public long versionId;
	public String sender;
	//if -1.0 then it was received from the IP interface
	public transient double rssi;
	
	public KnowledgeMetaData(String componentId, long versionId, String sender) {
		super();
		this.componentId = componentId;
		this.versionId = versionId;
		this.sender = sender;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((componentId == null) ? 0 : componentId.hashCode());
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
		return true;
	}
}
