package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import java.io.Serializable;

public class ObjectId<ValueType> implements Serializable {
	private static final long serialVersionUID = 1118708881491848620L;
	
	public ValueType value;
	
	public ObjectId(ValueType value) {
		this.value = value;
	}
		
		
	@Override
	public boolean equals(Object obj) {
	    if ( this == obj ) 
	    	return true;

		if ( !(obj instanceof ObjectId<?>) ) 
			return false;
		
		ObjectId<?> other = (ObjectId<?>) obj;
		return value.equals(other.value);
	}
	
	@Override
	public int hashCode() {
		if (value != null)
			return value.hashCode();
		else
			return super.hashCode();
	}
	
	@Override
	public String toString() {
		return value == null ? "" : value.toString();
	}
}
