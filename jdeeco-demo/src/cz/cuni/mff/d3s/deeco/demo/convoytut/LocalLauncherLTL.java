package cz.cuni.mff.d3s.deeco.demo.convoytut;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.KnowledgeJPF;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepositoryJPF;
import cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.provider.PreLauncherDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedSchedulerJPF;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;
import cz.cuni.mff.d3s.deeco.ltl.AtomicProposition;


public class LocalLauncherLTL 
{
	public static void main(String[] args) 
	{
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
				},
				new AtomicProposition() {					
					@Override
					public String getName() {
						return "isFollowerNearLeader";
					}
					
					@Override
					public Boolean evaluate(KnowledgeJPF knowledge) {
						Integer lx = (Integer) knowledge.getSingle("leader.position.x");
                        Integer ly = (Integer) knowledge.getSingle("leader.position.y");
                        Integer fx = (Integer) knowledge.getSingle("follower.position.x");
                        Integer fy = (Integer) knowledge.getSingle("follower.position.y");

                        return (Math.abs(lx - fx) <= 1) && (Math.abs(lx - fy) <= 1); 
					}
				}

			});
		
		LocalKnowledgeRepositoryJPF repo = new LocalKnowledgeRepositoryJPF(propositions);
		KnowledgeManager km = new RepositoryKnowledgeManager(repo);
		
		Scheduler scheduler = new MultithreadedSchedulerJPF();
		
		AbstractDEECoObjectProvider dop = new PreLauncherDEECoObjectProvider();
		
		Runtime rt = new Runtime(km, scheduler);
		rt.registerComponentsAndEnsembles(dop);
		
		// TODO: manage via runtime event listener mechanism (to be done) instead
		repo.onStart();
		
		rt.startRuntime();
		
	}
}
