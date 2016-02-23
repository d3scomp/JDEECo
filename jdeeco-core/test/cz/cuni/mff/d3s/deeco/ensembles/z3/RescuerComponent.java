package cz.cuni.mff.d3s.deeco.ensembles.z3;

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
	
	public Integer trainId;
	
	public RescuerComponent(String id, Integer pos) {
		this.id = id;
		this.pos = pos;
	}
	
	@Process
	@PeriodicScheduling(period = 1000, offset = 1)
	public static void printTrainId(@In("id") String id, @In("trainId") Integer trainId) {
		if (trainId > 0) {
			outputStream.printf("Rescuer %s: train %d\n", id, trainId);
		}
	}

}
