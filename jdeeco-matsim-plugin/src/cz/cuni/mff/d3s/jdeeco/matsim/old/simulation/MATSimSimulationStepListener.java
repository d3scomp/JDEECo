package cz.cuni.mff.d3s.jdeeco.matsim.old.simulation;

import org.matsim.core.mobsim.framework.Mobsim;

public interface MATSimSimulationStepListener {

	public void at(double seconds, Mobsim mobsim);
}
