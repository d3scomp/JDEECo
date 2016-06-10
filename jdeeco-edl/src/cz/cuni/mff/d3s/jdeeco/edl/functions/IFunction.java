package cz.cuni.mff.d3s.jdeeco.edl.functions;

import java.util.List;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;
import cz.cuni.mff.d3s.jdeeco.edl.typing.ITypeInformationProvider;

public interface IFunction {
	List<String> getParameterTypes();
	Object evaluate(Object... params);
	String getReturnType(ITypeInformationProvider typing, EnsembleDefinition ensemble, Query... params);
}
