package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import java.util.Date;

public class CarScheduleItem extends ScheduleItem<Position> {

	public CarScheduleItem(Position item, Date from, Date to) {
		super(item, from, to);
	}

	private static final long serialVersionUID = 8520763755229385192L;

}
