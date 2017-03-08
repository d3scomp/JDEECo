package cz.cuni.mff.d3s.jdeeco.edl.typing;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.inject.Inject;
import com.google.inject.Injector;

import cz.cuni.mff.d3s.jdeeco.edl.EDLStandaloneSetup;
import cz.cuni.mff.d3s.jdeeco.edl.functions.IFunctionRegistry;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.DataContractDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlDocument;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QualifiedName;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.TypeDefinition;

public class SimpleDataTypeContext implements IDataTypeContext {

	Map<String, TypeDefinition> dataTypes;
	
	@Inject
	IFunctionRegistry registry;
	
	public SimpleDataTypeContext(EdlDocument document) {
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
