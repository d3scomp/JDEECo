package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Optimize;

import cz.cuni.mff.d3s.jdeeco.edl.IFunctionRegistry;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.AdditiveInverse;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BinaryOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BoolLiteral;
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
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.StringLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.TypeDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.QueryVisitorImpl;
import cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils;
import cz.cuni.mff.d3s.jdeeco.edl.utils.ITypeResolutionContext;

class ConstraintParser extends QueryVisitorImpl<Expr> {
	private Context ctx;
	private Optimize opt;
	private DataContainer dataContainer;
	
	public ConstraintParser(Context ctx, Optimize opt, DataContainer dataContainer) {
		super();
		this.ctx = ctx;
		this.opt = opt;
		this.dataContainer = dataContainer;
	}
	
	public void parseConstraints(EnsembleDefinition ensembleDefinition) {
		for(EquitableQuery q : ensembleDefinition.getConstraints()) {
			opt.Add((BoolExpr) q.accept(this));
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
		String fieldName = query.getPath().toParts().get(1);
		
		//dataContainer.get(dataContractName, fieldName, componentIndex);
		
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