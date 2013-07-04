package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.invokable.types.IdListType;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;
import cz.cuni.mff.d3s.deeco.runtime.middleware.LinkedMiddlewareEntry;

/**
 * 
 * @author Julien Malvot
 *
 */
public class LinkEnsemble extends Ensemble {
	
	private static final long serialVersionUID = 1L;

	@Membership(candidateRange=1)
	public static IdListType membership(
			// input coordinator SCP instance
			@In("coord.id") String cScpInstanceId,
			@In("coord.isAssigned") Boolean cScpAssigned,
			@InOut("coord.isLinked") OutWrapper<Boolean> cScpLinked,
			// candidate ids supplied by the framework
			// there is only one in the current scenario
			@In("candidate.id") List<String> cdScpInstanceIds,
			@In("candidate.isAssigned") List<Boolean> cdScpAssigned,
			@InOut("candidate.isLinked") OutWrapper<List<Boolean>> cdScpLinked
			) {
		
		// if any of the SCP instances is already assigned or linked to another then no result
		if (cScpLinked.value.booleanValue() || cScpAssigned || cdScpAssigned.contains(Boolean.TRUE) || cdScpLinked.value.contains(Boolean.TRUE)){
			return null;
		/*if (LinkedMiddlewareEntry.getMiddlewareEntrySingleton().getBestDestinationsFromSource(cScpInstanceId, 1).get(0)){
			
		}*/
		// if none of the SCP instances is assigned or linked, we link these instances together
		// and avoid the other to link because the link is done between the better linked set of the cloud
		}else{
			cScpLinked.value = Boolean.TRUE;
			for (int i = 0; i < cdScpLinked.value.size(); i++){
				cdScpLinked.value.set(i, Boolean.TRUE);
			}
		}
		return new IdListType(cdScpInstanceIds);
	}

	// to expand to different cdScpInstanceIds in case of high candidate range
	@KnowledgeExchange
	@PeriodicScheduling(3000)
	public static void map(@InOut("coord.linkedScpInstanceIds") List<String> cLinkedScpInstanceIds,
			@InOut("member.linkedScpInstanceIds") List<String> mLinkedScpInstanceIds,
			@In("coord.id") String cId,
			@In("member.id") String mId) {
		// create the link locally on each node
		if (!cLinkedScpInstanceIds.contains(mId) && !mLinkedScpInstanceIds.contains(cId)){
			cLinkedScpInstanceIds.add(mId);
			mLinkedScpInstanceIds.add(cId);
		
			System.out.println(cId + " and " + mId + " are now linked together, latency=" + (Integer)LinkedMiddlewareEntry.getMiddlewareEntrySingleton().getDistance(cId, mId));
		}
	}
}
