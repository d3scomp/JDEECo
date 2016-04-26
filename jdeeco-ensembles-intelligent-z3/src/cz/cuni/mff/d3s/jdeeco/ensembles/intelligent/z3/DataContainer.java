package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.activation.UnsupportedDataTypeException;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Optimize;

import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;
import cz.cuni.mff.d3s.jdeeco.edl.BaseDataContract;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.DataContractDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlDocument;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.utils.ITypeResolutionContext;

class DataContainer {
	private EdlDocument edlDocument;
	private Map<String, DataContractInstancesContainer> containers;
	private int maxEnsembleCount;
	
	public DataContainer(Context ctx, Optimize opt, String packageName, EdlDocument edlDocument, KnowledgeContainer knowledgeContainer,
			ITypeResolutionContext typeResolution, EnsembleDefinition ensembleDefinition)
			throws ClassNotFoundException, KnowledgeContainerException, UnsupportedDataTypeException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		this.edlDocument = edlDocument;
		containers = new HashMap<>();
		
		FilteredKnowledgeContainer filteredKnowledgeContainer = new FilteredKnowledgeContainer(knowledgeContainer, typeResolution);
		filteredKnowledgeContainer.load(edlDocument, packageName);
		maxEnsembleCount = filteredKnowledgeContainer.getMaxEnsembleCount(edlDocument, ensembleDefinition);
		for (RoleDefinition roleDef : ensembleDefinition.getRoles()) {
			containers.put(roleDef.getName(), new DataContractInstancesContainer(ctx, opt, roleDef, typeResolution, 
					filteredKnowledgeContainer, roleDef.getWhereFilter(), maxEnsembleCount));
		}
	}
	
	public int getMaxEnsembleCount() {
		return maxEnsembleCount;
	}
	
	public EdlDocument getEdlDocument() {
		return edlDocument;
	}
	
	public Collection<DataContractInstancesContainer> getAllDataContracts() {
		return containers.values();
	}
	
	public DataContractInstancesContainer get(String roleName) {
		return containers.get(roleName);
	}
	/*
	public Expr get(String roleName, String fieldName, Expr componentIndex) {
		return get(roleName).get(fieldName, componentIndex);
	}*/
	
	public int getNumInstances(String roleName, int ensembleIndex) {
		return get(roleName).getNumInstances(ensembleIndex);
	}
	
	public BaseDataContract getInstance(String roleName, int componentIndex, int ensembleIndex) {
		return get(roleName).getInstance(componentIndex, ensembleIndex);
	}
	/*
	public int getInstanceGlobalIndex(String roleName, int componentIndex, int ensembleIndex) {
		return get(roleName).getInstanceGlobalIndex(componentIndex, ensembleIndex);
	}*/
}