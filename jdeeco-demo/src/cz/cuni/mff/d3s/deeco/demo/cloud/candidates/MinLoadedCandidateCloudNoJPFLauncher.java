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
import cz.cuni.mff.d3s.deeco.runtime.middleware.LinkedMiddlewareEntry;
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
		List<Component> minloadNodes = new ArrayList<Component>(4);
		for (int i = 0; i < 4; i++){
			minloadNodes.add(new MinLoadedCandidateNode("MinloadNode" + i, rand.nextFloat(), 1.0f));
		}
		// initialize the singleton of the RandomIntegerDistanceMiddlewareEntry to generate the network distances
		// these will be dynamically provided by the communication middleware if jDEECo gets connected to it
		LinkedMiddlewareEntry middlewareEntry = (LinkedMiddlewareEntry) LinkedMiddlewareEntry.getMiddlewareEntrySingleton();
		middlewareEntry.updateDistanceTopology(minloadNodes, 2);	
		// print the matrix
		String[] matrix = middlewareEntry.distanceLinksToString();
		for (int i = 0; i < matrix.length; i++)
			System.out.println(matrix[i]);
		
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
