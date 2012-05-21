package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import java.io.Serializable;

public class ProcessOutHolder<ItemType> implements Serializable {

	private static final long serialVersionUID = 3526860113119876016L;

	private ItemType item;
	
	public ProcessOutHolder(ItemType item) {
		this.item = item;
	}
	
	public ItemType get() {
		return item;
	}
	
	public void set(ItemType newItem) {
		item = newItem;
	}
	
	@Override
	public boolean equals(Object obj) {
		 if ( this == obj ) 
			 return true;
		 if (obj == null)
		    return false;
		if ( !(obj instanceof ProcessOutHolder<?>) ) 
			return false;
		ProcessOutHolder<?> other = (ProcessOutHolder<?>) obj;
		return item == null ? other.item == null : item.equals(other.item);
	}
	
	@Override
	public int hashCode() {
		return item == null ? 0 : item.hashCode();
	}
}
