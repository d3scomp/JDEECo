package cz.cuni.mff.d3s.deeco.demo.convoy;

import java.io.PrintStream;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;

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

	@Local
	public PrintStream out;
	
	@Local
	public CurrentTimeProvider clock;

	public Follower(String name, PrintStream out, CurrentTimeProvider clock) {
		this.name = name;
		this.out = out;
		this.clock = clock;
	}

	public Follower(PrintStream out, CurrentTimeProvider clock) {
		this("F", out, clock);
	}

	@Process
	@PeriodicScheduling(period = 2500)
	public static void followProcess(
			@InOut("position") ParamHolder<Waypoint> me,
			@In("destination") Waypoint destination,
			@In("name") String name,
			@In("leaderPosition") Waypoint leader,
			@In("out") PrintStream out,
			@In("clock") CurrentTimeProvider clock) {

		if (!destination.equals(me.value) && leader != null) {
			me.value.x += Integer.signum(leader.x - me.value.x);
			me.value.y += Integer.signum(leader.y - me.value.y);
		}

		out.format("%06d: Follower %s: me = %s leader = %s%n", clock.getCurrentMilliseconds(), name, me.value, leader);
	}
}
