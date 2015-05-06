package cz.cuni.mff.d3s.deeco.demo.convoy;

import java.io.PrintStream;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

/**
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
@Component
public class Follower {

	public String id = "Follower";
	public String name = "F";
	public Waypoint position = new Waypoint(1, 1);
	public Waypoint destination = new Waypoint(1, 3);
	public Waypoint leaderPosition;
	
	private static PrintStream output;
			
	public Follower(PrintStream output) {
		Follower.output = output;
	}

	@Process
	@PeriodicScheduling(period=250)
	public static void followProcess(
		@InOut("position") ParamHolder<Waypoint> me,
		@In("destination") Waypoint destination, 
		@In("name") String name, 
		@In("leaderPosition") Waypoint leader 
		) {
		
		if (!destination.equals(me.value) && leader != null) {
			me.value.x += Integer.signum(leader.x - me.value.x);
			me.value.y += Integer.signum(leader.y - me.value.y);
		}

		output.println("Follower " + name + ": me = " + me.value + " leader = " + leader);
	}
}
