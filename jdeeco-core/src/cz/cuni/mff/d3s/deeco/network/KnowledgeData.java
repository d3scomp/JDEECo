package cz.cuni.mff.d3s.deeco.network;

import java.io.Serializable;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;

/**
 * Class representing a container for sending knowledge values and related
 * metadata over the network.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 */
@SuppressWarnings("serial")
public class KnowledgeData implements Serializable {
	private ValueSet knowledge;
	private ValueSet securitySet;
	private ValueSet authors;
	private List<String> roleClasses;
	private KnowledgeMetaData metaData;
	
	public KnowledgeData(ValueSet knowledge, ValueSet securitySet, ValueSet authors, List<String> roleClasses,
			KnowledgeMetaData metaData) {
		this.knowledge = knowledge;
		this.metaData = metaData;
		this.securitySet = securitySet;
		this.authors = authors;
		this.roleClasses = roleClasses;
	}

	public ValueSet getKnowledge() {
		return knowledge;
	}

	public KnowledgeMetaData getMetaData() {
		return metaData;
	}

	public ValueSet getSecuritySet() {
		return securitySet;
	}
	
	public ValueSet getAuthors() {
		return authors;
	}
	
	public List<String> getRoleClasses() {
		return roleClasses;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authors == null) ? 0 : authors.hashCode());
		result = prime * result
				+ ((knowledge == null) ? 0 : knowledge.hashCode());
		result = prime * result
				+ ((metaData == null) ? 0 : metaData.hashCode());
		result = prime * result
				+ ((roleClasses == null) ? 0 : roleClasses.hashCode());
		result = prime * result
				+ ((securitySet == null) ? 0 : securitySet.hashCode());
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
		KnowledgeData other = (KnowledgeData) obj;
		if (authors == null) {
			if (other.authors != null)
				return false;
		} else if (!authors.equals(other.authors))
			return false;
		if (knowledge == null) {
			if (other.knowledge != null)
				return false;
		} else if (!knowledge.equals(other.knowledge))
			return false;
		if (metaData == null) {
			if (other.metaData != null)
				return false;
		} else if (!metaData.equals(other.metaData))
			return false;
		if (roleClasses == null) {
			if (other.roleClasses != null)
				return false;
		} else if (!roleClasses.equals(other.roleClasses))
			return false;
		if (securitySet == null) {
			if (other.securitySet != null)
				return false;
		} else if (!securitySet.equals(other.securitySet))
			return false;
		return true;
	}

	public String toString() {
		return "Owner " + metaData.componentId + " Sender: " + metaData.sender + " Values: " + knowledge;
	}
	
	
}
