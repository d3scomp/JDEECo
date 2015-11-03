package cz.cuni.mff.d3s.jdeeco.ros.sim;

import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.deeco.timer.WallTimeTimer;
import cz.cuni.mff.d3s.jdeeco.ros.RosServices;
import cz.cuni.mff.d3s.jdeeco.simulation.SimulationProvider;

public class ROSSimulation implements SimulationProvider {

	/**
	 * The ROS_MASTER_URI value.
	 */
	private String ros_master;

	/**
	 * The ROS_HOST address.
	 */
	private String ros_host;
	
	public ROSSimulation(String ros_master, String ros_host) {
		this.ros_master = ros_master;
		this.ros_host = ros_host;
	}

	/**
	 * Creates ROS services instance with parameters specific to this simulation
	 * @param namespace Robot namespace
	 * @return RosServices instance for robot
	 */
	public RosServices createROSServices(String namespace) {
		return new RosServices(ros_master, ros_host, namespace);
	}
	
	/**
	 * TODO: Report back ROS time, for now we expect simulation to be in sync with wall time
	 * 
	 * ROS simulation is not proper simulation, it simulates hardware, but not time compression. Simulated hardware
	 * runs at "almost" wall time and the controller (jDEECo) has to keep up with it.
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
