package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.Map;

/**
 * Interface for MATSim data retrieval. This data comes from MATSim side.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public interface MATSimDataReceiver {
	public void setMATSimData(Map<String, ?> data);
}
