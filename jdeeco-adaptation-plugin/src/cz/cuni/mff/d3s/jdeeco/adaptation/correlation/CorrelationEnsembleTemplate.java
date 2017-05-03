package cz.cuni.mff.d3s.jdeeco.adaptation.correlation;

import static cz.cuni.mff.d3s.metaadaptation.correlation.ConnectorManager.COORD_FILTER_FIELD;
import static cz.cuni.mff.d3s.metaadaptation.correlation.ConnectorManager.MEMBER_FILTER_FIELD;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.metaadaptation.correlation.CorrelationMetadataWrapper;

// This class is not used. It only illustrates what is being created inside the CorrelationEnsembleFactory
@Ensemble(enableLogging=false)
@PeriodicScheduling(period = 1000)
@SuppressWarnings({"rawtypes", "unchecked"})
public class CorrelationEnsembleTemplate {
	
	private static java.util.function.Predicate test;
	
	@Membership
	public static boolean membership(
			@In("member.position") CorrelationMetadataWrapper<Integer> memberPosition,
			@In("member.temperature") CorrelationMetadataWrapper<Integer> memberTemperature,
			@In("coord.position") CorrelationMetadataWrapper<Integer> coordPosition,
			@In("coord.temperature") CorrelationMetadataWrapper<Integer> coordTemperature) {

		final java.util.Map knowledge = new java.util.HashMap();
		knowledge.put(MEMBER_FILTER_FIELD, memberPosition.getValue());
		knowledge.put(COORD_FILTER_FIELD, coordPosition.getValue());
//		if((!memberTemperature.isOperational()
//				&& coordTemperature.isOperational()
//				&& CorrelationEnsembleTemplate.test.test(knowledge)))
//			System.out.println("Correl ensemble membership satisfied");
//		else System.out.println("Correl ensemble membership not satisfied");
		return (!memberTemperature.isOperational()
				&& coordTemperature.isOperational()
				&& CorrelationEnsembleTemplate.test.test(knowledge));
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
