package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;


import java.util.Collection;


public interface ModelInterface<T> {

	public InaccuracyParamHolder<T> getModelBoundaries(Collection<InaccuracyParamHolder<T>> eList);
}
