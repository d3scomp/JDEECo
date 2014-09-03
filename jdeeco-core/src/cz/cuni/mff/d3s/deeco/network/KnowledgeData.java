package cz.cuni.mff.d3s.deeco.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
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
	private  ValueSet knowledge;
	private KnowledgeMetaData metaData;
	
	public KnowledgeData(ValueSet knowledge, KnowledgeMetaData metaData) {
		this.knowledge = knowledge;
		this.metaData = metaData;
	}

	public ValueSet getKnowledge() {
		return knowledge;
	}

	public KnowledgeMetaData getMetaData() {
		return metaData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((knowledge == null) ? 0 : knowledge.hashCode());
		result = prime * result
				+ ((metaData == null) ? 0 : metaData.hashCode());
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
		return true;
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException {		
		oos.writeObject(metaData);
		
		oos.writeInt(knowledge.getKnowledgePaths().size());
		for (KnowledgePath kp: knowledge.getKnowledgePaths()) {
			oos.writeUTF(kp.toString());
			oos.writeObject(knowledge.getValue(kp));
		}
	}
	
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {		
		metaData = (KnowledgeMetaData) ois.readObject();
		
		RuntimeMetadataFactory factory = new RuntimeMetadataFactoryExt();
		int entries = ois.readInt();
		knowledge = new ValueSet();
		for (int i=0; i < entries; ++i) {
			String pathString = ois.readUTF();
			Object value = ois.readObject();
			
			KnowledgePath kp = factory.createKnowledgePath();
			for (String name: pathString.split("\\.")) {
				PathNodeField pnode = factory.createPathNodeField();
				pnode.setName(name);
				kp.getNodes().add(pnode);
			}
			knowledge.setValue(kp, value);			
		}
	}
	
	
	
	
}
