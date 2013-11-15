package cz.cuni.mff.d3s.deeco.demo.convoy;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
public class Leader {
	
	public String id = "Leader";
	public String name;
	public List<Waypoint> path;
	public Waypoint position;
	
	public Leader() {
		path = Arrays.asList(
				new Waypoint(2, 1),
				new Waypoint(1, 1),
				new Waypoint(1, 2),
				new Waypoint(1, 3),
				new Waypoint(1, 4),
				new Waypoint(1, 5));
				
		name = "Leader";
		id = "Leader";
		position = new Waypoint(3, 1);
	}
	
	@Process
	@PeriodicScheduling(1000)
	public static void moveProcess(
			@InOut("path") List<Waypoint> path,
			@In("name") String name,
			@InOut("position") Waypoint me
			) {
		
		if (!path.isEmpty() && me.equals(path.get(0))) {
			path.remove(0);
		}
		
		if (!path.isEmpty()) {
			Waypoint next = path.get(0);
			me.x += Integer.signum(next.x - me.x);
			me.y += Integer.signum(next.y - me.y);
		}

		System.out.println("Leader " + name + ": " + me);
	}
}
