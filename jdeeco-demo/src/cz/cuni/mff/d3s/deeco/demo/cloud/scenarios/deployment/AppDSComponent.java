package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment;

import cz.cuni.mff.d3s.deeco.knowledge.Component;

/**
 * The User Application (App) Component for the Deployment Scenario (DS).
 * 
 * The component needs be deployed onto a Scp component via the DeployDSEnsemble.
 * The isDeployed flag carries the deployment status of the application component.
 * whereas the onScpId string equals the id of the scp component which has deployed the app component.
 * 
 * @author Julien Malvot
 * 
 */
public class AppDSComponent extends Component {
	
	public final static long serialVersionUID = 1L;

	/** id of the scp component running the component */
	public String onScpId;
	/** id of the machine whom the component belongs */
	public String machineId;
	/** flag indicating if the application has been deployed */
	public Boolean isDeployed;
	
	/** constructor for the Application Component
	 * @param id
	 */
	public AppDSComponent(String id) {
		this.id = id;
		this.onScpId = null;
		this.machineId = null;
		this.isDeployed = false;
	}
}
