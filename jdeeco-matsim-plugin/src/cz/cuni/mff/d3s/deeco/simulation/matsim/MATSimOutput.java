package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.List;

import org.matsim.api.core.v01.Id;
import org.matsim.core.mobsim.framework.MobsimAgent.State;

/**
 * Data coming from MATSim.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class MATSimOutput {
	public Id currentLinkId;
	public State state;
	
	public MATSimOutput(Id currentLinkId, State state) {
		this.currentLinkId = currentLinkId;
		this.state = state;
	}
	
	
}
