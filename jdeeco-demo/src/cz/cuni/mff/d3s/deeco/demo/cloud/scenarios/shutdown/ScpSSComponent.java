package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.shutdown;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;
import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.ENetworkId;
import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.Snapshot;
import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment.ScpDSComponent;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

/**
 * The Science Cloud Platform (Scp) Component for the Shutdown Scenario (SS)
 * 
 * A scp ss component can play different roles here :
 * <ul>
 * <li>It can monitor other scp components listed in the moScpIds list.</li>
 * <li>It can backup app components listed in the buAppIds and taking their respective snapshot into the buSnapshots list.</li>
 * <li>It can process app components listed in the superclass ScpDSComponent from the deployment scenario.</li>
 * </ul>
 * 
 * The specification stipulates a SLA on the cores with a minimum number of two, and frequencies high or equal to 2 Ghz.
 * This is interpreted into the cores list.
 * 
 * @author Julien Malvot
 * 
 */
public class ScpSSComponent extends ScpDSComponent {

	public final static long serialVersionUID = 1L;

	/** ids of the scp nodes whom the component monitors */
	public List<String> moScpIds;
	/** ids of the backed-up application nodes whom the component makes snapshots */
	public List<String> buAppIds;
	/** snapshots of the backed-up application nodes */
	public List<Snapshot> buSnapshots;
	/** list of cores with the frequency in MHz */
	public List<Integer> cores;
	/** flag to indicate if the component is shutdown (true) or not */
	public Boolean isDown;
	
	/**
	 * constructor with input network id parameter
	 * 
	 * @param networkId
	 *            the network id of the Scp component among the cloud including
	 *            different networks
	 * @see EScenarioNetworkId
	 */
	public ScpSSComponent(String id, ENetworkId networkId, List<Integer> cores) {
		super(id, networkId);
		
		this.moScpIds = new ArrayList<String>();
		this.buAppIds = new ArrayList<String>();
		this.buSnapshots = new ArrayList<Snapshot>();
		this.cores = cores;
		this.isDown = false;
	}	
	
	@Process
	@PeriodicScheduling(4000)
	public static void shutdown(@In("id") String id, @In("onAppIds") List<String> onAppIds, @InOut("isDown") OutWrapper<Boolean> isDown){
		// if the node is processing some app components, shut it down intentionally
		// this is done by removing it from the demo runtime
		if (onAppIds.size() > 0){
			isDown.value = true;
		}
	}
	
}
