package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import java.io.Serializable;
import java.util.UUID;

public class Request implements Serializable{
	private static final long serialVersionUID = -4869798062506291704L;

	public ParkingLotScheduleItem scheduleItem;
	public UUID requestId;
	
	public Request(ParkingLotScheduleItem scheduleItem) {
		this.scheduleItem = scheduleItem;
		requestId = UUID.randomUUID();
	}
}
