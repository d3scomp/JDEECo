package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.io.PrintStream;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;

@Component
@PlaysRole(Rescuer.class)
public class RescuerComponent {

	public static PrintStream outputStream = System.out;
	
	public String id;
	
	public Integer pos;
	
	public Boolean isLeader;
	
	public Integer trainId;
	
	public RescuerComponent(String id, Integer pos) {
		this.id = id;
		this.pos = pos;
		this.isLeader = false;
		this.trainId = 0;
	}
	
	@Process
	@PeriodicScheduling(period = 1000, offset = 1)
	public static void printTrainId(@In("id") String id, @In("isLeader") Boolean isLeader, @In("trainId") Integer trainId) {
		if (trainId > 0) {
			outputStream.printf("Rescuer %s: train %d", id, trainId);
			if (isLeader) {
				outputStream.print(" (leader)");
			}
			outputStream.println();
		} else {
			outputStream.printf("Rescuer %s: unassigned\n", id);
		}
	}

}
