package cz.cuni.mff.d3s.deeco.demo.cloud.candidates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.knowledge.Component;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.DEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

public class LocalLauncherCandidateSPLNoJPF {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler();
		Runtime rt = new Runtime(km, scheduler);

		// networkDistance and loadRatio randomization based on a maxLoadRatio of 0.5f for all
		Random rand = new Random();
		List<Component> minloadNodes = new ArrayList<Component>();
		for (int i = 0; i < 4; i++){
			minloadNodes.add(new CandidateSPLComponent("MinloadSPLNode" + i, rand.nextFloat(), 1.0f));
		}
		for (int i = 0; i < minloadNodes.size(); i++) {
			CandidateSPLComponent c1 = (CandidateSPLComponent) minloadNodes.get(i);
			// generate the list of distances from one component to the others
			for (int j = 0; j < i; j++){
				// no loop on same index, symetric between source and destination
				if (i != j){
					double min, max;
					CandidateSPLComponent c2 = (CandidateSPLComponent) minloadNodes.get(j);
					// different treatment for NetworkComponents
					if (!c1.networkId.equals(c2.networkId)){
						min = 150.0;
						max = 200.0;
					}else{
						min = 0.0;
						max = 80.0;
					}
					CandidateSPLComponentOSLatencyData ld = new CandidateSPLComponentOSLatencyData(c1.id, c2.id, min, max);
					// attach to the components
					c1.latencies.put(c2.id, ld);
					System.out.println(c1.id + " - " + c2.id + " - min/max = " + ld.min + "/" + ld.max);
					c2.latencies.put(c1.id, ld);
				}
			}
		}
		
		DEECoObjectProvider dop = new DEECoObjectProvider();
		dop.addEnsemble(CandidateSPLEnsemble.class);
		dop.addInitialKnowledge(minloadNodes);
		rt.registerComponentsAndEnsembles(dop);
		
		rt.startRuntime();
	}
}
