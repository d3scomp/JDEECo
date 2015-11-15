/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Knowledge Security Tag</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeSecurityTagImpl#getRequiredRole <em>Required Role</em>}</li>
 * </ul>
 *
 * @generated
 */
public class KnowledgeSecurityTagImpl extends SecurityTagImpl implements KnowledgeSecurityTag {
	/**
	 * The cached value of the '{@link #getRequiredRole() <em>Required Role</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequiredRole()
	 * @generated
	 * @ordered
	 */
	protected SecurityRole requiredRole;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected KnowledgeSecurityTagImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.KNOWLEDGE_SECURITY_TAG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SecurityRole getRequiredRole() {
		return requiredRole;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRequiredRole(SecurityRole newRequiredRole, NotificationChain msgs) {
		SecurityRole oldRequiredRole = requiredRole;
		requiredRole = newRequiredRole;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.KNOWLEDGE_SECURITY_TAG__REQUIRED_ROLE, oldRequiredRole, newRequiredRole);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRequiredRole(SecurityRole newRequiredRole) {
		if (newRequiredRole != requiredRole) {
			NotificationChain msgs = null;
			if (requiredRole != null)
				msgs = ((InternalEObject)requiredRole).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.KNOWLEDGE_SECURITY_TAG__REQUIRED_ROLE, null, msgs);
			if (newRequiredRole != null)
				msgs = ((InternalEObject)newRequiredRole).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.KNOWLEDGE_SECURITY_TAG__REQUIRED_ROLE, null, msgs);
			msgs = basicSetRequiredRole(newRequiredRole, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.KNOWLEDGE_SECURITY_TAG__REQUIRED_ROLE, newRequiredRole, newRequiredRole));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimeMetadataPackage.KNOWLEDGE_SECURITY_TAG__REQUIRED_ROLE:
				return basicSetRequiredRole(null, msgs);
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
			case RuntimeMetadataPackage.KNOWLEDGE_SECURITY_TAG__REQUIRED_ROLE:
				return getRequiredRole();
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
			case RuntimeMetadataPackage.KNOWLEDGE_SECURITY_TAG__REQUIRED_ROLE:
				setRequiredRole((SecurityRole)newValue);
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
			case RuntimeMetadataPackage.KNOWLEDGE_SECURITY_TAG__REQUIRED_ROLE:
				setRequiredRole((SecurityRole)null);
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
			case RuntimeMetadataPackage.KNOWLEDGE_SECURITY_TAG__REQUIRED_ROLE:
				return requiredRole != null;
		}
		return super.eIsSet(featureID);
	}

} //KnowledgeSecurityTagImpl
