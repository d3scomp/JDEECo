package cz.cuni.mff.d3s.jdeeco.ros.sim;

import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.deeco.timer.WallTimeTimer;
import cz.cuni.mff.d3s.jdeeco.simulation.SimulationProvider;

public class ROSSimulation implements SimulationProvider {

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
