package cz.cuni.mff.d3s.jdeeco.edl.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.inject.Inject;
import com.google.inject.Injector;

import cz.cuni.mff.d3s.jdeeco.edl.EDLStandaloneSetup;
import cz.cuni.mff.d3s.jdeeco.edl.IFunctionRegistry;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.DataContractDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlDocument;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QualifiedName;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.TypeDefinition;

public class SimpleTypeResolutionContext implements ITypeResolutionContext {

	Map<String, TypeDefinition> dataTypes;
	
	@Inject
	IFunctionRegistry registry;
	
	public SimpleTypeResolutionContext(EdlDocument document) {
		new EDLStandaloneSetup().createInjector().injectMembers(this);		
		
		dataTypes = new HashMap();
		
		for(TypeDefinition d : document.getKnowledgeTypes()) {			
			dataTypes.put(d.getName(), d);
		}
		
		for(DataContractDefinition d : document.getDataContracts()) {			
			dataTypes.put(d.getName(), d);
		}
	}

	@Override
	public void reportError(String message, EObject source,
			EStructuralFeature feature) {
		// TODO Throw exception?

	}

	@Override
	public boolean isKnownType(QualifiedName name) {
		return dataTypes.containsKey(name.getName());
	}	
	
	@Override
	public TypeDefinition getDataType(QualifiedName name) {
		return dataTypes.get(name.getName());
	}

	@Override
	public IFunctionRegistry getFunctionRegistry() {
		return registry;
	}

}
