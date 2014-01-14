package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import java.io.Serializable;
import java.util.UUID;

public class Response implements Serializable {

	private static final long serialVersionUID = 8549009879319681514L;

	public ParkingPlaceId assignedParkingPlace;
	public UUID requestId;
	
	public Response(Request request) {
		requestId = request.requestId;
	}
	
	public boolean matchesRequest(Request req) {
		return req == null ? false : requestId.equals(req.requestId);
	}
}
