/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.AccessRights;
import cz.cuni.mff.d3s.deeco.model.runtime.api.WildcardSecurityTag;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Wildcard Security Tag</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.WildcardSecurityTagImpl#getAccessRights <em>Access Rights</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WildcardSecurityTagImpl extends SecurityTagImpl implements WildcardSecurityTag {
	/**
	 * The default value of the '{@link #getAccessRights() <em>Access Rights</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccessRights()
	 * @generated
	 * @ordered
	 */
	protected static final AccessRights ACCESS_RIGHTS_EDEFAULT = AccessRights.READ_WRITE;

	/**
	 * The cached value of the '{@link #getAccessRights() <em>Access Rights</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccessRights()
	 * @generated
	 * @ordered
	 */
	protected AccessRights accessRights = ACCESS_RIGHTS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected WildcardSecurityTagImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.WILDCARD_SECURITY_TAG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AccessRights getAccessRights() {
		return accessRights;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAccessRights(AccessRights newAccessRights) {
		AccessRights oldAccessRights = accessRights;
		accessRights = newAccessRights == null ? ACCESS_RIGHTS_EDEFAULT : newAccessRights;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.WILDCARD_SECURITY_TAG__ACCESS_RIGHTS, oldAccessRights, accessRights));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RuntimeMetadataPackage.WILDCARD_SECURITY_TAG__ACCESS_RIGHTS:
				return getAccessRights();
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
			case RuntimeMetadataPackage.WILDCARD_SECURITY_TAG__ACCESS_RIGHTS:
				setAccessRights((AccessRights)newValue);
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
			case RuntimeMetadataPackage.WILDCARD_SECURITY_TAG__ACCESS_RIGHTS:
				setAccessRights(ACCESS_RIGHTS_EDEFAULT);
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
			case RuntimeMetadataPackage.WILDCARD_SECURITY_TAG__ACCESS_RIGHTS:
				return accessRights != ACCESS_RIGHTS_EDEFAULT;
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
		result.append(" (accessRights: ");
		result.append(accessRights);
		result.append(')');
		return result.toString();
	}

} //WildcardSecurityTagImpl
