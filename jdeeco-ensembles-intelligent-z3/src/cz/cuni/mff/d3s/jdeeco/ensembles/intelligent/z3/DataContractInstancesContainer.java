package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.UnsupportedDataTypeException;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Optimize;

import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;
import cz.cuni.mff.d3s.jdeeco.edl.BaseDataContract;
import cz.cuni.mff.d3s.jdeeco.edl.PrimitiveTypes;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.DataContractDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EquitableQuery;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FieldDeclaration;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.utils.ITypeResolutionContext;

class DataContractInstancesPerEnsembleContainer {
	private List<? extends BaseDataContract> instances;
	//private int[] globalIndices;
	private Map<String, KnowledgeFieldVector> knowledgeFields;
	
	public DataContractInstancesPerEnsembleContainer(Context ctx, Optimize opt, DataContractDefinition dataContractDefinition,
			FilteredKnowledgeContainer filteredKnowledgeContainer, EquitableQuery whereClause, String roleName, int ensembleIndex)
			throws UnsupportedDataTypeException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		this.instances = filteredKnowledgeContainer.getFilteredTrackedKnowledge(dataContractDefinition.getName(), whereClause, ensembleIndex);
		//this.globalIndices = filteredKnowledgeContainer.getGlobalIndices(instances);
		
		knowledgeFields = new HashMap<>();
		
		for (FieldDeclaration fieldDecl : dataContractDefinition.getFields()) {
			String fieldName = fieldDecl.getName();
			String fieldType = fieldDecl.getType().toString();
			KnowledgeFieldVector field = new KnowledgeFieldVector(ctx, opt, roleName, ensembleIndex, fieldName, fieldType);
			knowledgeFields.put(fieldName, field);
			for (int i = 0; i < instances.size(); i++) {
				Object value = readField(fieldName, instances.get(i));
				Expr expr;
				if (PrimitiveTypes.BOOL.equals(fieldType)) {
					if (value != null)
						expr = ctx.mkBool(((Boolean)value).booleanValue());
					else
						expr = ctx.mkBool(false);
				} else if (PrimitiveTypes.INT.equals(fieldType)) {
					if (value != null)
						expr = ctx.mkInt(((Integer)value).intValue());
					else
						expr = ctx.mkInt(0);
				} else {
					throw new UnsupportedDataTypeException(fieldType);
				}
				
				field.set(i, expr);
			}
			
			field.close();
		}
	}
	
	private Object readField(String fieldName, BaseDataContract instance) 
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field field = instance.getClass().getField(fieldName);
		return field.get(instance);
	}
	
	public List<? extends BaseDataContract> getInstances() {
		return instances;
	}
	
	public Map<String, KnowledgeFieldVector> getKnowledgeFields() {
		return knowledgeFields;
	}
	/*
	public int[] getGlobalIndices() {
		return globalIndices;
	}*/
}

class DataContractInstancesContainer {
	private RoleDefinition roleDefinition;
	private DataContractDefinition dataContractDefinition;
	private DataContractInstancesPerEnsembleContainer[] instances;
	private String dataContractName;
	private EquitableQuery whereClause;
	
	public DataContractInstancesContainer(Context ctx, Optimize opt, RoleDefinition roleDefinition, ITypeResolutionContext typeResolution,
			FilteredKnowledgeContainer filteredKnowledgeContainer, EquitableQuery whereClause, int maxEnsembleCount)
			throws ClassNotFoundException, KnowledgeContainerException, UnsupportedDataTypeException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		this.roleDefinition = roleDefinition;
		this.dataContractDefinition = (DataContractDefinition) typeResolution.getDataType(roleDefinition.getType());	
		this.dataContractName = dataContractDefinition.getName();
		this.whereClause = whereClause;
		this.instances = new DataContractInstancesPerEnsembleContainer[maxEnsembleCount];
		for (int e = 0; e < maxEnsembleCount; e++) {
			this.instances[e] = new DataContractInstancesPerEnsembleContainer(ctx, opt, dataContractDefinition, filteredKnowledgeContainer, 
					whereClause, roleDefinition.getName(), e);
		}
	}
	
	public DataContractDefinition getDataContractDefinition() {
		return dataContractDefinition;
	}
	
	public String getName() {
		return dataContractName;
	}
	
	public BaseDataContract getInstance(int componentIndex, int ensembleIndex) {
		return instances[ensembleIndex].getInstances().get(componentIndex);
	}
	/*
	public int getInstanceGlobalIndex(int componentIndex, int ensembleIndex) {
		return instances[ensembleIndex].getGlobalIndices()[componentIndex];
	}
	*/
	public KnowledgeFieldVector get(String fieldName, int ensembleIndex) {
		return instances[ensembleIndex].getKnowledgeFields().get(fieldName);
	}
	
	public Expr get(String fieldName, Expr componentIndex, int ensembleIndex) {
		return get(fieldName, ensembleIndex).get(componentIndex);
	}
	
	public int getNumInstances(int ensembleIndex) {
		return instances[ensembleIndex].getInstances().size();
	}
	
}