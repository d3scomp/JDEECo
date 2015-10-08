package cz.cuni.mff.d3s.jdeeco.adaptation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.MetadataWrapper;

@Ensemble
@PeriodicScheduling(period = 1000)
public class GroupLeaderDataAggregation {

	@Membership
	public static boolean membership(
			@In("member.id") String memberId,
			@In("member.leaderId") Integer leaderId, // just to rule out GroupMembers
			@In("coord.knowledgeHistoryOfAllComponents") Map<String, Map<String, List<MetadataWrapper<? extends Object>>>> knowledgeHistoryOfAllComponents) {
		return true;
	}

	@KnowledgeExchange
	public static void map(
			@In("member.id") String memberId,
			@In("member.leaderId") Integer leaderId, // just to rule out GroupMembers 
			@In("member.position") MetadataWrapper<Integer> position,
			@In("member.temperature") MetadataWrapper<Integer> temperature,
			@InOut("coord.knowledgeHistoryOfAllComponents") ParamHolder<Map<String, Map<String, List<MetadataWrapper<? extends Object>>>>> knowledgeHistoryOfAllComponents) throws KnowledgeNotFoundException {

		System.out.println("KnowledgeExchange for component " + memberId);

		Map<String, List<MetadataWrapper<? extends Object>>> memberKnowledgeHistory = knowledgeHistoryOfAllComponents.value.get(memberId);
		if (memberKnowledgeHistory == null) {
			memberKnowledgeHistory = new HashMap<>();
		}

		String field = "position";
		List<MetadataWrapper<? extends Object>> fieldHistory = memberKnowledgeHistory.get(field);
		if (fieldHistory == null) {
			fieldHistory = new ArrayList<>();
		}
		fieldHistory.add(position);
		memberKnowledgeHistory.put(field, fieldHistory);
		
		field = "temperature";
		fieldHistory = memberKnowledgeHistory.get(field);
		if (fieldHistory == null) {
			fieldHistory = new ArrayList<>();
		}
		fieldHistory.add(temperature);
		memberKnowledgeHistory.put(field, fieldHistory);
		
		knowledgeHistoryOfAllComponents.value.put(memberId, memberKnowledgeHistory);

	}



}