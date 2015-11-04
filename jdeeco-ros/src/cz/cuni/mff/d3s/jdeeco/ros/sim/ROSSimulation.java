package cz.cuni.mff.d3s.jdeeco.ros.sim;

import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.deeco.timer.WallTimeTimer;
import cz.cuni.mff.d3s.jdeeco.ros.RosServices;
import cz.cuni.mff.d3s.jdeeco.simulation.SimulationProvider;

public class ROSSimulation implements SimulationProvider {
	/**
	 * The ROS_MASTER_URI value.
	 * 
	 * Used to connect to ROS master via ROS protocol
	 */
	private final String ros_master_uri;

	/**
	 * The ROS_MASTER
	 * 
	 * Used to connect to ROS master in order to start up the ROS simulation
	 */
	private final String ros_master;

	/**
	 * The ROS_HOST address.
	 */
	private final String ros_host;

	/**
	 * Creates ROS simulation
	 * 
	 * @param ros_master
	 *            ROS machine hostname or IP address
	 * @param ros_host
	 *            Local hostname or IP address
	 */
	public ROSSimulation(String ros_master, int ros_master_port, String ros_host) {
		this.ros_master = ros_master;
		this.ros_host = ros_host;
		this.ros_master_uri = String.format("http://%s:%d", ros_master, ros_master_port);
	}

	/**
	 * Creates ROS services instance with parameters specific to this simulation
	 * 
	 * @param namespace
	 *            Robot namespace
	 * @return RosServices instance for robot
	 */
	public RosServices createROSServices(String namespace) {
		return new RosServices(ros_master_uri, ros_host, namespace);
	}

	/**
	 * TODO: Report back ROS time, for now we expect simulation to be in sync with wall time
	 * 
	 * ROS simulation is not proper simulation, it simulates hardware, but not time compression. Simulated hardware runs
	 * at "almost" wall time and the controller (jDEECo) has to keep up with it.
	 *
	 */
	class ROSTimer extends WallTimeTimer implements SimulationTimer {
		@Override
		public void start(long duration) {
			start();
		}
	};

	@Override
	public SimulationTimer getTimer() {
		return new ROSTimer();
	}
}
