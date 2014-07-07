package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import java.util.Collection;


public interface ModelInterface {

	public InaccurateValueDefinition getModelBoundaries(Collection<InaccurateValueDefinition> eList);
}
