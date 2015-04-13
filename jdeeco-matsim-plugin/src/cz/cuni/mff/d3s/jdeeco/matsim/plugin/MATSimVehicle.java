package cz.cuni.mff.d3s.jdeeco.matsim.plugin;

import java.util.Arrays;
import java.util.List;

import org.matsim.api.core.v01.Id;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.utils.geometry.CoordImpl;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;
import cz.cuni.mff.d3s.jdeeco.matsim.dataaccess.ActuatorProvider;
import cz.cuni.mff.d3s.jdeeco.matsim.dataaccess.SensorProvider;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimRouter;

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

	/**
	 * Creates vehicle
	 * 
	 * Vehicle is initially located at specified coordinates
	 * 
	 * @param x
	 *            position coordinate
	 * @param y
	 *            position coordinate
	 */
	public MATSimVehicle(double x, double y) {
		startPosition = new CoordImpl(x, y);
	}

	/**
	 * Create vehicle
	 * 
	 * Vehicle is initially located on the specified link
	 * 
	 * @param startLink
	 */
	public MATSimVehicle(Id startLink) {
		this.startLink = startLink;
	}

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
		return Arrays.asList(MATSimSimulation.class);
	}

	@Override
	public void init(DEECoContainer container) {
		this.container = container;
		simulation = container.getPluginInstance(MATSimSimulation.class);

		if (startLink == null) {
			startLink = simulation.getRouter().findNearestLink(startPosition).getId();
		}

		// Add vehicle to simulation
		simulation.addVehicle(container.getId(), startLink);
	}
}
