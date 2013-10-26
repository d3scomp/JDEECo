/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Component;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Ensemble;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Runtime Metadata</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataImpl#getComponentInstances <em>Component Instances</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataImpl#getEnsembles <em>Ensembles</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataImpl#getComponents <em>Components</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RuntimeMetadataImpl extends MinimalEObjectImpl.Container implements RuntimeMetadata {
	/**
	 * The cached value of the '{@link #getComponentInstances() <em>Component Instances</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponentInstances()
	 * @generated
	 * @ordered
	 */
	protected EList<ComponentInstance> componentInstances;

	/**
	 * The cached value of the '{@link #getEnsembles() <em>Ensembles</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnsembles()
	 * @generated
	 * @ordered
	 */
	protected EList<Ensemble> ensembles;

	/**
	 * The cached value of the '{@link #getComponents() <em>Components</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponents()
	 * @generated
	 * @ordered
	 */
	protected EList<Component> components;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RuntimeMetadataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.RUNTIME_METADATA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ComponentInstance> getComponentInstances() {
		if (componentInstances == null) {
			componentInstances = new EObjectContainmentEList<ComponentInstance>(ComponentInstance.class, this, RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENT_INSTANCES);
		}
		return componentInstances;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Ensemble> getEnsembles() {
		if (ensembles == null) {
			ensembles = new EObjectContainmentEList<Ensemble>(Ensemble.class, this, RuntimeMetadataPackage.RUNTIME_METADATA__ENSEMBLES);
		}
		return ensembles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Component> getComponents() {
		if (components == null) {
			components = new EObjectContainmentEList<Component>(Component.class, this, RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENTS);
		}
		return components;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENT_INSTANCES:
				return ((InternalEList<?>)getComponentInstances()).basicRemove(otherEnd, msgs);
			case RuntimeMetadataPackage.RUNTIME_METADATA__ENSEMBLES:
				return ((InternalEList<?>)getEnsembles()).basicRemove(otherEnd, msgs);
			case RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENTS:
				return ((InternalEList<?>)getComponents()).basicRemove(otherEnd, msgs);
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
			case RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENT_INSTANCES:
				return getComponentInstances();
			case RuntimeMetadataPackage.RUNTIME_METADATA__ENSEMBLES:
				return getEnsembles();
			case RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENTS:
				return getComponents();
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
			case RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENT_INSTANCES:
				getComponentInstances().clear();
				getComponentInstances().addAll((Collection<? extends ComponentInstance>)newValue);
				return;
			case RuntimeMetadataPackage.RUNTIME_METADATA__ENSEMBLES:
				getEnsembles().clear();
				getEnsembles().addAll((Collection<? extends Ensemble>)newValue);
				return;
			case RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENTS:
				getComponents().clear();
				getComponents().addAll((Collection<? extends Component>)newValue);
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
			case RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENT_INSTANCES:
				getComponentInstances().clear();
				return;
			case RuntimeMetadataPackage.RUNTIME_METADATA__ENSEMBLES:
				getEnsembles().clear();
				return;
			case RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENTS:
				getComponents().clear();
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
			case RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENT_INSTANCES:
				return componentInstances != null && !componentInstances.isEmpty();
			case RuntimeMetadataPackage.RUNTIME_METADATA__ENSEMBLES:
				return ensembles != null && !ensembles.isEmpty();
			case RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENTS:
				return components != null && !components.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //RuntimeMetadataImpl
