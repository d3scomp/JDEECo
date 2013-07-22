package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.highload;

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
public class ScpHSComponent extends Component {

	public final static long serialVersionUID = 1L;

	/** the network which the application is running within */
	public ENetworkId networkId;
	/** the machine which the application is running on */
	public String machineId;
	/** */
	//public Map<String, Long> latencies;
	/** id of the application nodes which are linked to the scp component */
	public List<String> appIds;
	/** load of the scp component provided by the underlying OS 
	 * and based on the number of ready threads at some period of time */
	public Long load;
	/**
	 * constructor with input network id parameter
	 * 
	 * @param networkId
	 *            the network id of the Scp component among the cloud including
	 *            different networks
	 * @see EScenarioNetworkId
	 */
	public ScpHSComponent(String id, ENetworkId networkId) {
		this.id = id;
		this.networkId = networkId;
		//this.latencies = new HashMap<String, Long>();
		this.appIds = new ArrayList<String>();
	}

	/*
	 * @Process
	 * 
	 * @PeriodicScheduling(6000) public static void process(@In("id") String id,
	 * @In("linkedScpInstanceIds") List<String> scpIds) { //loadRatio.value =
	 * (new Random()).nextFloat();
	 * 
	 * System.out.print(id + " linked with"); for (String str : scpIds){
	 * System.out.println(" " + str); } System.out.println(); //+
	 * Math.round(loadRatio.value * 100) + "%"); }
	 */
}
