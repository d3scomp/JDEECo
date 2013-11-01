package cz.cuni.mff.d3s.jdeeco.javaInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.knowledge.Component;

public class ListComponent extends Component {
	
	public final static long serialVersionUID = 1L;
	
	public List<List<Path>> listOfLists;
	public Path path;

	public ListComponent() {
		this.listOfLists = new ArrayList<List<Path>>();
		List<Path> le = new ArrayList<Path>();
		Path p = new Path();
		p.remainingPath = new ArrayList<Integer>(Arrays.asList(new Integer [] {1,2,3,4,5,6}));
		p.currentPosition = 0;
		le.add(p);
		this.listOfLists.add(le);
		this.path = new Path();
		this.path.remainingPath = new ArrayList<Integer>(Arrays.asList(new Integer [] {1,2,3,4,5,6}));
		this.path.currentPosition = 0;
	}

	/*
	 * Input: path, crossingRobots, convoyRobot Output: path
	 */
	@PeriodicScheduling(2000)
	@Process
	public static void process(@In("path") Map<String, Integer> lol) {
		System.out.println("lol retrieved");
	}
}
