package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;
import cz.cuni.mff.d3s.jdeeco.edl.BaseDataContract;
import cz.cuni.mff.d3s.jdeeco.edl.ContextSymbols;
import cz.cuni.mff.d3s.jdeeco.edl.PrimitiveTypes;
import cz.cuni.mff.d3s.jdeeco.edl.functions.IFunction;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.AdditiveInverse;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BinaryOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BoolLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.DataContractDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlDocument;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EquitableQuery;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FloatLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FunctionCall;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.KnowledgeVariable;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.LogicalOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Negation;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.NumericLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QualifiedName;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RelationOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.StringLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Sum;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.QueryVisitorImpl;
import cz.cuni.mff.d3s.jdeeco.edl.typing.IDataTypeContext;

public class FilteredKnowledgeContainer {

	private Map<String, BaseDataContract[]> components;
	
	//private Set<Integer> usedComponentIds;
	
	private KnowledgeContainer knowledgeContainer;
	private IDataTypeContext typeResolution;
	
	public FilteredKnowledgeContainer(KnowledgeContainer knowledgeContainer, IDataTypeContext typeResolution) {
		this.knowledgeContainer = knowledgeContainer;
		this.typeResolution = typeResolution;
	}
	
	public void load(EdlDocument edlDocument, String packageName) throws ClassNotFoundException, KnowledgeContainerException {
		components = new HashMap<String, BaseDataContract[]>();
		
		for (DataContractDefinition contract : edlDocument.getDataContracts()) {
			@SuppressWarnings("unchecked")
			Class<? extends BaseDataContract> roleClass = (Class<? extends BaseDataContract>) Class.forName(
					packageName + "." + contract.getName());
			Collection<? extends BaseDataContract> instancesUnsorted = knowledgeContainer.getTrackedKnowledgeForRole(roleClass);
			
			BaseDataContract[] instances = new BaseDataContract[instancesUnsorted.size()];
			String dataContractName = contract.getName();
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
			
			components.put(dataContractName, instances);
		}
	}
	
	public <TDataContract extends BaseDataContract> List<TDataContract> getFilteredTrackedKnowledge(
			String dataContractName, EquitableQuery whereClause, int ensembleId) {
		
		BaseDataContract[] dataContractSet = components.get(dataContractName);
		List<TDataContract> resultSet = new ArrayList<>();
		
		for (BaseDataContract instance : dataContractSet) {
			DataContractFilterVisitor visitor = new DataContractFilterVisitor(typeResolution, instance, ensembleId);
			if (whereClause == null || (Boolean) whereClause.accept(visitor)) {
				resultSet.add((TDataContract) instance);
			}
		}
		
		return resultSet;
		
	}
	/*
	public int[] getGlobalIndices(List<? extends BaseDataContract> instances) {
		int[] result = new int[instances.size()];
		
		for (int i = 0; i < instances.size(); i++) {
			result[i] = Integer.parseInt(instances.get(i).id);
			usedComponentIds.add(result[i]);
		}
		
		return result;
	}
	
	public Set<Integer> getUsedComponentIds() {
		return usedComponentIds;
	}
	*/	
	public <TDataContract> int getMaxEnsembleCount(EdlDocument edlDocument,
			EnsembleDefinition ensembleDefinition) {
		int result = Integer.MAX_VALUE;
		
		QualifiedName idType = ensembleDefinition.getId().getType();
		
		// TODO Extend to int ranges
		if (!idType.toString().equals(PrimitiveTypes.INT)) {
			result = components.get(idType.getName()).length;
		}
		
		
		for (DataContractDefinition contract : edlDocument.getDataContracts()) {
			int maxEnsembleCount = getMaxEnsembleCount(contract.getName(), ensembleDefinition);
			if (maxEnsembleCount < result) {
				result = maxEnsembleCount;
			}
		}
		
		return result;
	}
	
	private <TDataContract> int getMaxEnsembleCount(String dataContractName, EnsembleDefinition ensembleDefinition) {
        List<RoleDefinition> roles = ensembleDefinition.getRoles();
        int minCardinalitiesSum = 0;
        for (RoleDefinition roleDefinition : roles) {
        	if (roleDefinition.getType().toString().equals(dataContractName)) {
        		minCardinalitiesSum += roleDefinition.getCardinalityMin();
        	}
        }
        
        if (minCardinalitiesSum == 0) {
        	return Integer.MAX_VALUE;
        }
        
        return getNumInstances(dataContractName) / Math.max(1, minCardinalitiesSum);
	}
	
	public int getNumInstances(String dataContractName) {
		return components.get(dataContractName).length;
	}

}

class DataContractFilterVisitor extends QueryVisitorImpl<Object> {

	BaseDataContract instance;
	int ensembleId;
	IDataTypeContext typeResolution;
	
	public DataContractFilterVisitor(IDataTypeContext typeResolution, BaseDataContract instance, int ensembleId) {
		super();
		this.typeResolution = typeResolution;
		this.instance = instance;
		this.ensembleId = ensembleId;
	}

	@Override
	public Object visit(AdditiveInverse query) {
		return -((Integer) query.getNested().accept(this));
	}

	@Override
	public Object visit(BinaryOperator query) {
		Number left = (Number) query.getLeft().accept(this);
		Number right = (Number) query.getRight().accept(this);
		
		if (left instanceof Integer && right instanceof Integer) {
			switch (query.getOperatorType()) {
			case ADDITION:
				return (Integer) left + (Integer) right;
			case SUBTRACTION:
				return (Integer) left - (Integer) right;
			case MULTIPLICATION:
				return (Integer) left * (Integer) right;
			case DIVISION:
				return (Integer) left / (Integer) right;
			default:
				return null;
			}
		} else if (left instanceof Double) {
			switch (query.getOperatorType()) {
			case ADDITION:
				return (Double) left + (Double) right;
			case SUBTRACTION:
				return (Double) left - (Double) right;
			case MULTIPLICATION:
				return (Double) left * (Double) right;
			case DIVISION:
				return (Double) left / (Double) right;
			default:
				return null;
			}
		} else {
			return null;
		}
	} 

	@Override
	public Object visit(BoolLiteral query) {
		return query.isValue();
	}

	@Override
	public Object visit(FloatLiteral query) {
		return query.getValue();
	}

	@Override
	public Object visit(FunctionCall query) {
		IFunction func = typeResolution.getFunctionRegistry().getFunction(query.getName());
		Object[] paramValues = new Object[query.getParameters().size()];
		for (int i = 0; i < paramValues.length; i++) {
			paramValues[i] = query.getParameters().get(i).accept(this);
		}
		
		return func.evaluate(paramValues);
	}

	@Override
	public Object visit(KnowledgeVariable query) {
		Class<? extends BaseDataContract> dataContractClass = instance.getClass();
		String qualifier = query.getPath().toParts().get(0);
		if (qualifier.equals(ContextSymbols.WHERE_IT_SYMBOL)) {
			String fieldName = query.getPath().toParts().get(1);
			try {
				return dataContractClass.getField(fieldName).get(instance);
			} catch (IllegalArgumentException | IllegalAccessException
					| NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return ensembleId;
		}
	}

	@Override
	public Object visit(LogicalOperator query) {
		Boolean left = (Boolean) query.getLeft().accept(this);
		Boolean right = (Boolean) query.getRight().accept(this);
		
		switch (query.getType()) {
		case AND:
			return left && right;
		case OR:
			return left || right;
		default:
			return null;
		}
	}

	@Override
	public Object visit(Negation query) {
		return !((Boolean) query.getNested().accept(this));
	}

	@Override
	public Object visit(NumericLiteral query) {
		return query.getValue();
	}

	@Override
	public Object visit(RelationOperator query) {
		Object left = query.getLeft().accept(this);
		Object right = query.getRight().accept(this);
		
		switch (query.getType()) {
		case EQUALITY:
			return left.equals(right);
		case GREATER_OR_EQUAL:
			return ((Number) left).doubleValue() >= ((Number) right).doubleValue();
		case GREATER_THAN:
			return ((Number) left).doubleValue() > ((Number) right).doubleValue();
		case LESS_OR_EQUAL:
			return ((Number) left).doubleValue() <= ((Number) right).doubleValue();
		case LESS_THAN:
			return ((Number) left).doubleValue() < ((Number) right).doubleValue();
		case NON_EQUALITY:
			return !left.equals(right);
		default:
			return null;
		}
	}

	@Override
	public Object visit(StringLiteral query) {
		return null;
	}

	@Override
	public Object visit(Sum query) {		
		return null;
	}	
}