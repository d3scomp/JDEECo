package cz.cuni.mff.d3s.jdeeco.adaptation.correlation;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.KnowledgeMetadataHolder;
import cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.CorrelationMetadataWrapper;
import cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.CorrelationLevel.DistanceClass;

// This class is not used. It only illustrates what is being created inside the CorrelationEnsembleFactory
@Ensemble
@PeriodicScheduling(period = 1000)
public class CorrelationEnsembleTemplate {

	@Membership
	public static boolean membership(
			@In("member.position") CorrelationMetadataWrapper<Integer> memberPosition,
			@In("member.temperature") CorrelationMetadataWrapper<Integer> memberTemperature,
			@In("coord.position") CorrelationMetadataWrapper<Integer> coordPosition,
			@In("coord.temperature") CorrelationMetadataWrapper<Integer> coordTemperature) {

		return (!memberTemperature.isOperational()
				&& coordTemperature.isOperational()
				&& KnowledgeMetadataHolder.classifyDistance("position", memberPosition.getValue(), coordPosition.getValue()) == DistanceClass.Close);
	}

	@KnowledgeExchange
	public static void map(
			@In("member.id") String memberId,
			@In("coord.id") String coordId,
			@In("coord.temperature") CorrelationMetadataWrapper<Integer> coordTemperature,
			@Out("member.temperature") ParamHolder<CorrelationMetadataWrapper<Integer>> memberTemperature) throws KnowledgeNotFoundException {

//		System.out.println("Knowledge injection " + coordId + " -> " + memberId + " temperature " + coordTemperature.getValue() + " at " + coordTemperature.getTimestamp());

		memberTemperature.value = coordTemperature;
		memberTemperature.value.malfunction();
	}
}
