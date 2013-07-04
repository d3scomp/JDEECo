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

public class ComplexLinkEnsemble  extends Ensemble {
	
	private static final long serialVersionUID = 1L;

	@Membership(candidateRange=3)
	public static IdListType membership(
			// input coordinator SCP instance
			@In("coord.id") String cScpInstanceId,
			@In("coord.isAssigned") Boolean cScpAssigned,
			@InOut("coord.isLinked") OutWrapper<Boolean> cScpLinked,
			// candidate ids supplied by the framework
			// there is only one in the current scenario
			@In("candidate.id") List<String> cdScpInstanceIds,
			@In("candidate.isAssigned") List<Boolean> cdScpAssigned,
			@InOut("candidate.isLinked") OutWrapper<List<Boolean>> cdScpLinked //OutWrapper
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
	public static void map(@InOut("coord.linkedScpInstanceIds") OutWrapper<List<String>> cLinkedScpInstanceIds,
			@InOut("candidate.linkedScpInstanceIds") OutWrapper<List<List<String>>> cdLinkedScpInstanceIds,
			@In("coord.id") String cId,
			@In("candidate.id") List<String> cdScpInstanceIds) {
		// create the link locally on each node
		int i = 0;
		String ids = cId;
		String latencies = "";
		for (String cdId : cdScpInstanceIds){
			ids += ", " + cdId;
			latencies += ", " + (Integer)LinkedMiddlewareEntry.getMiddlewareEntrySingleton().getDistance(cId, cdId);
			if (!cLinkedScpInstanceIds.value.contains(cdId))
				cLinkedScpInstanceIds.value.add(cdId);
			if (!cdLinkedScpInstanceIds.value.contains(cId))
				cdLinkedScpInstanceIds.value.get(i).add(cId);
			i++;
		}
		
		System.out.println(ids + " are now linked together, with latencies" + latencies);
	}
}
