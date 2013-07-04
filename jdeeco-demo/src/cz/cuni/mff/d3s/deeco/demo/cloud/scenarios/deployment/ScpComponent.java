package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.middleware.network.ENetworkId;
import cz.cuni.mff.d3s.deeco.runtime.middleware.network.NetworkComponent;

/**
 * 
 * @author Julien Malvot
 *
 */
public class ScpComponent extends NetworkComponent {
	
	public final static long serialVersionUID = 1L;

	/**
	 * the networkId which the SCP component belongs to.
	 * @see EScenarioNetworkId
	 */
	//public ENetworkId networkId;
	/**
	 * the SCP components which the SCP component are all linked to.
	 */
	public List<String> linkedScpInstanceIds;
	/**
	 * this boolean is used during the Application Assignment process
	 * to assign a AppComponent to a ScpComponent w.r.t. the preceding assignments
	 */
	public Boolean isAssigned;
	/**
	 * this boolean is used during the SCP instances interconnection process
	 * to link a ScpComponent to a ScpComponent, once it's linked, it can not be linked to new SCP instances anymore
	 */
	public Boolean isLinked;	
	/**
	 * constructor with input network id parameter
	 * @param networkId the network id of the Scp component among the cloud including different networks
	 * @see EScenarioNetworkId
	 */
	public ScpComponent(String id, ENetworkId networkId) {
		this.id = id;
		this.networkId = networkId;
		this.linkedScpInstanceIds = new ArrayList<String>();
		this.isAssigned = false;
		this.isLinked = false;
	}
	
	/*@Process
	@PeriodicScheduling(6000)
	public static void process(@In("id") String id, @In("linkedScpInstanceIds") List<String> scpIds) {
		//loadRatio.value = (new Random()).nextFloat();
		
		System.out.print(id + " linked with");
		for (String str : scpIds){
			System.out.println(" " + str);
		}
		System.out.println();
				//+ Math.round(loadRatio.value * 100) + "%");
	}*/
}
