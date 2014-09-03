/**
 */
package cz.cuni.mff.d3s.deeco.model.architecture.impl;

import cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance;

import cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage;

import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ensemble Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.EnsembleInstanceImpl#getEnsembleDefinition <em>Ensemble Definition</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.EnsembleInstanceImpl#getCoordinator <em>Coordinator</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.EnsembleInstanceImpl#getMembers <em>Members</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EnsembleInstanceImpl extends MinimalEObjectImpl.Container implements EnsembleInstance {
	/**
	 * The default value of the '{@link #getEnsembleDefinition() <em>Ensemble Definition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnsembleDefinition()
	 * @generated
	 * @ordered
	 */
	protected static final EnsembleDefinition ENSEMBLE_DEFINITION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEnsembleDefinition() <em>Ensemble Definition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnsembleDefinition()
	 * @generated
	 * @ordered
	 */
	protected EnsembleDefinition ensembleDefinition = ENSEMBLE_DEFINITION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCoordinator() <em>Coordinator</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCoordinator()
	 * @generated
	 * @ordered
	 */
	protected ComponentInstance coordinator;

	/**
	 * The cached value of the '{@link #getMembers() <em>Members</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMembers()
	 * @generated
	 * @ordered
	 */
	protected EList<ComponentInstance> members;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EnsembleInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ArchitecturePackage.Literals.ENSEMBLE_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnsembleDefinition getEnsembleDefinition() {
		return ensembleDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnsembleDefinition(EnsembleDefinition newEnsembleDefinition) {
		EnsembleDefinition oldEnsembleDefinition = ensembleDefinition;
		ensembleDefinition = newEnsembleDefinition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArchitecturePackage.ENSEMBLE_INSTANCE__ENSEMBLE_DEFINITION, oldEnsembleDefinition, ensembleDefinition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentInstance getCoordinator() {
		if (coordinator != null && coordinator.eIsProxy()) {
			InternalEObject oldCoordinator = (InternalEObject)coordinator;
			coordinator = (ComponentInstance)eResolveProxy(oldCoordinator);
			if (coordinator != oldCoordinator) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ArchitecturePackage.ENSEMBLE_INSTANCE__COORDINATOR, oldCoordinator, coordinator));
			}
		}
		return coordinator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentInstance basicGetCoordinator() {
		return coordinator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCoordinator(ComponentInstance newCoordinator) {
		ComponentInstance oldCoordinator = coordinator;
		coordinator = newCoordinator;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArchitecturePackage.ENSEMBLE_INSTANCE__COORDINATOR, oldCoordinator, coordinator));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ComponentInstance> getMembers() {
		if (members == null) {
			members = new EObjectResolvingEList<ComponentInstance>(ComponentInstance.class, this, ArchitecturePackage.ENSEMBLE_INSTANCE__MEMBERS);
		}
		return members;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ArchitecturePackage.ENSEMBLE_INSTANCE__ENSEMBLE_DEFINITION:
				return getEnsembleDefinition();
			case ArchitecturePackage.ENSEMBLE_INSTANCE__COORDINATOR:
				if (resolve) return getCoordinator();
				return basicGetCoordinator();
			case ArchitecturePackage.ENSEMBLE_INSTANCE__MEMBERS:
				return getMembers();
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
			case ArchitecturePackage.ENSEMBLE_INSTANCE__ENSEMBLE_DEFINITION:
				setEnsembleDefinition((EnsembleDefinition)newValue);
				return;
			case ArchitecturePackage.ENSEMBLE_INSTANCE__COORDINATOR:
				setCoordinator((ComponentInstance)newValue);
				return;
			case ArchitecturePackage.ENSEMBLE_INSTANCE__MEMBERS:
				getMembers().clear();
				getMembers().addAll((Collection<? extends ComponentInstance>)newValue);
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
			case ArchitecturePackage.ENSEMBLE_INSTANCE__ENSEMBLE_DEFINITION:
				setEnsembleDefinition(ENSEMBLE_DEFINITION_EDEFAULT);
				return;
			case ArchitecturePackage.ENSEMBLE_INSTANCE__COORDINATOR:
				setCoordinator((ComponentInstance)null);
				return;
			case ArchitecturePackage.ENSEMBLE_INSTANCE__MEMBERS:
				getMembers().clear();
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
			case ArchitecturePackage.ENSEMBLE_INSTANCE__ENSEMBLE_DEFINITION:
				return ENSEMBLE_DEFINITION_EDEFAULT == null ? ensembleDefinition != null : !ENSEMBLE_DEFINITION_EDEFAULT.equals(ensembleDefinition);
			case ArchitecturePackage.ENSEMBLE_INSTANCE__COORDINATOR:
				return coordinator != null;
			case ArchitecturePackage.ENSEMBLE_INSTANCE__MEMBERS:
				return members != null && !members.isEmpty();
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
		result.append(" (ensembleDefinition: ");
		result.append(ensembleDefinition);
		result.append(')');
		return result.toString();
	}

} //EnsembleInstanceImpl
