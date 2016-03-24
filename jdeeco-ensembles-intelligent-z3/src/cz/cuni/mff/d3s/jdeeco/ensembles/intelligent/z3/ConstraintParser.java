package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import javax.activation.UnsupportedDataTypeException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Sort;

import cz.cuni.mff.d3s.jdeeco.edl.IFunctionRegistry;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.AdditiveInverse;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BinaryOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BoolLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.DataContractDefinition;
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
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.TypeDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.QueryVisitorImpl;
import cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils;
import cz.cuni.mff.d3s.jdeeco.edl.utils.ITypeResolutionContext;

class ConstraintParser extends QueryVisitorImpl<Expr> {
	private Context ctx;
	private Optimize opt;
	private DataContainer dataContainer;
	private EnsembleRoleAssignmentMatrix assignmentMatrix;
	
	public ConstraintParser(Context ctx, Optimize opt, DataContainer dataContainer, EnsembleRoleAssignmentMatrix assignmentMatrix) {
		super();
		this.ctx = ctx;
		this.opt = opt;
		this.dataContainer = dataContainer;
		this.assignmentMatrix = assignmentMatrix;
	}
	
	public void parseConstraints() {
		for(EquitableQuery q : assignmentMatrix.getEnsembleDefinition().getConstraints()) {
			opt.Add(ctx.mkImplies(assignmentMatrix.ensembleExists(), (BoolExpr) q.accept(this)));
		}
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expr visit(KnowledgeVariable query) {
		String roleName = query.getPath().toParts().get(0);
		String fieldName = query.getPath().toParts().size() > 1 ? query.getPath().toParts().get(1) : null;
		
		RoleDefinition roleDefinition = getRoleDefinition(roleName);
		DataContractInstancesContainer dataContractContainer = dataContainer.get(roleDefinition.getType().toString());
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
				return dataContractContainer.get(fieldName, theComponentIndex);
			}
			
		} else {
			if (fieldName == null) {
				return componentAssignmentSet.getAssignedSet();
			} else {
				try {
					Sort sort = KnowledgeFieldVector.getSort(ctx, "int");
					ArrayExpr resultSet = ctx.mkArrayConst(RandomNameGenerator.getNext(), sort, ctx.mkBoolSort());
					
					for (int c = 0; c < componentAssignmentSet.getLength(); c++) {
						Expr knowledgeValue = dataContractContainer.get(fieldName, ctx.mkInt(c));
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
}