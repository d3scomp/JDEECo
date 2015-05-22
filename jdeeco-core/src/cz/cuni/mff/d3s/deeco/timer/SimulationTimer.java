package cz.cuni.mff.d3s.deeco.timer;

public interface SimulationTimer extends Timer {
	/**
	 * Run the simulation
	 * 
	 * @param duration
	 *            Simulation duration in ms
	 */
	public void start(long duration);
}
