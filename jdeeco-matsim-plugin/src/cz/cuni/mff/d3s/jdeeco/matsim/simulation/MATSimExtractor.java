package cz.cuni.mff.d3s.jdeeco.matsim.simulation;

import java.util.Collection;
import java.util.Map;

import org.matsim.api.core.v01.Id;
import org.matsim.core.mobsim.framework.Mobsim;

public interface MATSimExtractor {
	public Map<Id, MATSimOutput> extractFromMATSim(Collection<JDEECoAgent> agents, Mobsim mobsim);
}
