package cz.cuni.mff.d3s.deeco.ltl;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.jpf.ParsedObjectReader;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.KnowledgeJPF;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepositoryJPF;
import cz.cuni.mff.d3s.deeco.provider.RuntimeMetadataProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.RealTimeSchedulerJPF;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * 
 * @author Jaroslav Keznikl
 *
 */
public class LocalLauncherExampleJPF {

	public static void main(String[] args) {
		List<AtomicProposition> propositions = Arrays.asList(new AtomicProposition[] {
				new AtomicProposition() {					
					@Override
					public String getName() {
						return "isFollowerAtDestination";
					}
					
					@Override
					public Boolean evaluate(KnowledgeJPF knowledge) {
						return knowledge.getSingle("follower.position.x").equals(knowledge.getSingle("follower.destination.x"))
								&& knowledge.getSingle("follower.position.y").equals(knowledge.getSingle("follower.destination.y"));
					}
				}
		});
		Scheduler scheduler = new RealTimeSchedulerJPF();
		KnowledgeManager km = new RepositoryKnowledgeManager(new LocalKnowledgeRepositoryJPF(scheduler, propositions));
		RuntimeMetadataProvider provider = new ParsedObjectReader().read();
		
		Runtime rt = new Runtime(scheduler, km);
		rt.deployRuntimeMetadata(provider.getRuntimeMetadata());
		rt.run();
	}
}
