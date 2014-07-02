/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ModeTransition;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.MetadataType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mode Transition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ModeTransitionImpl#getFromMode <em>From Mode</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ModeTransitionImpl#getToMode <em>To Mode</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ModeTransitionImpl#getTransitionCondition <em>Transition Condition</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ModeTransitionImpl#getMeta <em>Meta</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModeTransitionImpl extends MinimalEObjectImpl.Container implements ModeTransition {
	/**
	 * The cached value of the '{@link #getFromMode() <em>From Mode</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromMode()
	 * @generated
	 * @ordered
	 */
	protected ComponentProcess fromMode;

	/**
	 * The cached value of the '{@link #getToMode() <em>To Mode</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToMode()
	 * @generated
	 * @ordered
	 */
	protected ComponentProcess toMode;

	/**
	 * The default value of the '{@link #getTransitionCondition() <em>Transition Condition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransitionCondition()
	 * @generated
	 * @ordered
	 */
	protected static final String TRANSITION_CONDITION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTransitionCondition() <em>Transition Condition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransitionCondition()
	 * @generated
	 * @ordered
	 */
	protected String transitionCondition = TRANSITION_CONDITION_EDEFAULT;

	/**
	 * The default value of the '{@link #getMeta() <em>Meta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMeta()
	 * @generated
	 * @ordered
	 */
	protected static final cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType META_EDEFAULT = cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType.EMPTY;

	/**
	 * The cached value of the '{@link #getMeta() <em>Meta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMeta()
	 * @generated
	 * @ordered
	 */
	protected cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType meta = META_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModeTransitionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.MODE_TRANSITION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentProcess getFromMode() {
		if (fromMode != null && fromMode.eIsProxy()) {
			InternalEObject oldFromMode = (InternalEObject)fromMode;
			fromMode = (ComponentProcess)eResolveProxy(oldFromMode);
			if (fromMode != oldFromMode) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, RuntimeMetadataPackage.MODE_TRANSITION__FROM_MODE, oldFromMode, fromMode));
			}
		}
		return fromMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentProcess basicGetFromMode() {
		return fromMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFromMode(ComponentProcess newFromMode) {
		ComponentProcess oldFromMode = fromMode;
		fromMode = newFromMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.MODE_TRANSITION__FROM_MODE, oldFromMode, fromMode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentProcess getToMode() {
		if (toMode != null && toMode.eIsProxy()) {
			InternalEObject oldToMode = (InternalEObject)toMode;
			toMode = (ComponentProcess)eResolveProxy(oldToMode);
			if (toMode != oldToMode) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, RuntimeMetadataPackage.MODE_TRANSITION__TO_MODE, oldToMode, toMode));
			}
		}
		return toMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentProcess basicGetToMode() {
		return toMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setToMode(ComponentProcess newToMode) {
		ComponentProcess oldToMode = toMode;
		toMode = newToMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.MODE_TRANSITION__TO_MODE, oldToMode, toMode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTransitionCondition() {
		return transitionCondition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTransitionCondition(String newTransitionCondition) {
		String oldTransitionCondition = transitionCondition;
		transitionCondition = newTransitionCondition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.MODE_TRANSITION__TRANSITION_CONDITION, oldTransitionCondition, transitionCondition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType getMeta() {
		return meta;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMeta(cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType newMeta) {
		cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType oldMeta = meta;
		meta = newMeta == null ? META_EDEFAULT : newMeta;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.MODE_TRANSITION__META, oldMeta, meta));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RuntimeMetadataPackage.MODE_TRANSITION__FROM_MODE:
				if (resolve) return getFromMode();
				return basicGetFromMode();
			case RuntimeMetadataPackage.MODE_TRANSITION__TO_MODE:
				if (resolve) return getToMode();
				return basicGetToMode();
			case RuntimeMetadataPackage.MODE_TRANSITION__TRANSITION_CONDITION:
				return getTransitionCondition();
			case RuntimeMetadataPackage.MODE_TRANSITION__META:
				return getMeta();
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
			case RuntimeMetadataPackage.MODE_TRANSITION__FROM_MODE:
				setFromMode((ComponentProcess)newValue);
				return;
			case RuntimeMetadataPackage.MODE_TRANSITION__TO_MODE:
				setToMode((ComponentProcess)newValue);
				return;
			case RuntimeMetadataPackage.MODE_TRANSITION__TRANSITION_CONDITION:
				setTransitionCondition((String)newValue);
				return;
			case RuntimeMetadataPackage.MODE_TRANSITION__META:
				setMeta((cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType)newValue);
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
			case RuntimeMetadataPackage.MODE_TRANSITION__FROM_MODE:
				setFromMode((ComponentProcess)null);
				return;
			case RuntimeMetadataPackage.MODE_TRANSITION__TO_MODE:
				setToMode((ComponentProcess)null);
				return;
			case RuntimeMetadataPackage.MODE_TRANSITION__TRANSITION_CONDITION:
				setTransitionCondition(TRANSITION_CONDITION_EDEFAULT);
				return;
			case RuntimeMetadataPackage.MODE_TRANSITION__META:
				setMeta(META_EDEFAULT);
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
			case RuntimeMetadataPackage.MODE_TRANSITION__FROM_MODE:
				return fromMode != null;
			case RuntimeMetadataPackage.MODE_TRANSITION__TO_MODE:
				return toMode != null;
			case RuntimeMetadataPackage.MODE_TRANSITION__TRANSITION_CONDITION:
				return TRANSITION_CONDITION_EDEFAULT == null ? transitionCondition != null : !TRANSITION_CONDITION_EDEFAULT.equals(transitionCondition);
			case RuntimeMetadataPackage.MODE_TRANSITION__META:
				return meta != META_EDEFAULT;
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
		result.append(" (transitionCondition: ");
		result.append(transitionCondition);
		result.append(", meta: ");
		result.append(meta);
		result.append(')');
		return result.toString();
	}

} //ModeTransitionImpl
