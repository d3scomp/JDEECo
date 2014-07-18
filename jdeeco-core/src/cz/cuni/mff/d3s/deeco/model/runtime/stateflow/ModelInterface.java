package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;


import java.util.Collection;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;




public interface ModelInterface<T> {

	public InaccuracyParamHolder<T> getModelBoundaries(Collection<InaccuracyParamHolder<T>> eList);
}
