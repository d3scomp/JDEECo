package cz.cuni.mff.d3s.deeco.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SealedObject;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
@SuppressWarnings("serial")
public class RatingsData implements Serializable {

	private RatingsMetaData metaData;
	private List<SealedObject> ratings;
	
	public RatingsData(List<SealedObject> ratings, RatingsMetaData metaData) {
		this.ratings = ratings;
		this.metaData = metaData;
	}
	
	public List<SealedObject> getRatings() {
		return ratings;
	}
	
	public RatingsMetaData getRatingsMetaData() {
		return metaData;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ratings == null) ? 0 : ratings.hashCode());
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
		RatingsData other = (RatingsData) obj;
		if (ratings == null) {
			if (other.ratings != null)
				return false;
		} else if (!ratings.equals(other.ratings))
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
		
		oos.writeInt(ratings.size());
		for (Object o : ratings) {
			oos.writeObject(o);
		}
	}
	
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {		
		metaData = (RatingsMetaData) ois.readObject();
		ratings = new ArrayList<>();
		
		int entries = ois.readInt();
		
		for (int i = 0; i < entries; ++i) {
			ratings.add((SealedObject) ois.readObject());
		}
	}
}
