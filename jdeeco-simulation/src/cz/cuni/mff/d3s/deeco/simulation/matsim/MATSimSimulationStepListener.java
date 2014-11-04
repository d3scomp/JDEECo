package cz.cuni.mff.d3s.deeco.simulation.matsim;

import org.matsim.core.mobsim.framework.Mobsim;

public interface MATSimSimulationStepListener {

	public void at(double seconds, Mobsim mobsim);
}
