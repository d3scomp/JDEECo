/**
 */
package cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.ChildDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlPackage;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QualifiedName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Child Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.ChildDefinitionImpl#getName <em>Name</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.ChildDefinitionImpl#getCardinalityMax <em>Cardinality Max</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.ChildDefinitionImpl#getCardinalityMin <em>Cardinality Min</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.ChildDefinitionImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ChildDefinitionImpl extends MinimalEObjectImpl.Container implements ChildDefinition {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getCardinalityMax() <em>Cardinality Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCardinalityMax()
	 * @generated
	 * @ordered
	 */
	protected static final int CARDINALITY_MAX_EDEFAULT = 1;

	/**
	 * The cached value of the '{@link #getCardinalityMax() <em>Cardinality Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCardinalityMax()
	 * @generated
	 * @ordered
	 */
	protected int cardinalityMax = CARDINALITY_MAX_EDEFAULT;

	/**
	 * The default value of the '{@link #getCardinalityMin() <em>Cardinality Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCardinalityMin()
	 * @generated
	 * @ordered
	 */
	protected static final int CARDINALITY_MIN_EDEFAULT = 1;

	/**
	 * The cached value of the '{@link #getCardinalityMin() <em>Cardinality Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCardinalityMin()
	 * @generated
	 * @ordered
	 */
	protected int cardinalityMin = CARDINALITY_MIN_EDEFAULT;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected QualifiedName type;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChildDefinitionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EdlPackage.Literals.CHILD_DEFINITION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.CHILD_DEFINITION__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCardinalityMax() {
		return cardinalityMax;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCardinalityMax(int newCardinalityMax) {
		int oldCardinalityMax = cardinalityMax;
		cardinalityMax = newCardinalityMax;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.CHILD_DEFINITION__CARDINALITY_MAX, oldCardinalityMax, cardinalityMax));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCardinalityMin() {
		return cardinalityMin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCardinalityMin(int newCardinalityMin) {
		int oldCardinalityMin = cardinalityMin;
		cardinalityMin = newCardinalityMin;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.CHILD_DEFINITION__CARDINALITY_MIN, oldCardinalityMin, cardinalityMin));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QualifiedName getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetType(QualifiedName newType, NotificationChain msgs) {
		QualifiedName oldType = type;
		type = newType;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EdlPackage.CHILD_DEFINITION__TYPE, oldType, newType);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(QualifiedName newType) {
		if (newType != type) {
			NotificationChain msgs = null;
			if (type != null)
				msgs = ((InternalEObject)type).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EdlPackage.CHILD_DEFINITION__TYPE, null, msgs);
			if (newType != null)
				msgs = ((InternalEObject)newType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EdlPackage.CHILD_DEFINITION__TYPE, null, msgs);
			msgs = basicSetType(newType, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.CHILD_DEFINITION__TYPE, newType, newType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EdlPackage.CHILD_DEFINITION__TYPE:
				return basicSetType(null, msgs);
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
			case EdlPackage.CHILD_DEFINITION__NAME:
				return getName();
			case EdlPackage.CHILD_DEFINITION__CARDINALITY_MAX:
				return getCardinalityMax();
			case EdlPackage.CHILD_DEFINITION__CARDINALITY_MIN:
				return getCardinalityMin();
			case EdlPackage.CHILD_DEFINITION__TYPE:
				return getType();
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
			case EdlPackage.CHILD_DEFINITION__NAME:
				setName((String)newValue);
				return;
			case EdlPackage.CHILD_DEFINITION__CARDINALITY_MAX:
				setCardinalityMax((Integer)newValue);
				return;
			case EdlPackage.CHILD_DEFINITION__CARDINALITY_MIN:
				setCardinalityMin((Integer)newValue);
				return;
			case EdlPackage.CHILD_DEFINITION__TYPE:
				setType((QualifiedName)newValue);
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
			case EdlPackage.CHILD_DEFINITION__NAME:
				setName(NAME_EDEFAULT);
				return;
			case EdlPackage.CHILD_DEFINITION__CARDINALITY_MAX:
				setCardinalityMax(CARDINALITY_MAX_EDEFAULT);
				return;
			case EdlPackage.CHILD_DEFINITION__CARDINALITY_MIN:
				setCardinalityMin(CARDINALITY_MIN_EDEFAULT);
				return;
			case EdlPackage.CHILD_DEFINITION__TYPE:
				setType((QualifiedName)null);
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
			case EdlPackage.CHILD_DEFINITION__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case EdlPackage.CHILD_DEFINITION__CARDINALITY_MAX:
				return cardinalityMax != CARDINALITY_MAX_EDEFAULT;
			case EdlPackage.CHILD_DEFINITION__CARDINALITY_MIN:
				return cardinalityMin != CARDINALITY_MIN_EDEFAULT;
			case EdlPackage.CHILD_DEFINITION__TYPE:
				return type != null;
		}
		return super.eIsSet(featureID);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", cardinalityMax: ");
		result.append(cardinalityMax);
		result.append(", cardinalityMin: ");
		result.append(cardinalityMin);
		result.append(')');
		return result.toString();
	}

} //ChildDefinitionImpl
