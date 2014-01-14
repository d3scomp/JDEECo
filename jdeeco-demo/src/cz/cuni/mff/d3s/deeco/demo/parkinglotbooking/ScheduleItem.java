package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import java.io.Serializable;
import java.util.Date;

public class ScheduleItem<ItemType> implements Serializable {
	private static final long serialVersionUID = 4399057749329279039L;

	public Date from;
	public Date to;
	public ItemType item;
	
	public ScheduleItem(ItemType item, Date from, Date to) {
		this.item = item;
		this.from = from;
		this.to = to;
	}
	
	public boolean isInConflict(ParkingLotScheduleItem other) {
		if (other == null)
			return true;
		
		if ((from.equals(other.from) || from.after(other.from)) && (from.before(other.to)))
			return true;
		if (to.after(other.from) && (to.before(other.to) || to.equals(other.to)))
			return true;
		
		return false;
	}
	
	public boolean dateEquals(ScheduleItem<?> other) {
		if (other == null)
			return false;
		return (from == null ? other.from == null : from.equals(other.from))
				&& (to == null ? other.to == null : to.equals(other.to));	
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) 
	    	return true;
		if (obj == null)
			return false;
		if ( !(obj instanceof ScheduleItem<?>) ) 
			return false;
		
		ScheduleItem<?> other = (ScheduleItem<?>) obj;
			
		return (item == null ? other.item == null : item.equals(other.item))
				&& (from == null ? other.from == null : from.equals(other.from))
				&& (to == null ? other.to == null : to.equals(other.to));
	}
	
	@Override
	public int hashCode() {		
		return (item == null ? 0 : item.hashCode())
				+ 1024 * (from == null ? 0 : from.hashCode())
				+ 2048 * (to == null ? 0 : to.hashCode());
	}
}
