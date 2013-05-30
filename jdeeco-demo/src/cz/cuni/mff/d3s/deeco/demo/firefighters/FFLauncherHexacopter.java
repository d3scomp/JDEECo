package cz.cuni.mff.d3s.deeco.demo.firefighters;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.Component;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.provider.ClassDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.provider.InitializedDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Main class for launching the FF scenario with hexacopter.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class FFLauncherHexacopter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Class<?>> components = Arrays
				.asList(new Class<?>[] { Hexacopter.class });
		List<Class<?>> ensembles = Arrays.asList(new Class<?>[] {
				SensorDataAggregation.class,
				CriticalDataAggregationOnHexacopter.class,
				CriticalDataCopyFromHexacopterToSL.class });
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler();
		AbstractDEECoObjectProvider dop = new ClassDEECoObjectProvider(
				components, ensembles);
		Runtime rt = new Runtime(km, scheduler);
		rt.registerComponentsAndEnsembles(dop);

		dop = new InitializedDEECoObjectProvider(Arrays.asList(new Component[] {
				new GroupMember("FF1", "T1"), new GroupMember("FF2", "T1"),
				new GroupMember("FF3", "T1"), new GroupMember("FF4", "T2"),
				new GroupMember("FF5", "T2"), new GroupMember("FF6", "T2"),
				new GroupLeader("GL1", "T1", false, new Position(1000, 1000)),
				new GroupLeader("GL2", "T2", true, new Position(0, 0)) }), null);
		rt.registerComponentsAndEnsembles(dop);

		rt.startRuntime();
	}
}
