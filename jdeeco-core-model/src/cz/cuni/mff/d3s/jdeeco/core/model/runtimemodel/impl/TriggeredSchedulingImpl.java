/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl;

import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Schedulable;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Trigger;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TriggeredScheduling;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Triggered Scheduling</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TriggeredSchedulingImpl#getOwner <em>Owner</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TriggeredSchedulingImpl#getTriggers <em>Triggers</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TriggeredSchedulingImpl extends EObjectImpl implements TriggeredScheduling {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TriggeredSchedulingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimemodelPackage.Literals.TRIGGERED_SCHEDULING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Schedulable getOwner() {
		if (eContainerFeatureID() != RuntimemodelPackage.TRIGGERED_SCHEDULING__OWNER) return null;
		return (Schedulable)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOwner(Schedulable newOwner, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newOwner, RuntimemodelPackage.TRIGGERED_SCHEDULING__OWNER, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOwner(Schedulable newOwner) {
		if (newOwner != eInternalContainer() || (eContainerFeatureID() != RuntimemodelPackage.TRIGGERED_SCHEDULING__OWNER && newOwner != null)) {
			if (EcoreUtil.isAncestor(this, newOwner))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newOwner != null)
				msgs = ((InternalEObject)newOwner).eInverseAdd(this, RuntimemodelPackage.SCHEDULABLE__SCHEDULING, Schedulable.class, msgs);
			msgs = basicSetOwner(newOwner, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.TRIGGERED_SCHEDULING__OWNER, newOwner, newOwner));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Trigger> getTriggers() {
		if (triggers == null) {
			triggers = new EObjectContainmentWithInverseEList<Trigger>(Trigger.class, this, RuntimemodelPackage.TRIGGERED_SCHEDULING__TRIGGERS, RuntimemodelPackage.TRIGGER__OWNER);
		}
		return triggers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimemodelPackage.TRIGGERED_SCHEDULING__OWNER:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetOwner((Schedulable)otherEnd, msgs);
			case RuntimemodelPackage.TRIGGERED_SCHEDULING__TRIGGERS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getTriggers()).basicAdd(otherEnd, msgs);
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
			case RuntimemodelPackage.TRIGGERED_SCHEDULING__OWNER:
				return basicSetOwner(null, msgs);
			case RuntimemodelPackage.TRIGGERED_SCHEDULING__TRIGGERS:
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
			case RuntimemodelPackage.TRIGGERED_SCHEDULING__OWNER:
				return eInternalContainer().eInverseRemove(this, RuntimemodelPackage.SCHEDULABLE__SCHEDULING, Schedulable.class, msgs);
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
			case RuntimemodelPackage.TRIGGERED_SCHEDULING__OWNER:
				return getOwner();
			case RuntimemodelPackage.TRIGGERED_SCHEDULING__TRIGGERS:
				return getTriggers();
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
			case RuntimemodelPackage.TRIGGERED_SCHEDULING__OWNER:
				setOwner((Schedulable)newValue);
				return;
			case RuntimemodelPackage.TRIGGERED_SCHEDULING__TRIGGERS:
				getTriggers().clear();
				getTriggers().addAll((Collection<? extends Trigger>)newValue);
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
			case RuntimemodelPackage.TRIGGERED_SCHEDULING__OWNER:
				setOwner((Schedulable)null);
				return;
			case RuntimemodelPackage.TRIGGERED_SCHEDULING__TRIGGERS:
				getTriggers().clear();
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
			case RuntimemodelPackage.TRIGGERED_SCHEDULING__OWNER:
				return getOwner() != null;
			case RuntimemodelPackage.TRIGGERED_SCHEDULING__TRIGGERS:
				return triggers != null && !triggers.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //TriggeredSchedulingImpl
