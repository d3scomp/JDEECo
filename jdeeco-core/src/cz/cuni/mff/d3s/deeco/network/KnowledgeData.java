package cz.cuni.mff.d3s.deeco.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

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
	private KnowledgeMetaData metaData;
	
	public KnowledgeData(ValueSet knowledge, ValueSet securitySet, ValueSet authors, KnowledgeMetaData metaData) {
		this.knowledge = knowledge;
		this.metaData = metaData;
		this.securitySet = securitySet;
		this.authors = authors;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((knowledge == null) ? 0 : knowledge.hashCode());
		result = prime * result
				+ ((metaData == null) ? 0 : metaData.hashCode());
		result = prime * result
				+ ((securitySet == null) ? 0 : securitySet.hashCode());
		result = prime * result
				+ ((authors == null) ? 0 : authors.hashCode());
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
		if (securitySet == null) {
			if (other.securitySet != null)
				return false;
		} else if (!securitySet.equals(other.securitySet))
			return false;
		if (authors == null) {
			if (other.authors != null)
				return false;
		} else if (!authors.equals(other.authors))
			return false;
		return true;
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException {		
		oos.writeObject(metaData);
		
		for (ValueSet valueSet : Arrays.asList(knowledge, securitySet, authors)) {
			oos.writeInt(valueSet.getKnowledgePaths().size());
			for (KnowledgePath kp: valueSet.getKnowledgePaths()) {
				oos.writeUTF(kp.toString());
				oos.writeObject(valueSet.getValue(kp));
			}
		}		
	}
	
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {		
		metaData = (KnowledgeMetaData) ois.readObject();
				
		int entries = ois.readInt();
		knowledge = new ValueSet();
		for (int i=0; i < entries; ++i) {
			String pathString = ois.readUTF();
			Object value = ois.readObject();
			KnowledgePath kp = getPathFromString(pathString);
			
			knowledge.setValue(kp, value);			
		}
		
		entries = ois.readInt();
		securitySet = new ValueSet();
		for (int i=0; i < entries; ++i) {
			String pathString = ois.readUTF();
			Object value = ois.readObject();
			KnowledgePath kp = getPathFromString(pathString);
			
			securitySet.setValue(kp, value);			
		}
		
		entries = ois.readInt();
		authors = new ValueSet();
		for (int i=0; i < entries; ++i) {
			String pathString = ois.readUTF();
			Object value = ois.readObject();
			KnowledgePath kp = getPathFromString(pathString);
			
			authors.setValue(kp, value);			
		}
	}
	
	private KnowledgePath getPathFromString(String pathString) {
		RuntimeMetadataFactory factory = new RuntimeMetadataFactoryExt();
		
		KnowledgePath kp = factory.createKnowledgePath();
		for (String name: pathString.split("\\.")) {
			PathNodeField pnode = factory.createPathNodeField();
			pnode.setName(name);
			kp.getNodes().add(pnode);
		}
		return kp;
	}
	
	public String toString() {
		return "Owner " + metaData.componentId + " Sender: " + metaData.sender + " Values: " + knowledge;
	}
	
	
}
