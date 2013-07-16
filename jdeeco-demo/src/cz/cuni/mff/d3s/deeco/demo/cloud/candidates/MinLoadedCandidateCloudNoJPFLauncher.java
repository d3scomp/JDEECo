package cz.cuni.mff.d3s.deeco.demo.cloud.candidates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
 * Main class for application launching based on four nodes
 * using the MinloadEnsemble
 * 
 * @author Julien Malvot
 *
 */
public class MinLoadedCandidateCloudNoJPFLauncher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Class<?>> components = Arrays
				.asList(new Class<?>[] {});
		List<Class<?>> ensembles = Arrays
				.asList(new Class<?>[] { MinLoadedCandidateEnsemble.class });
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler();
		AbstractDEECoObjectProvider dop = new ClassDEECoObjectProvider(
				components, ensembles);
		Runtime rt = new Runtime(km, scheduler);
		rt.registerComponentsAndEnsembles(dop);

		// networkDistance and loadRatio randomization based on a maxLoadRatio of 0.5f for all
		Random rand = new Random();
		List<Component> minloadNodes = new ArrayList<Component>();
		for (int i = 0; i < 4; i++){
			minloadNodes.add(new MinLoadedCandidateNode("MinloadNode" + i, rand.nextFloat(), 1.0f));
		}
		for (int i = 0; i < minloadNodes.size(); i++) {
			MinLoadedCandidateNode c1 = (MinLoadedCandidateNode) minloadNodes.get(i);
			// generate the list of distances from one component to the others
			for (int j = 0; j < i; j++){
				// no loop on same index, symetric between source and destination
				if (i != j){
					Long val = null;
					MinLoadedCandidateNode c2 = (MinLoadedCandidateNode) minloadNodes.get(j);
					// different treatment for NetworkComponents
					if (!c1.networkId.equals(c2.networkId)){
						val = 150L;
					}else{
						val = ((Integer) rand.nextInt(80)).longValue();
					}
					c1.latencies.put(c2.id, val);
					System.out.println(c1.id + " - " + c2.id + " - " + val);
					c2.latencies.put(c1.id, val);
				}
			}
		}
		
		dop = new InitializedDEECoObjectProvider(minloadNodes, null);
		rt.registerComponentsAndEnsembles(dop);
		
		/*Membership m = null;
		try {
			m = MinLoadedCandidateEnsemble.class.getDeclaredMethods()[0].getAnnotation(Membership.class);
			System.out.println("range = " + m.candidateRange() + " for defining the size of the candidates set\n");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		rt.startRuntime();
	}
}
