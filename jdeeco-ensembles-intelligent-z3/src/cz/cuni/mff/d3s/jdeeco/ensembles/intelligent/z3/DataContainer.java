package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.activation.UnsupportedDataTypeException;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;

import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.DataContractDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;

class DataContainer {
	private Map<String, DataContractInstancesContainer> containers;
	
	public DataContainer(Context ctx, String packageName, Collection<DataContractDefinition> dataContractDefinitions, 
			KnowledgeContainer knowledgeContainer) throws ClassNotFoundException, KnowledgeContainerException, UnsupportedDataTypeException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		containers = new HashMap<>();
		
		for (DataContractDefinition contract : dataContractDefinitions) {
			containers.put(contract.getName(), new DataContractInstancesContainer(ctx, packageName, contract, knowledgeContainer));
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