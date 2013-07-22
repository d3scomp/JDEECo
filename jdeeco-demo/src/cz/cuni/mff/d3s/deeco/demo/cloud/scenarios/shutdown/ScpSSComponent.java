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
			//LocalLauncherSSNoJPF.demoRuntime.unregisterComponent(id);
	}
	
}
