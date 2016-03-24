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
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.DataContractDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlDocument;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;

class DataContainer {
	private EdlDocument edlDocument;
	private Map<String, DataContractInstancesContainer> containers;
	
	public DataContainer(Context ctx, Optimize opt, String packageName, EdlDocument edlDocument, KnowledgeContainer knowledgeContainer)
			throws ClassNotFoundException, KnowledgeContainerException, UnsupportedDataTypeException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		this.edlDocument = edlDocument;
		containers = new HashMap<>();
		
		for (DataContractDefinition contract : edlDocument.getDataContracts()) {
			containers.put(contract.getName(), new DataContractInstancesContainer(ctx, opt, packageName, contract, knowledgeContainer));
		}
	}
	
	public int getMaxEnsembleCount(EnsembleDefinition ensembleDefinition) {
		int result = Integer.MAX_VALUE;
		for(DataContractInstancesContainer dataContractInstance : containers.values()) {
			int maxEnsembleCount = dataContractInstance.getMaxEnsembleCount(ensembleDefinition);
			if (maxEnsembleCount < result) {
				result = maxEnsembleCount;
			}
		}
		
		return result;
	}
	
	public EdlDocument getEdlDocument() {
		return edlDocument;
	}
	
	public Collection<DataContractInstancesContainer> getAllDataContracts() {
		return containers.values();
	}
	
	public DataContractInstancesContainer get(String dataContractName) {
		return containers.get(dataContractName);
	}
	
	public Expr get(String dataContractName, String fieldName, Expr componentIndex) {
		return get(dataContractName).get(fieldName, componentIndex);
	}
	
	public int getNumInstances(String dataContractName) {
		return get(dataContractName).getNumInstances();
	}
	
	public Object getInstance(String dataContractName, int componentIndex) {
		return get(dataContractName).getInstance(componentIndex);
	}
}