package cz.cuni.mff.d3s.jdeeco.ros.leaderfollowerdemo;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;
import cz.cuni.mff.d3s.jdeeco.position.Position;
import cz.cuni.mff.d3s.jdeeco.ros.Positioning;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.Orientation;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.PoseWithCovariance;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.ROSPosition;

@Component
public class FollowerRobot {
	/**
	 * Id of the vehicle component.
	 */
	public String id;
	
	/**
	 * Robots current position
	 */
	public Position position;
	
	/**
	 * Robots destination
	 */
	public Position destination;
		
	@Local
	public Positioning positioning;
	
	@Local
	public Position goal;
	
	@Local
	public static Random rand = new Random(42);
	
	@Local
	public CurrentTimeProvider clock;
	
	public FollowerRobot(String id, Positioning positioning, CurrentTimeProvider clock) {
		this.id = id;
		this.positioning = positioning;
		this.position = new Position(0, 0, 0);
		this.clock = clock;
	}
	
	@Process
	@PeriodicScheduling(period = 100)
	public static void sense(@InOut("position") ParamHolder<Position> position, @In("positioning") Positioning positioning) {
		PoseWithCovariance pos = positioning.getPoseWithCovariance();
		if(pos != null) {
			position.value = new Position(pos.position.x, pos.position.y, pos.position.z);
		}
	}

	@Process
	@PeriodicScheduling(period = 1000)
	public static void reportStatus(
			@In("id") String id,
			@In("position") Position position,
			@In("clock") CurrentTimeProvider clock) {
		
		System.out.format("%d: Follower: Id: %s, pos: %s%n", clock.getCurrentMilliseconds(), id, position.toString());
	}
	
	@Process
	@PeriodicScheduling(period = 500)
	public static void planRouteAndDrive(
			@In("id") String id,
			@In("position") Position position,
			@In("positioning") Positioning positioning,
			@In("destination") Position destination,
			@InOut("goal") ParamHolder<Position> goal,
			@In("clock") CurrentTimeProvider clock) throws Exception {
		// If we have the leader position
		if(destination != null) {
			System.out.format(clock.getCurrentMilliseconds() + ": Follower: Leader at: %s %n", destination);
			
			if (positioning.getMoveBaseResult() != null) {
				switch (positioning.getMoveBaseResult().status) {
				case Succeeded:
				case Rejected:
					goal.value = destination;
					positioning.setSimpleGoal(new ROSPosition(
							goal.value.x + rand.nextDouble() - 0.5,
							goal.value.y + rand.nextDouble() - 0.5,
							goal.value.z),
							new Orientation(0, 0, 0, 1));
					System.out.println(clock.getCurrentMilliseconds() + ": Follower: Goal set: " + goal.value.toString());
				default:
					System.out.println(clock.getCurrentMilliseconds() + ": Leader: unknown result: "
							+ positioning.getMoveBaseResult().toString());
				}

				System.out.println(clock.getCurrentMilliseconds() + ": Leader: result: " + positioning.getMoveBaseResult().toString());
			}
			
			if(goal.value == null || !goal.value.equals(destination)) {
				goal.value = destination;
				positioning.setSimpleGoal(new ROSPosition(
						goal.value.x + rand.nextDouble() - 0.5,
						goal.value.y + rand.nextDouble() - 0.5,
						goal.value.z),
						new Orientation(0, 0, 0, 1));
				System.out.println(clock.getCurrentMilliseconds() + ": Follower: Goal set: " + goal.value.toString());
			}
			
						
		} else {
			System.out.println(clock.getCurrentMilliseconds() + ": Follower: No leader position !!!");
		}
	}
}
