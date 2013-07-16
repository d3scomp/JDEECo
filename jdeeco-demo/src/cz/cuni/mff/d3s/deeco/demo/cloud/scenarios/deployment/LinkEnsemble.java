package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Selector;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

/**
 * 
 * @author Julien Malvot
 *
 */
public class LinkEnsemble extends Ensemble {
	
	private static final long serialVersionUID = 1L;

	// can be hidden from the framework and safe as being private
	private static Boolean appSelector(String cId, String cRunningOn, String mId, String mRunningOn){
		return (!mId.equals(cId) && ((cRunningOn == null && mRunningOn == null) || cRunningOn.equals(mRunningOn)));
	}
	
	// can be hidden from the framework and safe as being private
	private static void scpSelector(List<Boolean> selectors, List<String> scpIds, List<Map<String, Long>> scpLatencies){
		List<String> mLinkedIds = new ArrayList<String> ();
		//List<Boolean> selectors = new ArrayList<Boolean> ();
		int range = 2;
		// transforming the List<Map> data structure into a List data structure
		List<Link> mLinks = new ArrayList<Link> ();
		for (int i = 0; i < scpLatencies.size(); i++){
			// set all selectors to false
			selectors.set(i, false);
			// get the ids which the scp is linked to
			Map<String,Long> map = scpLatencies.get(i);
			Object[] toIdSet = scpLatencies.get(i).keySet().toArray();
			// iterate over all the link destinations
			for (int j = 0; j < toIdSet.length; j++){
				// get the latency
				Long latency = map.get((Object)toIdSet[j]);
				// if the latency respects the Service Level Agreement max latency of the source
				if (latency <= 50){
					// add the link to the data structure
					Link link = new Link(scpIds.get(i), (String)toIdSet[j], latency);
					mLinks.add(link);
				}
			}
		}
		// sort all the list of links by order of latency
		Collections.sort(mLinks, new LinkComparator());
		// reuse the initial implemented algorithm
		Integer indexer = -1; // the first required id to be explored for starting the exploration
		// while the linkedIds set is not well-sized or the algorithm runs out of possibilities
		while (mLinkedIds.size() < range && (range+indexer) <= mLinks.size()){
			mLinkedIds.clear();
			Integer firstAddIndex = 0;
			for (int i = 0; i < mLinks.size(); i++){
				Link link = mLinks.get(i);
				// if the link respects the maximum sla latency
				if (i > indexer){
					// first add into the linkage group
					if (mLinkedIds.size() == 0){
						// the starting index of the add is remembered as a bottom limit to be reached for a new search
						firstAddIndex = i;
						// add the two ids of the link
						mLinkedIds.add(link.getFromId());
						mLinkedIds.add(link.getToId());
					}else if (mLinkedIds.size() < range){
						String fId = link.getFromId();
						String tId = link.getToId();
						// if exclusively one or the other ids is part of the covered link
						if ((mLinkedIds.contains(fId) && !mLinkedIds.contains(tId))
								|| (!mLinkedIds.contains(fId) && mLinkedIds.contains(tId))){
							// we add the uncovered to the linkage group
							if (!mLinkedIds.contains(fId))
								mLinkedIds.add(fId);
							else
								mLinkedIds.add(tId);
						}
					}else{
						break;
					}
				}
			}
			// if we did not get enough ids into the interconnection, 
			// then we start the new iteration from the first found index
			if (mLinkedIds.size() < range)
				indexer = firstAddIndex;
		}
		// setting up the list of booleans
		for (int i = 0; i < mLinkedIds.size(); i++){
			int index = scpIds.indexOf(mLinkedIds.get(i));
			selectors.set(index, true);
		}
		//return selectors;
	}
	
	@Membership
	public static Boolean membership(
			// AppComponent coordinator
			@In("coord.id") String cAppId,
			@In("coord.runningOn") String cAppRunningOn,
			@In("coord.isDeployed") Boolean cAppIsDeployed,
			// AppComponent members
			@Selector("App") OutWrapper<List<Boolean>> msAppSelectors, // huge freedom on the size as only booleans rule it
			@In("members.App.id") List<String> msAppIds,
			@In("members.App.runningOn") List<String> msAppRunningOn,
			@In("members.App.isDeployed") List<Boolean> msAppIsDeployed,
			// ScpComponent members
			@Selector("Scp") OutWrapper<List<Boolean>> msScpSelectors,
			@In("members.Scp.id") List<String> msScpIds,
			@In("members.Scp.latencies") List<Map<String, Long>> scpLatencies
			) {
		if (!cAppIsDeployed && !msAppIsDeployed.contains(true)){
			// only after some preconditions, we can come to the selector computations
			// scp selection
			scpSelector(msScpSelectors.value, msScpIds,  scpLatencies);
			// app selection
			for (int i = 0; i < msAppIds.size(); i++){
				msAppSelectors.value.set(i, appSelector(cAppId, cAppRunningOn, msAppIds.get(i), msAppRunningOn.get(i))); 
			}
			// here we go
			return true;
		}
		return false;
	}

	// to expand to different cdScpInstanceIds in case of high candidate range
	@KnowledgeExchange
	@PeriodicScheduling(3000)
	public static void map(
			// AppComponent coordinator (1)
			@In("coord.id") String cAppId,
			@InOut("coord.scpId") OutWrapper<String> cAppScpId,
			@Out("coord.isDeployed") OutWrapper<Boolean> cAppIsDeployed,
			// AppComponent members (n-1)
			@In("members.App.id") List<String> msAppIds,
			@InOut("members.App.scpId") OutWrapper<List<String>> msAppScpIds,
			@InOut("members.App.isDeployed") OutWrapper<List<Boolean>> msAppIsDeployed,
			// ScpComponent members (n)
			@In("members.Scp.id") List<String> msScpIds,
			@InOut("members.Scp.appIds") OutWrapper<List<List<String>>> msScpAppIds) {
		String appComponentIds = cAppId;
		String scpComponentIds = msScpIds.get(0);
		// all AppComponents are now deployed by ScpComponents
		cAppScpId.value = msScpIds.get(0);
		cAppIsDeployed.value = Boolean.TRUE;
		msScpAppIds.value.get(0).add(cAppId);
		// linkage
		for (int i = 0; i < msAppIsDeployed.value.size(); i++){
			msAppIsDeployed.value.set(i,Boolean.TRUE);
			msAppScpIds.value.set(i, msScpIds.get(i+1));
			// app component id registration into the scp component
			// regarding of the first assigned id for the coordinator
			msScpAppIds.value.get(i+1).add(msAppIds.get(i));
			
			appComponentIds += " " + msAppIds.get(i);
			scpComponentIds += " " + msScpIds.get(i+1);
		}
		System.out.println("coordinator="+cAppId+ 
							"   AppComponents=" + appComponentIds + 
							"   ScpComponents=" +scpComponentIds);
	}
}
