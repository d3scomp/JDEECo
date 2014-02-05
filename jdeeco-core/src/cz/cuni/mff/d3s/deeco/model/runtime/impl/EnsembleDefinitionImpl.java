/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Condition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import cz.cuni.mff.d3s.deeco.network.CommunicationBoundaryPredicate;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ensemble Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.EnsembleDefinitionImpl#getName <em>Name</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.EnsembleDefinitionImpl#getMembership <em>Membership</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.EnsembleDefinitionImpl#getKnowledgeExchange <em>Knowledge Exchange</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.EnsembleDefinitionImpl#getTriggers <em>Triggers</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.EnsembleDefinitionImpl#getCommunicationBoundary <em>Communication Boundary</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EnsembleDefinitionImpl extends MinimalEObjectImpl.Container implements EnsembleDefinition {
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
	 * The cached value of the '{@link #getMembership() <em>Membership</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMembership()
	 * @generated
	 * @ordered
	 */
	protected Condition membership;

	/**
	 * The cached value of the '{@link #getKnowledgeExchange() <em>Knowledge Exchange</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKnowledgeExchange()
	 * @generated
	 * @ordered
	 */
	protected Exchange knowledgeExchange;

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
	 * The default value of the '{@link #getCommunicationBoundary() <em>Communication Boundary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommunicationBoundary()
	 * @generated
	 * @ordered
	 */
	protected static final CommunicationBoundaryPredicate COMMUNICATION_BOUNDARY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCommunicationBoundary() <em>Communication Boundary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommunicationBoundary()
	 * @generated
	 * @ordered
	 */
	protected CommunicationBoundaryPredicate communicationBoundary = COMMUNICATION_BOUNDARY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EnsembleDefinitionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.ENSEMBLE_DEFINITION;
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
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.ENSEMBLE_DEFINITION__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Condition getMembership() {
		return membership;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMembership(Condition newMembership, NotificationChain msgs) {
		Condition oldMembership = membership;
		membership = newMembership;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.ENSEMBLE_DEFINITION__MEMBERSHIP, oldMembership, newMembership);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMembership(Condition newMembership) {
		if (newMembership != membership) {
			NotificationChain msgs = null;
			if (membership != null)
				msgs = ((InternalEObject)membership).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.ENSEMBLE_DEFINITION__MEMBERSHIP, null, msgs);
			if (newMembership != null)
				msgs = ((InternalEObject)newMembership).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.ENSEMBLE_DEFINITION__MEMBERSHIP, null, msgs);
			msgs = basicSetMembership(newMembership, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.ENSEMBLE_DEFINITION__MEMBERSHIP, newMembership, newMembership));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Exchange getKnowledgeExchange() {
		return knowledgeExchange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetKnowledgeExchange(Exchange newKnowledgeExchange, NotificationChain msgs) {
		Exchange oldKnowledgeExchange = knowledgeExchange;
		knowledgeExchange = newKnowledgeExchange;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.ENSEMBLE_DEFINITION__KNOWLEDGE_EXCHANGE, oldKnowledgeExchange, newKnowledgeExchange);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKnowledgeExchange(Exchange newKnowledgeExchange) {
		if (newKnowledgeExchange != knowledgeExchange) {
			NotificationChain msgs = null;
			if (knowledgeExchange != null)
				msgs = ((InternalEObject)knowledgeExchange).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.ENSEMBLE_DEFINITION__KNOWLEDGE_EXCHANGE, null, msgs);
			if (newKnowledgeExchange != null)
				msgs = ((InternalEObject)newKnowledgeExchange).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.ENSEMBLE_DEFINITION__KNOWLEDGE_EXCHANGE, null, msgs);
			msgs = basicSetKnowledgeExchange(newKnowledgeExchange, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.ENSEMBLE_DEFINITION__KNOWLEDGE_EXCHANGE, newKnowledgeExchange, newKnowledgeExchange));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Trigger> getTriggers() {
		if (triggers == null) {
			triggers = new EObjectContainmentEList<Trigger>(Trigger.class, this, RuntimeMetadataPackage.ENSEMBLE_DEFINITION__TRIGGERS);
		}
		return triggers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommunicationBoundaryPredicate getCommunicationBoundary() {
		return communicationBoundary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCommunicationBoundary(CommunicationBoundaryPredicate newCommunicationBoundary) {
		CommunicationBoundaryPredicate oldCommunicationBoundary = communicationBoundary;
		communicationBoundary = newCommunicationBoundary;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.ENSEMBLE_DEFINITION__COMMUNICATION_BOUNDARY, oldCommunicationBoundary, communicationBoundary));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__MEMBERSHIP:
				return basicSetMembership(null, msgs);
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__KNOWLEDGE_EXCHANGE:
				return basicSetKnowledgeExchange(null, msgs);
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__TRIGGERS:
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
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__NAME:
				return getName();
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__MEMBERSHIP:
				return getMembership();
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__KNOWLEDGE_EXCHANGE:
				return getKnowledgeExchange();
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__TRIGGERS:
				return getTriggers();
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__COMMUNICATION_BOUNDARY:
				return getCommunicationBoundary();
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
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__NAME:
				setName((String)newValue);
				return;
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__MEMBERSHIP:
				setMembership((Condition)newValue);
				return;
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__KNOWLEDGE_EXCHANGE:
				setKnowledgeExchange((Exchange)newValue);
				return;
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__TRIGGERS:
				getTriggers().clear();
				getTriggers().addAll((Collection<? extends Trigger>)newValue);
				return;
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__COMMUNICATION_BOUNDARY:
				setCommunicationBoundary((CommunicationBoundaryPredicate)newValue);
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
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__NAME:
				setName(NAME_EDEFAULT);
				return;
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__MEMBERSHIP:
				setMembership((Condition)null);
				return;
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__KNOWLEDGE_EXCHANGE:
				setKnowledgeExchange((Exchange)null);
				return;
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__TRIGGERS:
				getTriggers().clear();
				return;
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__COMMUNICATION_BOUNDARY:
				setCommunicationBoundary(COMMUNICATION_BOUNDARY_EDEFAULT);
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
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__MEMBERSHIP:
				return membership != null;
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__KNOWLEDGE_EXCHANGE:
				return knowledgeExchange != null;
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__TRIGGERS:
				return triggers != null && !triggers.isEmpty();
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION__COMMUNICATION_BOUNDARY:
				return COMMUNICATION_BOUNDARY_EDEFAULT == null ? communicationBoundary != null : !COMMUNICATION_BOUNDARY_EDEFAULT.equals(communicationBoundary);
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
		result.append(", communicationBoundary: ");
		result.append(communicationBoundary);
		result.append(')');
		return result.toString();
	}

} //EnsembleDefinitionImpl
