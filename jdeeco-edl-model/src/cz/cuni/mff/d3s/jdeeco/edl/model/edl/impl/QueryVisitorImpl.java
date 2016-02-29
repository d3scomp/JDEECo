/**
 */
package cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.AdditiveInverse;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BinaryOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BoolLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlPackage;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FloatLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FunctionCall;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.KnowledgeVariable;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.LogicalOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Negation;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.NumericLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QueryVisitor;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RelationOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.StringLiteral;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Query Visitor</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public abstract class QueryVisitorImpl<T> extends MinimalEObjectImpl.Container implements QueryVisitor<T> {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected QueryVisitorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EdlPackage.Literals.QUERY_VISITOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public abstract T visit(AdditiveInverse query);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public abstract T visit(BinaryOperator query);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public abstract T visit(BoolLiteral query);	

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public abstract T visit(FloatLiteral query);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public abstract T visit(FunctionCall query);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public abstract T visit(KnowledgeVariable query);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public abstract T visit(LogicalOperator query);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public abstract T visit(Negation query);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public abstract T visit(NumericLiteral query);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public abstract T visit(RelationOperator query);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public abstract T visit(StringLiteral query);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case EdlPackage.QUERY_VISITOR___VISIT__ADDITIVEINVERSE:
				return visit((AdditiveInverse)arguments.get(0));
			case EdlPackage.QUERY_VISITOR___VISIT__BINARYOPERATOR:
				return visit((BinaryOperator)arguments.get(0));
			case EdlPackage.QUERY_VISITOR___VISIT__BOOLLITERAL:
				return visit((BoolLiteral)arguments.get(0));
			case EdlPackage.QUERY_VISITOR___VISIT__FLOATLITERAL:
				return visit((FloatLiteral)arguments.get(0));
			case EdlPackage.QUERY_VISITOR___VISIT__FUNCTIONCALL:
				return visit((FunctionCall)arguments.get(0));
			case EdlPackage.QUERY_VISITOR___VISIT__KNOWLEDGEVARIABLE:
				return visit((KnowledgeVariable)arguments.get(0));
			case EdlPackage.QUERY_VISITOR___VISIT__LOGICALOPERATOR:
				return visit((LogicalOperator)arguments.get(0));
			case EdlPackage.QUERY_VISITOR___VISIT__NEGATION:
				return visit((Negation)arguments.get(0));
			case EdlPackage.QUERY_VISITOR___VISIT__NUMERICLITERAL:
				return visit((NumericLiteral)arguments.get(0));
			case EdlPackage.QUERY_VISITOR___VISIT__RELATIONOPERATOR:
				return visit((RelationOperator)arguments.get(0));
			case EdlPackage.QUERY_VISITOR___VISIT__STRINGLITERAL:
				return visit((StringLiteral)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

} //QueryVisitorImpl
