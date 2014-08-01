/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transition Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.TransitionDefinitionImpl#getFromMode <em>From Mode</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.TransitionDefinitionImpl#getToMode <em>To Mode</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.TransitionDefinitionImpl#isInit <em>Init</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.TransitionDefinitionImpl#getTrigger <em>Trigger</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TransitionDefinitionImpl extends MinimalEObjectImpl.Container implements TransitionDefinition {
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
	 * The default value of the '{@link #isInit() <em>Init</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isInit()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INIT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isInit() <em>Init</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isInit()
	 * @generated
	 * @ordered
	 */
	protected boolean init = INIT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTrigger() <em>Trigger</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTrigger()
	 * @generated
	 * @ordered
	 */
	protected KnowledgeChangeTrigger trigger;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TransitionDefinitionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.TRANSITION_DEFINITION;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, RuntimeMetadataPackage.TRANSITION_DEFINITION__FROM_MODE, oldFromMode, fromMode));
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
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.TRANSITION_DEFINITION__FROM_MODE, oldFromMode, fromMode));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, RuntimeMetadataPackage.TRANSITION_DEFINITION__TO_MODE, oldToMode, toMode));
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
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.TRANSITION_DEFINITION__TO_MODE, oldToMode, toMode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isInit() {
		return init;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInit(boolean newInit) {
		boolean oldInit = init;
		init = newInit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.TRANSITION_DEFINITION__INIT, oldInit, init));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgeChangeTrigger getTrigger() {
		if (trigger != null && trigger.eIsProxy()) {
			InternalEObject oldTrigger = (InternalEObject)trigger;
			trigger = (KnowledgeChangeTrigger)eResolveProxy(oldTrigger);
			if (trigger != oldTrigger) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, RuntimeMetadataPackage.TRANSITION_DEFINITION__TRIGGER, oldTrigger, trigger));
			}
		}
		return trigger;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgeChangeTrigger basicGetTrigger() {
		return trigger;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTrigger(KnowledgeChangeTrigger newTrigger) {
		KnowledgeChangeTrigger oldTrigger = trigger;
		trigger = newTrigger;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.TRANSITION_DEFINITION__TRIGGER, oldTrigger, trigger));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__FROM_MODE:
				if (resolve) return getFromMode();
				return basicGetFromMode();
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__TO_MODE:
				if (resolve) return getToMode();
				return basicGetToMode();
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__INIT:
				return isInit();
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__TRIGGER:
				if (resolve) return getTrigger();
				return basicGetTrigger();
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
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__FROM_MODE:
				setFromMode((ComponentProcess)newValue);
				return;
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__TO_MODE:
				setToMode((ComponentProcess)newValue);
				return;
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__INIT:
				setInit((Boolean)newValue);
				return;
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__TRIGGER:
				setTrigger((KnowledgeChangeTrigger)newValue);
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
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__FROM_MODE:
				setFromMode((ComponentProcess)null);
				return;
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__TO_MODE:
				setToMode((ComponentProcess)null);
				return;
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__INIT:
				setInit(INIT_EDEFAULT);
				return;
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__TRIGGER:
				setTrigger((KnowledgeChangeTrigger)null);
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
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__FROM_MODE:
				return fromMode != null;
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__TO_MODE:
				return toMode != null;
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__INIT:
				return init != INIT_EDEFAULT;
			case RuntimeMetadataPackage.TRANSITION_DEFINITION__TRIGGER:
				return trigger != null;
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
		result.append(" (init: ");
		result.append(init);
		result.append(')');
		return result.toString();
	}

} //TransitionDefinitionImpl
