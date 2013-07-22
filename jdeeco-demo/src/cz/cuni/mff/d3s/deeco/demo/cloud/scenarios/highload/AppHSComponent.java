package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.highload;

import cz.cuni.mff.d3s.deeco.knowledge.Component;

/**
 * 
 * @author Julien Malvot
 *
 */
public class AppHSComponent extends Component {
	
	public final static long serialVersionUID = 1L;

	/**
	 * id of the SCP instance which the application component is processed by
	 */
	public String scpId;
	
	/**
	 * flag for deployment
	 */
	public Boolean isDeployed;
	
	/**
	 * constructor for the Application Component
	 * @param id
	 */
	public AppHSComponent(String id, String scpId, Boolean isDeployed) {
		this.id = id;
		this.scpId = scpId;
		this.isDeployed = isDeployed;
	}
	
	/*@Process
	@PeriodicScheduling(5000)
	public static void process(@In("id") String id, @Out("isDeployed") OutWrapper<Boolean> isDeployed) {
		isDeployed.value = Boolean.FALSE;
		System.out.println(id + " has been undeployed");
	}*/
}
