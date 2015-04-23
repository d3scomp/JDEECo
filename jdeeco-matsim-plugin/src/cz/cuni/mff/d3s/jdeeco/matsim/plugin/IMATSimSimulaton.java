package cz.cuni.mff.d3s.jdeeco.matsim.plugin;

import org.matsim.api.core.v01.Id;

import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.matsim.dataaccess.MATSimDataProviderReceiver;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimRouter;
import cz.cuni.mff.d3s.jdeeco.simulation.SimulationProvider;

public interface IMATSimSimulaton extends DEECoPlugin, SimulationProvider {
	public MATSimRouter getRouter();
	
	public MATSimDataProviderReceiver getMATSimProviderReceiver();
	
	public void addVehicle(int vehicleId, Id startLink);
}
