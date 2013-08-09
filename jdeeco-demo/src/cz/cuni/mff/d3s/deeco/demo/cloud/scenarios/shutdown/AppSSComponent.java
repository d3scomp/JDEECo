package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.shutdown;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.Snapshot;
import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment.AppDSComponent;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

/**
 * The User Application (App) Component for the Shutdown Scenario (SS).
 * 
 * The component needs be deployed onto a Scp component via the DeploySSEnsemble.
 * It takes periodically snapshots of its state into a Snapshot object.
 * When the Scp component which has deployed the App component shutdowns,
 * the App component gets deployed onto a new Scp component, created at runtime.
 * 
 * @author Julien Malvot
 * 
 */
public class AppSSComponent extends AppDSComponent {
	
	public final static long serialVersionUID = 1L;

	/** id of the backup scp component which makes snapshots of the component*/
	public String buScpId;
	
	/** component state snapshot which can be provided to a backup node */
	public Snapshot snapshot;
	
	/**constructor for the Application Component
	 * @param id
	 */
	public AppSSComponent(String id, String machineId) {
		super(id);
		this.machineId = machineId;
		this.buScpId = null;
		this.snapshot = null;
	}
	
	@Process
	@PeriodicScheduling(500)
	public static void takeSnapshot(
			@In("id") String id,
			@InOut("snapshot") OutWrapper<Snapshot> snapshot,
			@In("isDeployed") Boolean isDeployed,
			@In("onScpId") String onScpId,
			@In("buScpId") String buScpId) {
		// no need to put the app component id as the backup node can retrieve it
		if (isDeployed){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream w = new DataOutputStream(baos);
			try {
				w.writeBytes(onScpId);
				w.writeBytes(buScpId);
				w.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			snapshot.value = new Snapshot((snapshot.value == null ? 0 : snapshot.value.timestamp+1), baos.toByteArray());
			System.out.println(id + ", update snapshot, Scp="+onScpId+", buScp="+buScpId);
		}
	}
}
