/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;
import cz.cuni.mff.d3s.deeco.modes.DEECoMode;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component Process</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentProcessImpl#getName <em>Name</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentProcessImpl#getComponentInstance <em>Component Instance</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentProcessImpl#isActive <em>Active</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentProcessImpl#getTriggers <em>Triggers</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentProcessImpl#getModes <em>Modes</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ComponentProcessImpl extends InvocableImpl implements ComponentProcess {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #isActive() <em>Active</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isActive()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ACTIVE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isActive() <em>Active</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isActive()
	 * @generated
	 * @ordered
	 */
	protected boolean active = ACTIVE_EDEFAULT;

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
	 * The cached value of the '{@link #getModes() <em>Modes</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModes()
	 * @generated
	 * @ordered
	 */
	protected EList<DEECoMode> modes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComponentProcessImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.COMPONENT_PROCESS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.COMPONENT_PROCESS__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentInstance getComponentInstance() {
		if (eContainerFeatureID() != RuntimeMetadataPackage.COMPONENT_PROCESS__COMPONENT_INSTANCE) return null;
		return (ComponentInstance)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetComponentInstance(ComponentInstance newComponentInstance, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newComponentInstance, RuntimeMetadataPackage.COMPONENT_PROCESS__COMPONENT_INSTANCE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComponentInstance(ComponentInstance newComponentInstance) {
		if (newComponentInstance != eInternalContainer() || (eContainerFeatureID() != RuntimeMetadataPackage.COMPONENT_PROCESS__COMPONENT_INSTANCE && newComponentInstance != null)) {
			if (EcoreUtil.isAncestor(this, newComponentInstance))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newComponentInstance != null)
				msgs = ((InternalEObject)newComponentInstance).eInverseAdd(this, RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_PROCESSES, ComponentInstance.class, msgs);
			msgs = basicSetComponentInstance(newComponentInstance, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.COMPONENT_PROCESS__COMPONENT_INSTANCE, newComponentInstance, newComponentInstance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setActive(boolean newActive) {
		boolean oldActive = active;
		active = newActive;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.COMPONENT_PROCESS__ACTIVE, oldActive, active));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Trigger> getTriggers() {
		if (triggers == null) {
			triggers = new EObjectContainmentEList<Trigger>(Trigger.class, this, RuntimeMetadataPackage.COMPONENT_PROCESS__TRIGGERS);
		}
		return triggers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DEECoMode> getModes() {
		if (modes == null) {
			modes = new EDataTypeUniqueEList<DEECoMode>(DEECoMode.class, this, RuntimeMetadataPackage.COMPONENT_PROCESS__MODES);
		}
		return modes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimeMetadataPackage.COMPONENT_PROCESS__COMPONENT_INSTANCE:
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
			case RuntimeMetadataPackage.COMPONENT_PROCESS__COMPONENT_INSTANCE:
				return basicSetComponentInstance(null, msgs);
			case RuntimeMetadataPackage.COMPONENT_PROCESS__TRIGGERS:
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
			case RuntimeMetadataPackage.COMPONENT_PROCESS__COMPONENT_INSTANCE:
				return eInternalContainer().eInverseRemove(this, RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_PROCESSES, ComponentInstance.class, msgs);
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
			case RuntimeMetadataPackage.COMPONENT_PROCESS__NAME:
				return getName();
			case RuntimeMetadataPackage.COMPONENT_PROCESS__COMPONENT_INSTANCE:
				return getComponentInstance();
			case RuntimeMetadataPackage.COMPONENT_PROCESS__ACTIVE:
				return isActive();
			case RuntimeMetadataPackage.COMPONENT_PROCESS__TRIGGERS:
				return getTriggers();
			case RuntimeMetadataPackage.COMPONENT_PROCESS__MODES:
				return getModes();
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
			case RuntimeMetadataPackage.COMPONENT_PROCESS__NAME:
				setName((String)newValue);
				return;
			case RuntimeMetadataPackage.COMPONENT_PROCESS__COMPONENT_INSTANCE:
				setComponentInstance((ComponentInstance)newValue);
				return;
			case RuntimeMetadataPackage.COMPONENT_PROCESS__ACTIVE:
				setActive((Boolean)newValue);
				return;
			case RuntimeMetadataPackage.COMPONENT_PROCESS__TRIGGERS:
				getTriggers().clear();
				getTriggers().addAll((Collection<? extends Trigger>)newValue);
				return;
			case RuntimeMetadataPackage.COMPONENT_PROCESS__MODES:
				getModes().clear();
				getModes().addAll((Collection<? extends DEECoMode>)newValue);
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
			case RuntimeMetadataPackage.COMPONENT_PROCESS__NAME:
				setName(NAME_EDEFAULT);
				return;
			case RuntimeMetadataPackage.COMPONENT_PROCESS__COMPONENT_INSTANCE:
				setComponentInstance((ComponentInstance)null);
				return;
			case RuntimeMetadataPackage.COMPONENT_PROCESS__ACTIVE:
				setActive(ACTIVE_EDEFAULT);
				return;
			case RuntimeMetadataPackage.COMPONENT_PROCESS__TRIGGERS:
				getTriggers().clear();
				return;
			case RuntimeMetadataPackage.COMPONENT_PROCESS__MODES:
				getModes().clear();
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
			case RuntimeMetadataPackage.COMPONENT_PROCESS__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case RuntimeMetadataPackage.COMPONENT_PROCESS__COMPONENT_INSTANCE:
				return getComponentInstance() != null;
			case RuntimeMetadataPackage.COMPONENT_PROCESS__ACTIVE:
				return active != ACTIVE_EDEFAULT;
			case RuntimeMetadataPackage.COMPONENT_PROCESS__TRIGGERS:
				return triggers != null && !triggers.isEmpty();
			case RuntimeMetadataPackage.COMPONENT_PROCESS__MODES:
				return modes != null && !modes.isEmpty();
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
		result.append(" (name: ");
		result.append(name);
		result.append(", active: ");
		result.append(active);
		result.append(", modes: ");
		result.append(modes);
		result.append(')');
		return result.toString();
	}

} //ComponentProcessImpl
