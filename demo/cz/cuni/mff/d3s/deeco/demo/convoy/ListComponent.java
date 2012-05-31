package cz.cuni.mff.d3s.deeco.demo.convoy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoInitialize;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcess;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcessIn;
import cz.cuni.mff.d3s.deeco.knowledge.RootKnowledge;

public class ListComponent extends RootKnowledge {
	public List<List<Path>> listOfLists;

	@DEECoInitialize
	public static RootKnowledge getInitialKnowledge() {
		ListComponent k = new ListComponent();
		k.listOfLists = new ArrayList<List<Path>>();
		List<Path> le = new ArrayList<Path>();
		Path p = new Path();
		p.remainingPath = new ArrayList<Integer>(Arrays.asList(new Integer [] {1,2,3,4,5,6}));
		p.currentPosition = 0;
		le.add(p);
		k.listOfLists.add(le);
		return k;
	}

	/*
	 * Input: path, crossingRobots, convoyRobot Output: path
	 */
	@DEECoPeriodicScheduling(2000)
	@DEECoProcess
	public static void process(@DEECoProcessIn("listOfLists") List<List<Integer>> lol) {
		System.out.println("lol retrieved");
	}
}
