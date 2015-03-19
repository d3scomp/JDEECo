package cz.cuni.mff.d3s.jdeeco.matsim;

import java.util.Arrays;
import java.util.List;

import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.utils.geometry.CoordImpl;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.matsim.old.roadtrains.ActuatorProvider;
import cz.cuni.mff.d3s.jdeeco.matsim.old.roadtrains.SensorProvider;

/**
 * jDEECo plug-in that provides MATSim vehicle agent
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class MATSimVehicle implements DEECoPlugin {
	private MATSimSimulation simulation;
	private final CoordImpl startPos;
	private DEECoContainer container;
	
	public MATSimVehicle(CoordImpl startPosition) {
		startPos = startPosition;
	}
	
	public SensorProvider getSensorProvider() {
		return simulation.getMATSimProviderReceiver().getSensorProvider(new IdImpl(container.getId()));
	}
	
	public ActuatorProvider getActuatorProvider() {
		return simulation.getMATSimProviderReceiver().getActuatorProvider(new IdImpl(container.getId()));
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(MATSimSimulation.class);
	}
	
	@Override
	public void init(DEECoContainer container) {
		this.container = container; 
		simulation = container.getPluginInstance(MATSimSimulation.class);
		
		// Add vehicle to simulation
		simulation.addVehicle(container.getId(), simulation.getRouter().findNearestLink(startPos).getId());
	}

}
