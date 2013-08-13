package cz.cuni.mff.d3s.deeco.demo.cloud.candidates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Selector;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;
import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.StatisticSnapshot;
import cz.cuni.mff.d3s.spl.stat.MeanSorter;

public class CandidateSPLEnsemble extends Ensemble {
	private static final long serialVersionUID = 1L;
	
	@Membership
	public static Boolean membership(
			// input coordinator
			@In("coord.id") String cId, // just used for the console output here !
			@In("coord.latencies") Map<String, CandidateSPLComponentOSLatencyData> cLatencies,
			// candidates
			@Selector("candidate") List<Boolean> cdSelector, 
			@In("members.candidate.id") List<String> cdIds,
			@In("members.candidate.loadRatio") List<Float> cdLoadRatios) {
		if (cdIds.size() >= 1) {
			int range = 2;

			List<String> bestCdIds = new ArrayList<String>();
			List<String> ids = new ArrayList<>(cLatencies.keySet());
			List<? extends Data> data = new ArrayList<>(cLatencies.values());
			// retrieves all snapshots
			List<StatisticSnapshot> snapshots = new ArrayList<StatisticSnapshot> ();
			for (Data d : data){
				snapshots.add(d.getStatisticSnapshot());
			}
			// retrieves the indexes of the component ids from the sorting of latency data sources
			List<Integer> sortedIndexes = MeanSorter.sort(snapshots);	
			// if the null hypothesis has been checked successfully
			if (sortedIndexes != null){
				// gets the component ids from the indexes
				if (range <= sortedIndexes.size()){
					// as the candidate ids are ordered by decreasing means, one takes the last ones by descending order.
					for (int i = sortedIndexes.size()-1; i >= sortedIndexes.size()-range; i--){
						bestCdIds.add(ids.get(sortedIndexes.get(i)));
					}
				}
				// selects the candidates regarding the candidate ids and their load ratio
				CandidateEnsemble.cdSelection(cdSelector, cId, cdIds, bestCdIds, cdLoadRatios);
				return true;
			}
		}
		
		return false;
	}
	
	@KnowledgeExchange
	@PeriodicScheduling(3000)
	public static void map(@Out("coord.minMemberId") OutWrapper<String> mMinMemberId,
			@In("coord.id") String cId,
			@In("members.candidate.id") List<String> mId) {
		System.out.println(cId + " with min loaded node " + mId.get(0));
		mMinMemberId.value = mId.get(0);
	}
}
