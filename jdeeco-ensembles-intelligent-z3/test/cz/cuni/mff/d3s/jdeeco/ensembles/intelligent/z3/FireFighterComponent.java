package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.io.PrintStream;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;
import cz.cuni.mff.d3s.deeco.annotations.Process;

@Component
@PlaysRole(FireFighter.class)
public class FireFighterComponent {

	public static PrintStream outputStream = System.out;
	
	public String id;
	
	public Integer trainId;
	
	public FireFighterComponent(String id) {
		this.id = id;
	}
	
	@Process
	@PeriodicScheduling(period = 1000, offset = 1)
	public static void printTrainId(@In("id") String id, @In("trainId") Integer trainId) {
		if (trainId > 0) {
			outputStream.printf("FireFighter %s: train %d\n", id, trainId);
		}
	}
}
