package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.knowledge.Component;

/**
 * 
 * @author Julien Malvot
 *
 */
public class ScpComponent extends Component {
	
	public final static long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public ENetworkId networkId;
	/**
	 * 
	 */
	public Map<String,Long> latencies; 
	/**
	 * id of the application node which is linked
	 */
	public List<String> appIds;
	/**
	 * constructor with input network id parameter
	 * @param networkId the network id of the Scp component among the cloud including different networks
	 * @see EScenarioNetworkId
	 */
	public ScpComponent(String id, ENetworkId networkId) {
		this.id = id;
		this.networkId = networkId;
		this.latencies = new HashMap<String,Long>();
		this.appIds = new ArrayList<String>();
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
