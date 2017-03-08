package cz.cuni.mff.d3s.jdeeco.edl.typing;

import cz.cuni.mff.d3s.jdeeco.edl.functions.IFunctionRegistry;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QualifiedName;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.TypeDefinition;

public interface IDataTypeContext {
	boolean isKnownType(QualifiedName name);
	TypeDefinition getDataType(QualifiedName name);
	IFunctionRegistry getFunctionRegistry();
}
