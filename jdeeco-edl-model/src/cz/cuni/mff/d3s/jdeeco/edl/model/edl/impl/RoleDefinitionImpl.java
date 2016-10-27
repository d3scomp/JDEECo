/**
 */
package cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlPackage;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EquitableQuery;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Role Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.RoleDefinitionImpl#isExclusive <em>Exclusive</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.RoleDefinitionImpl#getWhereFilter <em>Where Filter</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RoleDefinitionImpl extends ChildDefinitionImpl implements RoleDefinition {
	/**
	 * The default value of the '{@link #isExclusive() <em>Exclusive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExclusive()
	 * @generated
	 * @ordered
	 */
	protected static final boolean EXCLUSIVE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isExclusive() <em>Exclusive</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExclusive()
	 * @generated
	 * @ordered
	 */
	protected boolean exclusive = EXCLUSIVE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getWhereFilter() <em>Where Filter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWhereFilter()
	 * @generated
	 * @ordered
	 */
	protected EquitableQuery whereFilter;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RoleDefinitionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EdlPackage.Literals.ROLE_DEFINITION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isExclusive() {
		return exclusive;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExclusive(boolean newExclusive) {
		boolean oldExclusive = exclusive;
		exclusive = newExclusive;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.ROLE_DEFINITION__EXCLUSIVE, oldExclusive, exclusive));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EquitableQuery getWhereFilter() {
		return whereFilter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetWhereFilter(EquitableQuery newWhereFilter, NotificationChain msgs) {
		EquitableQuery oldWhereFilter = whereFilter;
		whereFilter = newWhereFilter;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EdlPackage.ROLE_DEFINITION__WHERE_FILTER, oldWhereFilter, newWhereFilter);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWhereFilter(EquitableQuery newWhereFilter) {
		if (newWhereFilter != whereFilter) {
			NotificationChain msgs = null;
			if (whereFilter != null)
				msgs = ((InternalEObject)whereFilter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EdlPackage.ROLE_DEFINITION__WHERE_FILTER, null, msgs);
			if (newWhereFilter != null)
				msgs = ((InternalEObject)newWhereFilter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EdlPackage.ROLE_DEFINITION__WHERE_FILTER, null, msgs);
			msgs = basicSetWhereFilter(newWhereFilter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.ROLE_DEFINITION__WHERE_FILTER, newWhereFilter, newWhereFilter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EdlPackage.ROLE_DEFINITION__WHERE_FILTER:
				return basicSetWhereFilter(null, msgs);
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
			case EdlPackage.ROLE_DEFINITION__EXCLUSIVE:
				return isExclusive();
			case EdlPackage.ROLE_DEFINITION__WHERE_FILTER:
				return getWhereFilter();
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
			case EdlPackage.ROLE_DEFINITION__EXCLUSIVE:
				setExclusive((Boolean)newValue);
				return;
			case EdlPackage.ROLE_DEFINITION__WHERE_FILTER:
				setWhereFilter((EquitableQuery)newValue);
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
			case EdlPackage.ROLE_DEFINITION__EXCLUSIVE:
				setExclusive(EXCLUSIVE_EDEFAULT);
				return;
			case EdlPackage.ROLE_DEFINITION__WHERE_FILTER:
				setWhereFilter((EquitableQuery)null);
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
			case EdlPackage.ROLE_DEFINITION__EXCLUSIVE:
				return exclusive != EXCLUSIVE_EDEFAULT;
			case EdlPackage.ROLE_DEFINITION__WHERE_FILTER:
				return whereFilter != null;
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
		result.append(" (exclusive: ");
		result.append(exclusive);
		result.append(')');
		return result.toString();
	}

} //RoleDefinitionImpl
