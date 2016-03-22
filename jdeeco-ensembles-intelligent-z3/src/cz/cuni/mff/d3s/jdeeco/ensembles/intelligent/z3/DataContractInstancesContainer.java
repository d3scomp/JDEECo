package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.UnsupportedDataTypeException;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;

import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;
import cz.cuni.mff.d3s.jdeeco.edl.BaseDataContract;
import cz.cuni.mff.d3s.jdeeco.edl.PrimitiveTypes;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.DataContractDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FieldDeclaration;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;

class DataContractInstancesContainer {
	private DataContractDefinition dataContractDefinition;
	private BaseDataContract[] instances;
	private Map<String, KnowledgeFieldVector> knowledgeFields;
	private String dataContractName;
	
	public DataContractInstancesContainer(Context ctx, String packageName, DataContractDefinition dataContractDefinition,
			KnowledgeContainer knowledgeContainer)
			throws ClassNotFoundException, KnowledgeContainerException, UnsupportedDataTypeException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		this.dataContractDefinition = dataContractDefinition;
		
		@SuppressWarnings("unchecked")
		Class<? extends BaseDataContract> roleClass = (Class<? extends BaseDataContract>) Class.forName(packageName + "." + dataContractDefinition.getName());
		Collection<? extends BaseDataContract> instancesUnsorted = knowledgeContainer.getTrackedKnowledgeForRole(roleClass);
				
		this.instances = new BaseDataContract[instancesUnsorted.size()];
		this.dataContractName = dataContractDefinition.getName();
		for (int i = 0; !instancesUnsorted.isEmpty(); i++) {
			BaseDataContract minInstance = instancesUnsorted.iterator().next();
			for (BaseDataContract instance : instancesUnsorted) {
				if (Integer.parseInt(instance.id) < Integer.parseInt(minInstance.id)) {
					minInstance = instance;
				}
			}
			
			instances[i] = minInstance;
			instancesUnsorted.remove(minInstance);
		}
		
		knowledgeFields = new HashMap<>();
		
		for (FieldDeclaration fieldDecl : dataContractDefinition.getFields()) {
			String fieldName = fieldDecl.getName();
			String fieldType = fieldDecl.getType().toString();
			KnowledgeFieldVector field = new KnowledgeFieldVector(ctx, dataContractDefinition.getName(), fieldName, fieldType);
			knowledgeFields.put(fieldName, field);
			for (int i = 0; i < instances.length; i++) {
				Object value = readField(fieldName, instances[i]);
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
		}
	}
	
	public DataContractDefinition getDataContractDefinition() {
		return dataContractDefinition;
	}
	
	private Object readField(String fieldName, BaseDataContract instance) 
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field field = instance.getClass().getField(fieldName);
		return field.get(instance);
	}
	
	public int getMaxEnsembleCount(EnsembleDefinition ensembleDefinition) {
        List<RoleDefinition> roles = ensembleDefinition.getRoles();
        int minCardinalitiesSum = 0;
        for (RoleDefinition roleDefinition : roles) {
        	if (roleDefinition.getType().toString().equals(getName())) {
        		minCardinalitiesSum += roleDefinition.getCardinalityMin();
        	}
        }
        
        if (minCardinalitiesSum == 0) {
        	return Integer.MAX_VALUE;
        }
        
        return getNumInstances() / Math.max(1, minCardinalitiesSum);
	}
	
	public String getName() {
		return dataContractName;
	}
	
	public BaseDataContract getInstance(int componentIndex) {
		return instances[componentIndex];
	}
	
	public Expr get(String fieldName, Expr componentIndex) {
		return knowledgeFields.get(fieldName).get(componentIndex);
	}
	
	public int getNumInstances() {
		return instances.length;
	}
	
}