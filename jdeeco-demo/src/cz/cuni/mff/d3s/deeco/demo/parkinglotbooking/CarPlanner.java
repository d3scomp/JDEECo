package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.knowledge.Component;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;


public class CarPlanner extends Component {
	
	public final static long serialVersionUID = 1L;

	public static enum State {Idle, WaitingForResponse, DrivingToTarget}
	
	public CarId carId;
	public List<CarScheduleItem> schedule;
	public Request request;
	public Response response;
	public Position position;
	public Position targetPosition;
	public CarScheduleItem currentScheduleTarget;
	public State state;
	
	public CarPlanner() {
		this.carId = new CarId("C1");
		this.request = null;
		this.response = null;
		this.position = new Position(0,0);
		this.currentScheduleTarget = null;
		this.state = State.Idle;
		this.schedule = new LinkedList<CarScheduleItem>();
		//TODO init schedule
	}

	@Process
	@PeriodicScheduling(500)
	public static void requestParkingPlaceForCurrentTarget(
			@In("carId") CarId carId,
			@In("currentScheduleTarget") CarScheduleItem currentScheduleTarget,
			@InOut("request") OutWrapper<Request> request
			) {
		
		boolean isRequestActual = false;
		
		if ((request.value != null) && (currentScheduleTarget != null)) {
			isRequestActual = currentScheduleTarget.dateEquals(request.value.scheduleItem);
		} 
		
		if (!isRequestActual) {
			System.out.printf("Car %s is issuing request on position %s from %s to %s.\n", 
					carId, currentScheduleTarget.item.toString(), 
					currentScheduleTarget.from.toString(), currentScheduleTarget.to.toString());
			
			request.value = new Request(new ParkingLotScheduleItem(
							carId, currentScheduleTarget.from, currentScheduleTarget.to));
		}			
	}
	
	@Process
	@PeriodicScheduling(500)
	public static void moveIfParkingPlaceBooked(
			@In("response") Response response,
			@In("request") Request request,
			@In("currentScheduleTarget") CarScheduleItem currentScheduleTarget,
			@InOut("position") OutWrapper<Position> position
			) {
		// if the parking lot acknowledged the parking place, move to the target
		if ((response != null) && (response.matchesRequest(request)) 
				&& (currentScheduleTarget != null) 
				&& (!position.value.equals(currentScheduleTarget.item))) {
			
			System.out.printf("Moving the car %s to the target %s on place %s", 
					request.scheduleItem.item.toString(),
					currentScheduleTarget.item.toString(), 
					response.assignedParkingPlace.toString());
			
			position.value = currentScheduleTarget.item;
		}
	}
	
}