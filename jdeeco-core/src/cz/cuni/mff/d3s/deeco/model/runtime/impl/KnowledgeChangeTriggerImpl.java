/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.runtimePackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Knowledge Change Trigger</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeChangeTriggerImpl#getKnowledgePath <em>Knowledge Path</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class KnowledgeChangeTriggerImpl extends TriggerImpl implements KnowledgeChangeTrigger {
	/**
	 * The cached value of the '{@link #getKnowledgePath() <em>Knowledge Path</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKnowledgePath()
	 * @generated
	 * @ordered
	 */
	protected EList<KnowledgePath> knowledgePath;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected KnowledgeChangeTriggerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return runtimePackage.Literals.KNOWLEDGE_CHANGE_TRIGGER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<KnowledgePath> getKnowledgePath() {
		if (knowledgePath == null) {
			knowledgePath = new EObjectContainmentEList<KnowledgePath>(KnowledgePath.class, this, runtimePackage.KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH);
		}
		return knowledgePath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case runtimePackage.KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH:
				return ((InternalEList<?>)getKnowledgePath()).basicRemove(otherEnd, msgs);
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
			case runtimePackage.KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH:
				return getKnowledgePath();
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
			case runtimePackage.KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH:
				getKnowledgePath().clear();
				getKnowledgePath().addAll((Collection<? extends KnowledgePath>)newValue);
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
			case runtimePackage.KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH:
				getKnowledgePath().clear();
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
			case runtimePackage.KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH:
				return knowledgePath != null && !knowledgePath.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //KnowledgeChangeTriggerImpl
