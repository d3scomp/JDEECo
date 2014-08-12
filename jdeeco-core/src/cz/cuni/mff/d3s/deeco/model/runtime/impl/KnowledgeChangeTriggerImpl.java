/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;


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
	 * The cached value of the '{@link #getKnowledgePath() <em>Knowledge Path</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKnowledgePath()
	 * @generated
	 * @ordered
	 */
	protected KnowledgePath knowledgePath;

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
		return RuntimeMetadataPackage.Literals.KNOWLEDGE_CHANGE_TRIGGER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgePath getKnowledgePath() {
		return knowledgePath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetKnowledgePath(KnowledgePath newKnowledgePath, NotificationChain msgs) {
		KnowledgePath oldKnowledgePath = knowledgePath;
		knowledgePath = newKnowledgePath;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH, oldKnowledgePath, newKnowledgePath);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKnowledgePath(KnowledgePath newKnowledgePath) {
		if (newKnowledgePath != knowledgePath) {
			NotificationChain msgs = null;
			if (knowledgePath != null)
				msgs = ((InternalEObject)knowledgePath).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH, null, msgs);
			if (newKnowledgePath != null)
				msgs = ((InternalEObject)newKnowledgePath).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH, null, msgs);
			msgs = basicSetKnowledgePath(newKnowledgePath, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH, newKnowledgePath, newKnowledgePath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimeMetadataPackage.KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH:
				return basicSetKnowledgePath(null, msgs);
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
			case RuntimeMetadataPackage.KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH:
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
			case RuntimeMetadataPackage.KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH:
				setKnowledgePath((KnowledgePath)newValue);
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
			case RuntimeMetadataPackage.KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH:
				setKnowledgePath((KnowledgePath)null);
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
			case RuntimeMetadataPackage.KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH:
				return knowledgePath != null;
		}
		return super.eIsSet(featureID);
	}

} //KnowledgeChangeTriggerImpl
