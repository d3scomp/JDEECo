package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.jini.TSKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.model.provider.InstanceRuntimeMetadataProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.RealTimeSchedulerJPF;
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
		KnowledgeRepository kr = new TSKnowledgeRepository();
		KnowledgeManager km = new RepositoryKnowledgeManager(kr);
		Scheduler scheduler = new RealTimeSchedulerJPF(km);
		kr.setTimeProvider(scheduler);
		InstanceRuntimeMetadataProvider provider = new InstanceRuntimeMetadataProvider();
		provider.fromEnsembleDefinition(BookingEnsemble.class);
		provider.fromComponentInstance(new CarPlanner());
		provider.fromComponentInstance(new ParkingLot());
		Runtime rt = new Runtime(scheduler, km);
		rt.deployRuntimeMetadata(provider.getRuntimeMetadata());
		rt.run();
	}

}
