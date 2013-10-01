package cz.cuni.mff.d3s.deeco.demo.convoytut;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.jpf.ParsedObjectReader;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.KnowledgeJPF;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepositoryJPF;
import cz.cuni.mff.d3s.deeco.ltl.AtomicProposition;
import cz.cuni.mff.d3s.deeco.provider.RuntimeMetadataProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.RealTimeSchedulerJPF;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

public class LocalLauncherLTL {
	public static void main(String[] args) {
		List<AtomicProposition> propositions = Arrays
				.asList(new AtomicProposition[] {
						new IsFollowerAtDestination(),
						new IsFollowerNearLeader("LeaderA"),
						new IsFollowerNearLeader("LeaderB") });
		LocalKnowledgeRepositoryJPF repo = new LocalKnowledgeRepositoryJPF(
				propositions);
		KnowledgeManager km = new RepositoryKnowledgeManager(repo);
		Scheduler scheduler = new RealTimeSchedulerJPF(km);
		repo.setTimeProvider(scheduler);
		RuntimeMetadataProvider provider = new ParsedObjectReader().read();

		Runtime rt = new Runtime(scheduler, km);
		rt.deployRuntimeMetadata(provider.getRuntimeMetadata());

		// TODO: manage via runtime event listener mechanism (to be done)
		// instead
		repo.onStart();

		rt.run();

	}

	private static class IsFollowerAtDestination extends AtomicProposition {
		@Override
		public String getName() {
			return "isFollowerAtDestination";
		}

		@Override
		public Boolean evaluate(KnowledgeJPF knowledge) {
			return knowledge.getSingle("Follower.position.x").equals(
					knowledge.getSingle("Follower.destination.x"))
					&& knowledge.getSingle("Follower.position.y").equals(
							knowledge.getSingle("Follower.destination.y"));
		}
	}

	private static class IsFollowerNearLeader extends AtomicProposition {
		String leaderID;

		public IsFollowerNearLeader(String leaderID) {
			this.leaderID = leaderID;
		}

		@Override
		public String getName() {
			return "isFollowerNear" + leaderID;
		}

		@Override
		public Boolean evaluate(KnowledgeJPF knowledge) {
			Integer lx = (Integer) knowledge
					.getSingle(leaderID + ".position.x");
			Integer ly = (Integer) knowledge
					.getSingle(leaderID + ".position.y");
			Integer fx = (Integer) knowledge.getSingle("Follower.position.x");
			Integer fy = (Integer) knowledge.getSingle("Follower.position.y");

			return (Math.abs(lx - fx) <= 1) && (Math.abs(ly - fy) <= 1);
		}
	}

}
