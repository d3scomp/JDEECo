package cz.cuni.mff.d3s.deeco.demo.convoy;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;

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
			
	@Process
	@PeriodicScheduling(1000)
	public static void followProcess(
		@InOut("position") Waypoint me,
		@In("destination") Waypoint destination, 
		@In("name") String name, 
		@In("leaderPosition") Waypoint leader 
		) {
		
		if (!destination.equals(me) && leader != null) {
			me.x += Integer.signum(leader.x - me.x);
			me.y += Integer.signum(leader.y - me.y);
		}

		System.out.println("Follower " + name + ": me = " + me + " leader = " + leader);
	}
}
