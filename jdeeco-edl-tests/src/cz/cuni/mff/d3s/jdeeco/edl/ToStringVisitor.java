package cz.cuni.mff.d3s.jdeeco.edl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.AdditiveInverse;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BinaryOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BoolLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.ExpressionVisitor;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FloatLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FunctionCall;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.KnowledgeVariable;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.LogicalOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Negation;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.NumericLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RelationOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.StringLiteral;

public class ToStringVisitor implements ExpressionVisitor<String> {

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

	@Override
	public EClass eClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource eResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EObject eContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EStructuralFeature eContainingFeature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EReference eContainmentFeature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EList<EObject> eContents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeIterator<EObject> eAllContents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean eIsProxy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EList<EObject> eCrossReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object eGet(EStructuralFeature feature) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object eGet(EStructuralFeature feature, boolean resolve) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eSet(EStructuralFeature feature, Object newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean eIsSet(EStructuralFeature feature) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void eUnset(EStructuralFeature feature) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object eInvoke(EOperation operation, EList<?> arguments)
			throws InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EList<Adapter> eAdapters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean eDeliver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void eSetDeliver(boolean deliver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eNotify(Notification notification) {
		// TODO Auto-generated method stub
		
	}

}
