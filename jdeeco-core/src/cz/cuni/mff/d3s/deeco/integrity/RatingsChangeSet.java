package cz.cuni.mff.d3s.deeco.integrity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class RatingsChangeSet implements Serializable {

	private static final long serialVersionUID = 2415324270333544655L;

	private String authorComponentId, targetComponentId;
	private KnowledgePath knowledgePath;
	private PathRating rating;
	
	public RatingsChangeSet(String authorComponentId, String targetComponentId, KnowledgePath knowledgePath, PathRating rating) {
		this.authorComponentId = authorComponentId;
		this.targetComponentId = targetComponentId;
		this.knowledgePath = knowledgePath;
		this.rating = rating;
	}
	
	public String getAuthorComponentId() {
		return authorComponentId;
	}
	
	public String getTargetComponentId() {
		return targetComponentId;
	}
	
	public KnowledgePath getKnowledgePath() {
		return knowledgePath;
	}
	
	public PathRating getPathRating() {
		return rating;
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException {		
		oos.writeUTF(getAuthorComponentId());
		oos.writeUTF(getTargetComponentId());
		oos.writeUTF(getKnowledgePath().toString());
		oos.writeObject(getPathRating());		
	}
	
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {		
		RuntimeMetadataFactory factory = new RuntimeMetadataFactoryExt();
		authorComponentId = ois.readUTF();
		targetComponentId = ois.readUTF();
		String pathString = ois.readUTF();
		rating = (PathRating)ois.readObject();
		
		KnowledgePath kp = factory.createKnowledgePath();
		for (String name: pathString.split("\\.")) {
			PathNodeField pnode = factory.createPathNodeField();
			pnode.setName(name);
			kp.getNodes().add(pnode);
		}
		knowledgePath = kp;		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RatingsChangeSet other = (RatingsChangeSet) obj;		
		if (authorComponentId == null) {
			if (other.authorComponentId != null)
				return false;
		} else {
			if (!authorComponentId.equals(other.authorComponentId)) 
				return false;
		}
		if (targetComponentId == null) {
			if (other.targetComponentId != null)
				return false;
		} else {
			if (!targetComponentId.equals(other.targetComponentId)) 
				return false;
		}
		if (rating == null) {
			if (other.rating != null)
				return false;
		} else {
			if (!rating.equals(other.rating)) 
				return false;
		}
		if (knowledgePath == null) {
			if (other.knowledgePath != null)
				return false;
		} else {
			if (!knowledgePath.equals(other.knowledgePath)) 
				return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("RatingsChangeSet: author=%s, target=%s, knowledge path=%s, rating=%s", authorComponentId, targetComponentId, knowledgePath, rating);
	}
}
