package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import java.util.Date;

public class ParkingLotScheduleItem extends ScheduleItem<CarId> {

	public ParkingLotScheduleItem(CarId item, Date from, Date to) {
		super(item, from, to);
	}

	private static final long serialVersionUID = -220975145280933559L;
	
	
}
