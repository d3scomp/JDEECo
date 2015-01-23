/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ContextKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Path Security Role Argument</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathSecurityRoleArgumentImpl#getKnowledgePath <em>Knowledge Path</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathSecurityRoleArgumentImpl#getContextKind <em>Context Kind</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PathSecurityRoleArgumentImpl extends SecurityRoleArgumentImpl implements PathSecurityRoleArgument {
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
	 * The default value of the '{@link #getContextKind() <em>Context Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContextKind()
	 * @generated
	 * @ordered
	 */
	protected static final ContextKind CONTEXT_KIND_EDEFAULT = ContextKind.LOCAL;

	/**
	 * The cached value of the '{@link #getContextKind() <em>Context Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContextKind()
	 * @generated
	 * @ordered
	 */
	protected ContextKind contextKind = CONTEXT_KIND_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PathSecurityRoleArgumentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.PATH_SECURITY_ROLE_ARGUMENT;
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.PATH_SECURITY_ROLE_ARGUMENT__KNOWLEDGE_PATH, oldKnowledgePath, newKnowledgePath);
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
				msgs = ((InternalEObject)knowledgePath).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.PATH_SECURITY_ROLE_ARGUMENT__KNOWLEDGE_PATH, null, msgs);
			if (newKnowledgePath != null)
				msgs = ((InternalEObject)newKnowledgePath).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.PATH_SECURITY_ROLE_ARGUMENT__KNOWLEDGE_PATH, null, msgs);
			msgs = basicSetKnowledgePath(newKnowledgePath, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.PATH_SECURITY_ROLE_ARGUMENT__KNOWLEDGE_PATH, newKnowledgePath, newKnowledgePath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContextKind getContextKind() {
		return contextKind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContextKind(ContextKind newContextKind) {
		ContextKind oldContextKind = contextKind;
		contextKind = newContextKind == null ? CONTEXT_KIND_EDEFAULT : newContextKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.PATH_SECURITY_ROLE_ARGUMENT__CONTEXT_KIND, oldContextKind, contextKind));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimeMetadataPackage.PATH_SECURITY_ROLE_ARGUMENT__KNOWLEDGE_PATH:
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
			case RuntimeMetadataPackage.PATH_SECURITY_ROLE_ARGUMENT__KNOWLEDGE_PATH:
				return getKnowledgePath();
			case RuntimeMetadataPackage.PATH_SECURITY_ROLE_ARGUMENT__CONTEXT_KIND:
				return getContextKind();
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
			case RuntimeMetadataPackage.PATH_SECURITY_ROLE_ARGUMENT__KNOWLEDGE_PATH:
				setKnowledgePath((KnowledgePath)newValue);
				return;
			case RuntimeMetadataPackage.PATH_SECURITY_ROLE_ARGUMENT__CONTEXT_KIND:
				setContextKind((ContextKind)newValue);
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
			case RuntimeMetadataPackage.PATH_SECURITY_ROLE_ARGUMENT__KNOWLEDGE_PATH:
				setKnowledgePath((KnowledgePath)null);
				return;
			case RuntimeMetadataPackage.PATH_SECURITY_ROLE_ARGUMENT__CONTEXT_KIND:
				setContextKind(CONTEXT_KIND_EDEFAULT);
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
			case RuntimeMetadataPackage.PATH_SECURITY_ROLE_ARGUMENT__KNOWLEDGE_PATH:
				return knowledgePath != null;
			case RuntimeMetadataPackage.PATH_SECURITY_ROLE_ARGUMENT__CONTEXT_KIND:
				return contextKind != CONTEXT_KIND_EDEFAULT;
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
		result.append(" (contextKind: ");
		result.append(contextKind);
		result.append(')');
		return result.toString();
	}

} //PathSecurityRoleArgumentImpl
