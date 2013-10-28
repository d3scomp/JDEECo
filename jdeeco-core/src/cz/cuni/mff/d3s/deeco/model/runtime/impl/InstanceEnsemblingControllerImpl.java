/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Ensemble;
import cz.cuni.mff.d3s.deeco.model.runtime.api.InstanceEnsemblingController;

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
 * An implementation of the model object '<em><b>Instance Ensembling Controller</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.InstanceEnsemblingControllerImpl#getComponentInstance <em>Component Instance</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.InstanceEnsemblingControllerImpl#getEnsemble <em>Ensemble</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InstanceEnsemblingControllerImpl extends MinimalEObjectImpl.Container implements InstanceEnsemblingController {
	/**
	 * The cached value of the '{@link #getEnsemble() <em>Ensemble</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnsemble()
	 * @generated
	 * @ordered
	 */
	protected Ensemble ensemble;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InstanceEnsemblingControllerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.INSTANCE_ENSEMBLING_CONTROLLER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentInstance getComponentInstance() {
		if (eContainerFeatureID() != RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__COMPONENT_INSTANCE) return null;
		return (ComponentInstance)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetComponentInstance(ComponentInstance newComponentInstance, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newComponentInstance, RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__COMPONENT_INSTANCE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComponentInstance(ComponentInstance newComponentInstance) {
		if (newComponentInstance != eInternalContainer() || (eContainerFeatureID() != RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__COMPONENT_INSTANCE && newComponentInstance != null)) {
			if (EcoreUtil.isAncestor(this, newComponentInstance))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newComponentInstance != null)
				msgs = ((InternalEObject)newComponentInstance).eInverseAdd(this, RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLING_CONTROLLERS, ComponentInstance.class, msgs);
			msgs = basicSetComponentInstance(newComponentInstance, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__COMPONENT_INSTANCE, newComponentInstance, newComponentInstance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Ensemble getEnsemble() {
		if (ensemble != null && ensemble.eIsProxy()) {
			InternalEObject oldEnsemble = (InternalEObject)ensemble;
			ensemble = (Ensemble)eResolveProxy(oldEnsemble);
			if (ensemble != oldEnsemble) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__ENSEMBLE, oldEnsemble, ensemble));
			}
		}
		return ensemble;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Ensemble basicGetEnsemble() {
		return ensemble;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnsemble(Ensemble newEnsemble) {
		Ensemble oldEnsemble = ensemble;
		ensemble = newEnsemble;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__ENSEMBLE, oldEnsemble, ensemble));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__COMPONENT_INSTANCE:
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
			case RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__COMPONENT_INSTANCE:
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
			case RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__COMPONENT_INSTANCE:
				return eInternalContainer().eInverseRemove(this, RuntimeMetadataPackage.COMPONENT_INSTANCE__ENSEMBLING_CONTROLLERS, ComponentInstance.class, msgs);
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
			case RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__COMPONENT_INSTANCE:
				return getComponentInstance();
			case RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__ENSEMBLE:
				if (resolve) return getEnsemble();
				return basicGetEnsemble();
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
			case RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__COMPONENT_INSTANCE:
				setComponentInstance((ComponentInstance)newValue);
				return;
			case RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__ENSEMBLE:
				setEnsemble((Ensemble)newValue);
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
			case RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__COMPONENT_INSTANCE:
				setComponentInstance((ComponentInstance)null);
				return;
			case RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__ENSEMBLE:
				setEnsemble((Ensemble)null);
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
			case RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__COMPONENT_INSTANCE:
				return getComponentInstance() != null;
			case RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER__ENSEMBLE:
				return ensemble != null;
		}
		return super.eIsSet(featureID);
	}

} //InstanceEnsemblingControllerImpl
