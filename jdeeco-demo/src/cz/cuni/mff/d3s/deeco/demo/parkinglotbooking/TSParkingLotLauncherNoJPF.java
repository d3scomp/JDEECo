package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.jini.TSKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.InstanceRuntimeMetadataProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.RealTimeScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Main class for launching the parking lot booking demo.
 * 
 * @author Jaroslav Keznikl
 * 
 */
public class TSParkingLotLauncherNoJPF {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scheduler scheduler = new RealTimeScheduler();
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new TSKnowledgeRepository(scheduler));
		InstanceRuntimeMetadataProvider provider = new InstanceRuntimeMetadataProvider();
		provider.fromEnsembleDefinition(BookingEnsemble.class);
		provider.fromComponentInstance(new CarPlanner());
		provider.fromComponentInstance(new ParkingLot());
		Runtime rt = new Runtime(scheduler, km);
		rt.deployRuntimeMetadata(provider.getRuntimeMetadata());
		rt.run();
	}

}
