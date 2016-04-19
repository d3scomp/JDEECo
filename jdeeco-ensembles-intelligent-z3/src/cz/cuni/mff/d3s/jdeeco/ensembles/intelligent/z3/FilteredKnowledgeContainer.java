package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.util.ArrayList;
import java.util.Collection;

import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;
import cz.cuni.mff.d3s.jdeeco.edl.BaseDataContract;
import cz.cuni.mff.d3s.jdeeco.edl.ContextSymbols;
import cz.cuni.mff.d3s.jdeeco.edl.functions.IFunction;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.AdditiveInverse;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BinaryOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BoolLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EquitableQuery;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FloatLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FunctionCall;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.KnowledgeVariable;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.LogicalOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Negation;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.NumericLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RelationOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.StringLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.QueryVisitorImpl;
import cz.cuni.mff.d3s.jdeeco.edl.utils.ITypeResolutionContext;

public class FilteredKnowledgeContainer {

	private int ensembleId;
	private KnowledgeContainer knowledgeContainer;
	private ITypeResolutionContext typeResolution;
	
	public FilteredKnowledgeContainer(KnowledgeContainer knowledgeContainer, ITypeResolutionContext typeResolution,
			int ensembleId) {
		this.knowledgeContainer = knowledgeContainer;
		this.typeResolution = typeResolution;
		this.ensembleId = ensembleId;
	}
	
	public <TDataContract extends BaseDataContract> Collection<TDataContract> getFilteredTrackedKnowledge(
			Class<TDataContract> dataContractClass, EquitableQuery whereClause) throws KnowledgeContainerException {
		
		Collection<TDataContract> dataContractSet = knowledgeContainer.getTrackedKnowledgeForRole(dataContractClass);
		Collection<TDataContract> resultSet = new ArrayList<>();
		
		for (TDataContract instance : dataContractSet) {
			DataContractFilterVisitor visitor = new DataContractFilterVisitor(typeResolution, instance, ensembleId);
			if ((Boolean) whereClause.accept(visitor)) {
				resultSet.add(instance);
			}
		}
		
		return resultSet;
		
	}

}

class DataContractFilterVisitor extends QueryVisitorImpl<Object> {

	BaseDataContract instance;
	int ensembleId;
	ITypeResolutionContext typeResolution;
	
	public DataContractFilterVisitor(ITypeResolutionContext typeResolution, BaseDataContract instance, int ensembleId) {
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
	
}