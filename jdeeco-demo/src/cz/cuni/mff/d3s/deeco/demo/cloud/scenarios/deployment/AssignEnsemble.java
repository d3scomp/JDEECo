package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

/**
 * 
 * @author Julien Malvot
 *
 */
public class AssignEnsemble extends Ensemble {
	
	private static final long serialVersionUID = 1L;

	@Membership
	public static Boolean membership(
			// input coordinator SCP instance
			@In("coord.id") String appInstanceId,
			@InOut("coord.isDeployed") OutWrapper<Boolean> isAppDeployed,
			// candidate ids supplied by the framework
			// there is only one in the current scenario
			@In("member.id") String scpInstanceId,
			@In("member.linkedScpInstanceIds") List<String> linkedScpInstanceIds,
			@InOut("member.isAssigned") OutWrapper<Boolean> isScpAssigned) {
		// if the linked SCP instances have been already created by the framework
		// and the non-assignment of the SCP instance can be ensured then
		// we can assign the application instance to the SCP instance
		if (!isAppDeployed.value.booleanValue() && !isScpAssigned.value.booleanValue() && linkedScpInstanceIds != null && !linkedScpInstanceIds.isEmpty()){
			isScpAssigned.value = Boolean.TRUE;
			isAppDeployed.value = Boolean.TRUE;
			return true;
		}
		return false;
	}

	@KnowledgeExchange
	@PeriodicScheduling(3000)
	public static void map(@Out("member.scpInstanceId") OutWrapper<String> appScpInstanceId,
			@In("coord.id") String appInstanceId,
			@In("member.id") String scpInstanceId) {
		System.out.println(appInstanceId + " processed by " + scpInstanceId);
		appScpInstanceId.value = scpInstanceId;
	}

}
