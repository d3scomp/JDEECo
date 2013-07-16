package cz.cuni.mff.d3s.deeco.demo.cloud.candidates;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Selector;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

/**
 * Sample ensemble class for evaluating the feasability of
 * computation between a coordinator and multiple members
 * via a common metric (load).
 * The extensibility implies the possibility of defining an input set of members 
 * as a membership parameter and to properly parse the associated array
 * 
 * @author Julien Malvot
 */
public class MinLoadedCandidateEnsemble extends Ensemble {

	private static final long serialVersionUID = 1L;
	
	public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
		Comparator<K> valueComparator =  new Comparator<K>() {
		    public int compare(K k1, K k2) {
		        int compare = map.get(k1).compareTo(map.get(k2));
		        if (compare == 0) return 1;
		        else return compare;
		    }
		};
		Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
		sortedByValues.putAll(map);
		return sortedByValues;
	}
	
	@Membership
	public static Boolean membership(
			// input coordinator
			@In("coord.id") String cId, // just used for the console output here !
			@In("coord.latencies") Map<String, Long> cLatencies,
			// candidates
			@Selector("candidate") OutWrapper<List<Boolean>> cdSelector,
			@In("members.candidate.id") List<String> cdIds,
			@In("members.candidate.loadRatio") List<Float> cdLoadRatios) {
		/* if the array has at least one element
		 * can be omitted?
		 */
		if (cdIds.size() >= 1) {
			Float loadRatio = -1.0f;
			String minloadId = "";
			int range = 2;
			
			System.out.print(cId + " - Candidate Ids : ");

			cLatencies = sortByValues(cLatencies);
			List<String> bestCdIds = new ArrayList<String>();
			Set<Entry<String, Long>> latencySet = cLatencies.entrySet();
			if (range <= cLatencies.size()){
				int i = 0;
				for (Iterator<Entry<String, Long>> iterator = latencySet.iterator(); iterator.hasNext() && i < range; i++) {
			        Entry<String, Long> entry = iterator.next();
			        //if (!entry.getKey().equals(cId))
			        	bestCdIds.add(entry.getKey());
			    }
			}
			// find the minimum loaded node
			for (int i = 0; i < bestCdIds.size(); i++){
				System.out.print(bestCdIds.get(i) + (i < bestCdIds.size() - 1 ? " - " : ""));
				String cdId = bestCdIds.get(i);
				Float cdLoadRatio = cdLoadRatios.get(cdIds.indexOf(cdId));
				if (minloadId.isEmpty() || (!minloadId.isEmpty() && loadRatio.compareTo(cdLoadRatio) > 0)) {
					loadRatio = cdLoadRatio;
					minloadId = cdId;
				}
			}
			System.out.println("");
			Integer minloadIndex = cdIds.indexOf(minloadId);
			// unselect all nodes except the min loaded
			for (int i = 0; i < cdSelector.value.size(); i++){
				if (i != minloadIndex)
					cdSelector.value.set(i, false);
				else
					System.out.println(cId + " selects " + minloadId);
			}
			return true;
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
