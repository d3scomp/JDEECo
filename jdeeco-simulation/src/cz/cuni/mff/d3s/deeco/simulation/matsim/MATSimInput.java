package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.LinkedList;
import java.util.List;

import org.matsim.api.core.v01.Id;

public class MATSimInput {
	public Id destination;
	public double activityEndTime;
	public List<Id> route;
	public String activityType;
	
	public MATSimInput clone() {
		MATSimInput result = new MATSimInput();
		result.activityEndTime = activityEndTime;
		result.activityType = activityType;
		result.route = new LinkedList<Id>(route);
		result.destination = destination;
		return result;
	}
}
