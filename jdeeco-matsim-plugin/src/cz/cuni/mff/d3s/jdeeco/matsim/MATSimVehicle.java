package cz.cuni.mff.d3s.jdeeco.matsim;

import java.util.Arrays;
import java.util.List;

import org.matsim.api.core.v01.Id;
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
	private DEECoContainer container;
	
	// Initial configuration, either link or coordinates can be provided
	private Id startLink;
	private CoordImpl startPosition;
	
	public MATSimVehicle(double x, double y) {
		startPosition = new CoordImpl(x, y);
	}
	
	public MATSimVehicle(Id startLink) {
		this.startLink = startLink;
	}
	
	public SensorProvider getSensorProvider() {
		return simulation.getMATSimProviderReceiver().getSensorProvider(new IdImpl(getId()));
	}
	
	public ActuatorProvider getActuatorProvider() {
		return simulation.getMATSimProviderReceiver().getActuatorProvider(new IdImpl(getId()));
	}
	
	public MATSimSimulation getSimulation() {
		return simulation;
	}
	
	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(MATSimSimulation.class);
	}
	
	@Override
	public void init(DEECoContainer container) {
		this.container = container; 
		simulation = container.getPluginInstance(MATSimSimulation.class);
		
		if(startLink == null) {
			startLink = simulation.getRouter().findNearestLink(startPosition).getId();
		}
		
		// Add vehicle to simulation
		simulation.addVehicle(getId(), startLink);
	}
	
	protected String getId() {
		return "V" + container.getId();
	}

}
