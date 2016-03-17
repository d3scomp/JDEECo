package cz.cuni.mff.d3s.jdeeco.edl.utils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import cz.cuni.mff.d3s.jdeeco.edl.IFunctionRegistry;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QualifiedName;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.TypeDefinition;

public interface ITypeResolutionContext {
	void reportError(String message, EObject source, EStructuralFeature feature);
	boolean isKnownType(QualifiedName name);
	TypeDefinition getDataType(QualifiedName name);
	IFunctionRegistry getFunctionRegistry();
}
