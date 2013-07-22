package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.shutdown;

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
import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.Link;
import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.LinkComparator;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

/**
 * 
 * @author Julien Malvot
 *
 */
public class DeploySSEnsemble extends Ensemble {
	
	private static final long serialVersionUID = 1L;

	// returns true if the cores respect the SLA prescribed for the cpu cores
	protected static Boolean respectSLAScpCores(List<Integer> cores){
		Integer core2GhzCount = 0;
		for (int j = 0; j < cores.size(); j++){
			if (cores.get(j) >= 2000)
				core2GhzCount++;
		}
		// if at least two-gigahertz frequency cores
		return (core2GhzCount >= 2);
	}
	
	private static Boolean appSelection(String cId, String cMachineId, String mId, String mMachineId){
		return (!mId.equals(cId) && ((cMachineId == null && mMachineId == null) || cMachineId.equals(mMachineId)));
	}
	
	private static void scpSelection(List<Boolean> selectors, List<String> scpIds, List<Map<String, Long>> scpLatencies, List<List<Integer>> scpCores, int range){
		List<String> mLinkedIds = new ArrayList<String> ();
		// transforming the List<Map> data structure into a List data structure
		List<Link> mLinks = new ArrayList<Link> ();
		for (int i = 0; i < scpLatencies.size(); i++){
			// set all selectors to false
			selectors.set(i, false);
			// get the ids which the scp is linked to
			Map<String,Long> map = scpLatencies.get(i);
			Object[] toIdSet = scpLatencies.get(i).keySet().toArray();
			List<Integer> cores = scpCores.get(i);
			// if at least two-gigahertz frequency cores
			if (respectSLAScpCores(cores)){
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
	}
	
	private static void buSelection(List<Boolean> buSelectors, List<String> buIds, List<List<Integer>> cores, List<Boolean> scpSelectors, List<String> scpIds, int range){
		// set all selections to false
		for (int i = 0; i < buSelectors.size(); i++)
			buSelectors.set(i, false);
		// select backup nodes which are not already selected for any running purpose
		Integer buSelectCount = 0;
		Integer buIndex = 0;
		while (buSelectCount < range && buIndex < buIds.size()){
			Integer scpBuIndex = scpIds.indexOf(buIds.get(buIndex));
			// the backup node must respect the SLA (2x(f>=2Ghz) cpu cores)
			if (!scpSelectors.get(scpBuIndex) && respectSLAScpCores(cores.get(buIndex))){
				buSelectCount++;
				buSelectors.set(buIndex, true);
			}
			buIndex++;
		}
	}
	
	@Membership
	public static Boolean membership(
			// AppComponent coordinator
			@In("coord.id") String appId,
			@In("coord.isDeployed") Boolean appIsDeployed,
			@In("coord.machineId") String appMachineId,
			
			// AppComponent members
			@Selector("app") OutWrapper<List<Boolean>> appsSelectors,
			@In("members.app.id") List<String> appsIds,
			@In("members.app.isDeployed") List<Boolean> appsIsDeployed,
			@In("members.app.machineId") List<String> appsMachineIds,
			
			// ScpComponent members
			@Selector("scp") OutWrapper<List<Boolean>> scpSelectors,
			@In("members.scp.id") List<String> scpIds,
			@In("members.scp.latencies") List<Map<String, Long>> scpLatencies,
			@In("members.scp.isDown") List<Boolean> IsDown,
			@In("members.scp.cores") List<List<Integer>> scpCores,
			
			// ScpComponent backup members
			@Selector("bu") OutWrapper<List<Boolean>> buSelectors,
			@In("members.bu.id") List<String> buIds,
			@In("members.bu.isDown") List<Boolean> buIsDown,
			@In("members.bu.cores") List<List<Integer>> buCores
			) {
		// none of the application components are deployed 
		// and there are enough scp components for running and backup-ing the application components
		if (!appIsDeployed && !appsIsDeployed.contains(true) && (2*appsIds.size()) <= (scpIds.size() + buIds.size())){
			// app selection
			for (int i = 0; i < appsIds.size(); i++){
				appsSelectors.value.set(i, appSelection(appId, appMachineId, appsIds.get(i), appsMachineIds.get(i))); 
			}
			// scp selection
			scpSelection(scpSelectors.value, scpIds, scpLatencies, scpCores, appsIds.size());
			// bu selection (backup components)
			buSelection(buSelectors.value, buIds, buCores, scpSelectors.value, scpIds, appsIds.size());
			// accept the deployment with the selected ids
			return true;
		}
		return false;
	}

	// to expand to different cdScpInstanceIds in case of high candidate range
	@KnowledgeExchange
	@PeriodicScheduling(3000)
	public static void map(
			// AppComponent coordinator (1)
			@In("coord.id") String appId,
			@InOut("coord.onScpId") OutWrapper<String> appOnScpId,
			@InOut("coord.buScpId") OutWrapper<String> appBuScpId,
			@Out("coord.isDeployed") OutWrapper<Boolean> appIsDeployed,
			
			// AppComponent members (n-1)
			@In("members.app.id") List<String> appsIds,
			@InOut("members.app.onScpId") OutWrapper<List<String>> appsOnScpIds,
			@InOut("members.app.buScpId") OutWrapper<List<String>> appsBuScpIds,
			@InOut("members.app.isDeployed") OutWrapper<List<Boolean>> appsIsDeployed,
			
			// ScpComponent members (n)
			@In("members.scp.id") List<String> scpIds,
			@InOut("members.scp.onAppIds") OutWrapper<List<List<String>>> scpOnAppIds,
			
			// ScpComponent backup members (n)
			@In("members.bu.id") List<String> buIds,
			@InOut("members.bu.buAppIds") OutWrapper<List<List<String>>> buAppIds,
			@InOut("members.bu.moScpIds") OutWrapper<List<List<String>>> buMoScpIds
			) {
		// this is for user output
		String appComponentIds = appId;
		String scpComponentIds = scpIds.get(0);
		String buComponentIds = buIds.get(0);
		// deploy the application component coordinator
		appOnScpId.value = scpIds.get(0);
		appBuScpId.value = buIds.get(0);
		appIsDeployed.value = Boolean.TRUE;
		// deploy the scp component with the coordinator
		scpOnAppIds.value.get(0).add(appId);
		// deploy the backup component with the coordinator
		buAppIds.value.get(0).add(appId);
		// deploy the backup component with the scp component
		buMoScpIds.value.get(0).add(scpIds.get(0));
		// deploy all the other application components
		for (int i = 0; i < appsIsDeployed.value.size(); i++){
			// deploy the application component coordinator
			appsOnScpIds.value.set(i, scpIds.get(i+1));
			appsBuScpIds.value.set(i, buIds.get(i+1));
			appsIsDeployed.value.set(i, Boolean.TRUE);
			// deploy the scp component with the coordinator
			scpOnAppIds.value.get(i+1).add(appsIds.get(i));
			// deploy the backup component with the coordinator
			buAppIds.value.get(i+1).add(appsIds.get(i));
			// deploy the backup component with the scp component
			buMoScpIds.value.get(i+1).add(scpIds.get(i));
			// user output
			appComponentIds += " " + appsIds.get(i);
			scpComponentIds += " " + scpIds.get(i+1);
			buComponentIds += " " + scpIds.get(i+1);
		}
		// final user output
		System.out.println("coordinator="+appId+ 
							"   AppComponents=" + appComponentIds + 
							"   ScpComponents=" +scpComponentIds +
							"   BuComponents=" +buComponentIds);
	}
}
