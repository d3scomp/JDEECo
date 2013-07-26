package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.ENetworkId;
import cz.cuni.mff.d3s.deeco.knowledge.Component;

/**
 * 
 * @author Julien Malvot
 *
 */
public class ScpDSComponent extends Component {
	
	public final static long serialVersionUID = 1L;
	
	/** network id where the component is running */
	public ENetworkId networkId;
	/** os latency data with cached list of latencies between the component and other components */
	public Map<String, ScpDSComponentOSLatencyData> latencies;
	/** ids of the application nodes processed by the component */
	public List<String> onAppIds;
	
	/**
	 * constructor with input network id parameter
	 * @param networkId the network id of the Scp component among the cloud including different networks
	 * @see EScenarioNetworkId
	 */
	public ScpDSComponent(String id, ENetworkId networkId) {
		this.id = id;
		this.networkId = networkId;
		this.latencies = new HashMap<String, ScpDSComponentOSLatencyData>();
		this.onAppIds = new ArrayList<String>();
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
