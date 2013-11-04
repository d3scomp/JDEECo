package cz.cuni.mff.d3s.deeco.annotations.processor.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

@Component
public class RobotLeaderComponent {

	public final static long serialVersionUID = 1L;
	
	public Integer battery;
	public Path path;
	public List<Path> crossingRobots;

	public RobotLeaderComponent() {
//		this.id = "leader";
		this.battery = new Integer(100);
		this.path = new Path();
		this.path.currentPosition = new Integer(0);
		this.path.remainingPath = new ArrayList<Integer>(Arrays.asList(new Integer[] {new Integer(1), new Integer(2),
				new Integer(3), new Integer(4), new Integer(5), new Integer(6),
				new Integer(7), new Integer(8), new Integer(9)}));
		this.crossingRobots = null;
	}

	/*
	 * Input: path, crossingRobots, convoyRobot Output: path
	 */
	@PeriodicScheduling(3000)
	@Process
	public static void process(@InOut("path") Path path,
			@InOut("battery") OutWrapper<Integer> battery) {
		if (path.remainingPath.size() > 0) {
			path.currentPosition = path.remainingPath.remove(0);
			battery.value = new Integer(battery.value - 1);
			System.out.println("Leader is moving: " + path.remainingPath);
		}
	}
}