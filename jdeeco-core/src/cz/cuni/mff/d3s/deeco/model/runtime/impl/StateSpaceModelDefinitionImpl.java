/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccurateValueDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ModelInterface;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>State Space Model Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.StateSpaceModelDefinitionImpl#getTriggerKowledgePath <em>Trigger Kowledge Path</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.StateSpaceModelDefinitionImpl#getModel <em>Model</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.StateSpaceModelDefinitionImpl#getDerivationStates <em>Derivation States</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.StateSpaceModelDefinitionImpl#getComponentInstance <em>Component Instance</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.StateSpaceModelDefinitionImpl#getInStates <em>In States</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.StateSpaceModelDefinitionImpl#getTriggers <em>Triggers</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.StateSpaceModelDefinitionImpl#isIsActive <em>Is Active</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StateSpaceModelDefinitionImpl extends InvocableImpl implements StateSpaceModelDefinition {
	/**
	 * The cached value of the '{@link #getTriggerKowledgePath() <em>Trigger Kowledge Path</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTriggerKowledgePath()
	 * @generated
	 * @ordered
	 */
	protected KnowledgePath triggerKowledgePath;

	/**
	 * The default value of the '{@link #getModel() <em>Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModel()
	 * @generated
	 * @ordered
	 */
	protected static final ModelInterface MODEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getModel() <em>Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModel()
	 * @generated
	 * @ordered
	 */
	protected ModelInterface model = MODEL_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDerivationStates() <em>Derivation States</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDerivationStates()
	 * @generated
	 * @ordered
	 */
	protected EList<InaccurateValueDefinition> derivationStates;

	/**
	 * The cached value of the '{@link #getInStates() <em>In States</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInStates()
	 * @generated
	 * @ordered
	 */
	protected EList<InaccurateValueDefinition> inStates;

	/**
	 * The cached value of the '{@link #getTriggers() <em>Triggers</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTriggers()
	 * @generated
	 * @ordered
	 */
	protected EList<Trigger> triggers;

	/**
	 * The default value of the '{@link #isIsActive() <em>Is Active</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsActive()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_ACTIVE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsActive() <em>Is Active</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsActive()
	 * @generated
	 * @ordered
	 */
	protected boolean isActive = IS_ACTIVE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StateSpaceModelDefinitionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.STATE_SPACE_MODEL_DEFINITION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgePath getTriggerKowledgePath() {
		if (triggerKowledgePath != null && triggerKowledgePath.eIsProxy()) {
			InternalEObject oldTriggerKowledgePath = (InternalEObject)triggerKowledgePath;
			triggerKowledgePath = (KnowledgePath)eResolveProxy(oldTriggerKowledgePath);
			if (triggerKowledgePath != oldTriggerKowledgePath) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__TRIGGER_KOWLEDGE_PATH, oldTriggerKowledgePath, triggerKowledgePath));
			}
		}
		return triggerKowledgePath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgePath basicGetTriggerKowledgePath() {
		return triggerKowledgePath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTriggerKowledgePath(KnowledgePath newTriggerKowledgePath) {
		KnowledgePath oldTriggerKowledgePath = triggerKowledgePath;
		triggerKowledgePath = newTriggerKowledgePath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__TRIGGER_KOWLEDGE_PATH, oldTriggerKowledgePath, triggerKowledgePath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<InaccurateValueDefinition> getInStates() {
		if (inStates == null) {
			inStates = new EDataTypeUniqueEList<InaccurateValueDefinition>(InaccurateValueDefinition.class, this, RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__IN_STATES);
		}
		return inStates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Trigger> getTriggers() {
		if (triggers == null) {
			triggers = new EObjectContainmentEList<Trigger>(Trigger.class, this, RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__TRIGGERS);
		}
		return triggers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsActive() {
		return isActive;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsActive(boolean newIsActive) {
		boolean oldIsActive = isActive;
		isActive = newIsActive;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__IS_ACTIVE, oldIsActive, isActive));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelInterface getModel() {
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModel(ModelInterface newModel) {
		ModelInterface oldModel = model;
		model = newModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__MODEL, oldModel, model));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<InaccurateValueDefinition> getDerivationStates() {
		if (derivationStates == null) {
			derivationStates = new EDataTypeUniqueEList<InaccurateValueDefinition>(InaccurateValueDefinition.class, this, RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__DERIVATION_STATES);
		}
		return derivationStates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentInstance getComponentInstance() {
		if (eContainerFeatureID() != RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__COMPONENT_INSTANCE) return null;
		return (ComponentInstance)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetComponentInstance(ComponentInstance newComponentInstance, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newComponentInstance, RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__COMPONENT_INSTANCE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComponentInstance(ComponentInstance newComponentInstance) {
		if (newComponentInstance != eInternalContainer() || (eContainerFeatureID() != RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__COMPONENT_INSTANCE && newComponentInstance != null)) {
			if (EcoreUtil.isAncestor(this, newComponentInstance))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newComponentInstance != null)
				msgs = ((InternalEObject)newComponentInstance).eInverseAdd(this, RuntimeMetadataPackage.COMPONENT_INSTANCE__STATE_SPACE_MODELS, ComponentInstance.class, msgs);
			msgs = basicSetComponentInstance(newComponentInstance, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__COMPONENT_INSTANCE, newComponentInstance, newComponentInstance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__COMPONENT_INSTANCE:
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
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__COMPONENT_INSTANCE:
				return basicSetComponentInstance(null, msgs);
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__TRIGGERS:
				return ((InternalEList<?>)getTriggers()).basicRemove(otherEnd, msgs);
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
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__COMPONENT_INSTANCE:
				return eInternalContainer().eInverseRemove(this, RuntimeMetadataPackage.COMPONENT_INSTANCE__STATE_SPACE_MODELS, ComponentInstance.class, msgs);
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
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__TRIGGER_KOWLEDGE_PATH:
				if (resolve) return getTriggerKowledgePath();
				return basicGetTriggerKowledgePath();
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__MODEL:
				return getModel();
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__DERIVATION_STATES:
				return getDerivationStates();
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__COMPONENT_INSTANCE:
				return getComponentInstance();
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__IN_STATES:
				return getInStates();
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__TRIGGERS:
				return getTriggers();
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__IS_ACTIVE:
				return isIsActive();
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
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__TRIGGER_KOWLEDGE_PATH:
				setTriggerKowledgePath((KnowledgePath)newValue);
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__MODEL:
				setModel((ModelInterface)newValue);
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__DERIVATION_STATES:
				getDerivationStates().clear();
				getDerivationStates().addAll((Collection<? extends InaccurateValueDefinition>)newValue);
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__COMPONENT_INSTANCE:
				setComponentInstance((ComponentInstance)newValue);
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__IN_STATES:
				getInStates().clear();
				getInStates().addAll((Collection<? extends InaccurateValueDefinition>)newValue);
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__TRIGGERS:
				getTriggers().clear();
				getTriggers().addAll((Collection<? extends Trigger>)newValue);
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__IS_ACTIVE:
				setIsActive((Boolean)newValue);
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
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__TRIGGER_KOWLEDGE_PATH:
				setTriggerKowledgePath((KnowledgePath)null);
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__MODEL:
				setModel(MODEL_EDEFAULT);
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__DERIVATION_STATES:
				getDerivationStates().clear();
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__COMPONENT_INSTANCE:
				setComponentInstance((ComponentInstance)null);
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__IN_STATES:
				getInStates().clear();
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__TRIGGERS:
				getTriggers().clear();
				return;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__IS_ACTIVE:
				setIsActive(IS_ACTIVE_EDEFAULT);
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
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__TRIGGER_KOWLEDGE_PATH:
				return triggerKowledgePath != null;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__MODEL:
				return MODEL_EDEFAULT == null ? model != null : !MODEL_EDEFAULT.equals(model);
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__DERIVATION_STATES:
				return derivationStates != null && !derivationStates.isEmpty();
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__COMPONENT_INSTANCE:
				return getComponentInstance() != null;
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__IN_STATES:
				return inStates != null && !inStates.isEmpty();
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__TRIGGERS:
				return triggers != null && !triggers.isEmpty();
			case RuntimeMetadataPackage.STATE_SPACE_MODEL_DEFINITION__IS_ACTIVE:
				return isActive != IS_ACTIVE_EDEFAULT;
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
		result.append(", derivationStates: ");
		result.append(derivationStates);
		result.append(", inStates: ");
		result.append(inStates);
		result.append(", isActive: ");
		result.append(isActive);
		result.append(')');
		return result.toString();
	}

} //StateSpaceModelDefinitionImpl
