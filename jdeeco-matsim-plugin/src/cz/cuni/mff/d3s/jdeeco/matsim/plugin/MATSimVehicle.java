package cz.cuni.mff.d3s.jdeeco.matsim.plugin;

import java.util.Arrays;
import java.util.List;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.utils.geometry.CoordImpl;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;
import cz.cuni.mff.d3s.jdeeco.matsim.dataaccess.ActuatorProvider;
import cz.cuni.mff.d3s.jdeeco.matsim.dataaccess.Sensor;
import cz.cuni.mff.d3s.jdeeco.matsim.dataaccess.SensorProvider;
import cz.cuni.mff.d3s.jdeeco.matsim.dataaccess.SensorType;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimRouter;
import cz.cuni.mff.d3s.jdeeco.position.Position;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.position.PositionProvider;

/**
 * jDEECo plug-in that provides MATSim vehicle agent
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class MATSimVehicle implements DEECoPlugin, PositionProvider {
	private MATSimSimulation simulation;
	private DEECoContainer container;
	private Sensor<Id> currentLinkSensor;
	
	/**
	 * Gets vehicle sensor provider
	 * 
	 * Used to create sensors to obtain data from vehicle.
	 * 
	 * @return Sensor provider
	 */
	public SensorProvider getSensorProvider() {
		return simulation.getMATSimProviderReceiver().getSensorProvider(new IdImpl(container.getId()));
	}

	/**
	 * Gets vehicle actuator provider
	 * 
	 * Used to create actuator to control the vehicle.
	 * 
	 * @return Actuator provider
	 */
	public ActuatorProvider getActuatorProvider() {
		return simulation.getMATSimProviderReceiver().getActuatorProvider(new IdImpl(container.getId()));
	}

	/**
	 * Gets simulation router
	 * 
	 * Used to find routes in the simulated world.
	 * 
	 * @return Simulation router
	 */
	public MATSimRouter getRouter() {
		return simulation.getRouter();
	}

	/**
	 * Gets time provider
	 * 
	 * Used to get current time in the simulated world.
	 * 
	 * @return Time provider
	 */
	public CurrentTimeProvider getTimer() {
		return simulation.getTimer();
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return Arrays.asList(MATSimSimulation.class, PositionPlugin.class);
	}
	
	private Coord positionToCoord(final Position position) {
		if(position.z != 0) {
			throw new UnsupportedOperationException("MATSim cannot handle positions with z != 0");
		}
		
		CoordImpl coordinates = new CoordImpl(position.x, position.y);
		return coordinates;
	}

	@Override
	public void init(DEECoContainer container) {
		this.container = container;
		simulation = container.getPluginInstance(MATSimSimulation.class);
		
		// Obtain start position from position plug-in
		PositionPlugin positionPlugin = container.getPluginInstance(PositionPlugin.class);
		Coord coord = positionToCoord(positionPlugin.getStaticPosition());
		Id startLink = simulation.getRouter().findNearestLink(coord).getId();

		// Add vehicle to simulation
		simulation.addVehicle(container.getId(), startLink);
		
		// Setup this as position provider for the node
		currentLinkSensor = getSensorProvider().createSensor(SensorType.CURRENT_LINK);
		positionPlugin.setProvider(this);
	}

	/**
	 * Gets MATSim agent position from simulation and passes
	 *  
	 * @return Current agent position
	 */
	@Override
	public Position getPosition() {
		Id currentLink = currentLinkSensor.read();
		Coord coord = getRouter().findLinkById(currentLink).getCoord();
		return new Position(coord.getX(), coord.getY(), 0);
	}
}
