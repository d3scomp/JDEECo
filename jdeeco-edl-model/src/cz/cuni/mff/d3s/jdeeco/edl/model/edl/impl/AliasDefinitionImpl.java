/**
 */
package cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.AliasDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlPackage;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Alias Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.AliasDefinitionImpl#getAliasId <em>Alias Id</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl.AliasDefinitionImpl#getAliasValue <em>Alias Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AliasDefinitionImpl extends MinimalEObjectImpl.Container implements AliasDefinition {
	/**
	 * The default value of the '{@link #getAliasId() <em>Alias Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAliasId()
	 * @generated
	 * @ordered
	 */
	protected static final String ALIAS_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAliasId() <em>Alias Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAliasId()
	 * @generated
	 * @ordered
	 */
	protected String aliasId = ALIAS_ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAliasValue() <em>Alias Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAliasValue()
	 * @generated
	 * @ordered
	 */
	protected Query aliasValue;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AliasDefinitionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EdlPackage.Literals.ALIAS_DEFINITION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAliasId() {
		return aliasId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAliasId(String newAliasId) {
		String oldAliasId = aliasId;
		aliasId = newAliasId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.ALIAS_DEFINITION__ALIAS_ID, oldAliasId, aliasId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Query getAliasValue() {
		return aliasValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAliasValue(Query newAliasValue, NotificationChain msgs) {
		Query oldAliasValue = aliasValue;
		aliasValue = newAliasValue;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, EdlPackage.ALIAS_DEFINITION__ALIAS_VALUE, oldAliasValue, newAliasValue);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAliasValue(Query newAliasValue) {
		if (newAliasValue != aliasValue) {
			NotificationChain msgs = null;
			if (aliasValue != null)
				msgs = ((InternalEObject)aliasValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - EdlPackage.ALIAS_DEFINITION__ALIAS_VALUE, null, msgs);
			if (newAliasValue != null)
				msgs = ((InternalEObject)newAliasValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - EdlPackage.ALIAS_DEFINITION__ALIAS_VALUE, null, msgs);
			msgs = basicSetAliasValue(newAliasValue, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EdlPackage.ALIAS_DEFINITION__ALIAS_VALUE, newAliasValue, newAliasValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EdlPackage.ALIAS_DEFINITION__ALIAS_VALUE:
				return basicSetAliasValue(null, msgs);
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
			case EdlPackage.ALIAS_DEFINITION__ALIAS_ID:
				return getAliasId();
			case EdlPackage.ALIAS_DEFINITION__ALIAS_VALUE:
				return getAliasValue();
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
			case EdlPackage.ALIAS_DEFINITION__ALIAS_ID:
				setAliasId((String)newValue);
				return;
			case EdlPackage.ALIAS_DEFINITION__ALIAS_VALUE:
				setAliasValue((Query)newValue);
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
			case EdlPackage.ALIAS_DEFINITION__ALIAS_ID:
				setAliasId(ALIAS_ID_EDEFAULT);
				return;
			case EdlPackage.ALIAS_DEFINITION__ALIAS_VALUE:
				setAliasValue((Query)null);
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
			case EdlPackage.ALIAS_DEFINITION__ALIAS_ID:
				return ALIAS_ID_EDEFAULT == null ? aliasId != null : !ALIAS_ID_EDEFAULT.equals(aliasId);
			case EdlPackage.ALIAS_DEFINITION__ALIAS_VALUE:
				return aliasValue != null;
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
		result.append(" (aliasId: ");
		result.append(aliasId);
		result.append(')');
		return result.toString();
	}

} //AliasDefinitionImpl
