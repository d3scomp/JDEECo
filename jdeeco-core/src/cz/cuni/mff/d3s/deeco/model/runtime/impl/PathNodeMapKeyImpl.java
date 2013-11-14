/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Path Node Map Key</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeMapKeyImpl#getKeyPath <em>Key Path</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PathNodeMapKeyImpl extends PathNodeImpl implements PathNodeMapKey {
	/**
	 * The cached value of the '{@link #getKeyPath() <em>Key Path</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKeyPath()
	 * @generated
	 * @ordered
	 */
	protected KnowledgePath keyPath;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PathNodeMapKeyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.PATH_NODE_MAP_KEY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgePath getKeyPath() {
		return keyPath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetKeyPath(KnowledgePath newKeyPath, NotificationChain msgs) {
		KnowledgePath oldKeyPath = keyPath;
		keyPath = newKeyPath;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.PATH_NODE_MAP_KEY__KEY_PATH, oldKeyPath, newKeyPath);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKeyPath(KnowledgePath newKeyPath) {
		if (newKeyPath != keyPath) {
			NotificationChain msgs = null;
			if (keyPath != null)
				msgs = ((InternalEObject)keyPath).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.PATH_NODE_MAP_KEY__KEY_PATH, null, msgs);
			if (newKeyPath != null)
				msgs = ((InternalEObject)newKeyPath).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.PATH_NODE_MAP_KEY__KEY_PATH, null, msgs);
			msgs = basicSetKeyPath(newKeyPath, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.PATH_NODE_MAP_KEY__KEY_PATH, newKeyPath, newKeyPath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimeMetadataPackage.PATH_NODE_MAP_KEY__KEY_PATH:
				return basicSetKeyPath(null, msgs);
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
			case RuntimeMetadataPackage.PATH_NODE_MAP_KEY__KEY_PATH:
				return getKeyPath();
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
			case RuntimeMetadataPackage.PATH_NODE_MAP_KEY__KEY_PATH:
				setKeyPath((KnowledgePath)newValue);
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
			case RuntimeMetadataPackage.PATH_NODE_MAP_KEY__KEY_PATH:
				setKeyPath((KnowledgePath)null);
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
			case RuntimeMetadataPackage.PATH_NODE_MAP_KEY__KEY_PATH:
				return keyPath != null;
		}
		return super.eIsSet(featureID);
	}

} //PathNodeMapKeyImpl
