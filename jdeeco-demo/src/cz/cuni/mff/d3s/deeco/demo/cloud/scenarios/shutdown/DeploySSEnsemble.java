package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.shutdown;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Selector;
import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment.DeployDSEnsemble;
import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment.ScpDSComponentOSLatencyData;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

/**
 * The Deployment Ensemble for the Shutdown Scenario (SS).
 * 
 * This is called initially to deploy the application singleton onto a scp machine.
 * Similar to the deployment scenario with a single application instance.
 * The backup scp component can copy the snapshot of the app component only after this step.
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
	
	private static List<ScpDSComponentOSLatencyData> scpSelectLatenciesFromSLA(List<Map<String, ScpDSComponentOSLatencyData>> scpLatencies, List<List<Integer>> scpCores){
		// transforming the List<Map> data structure into a List data structure
		List<ScpDSComponentOSLatencyData> mLatencies = new ArrayList<ScpDSComponentOSLatencyData> ();
		for (int i = 0; i < scpLatencies.size(); i++){
			List<Integer> cores = scpCores.get(i);
			// if at least two-gigahertz frequency cores
			if (respectSLAScpCores(cores)){
				// get the ids which the scp is linked to
				Map<String,ScpDSComponentOSLatencyData> map = scpLatencies.get(i);
				Object[] toIdSet = scpLatencies.get(i).keySet().toArray();
				// iterate over all the link destinations
				for (int j = 0; j < toIdSet.length; j++){
					// get the latency
					ScpDSComponentOSLatencyData latencyData = map.get((Object)toIdSet[j]);
					// if the latency respects the Service Level Agreement max latency of the source
					// do not add a latency data which is already existing in the list
					if (latencyData.cache <= 50 && !mLatencies.contains(latencyData)){
						// add to the data structure
						mLatencies.add(latencyData);
					}
				}
			}
		}
		return mLatencies;
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
			@Selector("app") List<Boolean> appsSelectors,
			@In("members.app.id") List<String> appsIds,
			@In("members.app.isDeployed") List<Boolean> appsIsDeployed,
			@In("members.app.machineId") List<String> appsMachineIds,
			
			// ScpComponent members
			@Selector("scp") List<Boolean> scpSelectors,
			@In("members.scp.id") List<String> scpIds,
			@In("members.scp.latencies") List<Map<String, ScpDSComponentOSLatencyData>> scpLatencies,
			@In("members.scp.isDown") List<Boolean> IsDown,
			@In("members.scp.cores") List<List<Integer>> scpCores,
			
			// ScpComponent backup members
			@Selector("bu") List<Boolean> buSelectors,
			@In("members.bu.id") List<String> buIds,
			@In("members.bu.isDown") List<Boolean> buIsDown,
			@In("members.bu.cores") List<List<Integer>> buCores
			) {
		// none of the application components are deployed 
		// and there are enough scp components for running and backup-ing the application components
		if (!appIsDeployed && !appsIsDeployed.contains(true) && (2*appsIds.size()) <= (scpIds.size() + buIds.size())){
			// app selection
			for (int i = 0; i < appsIds.size(); i++){
				appsSelectors.set(i, appSelection(appId, appMachineId, appsIds.get(i), appsMachineIds.get(i))); 
			}
			// select the latencies based on the SLA
			List<ScpDSComponentOSLatencyData> slaSelectedLatencies = scpSelectLatenciesFromSLA(scpLatencies, scpCores);
			// scp selection
			DeployDSEnsemble.scpSelection(scpSelectors, scpIds, slaSelectedLatencies, appsIds.size());
			// bu selection (backup components)
			buSelection(buSelectors, buIds, buCores, scpSelectors, scpIds, appsIds.size());
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
