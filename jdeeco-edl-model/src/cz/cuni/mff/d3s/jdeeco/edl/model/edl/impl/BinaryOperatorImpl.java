/**
 */
package cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BinaryOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BinaryOperatorType;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlPackage;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.ExpressionVisitor;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Binary Operator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.BinaryOperatorImpl#getLeft <em>Left</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.BinaryOperatorImpl#getRight <em>Right</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.BinaryOperatorImpl#getOperatorType <em>Operator Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class BinaryOperatorImpl extends MinimalEObjectImpl.Container implements BinaryOperator {
	/**
	 * The cached value of the '{@link #getLeft() <em>Left</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeft()
	 * @generated
	 * @ordered
	 */
	protected Query left;

	/**
	 * The cached value of the '{@link #getRight() <em>Right</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRight()
	 * @generated
	 * @ordered
	 */
	protected Query right;

	/**
	 * The default value of the '{@link #getOperatorType() <em>Operator Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperatorType()
	 * @generated
	 * @ordered
	 */
	protected static final BinaryOperatorType OPERATOR_TYPE_EDEFAULT = BinaryOperatorType.ADDITION;

	/**
	 * The cached value of the '{@link #getOperatorType() <em>Operator Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperatorType()
	 * @generated
	 * @ordered
	 */
	protected BinaryOperatorType operatorType = OPERATOR_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BinaryOperatorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EdlPackage.Literals.BINARY_OPERATOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Query getLeft() {
		return left;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLeft(Query newLeft, NotificationChain msgs) {
		Query oldLeft = left;
		left = newLeft;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EdlPackage.BINARY_OPERATOR__LEFT, oldLeft, newLeft);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeft(Query newLeft) {
		if (newLeft != left) {
			NotificationChain msgs = null;
			if (left != null)
				msgs = ((InternalEObject)left).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EdlPackage.BINARY_OPERATOR__LEFT, null, msgs);
			if (newLeft != null)
				msgs = ((InternalEObject)newLeft).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EdlPackage.BINARY_OPERATOR__LEFT, null, msgs);
			msgs = basicSetLeft(newLeft, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.BINARY_OPERATOR__LEFT, newLeft, newLeft));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Query getRight() {
		return right;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRight(Query newRight, NotificationChain msgs) {
		Query oldRight = right;
		right = newRight;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EdlPackage.BINARY_OPERATOR__RIGHT, oldRight, newRight);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRight(Query newRight) {
		if (newRight != right) {
			NotificationChain msgs = null;
			if (right != null)
				msgs = ((InternalEObject)right).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EdlPackage.BINARY_OPERATOR__RIGHT, null, msgs);
			if (newRight != null)
				msgs = ((InternalEObject)newRight).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EdlPackage.BINARY_OPERATOR__RIGHT, null, msgs);
			msgs = basicSetRight(newRight, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.BINARY_OPERATOR__RIGHT, newRight, newRight));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BinaryOperatorType getOperatorType() {
		return operatorType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOperatorType(BinaryOperatorType newOperatorType) {
		BinaryOperatorType oldOperatorType = operatorType;
		operatorType = newOperatorType == null ? OPERATOR_TYPE_EDEFAULT : newOperatorType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.BINARY_OPERATOR__OPERATOR_TYPE, oldOperatorType, operatorType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visit(this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EdlPackage.BINARY_OPERATOR__LEFT:
				return basicSetLeft(null, msgs);
			case EdlPackage.BINARY_OPERATOR__RIGHT:
				return basicSetRight(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EdlPackage.BINARY_OPERATOR__LEFT:
				return getLeft();
			case EdlPackage.BINARY_OPERATOR__RIGHT:
				return getRight();
			case EdlPackage.BINARY_OPERATOR__OPERATOR_TYPE:
				return getOperatorType();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case EdlPackage.BINARY_OPERATOR__LEFT:
				setLeft((Query)newValue);
				return;
			case EdlPackage.BINARY_OPERATOR__RIGHT:
				setRight((Query)newValue);
				return;
			case EdlPackage.BINARY_OPERATOR__OPERATOR_TYPE:
				setOperatorType((BinaryOperatorType)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case EdlPackage.BINARY_OPERATOR__LEFT:
				setLeft((Query)null);
				return;
			case EdlPackage.BINARY_OPERATOR__RIGHT:
				setRight((Query)null);
				return;
			case EdlPackage.BINARY_OPERATOR__OPERATOR_TYPE:
				setOperatorType(OPERATOR_TYPE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case EdlPackage.BINARY_OPERATOR__LEFT:
				return left != null;
			case EdlPackage.BINARY_OPERATOR__RIGHT:
				return right != null;
			case EdlPackage.BINARY_OPERATOR__OPERATOR_TYPE:
				return operatorType != OPERATOR_TYPE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings({"rawtypes", "unchecked" })
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case EdlPackage.BINARY_OPERATOR___ACCEPT__EXPRESSIONVISITOR:
				return accept((ExpressionVisitor)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (operatorType: ");
		result.append(operatorType);
		result.append(')');
		return result.toString();
	}

} //BinaryOperatorImpl
