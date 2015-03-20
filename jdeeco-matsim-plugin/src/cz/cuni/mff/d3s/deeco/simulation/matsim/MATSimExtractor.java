package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.Collection;

import org.matsim.core.mobsim.framework.Mobsim;

public interface MATSimExtractor {
	public Object extractFromMATSim(Collection<JDEECoAgent> agents, Mobsim mobsim);
}
