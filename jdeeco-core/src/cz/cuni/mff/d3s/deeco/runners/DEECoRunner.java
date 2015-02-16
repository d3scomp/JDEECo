package cz.cuni.mff.d3s.deeco.runners;

import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.RunnerTimer;

/**
 * Main entry for running DEECo in "real" deployment.
 * For simulation purposes use {@link DEECoSimulation} instead.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class DEECoRunner {

	DEECoNode deecoNode;
	RunnerTimer runnerTimer;
	
	public DEECoRunner(RunnerTimer runnerTimer, DEECoNode deecoNode) {
		this.runnerTimer = runnerTimer;
		this.deecoNode = deecoNode;
	}
	
	public void start() {
		runnerTimer.start();
	}

}
