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
	
	public Integer pos;
	
	public FireFighterComponent(String id, Integer pos) {
		this.id = id;
		this.pos = pos;
		this.trainId = 0;
	}
	
	@Process
	@PeriodicScheduling(period = 1000, offset = 1)
	public static void printTrainId(@In("id") String id, @In("trainId") Integer trainId) {
		if (trainId > 0) {
			outputStream.printf("FireFighter %s: train %d\n", id, trainId);
		} else {
			outputStream.printf("FireFighter %s: unassigned\n", id);
		}
	}
}
