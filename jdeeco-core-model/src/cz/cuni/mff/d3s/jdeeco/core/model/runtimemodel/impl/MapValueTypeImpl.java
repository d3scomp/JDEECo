/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl;

import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MapValueType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TypeParameter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Map Value Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MapValueTypeImpl#getKeyTypeParameter <em>Key Type Parameter</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MapValueTypeImpl#getValueTypeParameter <em>Value Type Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MapValueTypeImpl extends ParametricKnowledgeTypeImpl implements MapValueType {
	/**
	 * The cached value of the '{@link #getKeyTypeParameter() <em>Key Type Parameter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKeyTypeParameter()
	 * @generated
	 * @ordered
	 */
	protected TypeParameter keyTypeParameter;

	/**
	 * The cached value of the '{@link #getValueTypeParameter() <em>Value Type Parameter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValueTypeParameter()
	 * @generated
	 * @ordered
	 */
	protected TypeParameter valueTypeParameter;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MapValueTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimemodelPackage.Literals.MAP_VALUE_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TypeParameter getKeyTypeParameter() {
		return keyTypeParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetKeyTypeParameter(TypeParameter newKeyTypeParameter, NotificationChain msgs) {
		TypeParameter oldKeyTypeParameter = keyTypeParameter;
		keyTypeParameter = newKeyTypeParameter;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.MAP_VALUE_TYPE__KEY_TYPE_PARAMETER, oldKeyTypeParameter, newKeyTypeParameter);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKeyTypeParameter(TypeParameter newKeyTypeParameter) {
		if (newKeyTypeParameter != keyTypeParameter) {
			NotificationChain msgs = null;
			if (keyTypeParameter != null)
				msgs = ((InternalEObject)keyTypeParameter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RuntimemodelPackage.MAP_VALUE_TYPE__KEY_TYPE_PARAMETER, null, msgs);
			if (newKeyTypeParameter != null)
				msgs = ((InternalEObject)newKeyTypeParameter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RuntimemodelPackage.MAP_VALUE_TYPE__KEY_TYPE_PARAMETER, null, msgs);
			msgs = basicSetKeyTypeParameter(newKeyTypeParameter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.MAP_VALUE_TYPE__KEY_TYPE_PARAMETER, newKeyTypeParameter, newKeyTypeParameter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TypeParameter getValueTypeParameter() {
		return valueTypeParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetValueTypeParameter(TypeParameter newValueTypeParameter, NotificationChain msgs) {
		TypeParameter oldValueTypeParameter = valueTypeParameter;
		valueTypeParameter = newValueTypeParameter;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.MAP_VALUE_TYPE__VALUE_TYPE_PARAMETER, oldValueTypeParameter, newValueTypeParameter);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValueTypeParameter(TypeParameter newValueTypeParameter) {
		if (newValueTypeParameter != valueTypeParameter) {
			NotificationChain msgs = null;
			if (valueTypeParameter != null)
				msgs = ((InternalEObject)valueTypeParameter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RuntimemodelPackage.MAP_VALUE_TYPE__VALUE_TYPE_PARAMETER, null, msgs);
			if (newValueTypeParameter != null)
				msgs = ((InternalEObject)newValueTypeParameter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RuntimemodelPackage.MAP_VALUE_TYPE__VALUE_TYPE_PARAMETER, null, msgs);
			msgs = basicSetValueTypeParameter(newValueTypeParameter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.MAP_VALUE_TYPE__VALUE_TYPE_PARAMETER, newValueTypeParameter, newValueTypeParameter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimemodelPackage.MAP_VALUE_TYPE__KEY_TYPE_PARAMETER:
				return basicSetKeyTypeParameter(null, msgs);
			case RuntimemodelPackage.MAP_VALUE_TYPE__VALUE_TYPE_PARAMETER:
				return basicSetValueTypeParameter(null, msgs);
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
			case RuntimemodelPackage.MAP_VALUE_TYPE__KEY_TYPE_PARAMETER:
				return getKeyTypeParameter();
			case RuntimemodelPackage.MAP_VALUE_TYPE__VALUE_TYPE_PARAMETER:
				return getValueTypeParameter();
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
			case RuntimemodelPackage.MAP_VALUE_TYPE__KEY_TYPE_PARAMETER:
				setKeyTypeParameter((TypeParameter)newValue);
				return;
			case RuntimemodelPackage.MAP_VALUE_TYPE__VALUE_TYPE_PARAMETER:
				setValueTypeParameter((TypeParameter)newValue);
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
			case RuntimemodelPackage.MAP_VALUE_TYPE__KEY_TYPE_PARAMETER:
				setKeyTypeParameter((TypeParameter)null);
				return;
			case RuntimemodelPackage.MAP_VALUE_TYPE__VALUE_TYPE_PARAMETER:
				setValueTypeParameter((TypeParameter)null);
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
			case RuntimemodelPackage.MAP_VALUE_TYPE__KEY_TYPE_PARAMETER:
				return keyTypeParameter != null;
			case RuntimemodelPackage.MAP_VALUE_TYPE__VALUE_TYPE_PARAMETER:
				return valueTypeParameter != null;
		}
		return super.eIsSet(featureID);
	}

} //MapValueTypeImpl
