package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.DEECoComponent;
import cz.cuni.mff.d3s.deeco.annotations.DEECoInitialize;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcess;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcessIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcessInOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcessOut;
import cz.cuni.mff.d3s.deeco.knowledge.RootKnowledge;

@DEECoComponent
public class ParkingLot extends RootKnowledge {

	public Map<ParkingPlaceId, List<ParkingLotScheduleItem> > schedule;
	public Request incomingRequest;
	public Response processedResponse;
	public Position position;
	public ParkingPlaceId[] parkingPlaces;
	
	

	@DEECoInitialize
	public static RootKnowledge getInitialKnowledge() {
		ParkingLot k = new ParkingLot();
		k.schedule = new HashMap<>();
		k.incomingRequest = null;
		k.processedResponse = null;
		k.position = new Position(1,1);
		k.parkingPlaces = new ParkingPlaceId[10];
		for (int i = 0; i < k.parkingPlaces.length; ++i)
			k.parkingPlaces[i] = new ParkingPlaceId(i);
		return k;
	}

	@DEECoProcess
	@DEECoPeriodicScheduling(500)
	public static void processRequests(
			@DEECoProcessInOut("scheduleItem") Map<ParkingPlaceId, List<ParkingLotScheduleItem> > schedule,
			@DEECoProcessOut("processedResponse") ProcessOutHolder<Response> processedResponse,
			@DEECoProcessIn("incomingRequest") Request incomingRequest,
			@DEECoProcessIn("parkingPlaces") ParkingPlaceId[] parkingPlaces
			) {
		
		for (ParkingPlaceId place: parkingPlaces) {
			List<ParkingLotScheduleItem> placeSchedule = schedule.get(place);
			// if the current request is alread scheduled, then finish
			if (placeSchedule.contains(incomingRequest.scheduleItem))
				break;
			
			// if no collision with the scheduled items is found, then the request 
			// is scheduled to the current parking place
			if (canBeScheduled(incomingRequest.scheduleItem, placeSchedule)) {
				// schedule the request
				placeSchedule.add(incomingRequest.scheduleItem);
				
				// produce the response
				// TODO fix the ProcessOut bug (the object shouldn't be allocated) 
				processedResponse.set(new Response(incomingRequest));
				processedResponse.get().assignedParkingPlace = place;
								
				break;
			}
		}
	}
	
	
	
	private static boolean canBeScheduled(ParkingLotScheduleItem item, List<ParkingLotScheduleItem> schedule) {
		for (ParkingLotScheduleItem scheduledItem: schedule) {
			if (item.isInConflict(scheduledItem))
				return false;
		}
		return true;
	}
}
