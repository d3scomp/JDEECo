package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.util.ArrayList;
import java.util.List;

import javax.activation.UnsupportedDataTypeException;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Sort;

import cz.cuni.mff.d3s.jdeeco.edl.ContextSymbols;
import cz.cuni.mff.d3s.jdeeco.edl.functions.IConstraintFunction;
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
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RelationOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.StringLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Sum;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.QueryVisitorImpl;
import cz.cuni.mff.d3s.jdeeco.edl.typing.IDataTypeContext;


class ConstraintParser extends QueryVisitorImpl<Expr> {
	protected Context ctx;
	protected Optimize opt;
	protected DataContainer dataContainer;
	protected EnsembleRoleAssignmentMatrix assignmentMatrix;
	protected int ensembleIndex;
	protected IDataTypeContext typeResolution;
	protected EnsembleIdMapping idMapping;
	
	public ConstraintParser(Context ctx, Optimize opt, DataContainer dataContainer, EnsembleRoleAssignmentMatrix assignmentMatrix,
			int ensembleIndex, IDataTypeContext typeResolution, EnsembleIdMapping idMapping) {
		super();
		this.ctx = ctx;
		this.opt = opt;
		this.dataContainer = dataContainer;
		this.assignmentMatrix = assignmentMatrix;
		this.ensembleIndex = ensembleIndex;
		this.typeResolution = typeResolution;
		this.idMapping = idMapping;
	}
	
	public void parseConstraints() {
		for(EquitableQuery q : assignmentMatrix.getEnsembleDefinition().getConstraints()) {
			opt.Add(ctx.mkImplies(assignmentMatrix.ensembleExists(), (BoolExpr) q.accept(this)));
		}
	}
	
	public ArithExpr parseFitness() {
		Query q = assignmentMatrix.getEnsembleDefinition().getFitness();
		Expr expr;
		if (q == null)
			expr = ctx.mkInt(1);
		else
			expr = q.accept(this);
		
		Expr resultExpr = ctx.mkConst("fitness_e" + assignmentMatrix.getEnsembleIndex(), expr.getSort());
		opt.Add(ctx.mkImplies(assignmentMatrix.ensembleExists(), ctx.mkEq(resultExpr, expr)));
		opt.Add(ctx.mkImplies(ctx.mkNot(assignmentMatrix.ensembleExists()), ctx.mkEq(resultExpr, ctx.mkInt(0))));
		return (ArithExpr) resultExpr;
	}

	@Override
	public Expr visit(AdditiveInverse query) {
		ArithExpr nested = (ArithExpr) query.getNested().accept(this);
		return ctx.mkSub(ctx.mkInt(0), nested);
	}

	@Override
	public Expr visit(BinaryOperator query) {
		ArithExpr left = (ArithExpr) query.getLeft().accept(this);
		ArithExpr right = (ArithExpr) query.getRight().accept(this);
		switch (query.getOperatorType()) {
		case ADDITION:
			return ctx.mkAdd(left, right);
			
		case SUBTRACTION:
			return ctx.mkSub(left, right);
			
		case MULTIPLICATION:
			return ctx.mkMul(left, right);
			
		case DIVISION:
			return ctx.mkDiv(left, right);
		
		default:
			return null;
		}
	}

	@Override
	public Expr visit(BoolLiteral query) {
		return ctx.mkBool(query.isValue());
	}

	@Override
	public Expr visit(FloatLiteral query) {
		return null;
	}

	@Override
	public Expr visit(FunctionCall query) {
		IConstraintFunction func = typeResolution.getFunctionRegistry().getConstraintFunction(query.getName());
		Expr[] paramExprs = new Expr[query.getParameters().size()];
		for (int i = 0; i < paramExprs.length; i++) {
			paramExprs[i] = query.getParameters().get(i).accept(this);
		}
		
		return func.generateFormula(ctx, paramExprs);
	}

	@Override
	public Expr visit(KnowledgeVariable query) {
		String roleName = query.getPath().toParts().get(0);
		String fieldName = query.getPath().toParts().size() > 1 ? query.getPath().toParts().get(1) : null;
		
		if (roleName.equals(assignmentMatrix.getEnsembleDefinition().getId().getFieldName())) {
			if (Extensions.hasDataContractBoundId(assignmentMatrix.getEnsembleDefinition())) {
				try {
					return idMapping.getFieldExpression(ctx, ensembleIndex, query.getPath());
				} catch (Exception e) {				
					// TODO Rethrow?				
				}
			} else {
				return ctx.mkInt(assignmentMatrix.getEnsembleIndex());				
			}
		}
		
		RoleDefinition roleDefinition = getRoleDefinition(roleName);
		DataContractInstancesContainer dataContractContainer = dataContainer.get(roleDefinition.getName().toString());
		int roleIndex = assignmentMatrix.getEnsembleDefinition().getRoles().indexOf(roleDefinition);
		ComponentAssignmentSet componentAssignmentSet = assignmentMatrix.get(roleIndex);
		
		if (roleDefinition.getCardinalityMin() == 1 && roleDefinition.getCardinalityMax() == 1) {
			Expr theComponentIndex = ctx.mkIntConst(RandomNameGenerator.getNext());
			for (int c = 0; c < componentAssignmentSet.getLength(); c++) {
				opt.Add(ctx.mkImplies(componentAssignmentSet.get(c), ctx.mkEq(theComponentIndex, ctx.mkInt(c))));
			}
			
			if (fieldName == null) {				
				return theComponentIndex;
			} else {
				return dataContractContainer.get(fieldName, theComponentIndex, ensembleIndex);
			}
			
		} else {
			if (fieldName == null) {
				return componentAssignmentSet.getAssignedSet();
			} else {
				try {
					Sort sort = KnowledgeFieldVector.getSort(ctx, "int"); // TODO infer type
					String resultSetName = RandomNameGenerator.getNext();
					ArrayExpr resultSet = ctx.mkArrayConst(resultSetName, sort, ctx.mkBoolSort());
					
					for (int c = 0; c < componentAssignmentSet.getLength(); c++) {
						Expr knowledgeValue = dataContractContainer.get(fieldName, ctx.mkInt(c), ensembleIndex);
						opt.Add(ctx.mkEq(componentAssignmentSet.get(c), (BoolExpr) ctx.mkSelect(resultSet, knowledgeValue)));
					}
					
					return resultSet;
				
				} catch (UnsupportedDataTypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			
			}
		}
		
		//dataContainer.get(dataContractName, fieldName, componentIndex);
	}
	
	private RoleDefinition getRoleDefinition(String roleName) {
		for (RoleDefinition roleDef : assignmentMatrix.getEnsembleDefinition().getRoles()) {
			if (roleDef.getName().equals(roleName)) {
				return roleDef;
			}
		}
		
		return null;
	}

	@Override
	public Expr visit(LogicalOperator query) {
		BoolExpr left = (BoolExpr) query.getLeft().accept(this);
		BoolExpr right = (BoolExpr) query.getRight().accept(this);
		
		switch(query.getType()) {
		case AND:
			return ctx.mkAnd(left, right);
			
		case OR:
			return ctx.mkOr(left, right);
		
		default:
			return null;
		}
	}

	@Override
	public Expr visit(Negation query) {
		BoolExpr nested = (BoolExpr) query.getNested().accept(this);
		return ctx.mkNot(nested);
	}

	@Override
	public Expr visit(NumericLiteral query) {
		return ctx.mkInt(query.getValue());
	}

	@Override
	public Expr visit(RelationOperator query) {
		Expr left = query.getLeft().accept(this);
		Expr right = query.getRight().accept(this);
		switch (query.getType()) {
		case EQUALITY:
			return ctx.mkEq(left, right);
			
		case GREATER_OR_EQUAL:
			return ctx.mkGe((ArithExpr)left, (ArithExpr)right);
			
		case GREATER_THAN:
			return ctx.mkGt((ArithExpr)left, (ArithExpr)right);
			
		case LESS_OR_EQUAL:
			return ctx.mkLe((ArithExpr)left, (ArithExpr)right);
			
		case LESS_THAN:
			return ctx.mkLt((ArithExpr)left, (ArithExpr)right);
			
		case NON_EQUALITY:
			return ctx.mkNot(ctx.mkEq(left, right));
			
		default:
			return null;
		}
	}

	@Override
	public Expr visit(StringLiteral query) {
		return null;
	}

	@Override
	public Expr visit(Sum query) {		
		
		RoleDefinition roleDefinition = getRoleDefinition(query.getCollection().toString());
		
		DataContractInstancesContainer dataContractContainer = dataContainer.get(roleDefinition.getName().toString());
		int roleIndex = assignmentMatrix.getEnsembleDefinition().getRoles().indexOf(roleDefinition);
		ComponentAssignmentSet componentAssignmentSet = assignmentMatrix.get(roleIndex);
		
		ArithExpr zero = ctx.mkInt(0);
		
		if (componentAssignmentSet.getLength() == 0)
			return zero;
		
		
		ArithExpr prev = zero;
		
				
		for (int i = 0; i < componentAssignmentSet.getLength(); ++i) {
			
			//ArithExpr knowledgeValue = (ArithExpr) dataContractContainer.get(fieldName, ctx.mkInt(i), ensembleIndex);
			
			String itemValueName = RandomNameGenerator.getNext();
			IntExpr itemValueResult = ctx.mkIntConst(itemValueName);
			
			ArithExpr itemValue = (ArithExpr) query.getItem().accept(new ItemBoundConstraintParser(this, dataContractContainer, i));
			
			opt.Add(ctx.mkImplies(componentAssignmentSet.get(i), ctx.mkEq(itemValueResult, itemValue)));
			opt.Add(ctx.mkImplies(ctx.mkNot(componentAssignmentSet.get(i)), ctx.mkEq(itemValueResult, zero)));			
			//opt.Add(ctx.mkImplies(componentAssignmentSet.get(i), ctx.mkEq(itemValueResult, zero)));
			
			prev = ctx.mkAdd(prev, itemValueResult);
		}
		
		return prev;
	}
}

class ItemBoundConstraintParser extends ConstraintParser {
	
	private DataContractInstancesContainer dataSource;
	private int currentItem;	
	
	public ItemBoundConstraintParser(ConstraintParser origin, DataContractInstancesContainer dataSource, int currentItem) {
		super(origin.ctx, origin.opt,origin.dataContainer, origin.assignmentMatrix, origin.ensembleIndex, origin.typeResolution, origin.idMapping);
		this.dataSource = dataSource;
		this.currentItem = currentItem;
	}
	
	@Override
	public Expr visit(KnowledgeVariable query) {
		List<String> parts = new ArrayList<String>(query.getPath().toParts());		
		
		if (parts.get(0).equals(ContextSymbols.WHERE_IT_SYMBOL)) {
			parts.remove(0);
			String fieldName = String.join(".", parts);
			
			ArithExpr knowledgeValue = (ArithExpr) dataSource.get(fieldName, ctx.mkInt(currentItem), ensembleIndex);
			
			return knowledgeValue;
		} else
			return super.visit(query);
	}
	
}