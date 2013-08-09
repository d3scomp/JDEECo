package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.shutdown;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Selector;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;
/*
 * abbreviations in the ensemble
 * app:application, scp:ScienceCloudPlatform, sd:shutdown, onX:running the component X,
 */

/**
 * The Monitor Ensemble for the Shutdown Scenario (SS).
 * 
 * After the deployment, the backup scp component retrieves periodically snapshots of the app component.
 * It also monitors the scp component processing the app component in case of any shutdown.
 * If the scp component shutdowns, then the backup scp component takes its role and delegates his former one (backup-ing)
 * to another scp component which is not shutdown.
 * 
 * @author Julien Malvot
 * 
 */
public class MonitorSSEnsemble extends Ensemble {
	
	private static final long serialVersionUID = 1L;
	
	private static List<String> sdSelection(List<Boolean> sdSelectors, List<String> sdIds, List<Boolean> sdIsDown, List<String> cMoScpIds){
		List<String> sdMoScpIds = null;
		
		for (int i = 0; i < sdIds.size(); i++){
			sdSelectors.set(i, false);
		}
		
		for (int i = 0; i < cMoScpIds.size(); i++){
			String moScpId = cMoScpIds.get(i);
			Integer sdIdx = sdIds.indexOf(moScpId);
			// if the monitored scp id is down
			if (sdIsDown.get(sdIdx)){
				// if needed, initialize the list of shutdown monitored scp ids for the first time
				if (sdMoScpIds == null){
					sdMoScpIds = new ArrayList<String> ();
				}
				// and add the shutdown monitored id in the result
				sdMoScpIds.add(moScpId);
				// select the shutdown monitored id among the scp ids
				sdSelectors.set(sdIdx, true);
			}
		}
		return sdMoScpIds;
	}
	
	
	private static void appSelection(
			// AppComponent knowledge
			List<Boolean> appSelectors, List<String> appIds, List<Boolean> appIsDeployed,
			// ScpComponent knowledge
			List<String> scpIds, List<List<String>> scpOnAppIds,
			// Shutdown ScpComponent knowledge
			List<String> sdMoScpIds){
		// unselect all the possible application components
		for (int i = 0; i < appIds.size(); i++){
			appSelectors.set(i, false);
		}
		// for each shutdown scp component
		for (int i = 0; i < sdMoScpIds.size(); i++){
			// find the index of the shutdown scp component in the list of scp component
			Integer sdMoScpIdx = scpIds.indexOf(sdMoScpIds.get(i));
			// get detached app components which were attached previously to the shutdown scp component
			List<String> detachedOnScpAppIds = scpOnAppIds.get(sdMoScpIdx);
			// for each detached app components from the shutdown scp component
			for (int j = 0; j < detachedOnScpAppIds.size(); j++){
				Integer detachedOnScpAppIdx = appIds.indexOf(detachedOnScpAppIds.get(j));
				// if the detached app component is deployed 
				if (appIsDeployed.get(detachedOnScpAppIdx)){
					// select the detached app component 
					// in order to migrate it to the current backup component which monitored the shutdown
					appSelectors.set(detachedOnScpAppIdx, true);
				}
			}
		}
	}
	
	private static void scpSelection(List<Boolean> scpSelectors, String cId, 
			List<String> moScpIds, List<String> scpIds){
		// set all selections to false
		for (int i = 0; i < scpSelectors.size(); i++)
			scpSelectors.set(i, false);
		// select the scp backup component coordinator
		scpSelectors.set(scpIds.indexOf(cId), true);
	}
	
	protected static Integer buSelection(
			List<Boolean> buSelectors, List<String> buIds, List<List<Integer>> cores, 
			List<Boolean> scpSelectors, List<String> scpIds, List<Boolean> scpIsDown,  List<Boolean> buIsDown, 
			int range){
		// set all selections to false
		for (int i = 0; i < buSelectors.size(); i++)
			buSelectors.set(i, false);
		// select backup nodes which are not already selected for any running purpose
		Integer buSelectCount = 0;
		Integer buIndex = 0;
		while (buSelectCount < range && buIndex < buIds.size()){
			Integer scpBuIndex = scpIds.indexOf(buIds.get(buIndex));
			// the backup node must respect the SLA (2x(f>=2Ghz) cpu cores)
			if (!buIsDown.get(buIndex) && !scpSelectors.get(scpBuIndex) && !scpIsDown.get(scpBuIndex) && DeploySSEnsemble.respectSLAScpCores(cores.get(buIndex))){
				buSelectCount++;
				buSelectors.set(buIndex, true);
			}
			buIndex++;
		}
		return buSelectCount;
	}
			
	@Membership
	public static Boolean membership(
			// ScpComponent backup coordinator (1)
			@In("coord.id") String cId,
				// the monitored scp components (m)
			@In("coord.moScpIds") List<String> cMoScpIds,
			
			// AppComponent members (n)
			@Selector("app") List<Boolean> appSelectors,
			@In("members.app.id") List<String> appIds,
				// need this for the duck typing and state checking
			@In("members.app.isDeployed") List<Boolean> appIsDeployed,
			
			// ScpComponent members (n)
			@Selector("scp") List<Boolean> scpSelectors,
			@In("members.scp.id") List<String> scpIds,
			@In("members.scp.isDown") List<Boolean> scpIsDown,
				// need this for the duck typing
			@In("members.scp.onAppIds") List<List<String>> scpOnAppIds,	
			
			// ScpComponent backup members (n)
			@Selector("bu") List<Boolean> buSelectors,
			@In("members.bu.id") List<String> buIds,
			@In("members.bu.isDown") List<Boolean> buIsDown,
				// need this for the duck typing
			@In("members.bu.cores") List<List<Integer>> buCores,			
			
			// ScpComponent shutdown members
			@Selector("sd") List<Boolean> sdSelectors,
			@In("members.sd.id") List<String> sdIds,
			@In("members.sd.isDown") List<Boolean> sdIsDown
			) {
		String allSdComponentIds = "";
		// check if all monitored scp ids of the backup component are still existing
		List<String> sdMoScpIds = sdSelection(sdSelectors, sdIds, sdIsDown, cMoScpIds);
		// display all shutdown nodes
		Integer downCount = 0;
		for (int i = 0; i < scpIsDown.size(); i++){
			if (scpIsDown.get(i)){
				allSdComponentIds += " " + scpIds.get(i);
				downCount++;
			}
		}
		System.out.println(cId+": All ShutdownScps="+(!allSdComponentIds.isEmpty() ? allSdComponentIds : "none"));
		
		// some monitored scp ids are missing, find new scp ids which could solve the shutdown issue
		if (sdMoScpIds != null){
			String sdScpComponentIds = "";
			for (int i = 0; i < sdMoScpIds.size(); i++)
				sdScpComponentIds += " " + sdMoScpIds.get(i);
			System.out.println(cId+": ShutdownScps="+sdScpComponentIds);
			// app selection
			appSelection(appSelectors, appIds, appIsDeployed, scpIds, scpOnAppIds, sdMoScpIds);
			// scp selection
			scpSelection(scpSelectors, cId, cMoScpIds, scpIds);
			// count app selections
			Integer appSelectionCount = 0;
			for (int i = 0; i < appSelectors.size(); i++){
				if (appSelectors.get(i))
					appSelectionCount++;
			}
			// bu selection
			Integer buSelectCount = buSelection(buSelectors, buIds, buCores, scpSelectors, scpIds, scpIsDown, buIsDown, appSelectionCount);
			// accept the membership from the selections
			if (buSelectCount >= 1)
				return true;
			else
				System.out.println(cId+": Too many shutdown scps to find any other solution");
		}
		return false;
	}

	// to expand to different cdScpInstanceIds in case of high candidate range
	@KnowledgeExchange
	@PeriodicScheduling(3000)
	public static void map(
			// ScpComponent backup coordinator (1)
			@In("coord.id") String cId,
			@InOut("coord.moScpIds") OutWrapper<List<String>> cMoScpIds,
			@InOut("coord.onAppIds") OutWrapper<List<String>> cOnAppIds,
			@InOut("coord.buAppIds") OutWrapper<List<String>> cBuAppIds,
			
			// AppComponent members (n)
			@In("members.app.id") List<String> appsIds,
			@InOut("members.app.onScpId") OutWrapper<List<String>> appsOnScpIds,
			@InOut("members.app.buScpId") OutWrapper<List<String>> appsBuScpIds,
			
			// ScpComponent members (0)
			@In("members.scp.id") List<String> scpIds,
			@InOut("members.scp.isDown") OutWrapper<List<Boolean>> scpIsDown,
			
			// ScpComponent backup members (n)
			@In("members.bu.id") List<String> buIds,
			@InOut("members.bu.moScpIds") OutWrapper<List<List<String>>> buMoScpIds,
			@InOut("members.bu.buAppIds") OutWrapper<List<List<String>>> buOnAppIds,
			
			// ScpComponent shutdown members
			@In("members.sd.id") List<String> sdIds,
			@In("members.sd.isDown") List<Boolean> sdIsDown
			) {
		String appComponentIds = "";
		String scpComponentIds = cId;
		String buComponentIds = "";
		String sdComponentIds = "";
		// remove all shutdown scp components, which are no longer monitored by the backup component
		cMoScpIds.value.removeAll(sdIds);
		// the application components are now run by the scp backup component now turned into a scp component for those
		cOnAppIds.value.addAll(appsIds);
		cBuAppIds.value.removeAll(appsIds);
		//  each application component get a new scp component and a new scp backup component
		for (int i = 0; i < appsIds.size(); i++){
			appsOnScpIds.value.set(i, cId);
			// TODO: do not know yet how to attribute a backup node to an app node arbitraly
			appsBuScpIds.value.set(i, buIds.get(0));
			
			appComponentIds += appsIds.get(i) + " ";
		}
		//
		for (int i = 0; i < buIds.size(); i++){
			buMoScpIds.value.get(i).add(cId);
			buOnAppIds.value.get(i).addAll(appsIds);
			
			buComponentIds += buIds.get(i) + " ";
		}
		// reactivate the shutdown scp components
		for (int i = 0; i < sdIds.size(); i++){
			//sdIsDown.value.set(i, false);
			sdComponentIds += sdIds.get(i) + " "; 
		}
		
		System.out.println("Current BuScp="+cId+ 
							"-Apps=" + appComponentIds + 
							"-NewScps=" + scpComponentIds + 
							"-BuScps=" + buComponentIds +
							"-SdScps=" + sdComponentIds);
	}
}
