package cz.cuni.mff.d3s.deeco.demo.convoytut;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.knowledge.Component;

public class LeaderB extends Component {
	
	public String name;
	public List<Waypoint> path;
	public Waypoint position;
	
	public LeaderB() {
		path = new LinkedList<Waypoint>();
		path.add(new Waypoint(1, 3));
		path.add(new Waypoint(2, 3));		
		path.add(new Waypoint(3, 3));
		path.add(new Waypoint(4, 3));
		path.add(new Waypoint(5, 3));		
		path.add(new Waypoint(6, 3));
		path.add(new Waypoint(6, 4));
		path.add(new Waypoint(6, 5));		
		path.add(new Waypoint(6, 6));
		path.add(new Waypoint(6, 7));
		path.add(new Waypoint(6, 8));		
		path.add(new Waypoint(7, 8));
		path.add(new Waypoint(8, 8));
		name = "B";
		id = "LeaderB";
		position = new Waypoint(1,2);
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
