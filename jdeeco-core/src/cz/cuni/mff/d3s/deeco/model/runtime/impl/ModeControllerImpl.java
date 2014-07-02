/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ModeController;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mode Controller</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ModeControllerImpl#getInitMode <em>Init Mode</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ModeControllerImpl#getParentMode <em>Parent Mode</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ModeControllerImpl#getAllModes <em>All Modes</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ModeControllerImpl#getComponentInstance <em>Component Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModeControllerImpl extends MinimalEObjectImpl.Container implements ModeController {
	/**
	 * The cached value of the '{@link #getInitMode() <em>Init Mode</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitMode()
	 * @generated
	 * @ordered
	 */
	protected ComponentProcess initMode;

	/**
	 * The cached value of the '{@link #getParentMode() <em>Parent Mode</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParentMode()
	 * @generated
	 * @ordered
	 */
	protected ComponentProcess parentMode;

	/**
	 * The cached value of the '{@link #getAllModes() <em>All Modes</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllModes()
	 * @generated
	 * @ordered
	 */
	protected EList<ComponentProcess> allModes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModeControllerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.MODE_CONTROLLER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentProcess getInitMode() {
		if (initMode != null && initMode.eIsProxy()) {
			InternalEObject oldInitMode = (InternalEObject)initMode;
			initMode = (ComponentProcess)eResolveProxy(oldInitMode);
			if (initMode != oldInitMode) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, RuntimeMetadataPackage.MODE_CONTROLLER__INIT_MODE, oldInitMode, initMode));
			}
		}
		return initMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentProcess basicGetInitMode() {
		return initMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInitMode(ComponentProcess newInitMode) {
		ComponentProcess oldInitMode = initMode;
		initMode = newInitMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.MODE_CONTROLLER__INIT_MODE, oldInitMode, initMode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentProcess getParentMode() {
		if (parentMode != null && parentMode.eIsProxy()) {
			InternalEObject oldParentMode = (InternalEObject)parentMode;
			parentMode = (ComponentProcess)eResolveProxy(oldParentMode);
			if (parentMode != oldParentMode) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, RuntimeMetadataPackage.MODE_CONTROLLER__PARENT_MODE, oldParentMode, parentMode));
			}
		}
		return parentMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentProcess basicGetParentMode() {
		return parentMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentMode(ComponentProcess newParentMode) {
		ComponentProcess oldParentMode = parentMode;
		parentMode = newParentMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.MODE_CONTROLLER__PARENT_MODE, oldParentMode, parentMode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ComponentProcess> getAllModes() {
		if (allModes == null) {
			allModes = new EObjectResolvingEList<ComponentProcess>(ComponentProcess.class, this, RuntimeMetadataPackage.MODE_CONTROLLER__ALL_MODES);
		}
		return allModes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentInstance getComponentInstance() {
		if (eContainerFeatureID() != RuntimeMetadataPackage.MODE_CONTROLLER__COMPONENT_INSTANCE) return null;
		return (ComponentInstance)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetComponentInstance(ComponentInstance newComponentInstance, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newComponentInstance, RuntimeMetadataPackage.MODE_CONTROLLER__COMPONENT_INSTANCE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComponentInstance(ComponentInstance newComponentInstance) {
		if (newComponentInstance != eInternalContainer() || (eContainerFeatureID() != RuntimeMetadataPackage.MODE_CONTROLLER__COMPONENT_INSTANCE && newComponentInstance != null)) {
			if (EcoreUtil.isAncestor(this, newComponentInstance))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newComponentInstance != null)
				msgs = ((InternalEObject)newComponentInstance).eInverseAdd(this, RuntimeMetadataPackage.COMPONENT_INSTANCE__MODE_CONTROLLERS, ComponentInstance.class, msgs);
			msgs = basicSetComponentInstance(newComponentInstance, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.MODE_CONTROLLER__COMPONENT_INSTANCE, newComponentInstance, newComponentInstance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimeMetadataPackage.MODE_CONTROLLER__COMPONENT_INSTANCE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetComponentInstance((ComponentInstance)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimeMetadataPackage.MODE_CONTROLLER__COMPONENT_INSTANCE:
				return basicSetComponentInstance(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case RuntimeMetadataPackage.MODE_CONTROLLER__COMPONENT_INSTANCE:
				return eInternalContainer().eInverseRemove(this, RuntimeMetadataPackage.COMPONENT_INSTANCE__MODE_CONTROLLERS, ComponentInstance.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RuntimeMetadataPackage.MODE_CONTROLLER__INIT_MODE:
				if (resolve) return getInitMode();
				return basicGetInitMode();
			case RuntimeMetadataPackage.MODE_CONTROLLER__PARENT_MODE:
				if (resolve) return getParentMode();
				return basicGetParentMode();
			case RuntimeMetadataPackage.MODE_CONTROLLER__ALL_MODES:
				return getAllModes();
			case RuntimeMetadataPackage.MODE_CONTROLLER__COMPONENT_INSTANCE:
				return getComponentInstance();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case RuntimeMetadataPackage.MODE_CONTROLLER__INIT_MODE:
				setInitMode((ComponentProcess)newValue);
				return;
			case RuntimeMetadataPackage.MODE_CONTROLLER__PARENT_MODE:
				setParentMode((ComponentProcess)newValue);
				return;
			case RuntimeMetadataPackage.MODE_CONTROLLER__ALL_MODES:
				getAllModes().clear();
				getAllModes().addAll((Collection<? extends ComponentProcess>)newValue);
				return;
			case RuntimeMetadataPackage.MODE_CONTROLLER__COMPONENT_INSTANCE:
				setComponentInstance((ComponentInstance)newValue);
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
			case RuntimeMetadataPackage.MODE_CONTROLLER__INIT_MODE:
				setInitMode((ComponentProcess)null);
				return;
			case RuntimeMetadataPackage.MODE_CONTROLLER__PARENT_MODE:
				setParentMode((ComponentProcess)null);
				return;
			case RuntimeMetadataPackage.MODE_CONTROLLER__ALL_MODES:
				getAllModes().clear();
				return;
			case RuntimeMetadataPackage.MODE_CONTROLLER__COMPONENT_INSTANCE:
				setComponentInstance((ComponentInstance)null);
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
			case RuntimeMetadataPackage.MODE_CONTROLLER__INIT_MODE:
				return initMode != null;
			case RuntimeMetadataPackage.MODE_CONTROLLER__PARENT_MODE:
				return parentMode != null;
			case RuntimeMetadataPackage.MODE_CONTROLLER__ALL_MODES:
				return allModes != null && !allModes.isEmpty();
			case RuntimeMetadataPackage.MODE_CONTROLLER__COMPONENT_INSTANCE:
				return getComponentInstance() != null;
		}
		return super.eIsSet(featureID);
	}

} //ModeControllerImpl
