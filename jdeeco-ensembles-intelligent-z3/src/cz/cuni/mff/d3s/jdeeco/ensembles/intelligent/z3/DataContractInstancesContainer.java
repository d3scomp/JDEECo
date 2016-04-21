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

class DataContractInstancesPerEnsembleContainer {
	private List<? extends BaseDataContract> instances;
	private Map<String, KnowledgeFieldVector> knowledgeFields;
	
	public DataContractInstancesPerEnsembleContainer(Context ctx, Optimize opt, DataContractDefinition dataContractDefinition,
			FilteredKnowledgeContainer filteredKnowledgeContainer, EquitableQuery whereClause)
			throws UnsupportedDataTypeException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		this.instances = filteredKnowledgeContainer.getFilteredTrackedKnowledge(dataContractDefinition.getName(), whereClause, 0);
		
		knowledgeFields = new HashMap<>();
		
		for (FieldDeclaration fieldDecl : dataContractDefinition.getFields()) {
			String fieldName = fieldDecl.getName();
			String fieldType = fieldDecl.getType().toString();
			KnowledgeFieldVector field = new KnowledgeFieldVector(ctx, opt, dataContractDefinition.getName(), fieldName, fieldType);
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
}

class DataContractInstancesContainer {
	private DataContractDefinition dataContractDefinition;
	private DataContractInstancesPerEnsembleContainer instances;
	private String dataContractName;
	private EquitableQuery whereClause;
	
	public DataContractInstancesContainer(Context ctx, Optimize opt, DataContractDefinition dataContractDefinition,
			FilteredKnowledgeContainer filteredKnowledgeContainer, EquitableQuery whereClause)
			throws ClassNotFoundException, KnowledgeContainerException, UnsupportedDataTypeException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		this.dataContractDefinition = dataContractDefinition;	
		this.dataContractName = dataContractDefinition.getName();
		this.whereClause = whereClause;
		this.instances = new DataContractInstancesPerEnsembleContainer(ctx, opt, dataContractDefinition, filteredKnowledgeContainer, 
				whereClause);
	}
	
	public DataContractDefinition getDataContractDefinition() {
		return dataContractDefinition;
	}
	
	public String getName() {
		return dataContractName;
	}
	
	public BaseDataContract getInstance(int componentIndex) {
		return instances.getInstances().get(componentIndex);
	}
	
	public KnowledgeFieldVector get(String fieldName) {
		return instances.getKnowledgeFields().get(fieldName);
	}
	
	public Expr get(String fieldName, Expr componentIndex) {
		return get(fieldName).get(componentIndex);
	}
	
	public int getNumInstances() {
		return instances.getInstances().size();
	}
	
}