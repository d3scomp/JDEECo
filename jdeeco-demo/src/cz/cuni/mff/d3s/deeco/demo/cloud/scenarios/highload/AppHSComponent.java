package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.highload;

import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment.AppDSComponent;
import cz.cuni.mff.d3s.deeco.knowledge.Component;

/**
 * The User Application (App) Component for the Highload Scenario (HS).
 * 
 * The component needs be deployed onto a Scp component via the DeploySSEnsemble.
 * It takes periodically snapshots of its state into a Snapshot object.
 * When the Scp component which has deployed the App component shutdowns,
 * the App component gets deployed onto a new Scp component, created at runtime.
 * 
 * @author Julien Malvot
 * 
 */
public class AppHSComponent extends AppDSComponent {
	
	public final static long serialVersionUID = 1L;
	
	/**
	 * constructor for the Application Component
	 * @param id
	 */
	public AppHSComponent(String id, String machineId, String scpId, Boolean isDeployed) {
		super(id);
		this.machineId = machineId;
		this.onScpId = scpId;
		this.isDeployed = isDeployed;
	}
	
	/*@Process
	@PeriodicScheduling(5000)
	public static void process(@In("id") String id, @Out("isDeployed") OutWrapper<Boolean> isDeployed) {
		isDeployed.value = Boolean.FALSE;
		System.out.println(id + " has been undeployed");
	}*/
}
