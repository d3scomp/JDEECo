package cz.cuni.mff.d3s.deeco.demo.convoytut;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.knowledge.Component;

public class Follower extends Component {

	public String name = "F";
	public Waypoint position = new Waypoint(1, 4);
	public Waypoint destination = new Waypoint(7, 2);
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
