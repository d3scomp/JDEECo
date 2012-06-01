package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import java.rmi.RMISecurityManager;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.jini.TSKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Main class for launching the parking lot booking demo.
 * 
 * @author Jaroslav Keznikl
 * 
 */
public class Launcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		Class[] classes = { CarPlanner.class, ParkingLot.class };
		Class[] ensembles = { BookingEnsemble.class };
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new TSKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler(km);
		Runtime runtime = new Runtime(classes, ensembles, km, scheduler);
	}

}
