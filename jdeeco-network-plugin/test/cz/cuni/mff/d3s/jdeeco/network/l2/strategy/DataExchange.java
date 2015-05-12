package cz.cuni.mff.d3s.jdeeco.network.l2.strategy;

import cz.cuni.mff.d3s.deeco.annotations.CommunicationBoundary;
import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

/**
 * Maps value integer from DataSource to DataSink components
 */
@Ensemble
@PeriodicScheduling(period = 1000)
public class DataExchange {
	@Membership
	public static boolean membership(@In("member.id") String mId, @In("coord.id") String cId) {
		return !mId.equals(cId);
	}

	@KnowledgeExchange
	public static void map(@In("coord.value") Integer sourceValue,
			@Out("member.outValue") ParamHolder<Integer> sinkValue) {
		sinkValue.value = sourceValue;
	}

	/**
	 * Tested boundary
	 * 
	 * Only Sink0 is allowed to rebroadcast data
	 * 
	 * @param knowledge
	 *            Knowledge to be propagated
	 * @param sender
	 *            Knowledge of rebroadcast node
	 * @return Whether the knowledge should be propagated
	 */
	@CommunicationBoundary
	public static boolean boundary(KnowledgeData knowledge, ReadOnlyKnowledgeManager sender) {
		return sender.getId().equals("Sink0");
	}
}
