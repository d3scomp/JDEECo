package cz.cuni.mff.d3s.jdeeco.network.demo.convoy;

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
	public final String name;
	public Waypoint position = new Waypoint(1, 1);
	public Waypoint destination = new Waypoint(1, 3);
	public Waypoint leaderPosition;

	private static PrintStream out;

	public Follower(String name, PrintStream out) {
		this.name = name;
		Follower.out = out;
	}

	public Follower(PrintStream out) {
		this("F", out);
	}

	@Process
	@PeriodicScheduling(period = 2500)
	public static void followProcess(@InOut("position") ParamHolder<Waypoint> me,
			@In("destination") Waypoint destination, @In("name") String name, @In("leaderPosition") Waypoint leader) {

		if (!destination.equals(me.value) && leader != null) {
			me.value.x += Integer.signum(leader.x - me.value.x);
			me.value.y += Integer.signum(leader.y - me.value.y);
		}

		out.println("Follower " + name + ": me = " + me.value + " leader = " + leader);
	}
}
