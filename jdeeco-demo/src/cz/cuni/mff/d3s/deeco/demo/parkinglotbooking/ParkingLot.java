package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;
import cz.cuni.mff.d3s.deeco.knowledge.Component;


public class ParkingLot extends Component {

	public Map<ParkingPlaceId, List<ParkingLotScheduleItem> > schedule;
	public Map<UUID, Request> incomingRequests;
	public Map<UUID, Response> processedResponses;
	public Position position;
	public ParkingPlaceId[] parkingPlaces;
	
	
	public ParkingLot() {
		this.schedule = new HashMap<ParkingPlaceId, List<ParkingLotScheduleItem> >();
		this.incomingRequests = new HashMap<UUID, Request>();
		this.processedResponses = new HashMap<UUID, Response>();
		this.position = new Position(1,1);
		this.parkingPlaces = new ParkingPlaceId[10];
		for (int i = 0; i < this.parkingPlaces.length; ++i)
			this.parkingPlaces[i] = new ParkingPlaceId(i);
	}

	@Process
	public static void processRequests(
			@InOut("scheduleItem") Map<ParkingPlaceId, List<ParkingLotScheduleItem> > schedule,					
			@In("parkingPlaces") ParkingPlaceId[] parkingPlaces,
			@In("incomingRequests[*]") @TriggerOnChange Request incomingRequest,
			@Out("processedResponses") Map<UUID, Response> processedResponses
			) {
		
		System.out.printf("Processing request issued by %s on parking from %d to %d.\n",
				incomingRequest.scheduleItem.item.toString(),
				incomingRequest.scheduleItem.from.toString(),
				incomingRequest.scheduleItem.to.toString());
		
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
				processedResponses.put(incomingRequest.requestId, new Response(incomingRequest));
				processedResponses.get(incomingRequest.requestId).assignedParkingPlace = place;
								
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
