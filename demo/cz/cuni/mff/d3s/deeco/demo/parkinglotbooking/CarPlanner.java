package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.DEECoComponent;
import cz.cuni.mff.d3s.deeco.annotations.DEECoInitialize;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcess;
import cz.cuni.mff.d3s.deeco.annotations.DEECoIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoInOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoOut;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;

@DEECoComponent
public class CarPlanner extends ComponentKnowledge {

	public static enum State {Idle, WaitingForResponse, DrivingToTarget}
	
	public CarId carId;
	public List<CarScheduleItem> schedule;
	public Request request;
	public Response response;
	public Position position;
	public Position targetPosition;
	public CarScheduleItem currentScheduleTarget;
	public State state;
	

	@DEECoInitialize
	public static ComponentKnowledge getInitialKnowledge() {
		CarPlanner k = new CarPlanner();
		k.carId = new CarId("C1");
		k.request = null;
		k.response = null;
		k.position = new Position(0,0);
		k.currentScheduleTarget = null;
		k.state = State.Idle;
		k.schedule = new LinkedList();
		//TODO init schedule
		return k;
	}

	@DEECoProcess
	@DEECoPeriodicScheduling(500)
	public static void requestParkingPlaceForCurrentTarget(
			@DEECoIn("carId") CarId carId,
			@DEECoIn("currentScheduleTarget") CarScheduleItem currentScheduleTarget,
			@DEECoInOut("request") OutWrapper<Request> request
			) {
		
		boolean isRequestActual = false;
		
		if ((request.item != null) && (currentScheduleTarget != null)) {
			isRequestActual = currentScheduleTarget.dateEquals(request.item.scheduleItem);
		} 
		
		if (!isRequestActual) {
			System.out.printf("Car %s is issuing request on position %s from %s to %s.\n", 
					carId, currentScheduleTarget.item.toString(), 
					currentScheduleTarget.from.toString(), currentScheduleTarget.to.toString());
			
			request.item = new Request(new ParkingLotScheduleItem(
							carId, currentScheduleTarget.from, currentScheduleTarget.to));
		}			
	}
	
	@DEECoProcess
	@DEECoPeriodicScheduling(500)
	public static void moveIfParkingPlaceBooked(
			@DEECoIn("response") Response response,
			@DEECoIn("request") Request request,
			@DEECoIn("currentScheduleTarget") CarScheduleItem currentScheduleTarget,
			@DEECoInOut("position") OutWrapper<Position> position
			) {
		// if the parking lot acknowledged the parking place, move to the target
		if ((response != null) && (response.matchesRequest(request)) 
				&& (currentScheduleTarget != null) 
				&& (!position.item.equals(currentScheduleTarget.item))) {
			
			System.out.printf("Moving the car %s to the target %s on place %s", 
					request.scheduleItem.item.toString(),
					currentScheduleTarget.item.toString(), 
					response.assignedParkingPlace.toString());
			
			position.item = currentScheduleTarget.item;
		}
	}
	
}