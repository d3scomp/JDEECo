/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl;

import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeTypeOwner;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Knowledge Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeTypeImpl#isIsStructured <em>Is Structured</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeTypeImpl#isIsWrapper <em>Is Wrapper</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeTypeImpl#getClazz <em>Clazz</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeTypeImpl#getOwner <em>Owner</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class KnowledgeTypeImpl extends EObjectImpl implements KnowledgeType {
	/**
	 * The default value of the '{@link #isIsStructured() <em>Is Structured</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsStructured()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_STRUCTURED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsStructured() <em>Is Structured</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsStructured()
	 * @generated
	 * @ordered
	 */
	protected boolean isStructured = IS_STRUCTURED_EDEFAULT;

	/**
	 * The default value of the '{@link #isIsWrapper() <em>Is Wrapper</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsWrapper()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_WRAPPER_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsWrapper() <em>Is Wrapper</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsWrapper()
	 * @generated
	 * @ordered
	 */
	protected boolean isWrapper = IS_WRAPPER_EDEFAULT;

	/**
	 * The cached value of the '{@link #getClazz() <em>Clazz</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClazz()
	 * @generated
	 * @ordered
	 */
	protected Class clazz;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected KnowledgeTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimemodelPackage.Literals.KNOWLEDGE_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsStructured() {
		return isStructured;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsStructured(boolean newIsStructured) {
		boolean oldIsStructured = isStructured;
		isStructured = newIsStructured;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.KNOWLEDGE_TYPE__IS_STRUCTURED, oldIsStructured, isStructured));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsWrapper() {
		return isWrapper;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsWrapper(boolean newIsWrapper) {
		boolean oldIsWrapper = isWrapper;
		isWrapper = newIsWrapper;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.KNOWLEDGE_TYPE__IS_WRAPPER, oldIsWrapper, isWrapper));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Class getClazz() {
		return clazz;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setClazz(Class newClazz) {
		Class oldClazz = clazz;
		clazz = newClazz;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.KNOWLEDGE_TYPE__CLAZZ, oldClazz, clazz));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgeTypeOwner getOwner() {
		if (eContainerFeatureID() != RuntimemodelPackage.KNOWLEDGE_TYPE__OWNER) return null;
		return (KnowledgeTypeOwner)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOwner(KnowledgeTypeOwner newOwner, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newOwner, RuntimemodelPackage.KNOWLEDGE_TYPE__OWNER, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOwner(KnowledgeTypeOwner newOwner) {
		if (newOwner != eInternalContainer() || (eContainerFeatureID() != RuntimemodelPackage.KNOWLEDGE_TYPE__OWNER && newOwner != null)) {
			if (EcoreUtil.isAncestor(this, newOwner))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newOwner != null)
				msgs = ((InternalEObject)newOwner).eInverseAdd(this, RuntimemodelPackage.KNOWLEDGE_TYPE_OWNER__TYPE, KnowledgeTypeOwner.class, msgs);
			msgs = basicSetOwner(newOwner, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.KNOWLEDGE_TYPE__OWNER, newOwner, newOwner));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createFromRaw(Object rawValue) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object storeToRaw(Object value) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimemodelPackage.KNOWLEDGE_TYPE__OWNER:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetOwner((KnowledgeTypeOwner)otherEnd, msgs);
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
			case RuntimemodelPackage.KNOWLEDGE_TYPE__OWNER:
				return basicSetOwner(null, msgs);
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
			case RuntimemodelPackage.KNOWLEDGE_TYPE__OWNER:
				return eInternalContainer().eInverseRemove(this, RuntimemodelPackage.KNOWLEDGE_TYPE_OWNER__TYPE, KnowledgeTypeOwner.class, msgs);
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
			case RuntimemodelPackage.KNOWLEDGE_TYPE__IS_STRUCTURED:
				return isIsStructured();
			case RuntimemodelPackage.KNOWLEDGE_TYPE__IS_WRAPPER:
				return isIsWrapper();
			case RuntimemodelPackage.KNOWLEDGE_TYPE__CLAZZ:
				return getClazz();
			case RuntimemodelPackage.KNOWLEDGE_TYPE__OWNER:
				return getOwner();
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
			case RuntimemodelPackage.KNOWLEDGE_TYPE__IS_STRUCTURED:
				setIsStructured((Boolean)newValue);
				return;
			case RuntimemodelPackage.KNOWLEDGE_TYPE__IS_WRAPPER:
				setIsWrapper((Boolean)newValue);
				return;
			case RuntimemodelPackage.KNOWLEDGE_TYPE__CLAZZ:
				setClazz((Class)newValue);
				return;
			case RuntimemodelPackage.KNOWLEDGE_TYPE__OWNER:
				setOwner((KnowledgeTypeOwner)newValue);
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
			case RuntimemodelPackage.KNOWLEDGE_TYPE__IS_STRUCTURED:
				setIsStructured(IS_STRUCTURED_EDEFAULT);
				return;
			case RuntimemodelPackage.KNOWLEDGE_TYPE__IS_WRAPPER:
				setIsWrapper(IS_WRAPPER_EDEFAULT);
				return;
			case RuntimemodelPackage.KNOWLEDGE_TYPE__CLAZZ:
				setClazz((Class)null);
				return;
			case RuntimemodelPackage.KNOWLEDGE_TYPE__OWNER:
				setOwner((KnowledgeTypeOwner)null);
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
			case RuntimemodelPackage.KNOWLEDGE_TYPE__IS_STRUCTURED:
				return isStructured != IS_STRUCTURED_EDEFAULT;
			case RuntimemodelPackage.KNOWLEDGE_TYPE__IS_WRAPPER:
				return isWrapper != IS_WRAPPER_EDEFAULT;
			case RuntimemodelPackage.KNOWLEDGE_TYPE__CLAZZ:
				return clazz != null;
			case RuntimemodelPackage.KNOWLEDGE_TYPE__OWNER:
				return getOwner() != null;
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
		result.append(" (isStructured: ");
		result.append(isStructured);
		result.append(", isWrapper: ");
		result.append(isWrapper);
		result.append(", clazz: ");
		result.append(clazz);
		result.append(')');
		return result.toString();
	}

} //KnowledgeTypeImpl
