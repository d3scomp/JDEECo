package cz.cuni.mff.d3s.jdeeco.matsim.demo.convoy;


import java.util.Arrays;
import java.util.List;

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
public class Leader {
	
	public String id;
	public String name;
	public List<Waypoint> path;
	public Waypoint position;
	
	public Leader() {
		path = Arrays.asList(
				new Waypoint(3, 1),
				new Waypoint(2, 1),
				new Waypoint(1, 1),
				new Waypoint(1, 2),
				new Waypoint(1, 3),
				new Waypoint(1, 4),
				new Waypoint(1, 5));
				
		name = "L";
		id = "Leader1";
		position = new Waypoint(3, 1);
	}
	
	@Process
	@PeriodicScheduling(period=2500)
	public static void moveProcess(
			@InOut("path") ParamHolder<List<Waypoint>> path,
			@In("name") String name,
			@InOut("position") ParamHolder<Waypoint> me
			) {
		
		if (!path.value.isEmpty() && me.value.equals(path.value.get(0))) {
			path.value = path.value.subList(1, path.value.size());
		}
		
		if (!path.value.isEmpty()) {
			Waypoint next = path.value.get(0);
			me.value.x += Integer.signum(next.x - me.value.x);
			me.value.y += Integer.signum(next.y - me.value.y);
		}

		System.out.println("Leader " + name + ": " + me.value);
	}
}
