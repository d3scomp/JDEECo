package cz.cuni.mff.d3s.jdeeco.ros.leaderfollowerdemo;

import java.util.LinkedList;
import java.util.List;

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
public class LeaderRobot {
	/**
	 * Id of the vehicle component.
	 */
	public String id;

	public Position position;

	@Local
	public List<Position> route;

	@Local
	public Positioning positioning;

	@Local
	public Position goal;

	@Local
	public CurrentTimeProvider clock;

	public LeaderRobot(String id, Positioning positioning, CurrentTimeProvider clock) {
		this.id = id;
		this.positioning = positioning;
		this.position = new Position(0, 0, 0);
		this.clock = clock;

		route = new LinkedList<>();
		route.add(new Position(13, 2, 0));
		route.add(new Position(3, 14, 0));
		route.add(new Position(27, 14, 0));
	}

	@Process
	@PeriodicScheduling(period = 100)
	public static void sense(@InOut("position") ParamHolder<Position> position,
			@In("positioning") Positioning positioning) {
		PoseWithCovariance pos = positioning.getPoseWithCovariance();
		if (pos != null) {
			position.value = new Position(pos.position.x, pos.position.y, pos.position.z);
		}
	}

	@Process
	@PeriodicScheduling(period = 1000)
	public static void reportStatus(@In("id") String id, @In("position") Position position,
			@In("clock") CurrentTimeProvider clock) {

		System.out.format("%d: Leader: Id: %s, pos: %s%n", clock.getCurrentMilliseconds(), id, position.toString());
	}

	@Process
	@PeriodicScheduling(period = 2000)
	public static void planRouteAndDrive(@In("id") String id, @In("position") Position position,
			@In("positioning") Positioning positioning, @InOut("route") ParamHolder<List<Position>> route,
			@InOut("goal") ParamHolder<Position> goal, @In("clock") CurrentTimeProvider clock) throws Exception {

		// If goal not set go to the first one
		if (goal.value == null) {
			goal.value = route.value.get(0);

			positioning.setSimpleGoal(ROSPosition.fromPosition(goal.value), new Orientation(0, 0, 0, 1));

			System.out.println(clock.getCurrentMilliseconds() + ":Leader: initial goal set.");
		}

		if (positioning.getMoveBaseResult() != null) {
			switch (positioning.getMoveBaseResult().status) {
			case Succeeded:
				Position reached = route.value.get(0);
				System.out.format(clock.getCurrentMilliseconds() + ": Leader: At: %s reached %s%n", position, reached);
				route.value.remove(reached);
				route.value.add(reached);
				goal.value = route.value.get(0);

				positioning.setSimpleGoal(ROSPosition.fromPosition(goal.value), new Orientation(0, 0, 0, 1));
				break;
			case Rejected:
				positioning.setSimpleGoal(ROSPosition.fromPosition(goal.value), new Orientation(0, 0, 0, 1));
			default:
				System.out.println(clock.getCurrentMilliseconds() + ": Leader: unknown result: "
						+ positioning.getMoveBaseResult().toString());
			}

			System.out.println(
					clock.getCurrentMilliseconds() + ": Leader: result: " + positioning.getMoveBaseResult().toString());
		}
	}
}
