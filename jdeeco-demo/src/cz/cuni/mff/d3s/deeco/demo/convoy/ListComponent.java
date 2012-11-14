package cz.cuni.mff.d3s.deeco.demo.convoy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.DEECoIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoInitialize;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;

public class ListComponent extends ComponentKnowledge {
	public List<List<Path>> listOfLists;
	public Path path;

	@DEECoInitialize
	public static ComponentKnowledge getInitialKnowledge() {
		ListComponent k = new ListComponent();
		k.listOfLists = new ArrayList<List<Path>>();
		List<Path> le = new ArrayList<Path>();
		Path p = new Path();
		p.remainingPath = new ArrayList<Integer>(Arrays.asList(new Integer [] {1,2,3,4,5,6}));
		p.currentPosition = 0;
		le.add(p);
		k.listOfLists.add(le);
		k.path = new Path();
		k.path.remainingPath = new ArrayList<Integer>(Arrays.asList(new Integer [] {1,2,3,4,5,6}));
		k.path.currentPosition = 0;
		return k;
	}

	/*
	 * Input: path, crossingRobots, convoyRobot Output: path
	 */
	@DEECoPeriodicScheduling(2000)
	@DEECoProcess
	public static void process(@DEECoIn("path") Map<String, Integer> lol) {
		System.out.println("lol retrieved");
	}
}
