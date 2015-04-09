package cz.cuni.mff.d3s.jdeeco.matsim.old.simulation;

import java.util.Collection;

import org.matsim.core.mobsim.framework.Mobsim;

public interface MATSimExtractor {
	public Object extractFromMATSim(Collection<JDEECoAgent> agents, Mobsim mobsim);
}
