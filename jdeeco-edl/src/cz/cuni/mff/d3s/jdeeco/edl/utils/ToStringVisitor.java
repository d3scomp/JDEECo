package cz.cuni.mff.d3s.jdeeco.edl.utils;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.AdditiveInverse;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BinaryOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BoolLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QueryVisitor;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FloatLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FunctionCall;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.KnowledgeVariable;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.LogicalOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Negation;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.NumericLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RelationOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.StringLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.QueryVisitorImpl;

public class ToStringVisitor extends QueryVisitorImpl<String> implements QueryVisitor<String> {

	public ToStringVisitor() {
		// TODO Auto-generated constructor stub
	}	

	@Override
	public String visit(AdditiveInverse query) {
		return "(-" + query.getNested().accept(this) + ')';
	}

	@Override
	public String visit(BinaryOperator query) {
		return '(' + query.getLeft().accept(this) + ')' + query.getOperatorType().toString() + '(' + query.getRight().accept(this) + ')';
	}

	@Override
	public String visit(BoolLiteral query) {
		return ((BoolLiteral) query).isValue() + "";
	}

	@Override
	public String visit(FloatLiteral query) {
		return ((FloatLiteral) query).getValue() + "";
	}

	@Override
	public String visit(FunctionCall query) {
		FunctionCall call = query;
		StringBuilder b = new StringBuilder();			
		b.append(call.getName());
		b.append('(');
		
		List<String> paramValues = new ArrayList<String>();
		
		for (Query param : call.getParameters()) {
			paramValues.add(param.accept(this));				
		}
		
		b.append(String.join(",", paramValues));
		
		b.append(')');
		return b.toString();
	}

	@Override
	public String visit(KnowledgeVariable query) {
		return query.getPath().toString();
	}

	@Override
	public String visit(LogicalOperator query) {
		return '(' + query.getLeft().accept(this) + ')' + query.getType().toString() + '(' + query.getRight().accept(this) + ')';
	}

	@Override
	public String visit(Negation query) {
		return "(!" + query.getNested().accept(this) + ')';
	}

	@Override
	public String visit(NumericLiteral query) {
		return ((NumericLiteral) query).getValue() + "";
	}

	@Override
	public String visit(RelationOperator query) {
		return '(' + query.getLeft().accept(this) + ')' + query.getType().toString() + '(' + query.getRight().accept(this) + ')';	
	}

	@Override
	public String visit(StringLiteral query) {
		return "\"" + ((StringLiteral) query).getValue() + "\"";
	}
}
