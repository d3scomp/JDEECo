package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.Map;

import org.matsim.api.core.v01.Id;

/**
 * Interface for MATSim data i.e. the data that is for the MATSim side.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public interface MATSimDataProvider {
	public Map<Id, ?> getMATSimData();
}
