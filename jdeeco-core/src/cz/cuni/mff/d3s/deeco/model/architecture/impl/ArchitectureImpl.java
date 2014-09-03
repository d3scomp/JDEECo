/**
 */
package cz.cuni.mff.d3s.deeco.model.architecture.impl;

import cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture;
import cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance;

import cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage;

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
 * An implementation of the model object '<em><b>Architecture</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitectureImpl#getEnsembleInstances <em>Ensemble Instances</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitectureImpl#getComponentInstances <em>Component Instances</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ArchitectureImpl extends MinimalEObjectImpl.Container implements Architecture {
	/**
	 * The cached value of the '{@link #getEnsembleInstances() <em>Ensemble Instances</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnsembleInstances()
	 * @generated
	 * @ordered
	 */
	protected EList<EnsembleInstance> ensembleInstances;

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
	protected ArchitectureImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ArchitecturePackage.Literals.ARCHITECTURE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EnsembleInstance> getEnsembleInstances() {
		if (ensembleInstances == null) {
			ensembleInstances = new EObjectContainmentEList<EnsembleInstance>(EnsembleInstance.class, this, ArchitecturePackage.ARCHITECTURE__ENSEMBLE_INSTANCES);
		}
		return ensembleInstances;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ComponentInstance> getComponentInstances() {
		if (componentInstances == null) {
			componentInstances = new EObjectContainmentEList<ComponentInstance>(ComponentInstance.class, this, ArchitecturePackage.ARCHITECTURE__COMPONENT_INSTANCES);
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
			case ArchitecturePackage.ARCHITECTURE__ENSEMBLE_INSTANCES:
				return ((InternalEList<?>)getEnsembleInstances()).basicRemove(otherEnd, msgs);
			case ArchitecturePackage.ARCHITECTURE__COMPONENT_INSTANCES:
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
			case ArchitecturePackage.ARCHITECTURE__ENSEMBLE_INSTANCES:
				return getEnsembleInstances();
			case ArchitecturePackage.ARCHITECTURE__COMPONENT_INSTANCES:
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
			case ArchitecturePackage.ARCHITECTURE__ENSEMBLE_INSTANCES:
				getEnsembleInstances().clear();
				getEnsembleInstances().addAll((Collection<? extends EnsembleInstance>)newValue);
				return;
			case ArchitecturePackage.ARCHITECTURE__COMPONENT_INSTANCES:
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
			case ArchitecturePackage.ARCHITECTURE__ENSEMBLE_INSTANCES:
				getEnsembleInstances().clear();
				return;
			case ArchitecturePackage.ARCHITECTURE__COMPONENT_INSTANCES:
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
			case ArchitecturePackage.ARCHITECTURE__ENSEMBLE_INSTANCES:
				return ensembleInstances != null && !ensembleInstances.isEmpty();
			case ArchitecturePackage.ARCHITECTURE__COMPONENT_INSTANCES:
				return componentInstances != null && !componentInstances.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ArchitectureImpl
