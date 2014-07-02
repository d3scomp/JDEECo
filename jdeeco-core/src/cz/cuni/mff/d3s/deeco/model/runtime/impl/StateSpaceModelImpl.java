/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModel;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.Model;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>State Space Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.StateSpaceModelImpl#getInStates <em>In States</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.StateSpaceModelImpl#getDerivationStates <em>Derivation States</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.StateSpaceModelImpl#getModel <em>Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StateSpaceModelImpl extends MinimalEObjectImpl.Container implements StateSpaceModel {
	/**
	 * The cached value of the '{@link #getInStates() <em>In States</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInStates()
	 * @generated
	 * @ordered
	 */
	protected EList<KnowledgePath> inStates;

	/**
	 * The cached value of the '{@link #getDerivationStates() <em>Derivation States</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDerivationStates()
	 * @generated
	 * @ordered
	 */
	protected EList<KnowledgePath> derivationStates;

	/**
	 * The default value of the '{@link #getModel() <em>Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModel()
	 * @generated
	 * @ordered
	 */
	protected static final Model MODEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getModel() <em>Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModel()
	 * @generated
	 * @ordered
	 */
	protected Model model = MODEL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StateSpaceModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.STATE_SPACE_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<KnowledgePath> getInStates() {
		if (inStates == null) {
			inStates = new EObjectResolvingEList<KnowledgePath>(KnowledgePath.class, this, RuntimeMetadataPackage.STATE_SPACE_MODEL__IN_STATES);
		}
		return inStates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<KnowledgePath> getDerivationStates() {
		if (derivationStates == null) {
			derivationStates = new EObjectResolvingEList<KnowledgePath>(KnowledgePath.class, this, RuntimeMetadataPackage.STATE_SPACE_MODEL__DERIVATION_STATES);
		}
		return derivationStates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModel(Model newModel) {
		Model oldModel = model;
		model = newModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.STATE_SPACE_MODEL__MODEL, oldModel, model));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RuntimeMetadataPackage.STATE_SPACE_MODEL__IN_STATES:
				return getInStates();
			case RuntimeMetadataPackage.STATE_SPACE_MODEL__DERIVATION_STATES:
				return getDerivationStates();
			case RuntimeMetadataPackage.STATE_SPACE_MODEL__MODEL:
				return getModel();
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
			case RuntimeMetadataPackage.STATE_SPACE_MODEL__IN_STATES:
				getInStates().clear();
				getInStates().addAll((Collection<? extends KnowledgePath>)newValue);
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL__DERIVATION_STATES:
				getDerivationStates().clear();
				getDerivationStates().addAll((Collection<? extends KnowledgePath>)newValue);
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL__MODEL:
				setModel((Model)newValue);
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
			case RuntimeMetadataPackage.STATE_SPACE_MODEL__IN_STATES:
				getInStates().clear();
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL__DERIVATION_STATES:
				getDerivationStates().clear();
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL__MODEL:
				setModel(MODEL_EDEFAULT);
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
			case RuntimeMetadataPackage.STATE_SPACE_MODEL__IN_STATES:
				return inStates != null && !inStates.isEmpty();
			case RuntimeMetadataPackage.STATE_SPACE_MODEL__DERIVATION_STATES:
				return derivationStates != null && !derivationStates.isEmpty();
			case RuntimeMetadataPackage.STATE_SPACE_MODEL__MODEL:
				return MODEL_EDEFAULT == null ? model != null : !MODEL_EDEFAULT.equals(model);
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
		result.append(" (model: ");
		result.append(model);
		result.append(')');
		return result.toString();
	}

} //StateSpaceModelImpl
