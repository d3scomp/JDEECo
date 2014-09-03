/**
 */
package cz.cuni.mff.d3s.deeco.model.architecture.impl;

import cz.cuni.mff.d3s.deeco.model.architecture.api.LocalComponentInstance;

import cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Local Component Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.LocalComponentInstanceImpl#getRuntimeInstance <em>Runtime Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LocalComponentInstanceImpl extends ComponentInstanceImpl implements LocalComponentInstance {
	/**
	 * The default value of the '{@link #getRuntimeInstance() <em>Runtime Instance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRuntimeInstance()
	 * @generated
	 * @ordered
	 */
	protected static final ComponentInstance RUNTIME_INSTANCE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRuntimeInstance() <em>Runtime Instance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRuntimeInstance()
	 * @generated
	 * @ordered
	 */
	protected ComponentInstance runtimeInstance = RUNTIME_INSTANCE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocalComponentInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ArchitecturePackage.Literals.LOCAL_COMPONENT_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentInstance getRuntimeInstance() {
		return runtimeInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRuntimeInstance(ComponentInstance newRuntimeInstance) {
		ComponentInstance oldRuntimeInstance = runtimeInstance;
		runtimeInstance = newRuntimeInstance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArchitecturePackage.LOCAL_COMPONENT_INSTANCE__RUNTIME_INSTANCE, oldRuntimeInstance, runtimeInstance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ArchitecturePackage.LOCAL_COMPONENT_INSTANCE__RUNTIME_INSTANCE:
				return getRuntimeInstance();
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
			case ArchitecturePackage.LOCAL_COMPONENT_INSTANCE__RUNTIME_INSTANCE:
				setRuntimeInstance((ComponentInstance)newValue);
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
			case ArchitecturePackage.LOCAL_COMPONENT_INSTANCE__RUNTIME_INSTANCE:
				setRuntimeInstance(RUNTIME_INSTANCE_EDEFAULT);
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
			case ArchitecturePackage.LOCAL_COMPONENT_INSTANCE__RUNTIME_INSTANCE:
				return RUNTIME_INSTANCE_EDEFAULT == null ? runtimeInstance != null : !RUNTIME_INSTANCE_EDEFAULT.equals(runtimeInstance);
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
		result.append(" (runtimeInstance: ");
		result.append(runtimeInstance);
		result.append(')');
		return result.toString();
	}

} //LocalComponentInstanceImpl
