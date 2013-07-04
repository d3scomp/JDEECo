package cz.cuni.mff.d3s.deeco.demo.cloud.candidates;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.invokable.types.IdType;
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

	@Membership(candidateRange=2)
	public static IdType membership(
			// input coordinator
			@In("coord.id") String cId,
			@In("candidate.id") List<String> cdIds,
			// candidate load ratios
			@In("candidate.loadRatio") List<Float> imLoadRatios) {
		/* if the array has at least one element
		 * can be omitted?
		 */
		if (cdIds.size() >= 1) { //&& imIds.contains(mId) && && !mId.equals(cId)
			Float loadRatio = -1.0f;
			String minloadId = "";
			System.out.print(cId + " - Candidate Ids : ");
			// find the minimum loaded node
			for (int i = 0; i < cdIds.size(); i++){
				System.out.print(cdIds.get(i) + (i < cdIds.size() - 1 ? " - " : ""));
				String imId = cdIds.get(i);
				Float imLoadRatio = imLoadRatios.get(i);
				if (minloadId.isEmpty() || (!minloadId.isEmpty() && loadRatio.compareTo(imLoadRatio) > 0)) {
					loadRatio = imLoadRatio;
					minloadId = imId;
				}
			}
			System.out.println("");
			return new IdType(minloadId);
		}
		
		return null;
	}

	@KnowledgeExchange
	@PeriodicScheduling(3000)
	public static void map(@Out("coord.minMemberId") OutWrapper<String> mMinMemberId,
			@In("coord.id") String cId,
			@In("member.id") String mId) {
		System.out.println(cId + " with min loaded node " + mId);
		mMinMemberId.value = mId;
	}
}
