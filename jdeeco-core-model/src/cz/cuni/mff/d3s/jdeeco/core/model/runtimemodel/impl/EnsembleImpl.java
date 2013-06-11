/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl;

import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeExchange;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MembershipCondition;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Scheduling;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ensemble</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.EnsembleImpl#getScheduling <em>Scheduling</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.EnsembleImpl#getMembership <em>Membership</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.EnsembleImpl#getKnowledgeExchange <em>Knowledge Exchange</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EnsembleImpl extends EObjectImpl implements Ensemble {
	/**
	 * The cached value of the '{@link #getScheduling() <em>Scheduling</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScheduling()
	 * @generated
	 * @ordered
	 */
	protected Scheduling scheduling;

	/**
	 * The cached value of the '{@link #getMembership() <em>Membership</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMembership()
	 * @generated
	 * @ordered
	 */
	protected MembershipCondition membership;

	/**
	 * The cached value of the '{@link #getKnowledgeExchange() <em>Knowledge Exchange</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKnowledgeExchange()
	 * @generated
	 * @ordered
	 */
	protected KnowledgeExchange knowledgeExchange;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EnsembleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimemodelPackage.Literals.ENSEMBLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Scheduling getScheduling() {
		return scheduling;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetScheduling(Scheduling newScheduling, NotificationChain msgs) {
		Scheduling oldScheduling = scheduling;
		scheduling = newScheduling;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.ENSEMBLE__SCHEDULING, oldScheduling, newScheduling);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScheduling(Scheduling newScheduling) {
		if (newScheduling != scheduling) {
			NotificationChain msgs = null;
			if (scheduling != null)
				msgs = ((InternalEObject)scheduling).eInverseRemove(this, RuntimemodelPackage.SCHEDULING__OWNER, Scheduling.class, msgs);
			if (newScheduling != null)
				msgs = ((InternalEObject)newScheduling).eInverseAdd(this, RuntimemodelPackage.SCHEDULING__OWNER, Scheduling.class, msgs);
			msgs = basicSetScheduling(newScheduling, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.ENSEMBLE__SCHEDULING, newScheduling, newScheduling));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MembershipCondition getMembership() {
		return membership;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMembership(MembershipCondition newMembership, NotificationChain msgs) {
		MembershipCondition oldMembership = membership;
		membership = newMembership;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.ENSEMBLE__MEMBERSHIP, oldMembership, newMembership);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMembership(MembershipCondition newMembership) {
		if (newMembership != membership) {
			NotificationChain msgs = null;
			if (membership != null)
				msgs = ((InternalEObject)membership).eInverseRemove(this, RuntimemodelPackage.MEMBERSHIP_CONDITION__ENSEMBLE, MembershipCondition.class, msgs);
			if (newMembership != null)
				msgs = ((InternalEObject)newMembership).eInverseAdd(this, RuntimemodelPackage.MEMBERSHIP_CONDITION__ENSEMBLE, MembershipCondition.class, msgs);
			msgs = basicSetMembership(newMembership, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.ENSEMBLE__MEMBERSHIP, newMembership, newMembership));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgeExchange getKnowledgeExchange() {
		return knowledgeExchange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetKnowledgeExchange(KnowledgeExchange newKnowledgeExchange, NotificationChain msgs) {
		KnowledgeExchange oldKnowledgeExchange = knowledgeExchange;
		knowledgeExchange = newKnowledgeExchange;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.ENSEMBLE__KNOWLEDGE_EXCHANGE, oldKnowledgeExchange, newKnowledgeExchange);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKnowledgeExchange(KnowledgeExchange newKnowledgeExchange) {
		if (newKnowledgeExchange != knowledgeExchange) {
			NotificationChain msgs = null;
			if (knowledgeExchange != null)
				msgs = ((InternalEObject)knowledgeExchange).eInverseRemove(this, RuntimemodelPackage.KNOWLEDGE_EXCHANGE__ENSEMBLE, KnowledgeExchange.class, msgs);
			if (newKnowledgeExchange != null)
				msgs = ((InternalEObject)newKnowledgeExchange).eInverseAdd(this, RuntimemodelPackage.KNOWLEDGE_EXCHANGE__ENSEMBLE, KnowledgeExchange.class, msgs);
			msgs = basicSetKnowledgeExchange(newKnowledgeExchange, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.ENSEMBLE__KNOWLEDGE_EXCHANGE, newKnowledgeExchange, newKnowledgeExchange));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimemodelPackage.ENSEMBLE__SCHEDULING:
				if (scheduling != null)
					msgs = ((InternalEObject)scheduling).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RuntimemodelPackage.ENSEMBLE__SCHEDULING, null, msgs);
				return basicSetScheduling((Scheduling)otherEnd, msgs);
			case RuntimemodelPackage.ENSEMBLE__MEMBERSHIP:
				if (membership != null)
					msgs = ((InternalEObject)membership).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RuntimemodelPackage.ENSEMBLE__MEMBERSHIP, null, msgs);
				return basicSetMembership((MembershipCondition)otherEnd, msgs);
			case RuntimemodelPackage.ENSEMBLE__KNOWLEDGE_EXCHANGE:
				if (knowledgeExchange != null)
					msgs = ((InternalEObject)knowledgeExchange).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RuntimemodelPackage.ENSEMBLE__KNOWLEDGE_EXCHANGE, null, msgs);
				return basicSetKnowledgeExchange((KnowledgeExchange)otherEnd, msgs);
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
			case RuntimemodelPackage.ENSEMBLE__SCHEDULING:
				return basicSetScheduling(null, msgs);
			case RuntimemodelPackage.ENSEMBLE__MEMBERSHIP:
				return basicSetMembership(null, msgs);
			case RuntimemodelPackage.ENSEMBLE__KNOWLEDGE_EXCHANGE:
				return basicSetKnowledgeExchange(null, msgs);
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
			case RuntimemodelPackage.ENSEMBLE__SCHEDULING:
				return getScheduling();
			case RuntimemodelPackage.ENSEMBLE__MEMBERSHIP:
				return getMembership();
			case RuntimemodelPackage.ENSEMBLE__KNOWLEDGE_EXCHANGE:
				return getKnowledgeExchange();
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
			case RuntimemodelPackage.ENSEMBLE__SCHEDULING:
				setScheduling((Scheduling)newValue);
				return;
			case RuntimemodelPackage.ENSEMBLE__MEMBERSHIP:
				setMembership((MembershipCondition)newValue);
				return;
			case RuntimemodelPackage.ENSEMBLE__KNOWLEDGE_EXCHANGE:
				setKnowledgeExchange((KnowledgeExchange)newValue);
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
			case RuntimemodelPackage.ENSEMBLE__SCHEDULING:
				setScheduling((Scheduling)null);
				return;
			case RuntimemodelPackage.ENSEMBLE__MEMBERSHIP:
				setMembership((MembershipCondition)null);
				return;
			case RuntimemodelPackage.ENSEMBLE__KNOWLEDGE_EXCHANGE:
				setKnowledgeExchange((KnowledgeExchange)null);
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
			case RuntimemodelPackage.ENSEMBLE__SCHEDULING:
				return scheduling != null;
			case RuntimemodelPackage.ENSEMBLE__MEMBERSHIP:
				return membership != null;
			case RuntimemodelPackage.ENSEMBLE__KNOWLEDGE_EXCHANGE:
				return knowledgeExchange != null;
		}
		return super.eIsSet(featureID);
	}

} //EnsembleImpl
