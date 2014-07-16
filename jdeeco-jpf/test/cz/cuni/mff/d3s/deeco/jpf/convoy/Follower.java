package cz.cuni.mff.d3s.deeco.jpf.convoy;

import cz.cuni.mff.d3s.deeco.annotations.*;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
public class Follower {

    public String id;
	public Waypoint position; // = new Waypoint(1, 4);
	public Waypoint destination; // = new Waypoint(7, 2);
	public Waypoint leaderPosition;

    public Follower(String id, Waypoint position, Waypoint destination) {
        this.id = id;
        this.position = position;
        this.destination = destination;
        leaderPosition = null;
    }
	
	@Process
	@PeriodicScheduling(100)
	public static void followProcess(
		@InOut("position") ParamHolder<Waypoint> me,
		@In("destination") Waypoint destination,
		@In("id") String id,
		@In("leaderPosition") Waypoint leader
		) {
		
		if (!destination.equals(me.value) && leader != null) {
			me.value.x += Integer.signum(leader.x - me.value.x);
			me.value.y += Integer.signum(leader.y - me.value.y);
		}

		System.out.println("Follower " + id + ": me = " + me.value + " leader = " + leader);
		
		if (destination.equals(me.value)) {
			System.out.println("Follower " + id + " at destination\n");
			System.exit(0);
		}
		
	}
}
