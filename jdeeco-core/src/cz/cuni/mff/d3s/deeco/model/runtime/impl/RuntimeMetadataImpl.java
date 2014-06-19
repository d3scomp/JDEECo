/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
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
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataImpl#getEnsembleDefinitions <em>Ensemble Definitions</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataImpl#getComponentInstances <em>Component Instances</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RuntimeMetadataImpl extends MinimalEObjectImpl.Container implements RuntimeMetadata {
	/**
	 * The cached value of the '{@link #getEnsembleDefinitions() <em>Ensemble Definitions</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnsembleDefinitions()
	 * @generated
	 * @ordered
	 */
	protected EList<EnsembleDefinition> ensembleDefinitions;

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
	public EList<EnsembleDefinition> getEnsembleDefinitions() {
		if (ensembleDefinitions == null) {
			ensembleDefinitions = new EObjectContainmentEList<EnsembleDefinition>(EnsembleDefinition.class, this, RuntimeMetadataPackage.RUNTIME_METADATA__ENSEMBLE_DEFINITIONS);
		}
		return ensembleDefinitions;
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
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimeMetadataPackage.RUNTIME_METADATA__ENSEMBLE_DEFINITIONS:
				return ((InternalEList<?>)getEnsembleDefinitions()).basicRemove(otherEnd, msgs);
			case RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENT_INSTANCES:
				return ((InternalEList<?>)getComponentInstances()).basicRemove(otherEnd, msgs);
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
			case RuntimeMetadataPackage.RUNTIME_METADATA__ENSEMBLE_DEFINITIONS:
				return getEnsembleDefinitions();
			case RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENT_INSTANCES:
				return getComponentInstances();
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
			case RuntimeMetadataPackage.RUNTIME_METADATA__ENSEMBLE_DEFINITIONS:
				getEnsembleDefinitions().clear();
				getEnsembleDefinitions().addAll((Collection<? extends EnsembleDefinition>)newValue);
				return;
			case RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENT_INSTANCES:
				getComponentInstances().clear();
				getComponentInstances().addAll((Collection<? extends ComponentInstance>)newValue);
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
			case RuntimeMetadataPackage.RUNTIME_METADATA__ENSEMBLE_DEFINITIONS:
				getEnsembleDefinitions().clear();
				return;
			case RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENT_INSTANCES:
				getComponentInstances().clear();
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
			case RuntimeMetadataPackage.RUNTIME_METADATA__ENSEMBLE_DEFINITIONS:
				return ensembleDefinitions != null && !ensembleDefinitions.isEmpty();
			case RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENT_INSTANCES:
				return componentInstances != null && !componentInstances.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //RuntimeMetadataImpl
