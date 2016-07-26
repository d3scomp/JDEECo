/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;
import java.lang.reflect.Type;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ParameterImpl#getKind <em>Kind</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ParameterImpl#getKnowledgePath <em>Knowledge Path</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ParameterImpl#getType <em>Type</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ParameterImpl#getGenericType <em>Generic Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ParameterImpl extends MinimalEObjectImpl.Container implements Parameter {
	/**
	 * The default value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected static final ParameterKind KIND_EDEFAULT = ParameterKind.INOUT;

	/**
	 * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected ParameterKind kind = KIND_EDEFAULT;

	/**
	 * This is true if the Kind attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean kindESet;

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
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected Class type;

	/**
	 * The default value of the '{@link #getGenericType() <em>Generic Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGenericType()
	 * @generated
	 * @ordered
	 */
	protected static final Type GENERIC_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGenericType() <em>Generic Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGenericType()
	 * @generated
	 * @ordered
	 */
	protected Type genericType = GENERIC_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ParameterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.PARAMETER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterKind getKind() {
		return kind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKind(ParameterKind newKind) {
		ParameterKind oldKind = kind;
		kind = newKind == null ? KIND_EDEFAULT : newKind;
		boolean oldKindESet = kindESet;
		kindESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.PARAMETER__KIND, oldKind, kind, !oldKindESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetKind() {
		ParameterKind oldKind = kind;
		boolean oldKindESet = kindESet;
		kind = KIND_EDEFAULT;
		kindESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, RuntimeMetadataPackage.PARAMETER__KIND, oldKind, KIND_EDEFAULT, oldKindESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetKind() {
		return kindESet;
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH, oldKnowledgePath, newKnowledgePath);
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
				msgs = ((InternalEObject)knowledgePath).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH, null, msgs);
			if (newKnowledgePath != null)
				msgs = ((InternalEObject)newKnowledgePath).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH, null, msgs);
			msgs = basicSetKnowledgePath(newKnowledgePath, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH, newKnowledgePath, newKnowledgePath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Class getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(Class newType) {
		Class oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.PARAMETER__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Type getGenericType() {
		return genericType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGenericType(Type newGenericType) {
		Type oldGenericType = genericType;
		genericType = newGenericType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.PARAMETER__GENERIC_TYPE, oldGenericType, genericType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH:
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
			case RuntimeMetadataPackage.PARAMETER__KIND:
				return getKind();
			case RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH:
				return getKnowledgePath();
			case RuntimeMetadataPackage.PARAMETER__TYPE:
				return getType();
			case RuntimeMetadataPackage.PARAMETER__GENERIC_TYPE:
				return getGenericType();
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
			case RuntimeMetadataPackage.PARAMETER__KIND:
				setKind((ParameterKind)newValue);
				return;
			case RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH:
				setKnowledgePath((KnowledgePath)newValue);
				return;
			case RuntimeMetadataPackage.PARAMETER__TYPE:
				setType((Class)newValue);
				return;
			case RuntimeMetadataPackage.PARAMETER__GENERIC_TYPE:
				setGenericType((Type)newValue);
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
			case RuntimeMetadataPackage.PARAMETER__KIND:
				unsetKind();
				return;
			case RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH:
				setKnowledgePath((KnowledgePath)null);
				return;
			case RuntimeMetadataPackage.PARAMETER__TYPE:
				setType((Class)null);
				return;
			case RuntimeMetadataPackage.PARAMETER__GENERIC_TYPE:
				setGenericType(GENERIC_TYPE_EDEFAULT);
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
			case RuntimeMetadataPackage.PARAMETER__KIND:
				return isSetKind();
			case RuntimeMetadataPackage.PARAMETER__KNOWLEDGE_PATH:
				return knowledgePath != null;
			case RuntimeMetadataPackage.PARAMETER__TYPE:
				return type != null;
			case RuntimeMetadataPackage.PARAMETER__GENERIC_TYPE:
				return GENERIC_TYPE_EDEFAULT == null ? genericType != null : !GENERIC_TYPE_EDEFAULT.equals(genericType);
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
		result.append(" (kind: ");
		if (kindESet) result.append(kind); else result.append("<unset>");
		result.append(", type: ");
		result.append(type);
		result.append(", genericType: ");
		result.append(genericType);
		result.append(')');
		return result.toString();
	}

} //ParameterImpl
