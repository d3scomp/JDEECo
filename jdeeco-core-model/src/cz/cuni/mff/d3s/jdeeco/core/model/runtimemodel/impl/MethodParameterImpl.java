/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl;

import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterKind;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Method Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MethodParameterImpl#getKind <em>Kind</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MethodParameterImpl#getKnowledgePath <em>Knowledge Path</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MethodParameterImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MethodParameterImpl#getOwner <em>Owner</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MethodParameterImpl extends KnowledgeReferenceImpl implements MethodParameter {
	/**
	 * The default value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected static final ParameterKind KIND_EDEFAULT = ParameterKind.IN;

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
	 * The default value of the '{@link #getKnowledgePath() <em>Knowledge Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKnowledgePath()
	 * @generated
	 * @ordered
	 */
	protected static final String KNOWLEDGE_PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getKnowledgePath() <em>Knowledge Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKnowledgePath()
	 * @generated
	 * @ordered
	 */
	protected String knowledgePath = KNOWLEDGE_PATH_EDEFAULT;

	/**
	 * The default value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected static final Integer INDEX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected Integer index = INDEX_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MethodParameterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimemodelPackage.Literals.METHOD_PARAMETER;
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
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.METHOD_PARAMETER__KIND, oldKind, kind));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getKnowledgePath() {
		return knowledgePath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKnowledgePath(String newKnowledgePath) {
		String oldKnowledgePath = knowledgePath;
		knowledgePath = newKnowledgePath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.METHOD_PARAMETER__KNOWLEDGE_PATH, oldKnowledgePath, knowledgePath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndex(Integer newIndex) {
		Integer oldIndex = index;
		index = newIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.METHOD_PARAMETER__INDEX, oldIndex, index));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterizedMethod getOwner() {
		if (eContainerFeatureID() != RuntimemodelPackage.METHOD_PARAMETER__OWNER) return null;
		return (ParameterizedMethod)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOwner(ParameterizedMethod newOwner, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newOwner, RuntimemodelPackage.METHOD_PARAMETER__OWNER, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOwner(ParameterizedMethod newOwner) {
		if (newOwner != eInternalContainer() || (eContainerFeatureID() != RuntimemodelPackage.METHOD_PARAMETER__OWNER && newOwner != null)) {
			if (EcoreUtil.isAncestor(this, newOwner))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newOwner != null)
				msgs = ((InternalEObject)newOwner).eInverseAdd(this, RuntimemodelPackage.PARAMETERIZED_METHOD__FORMAL_PARAMETERS, ParameterizedMethod.class, msgs);
			msgs = basicSetOwner(newOwner, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimemodelPackage.METHOD_PARAMETER__OWNER, newOwner, newOwner));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RuntimemodelPackage.METHOD_PARAMETER__OWNER:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetOwner((ParameterizedMethod)otherEnd, msgs);
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
			case RuntimemodelPackage.METHOD_PARAMETER__OWNER:
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
			case RuntimemodelPackage.METHOD_PARAMETER__OWNER:
				return eInternalContainer().eInverseRemove(this, RuntimemodelPackage.PARAMETERIZED_METHOD__FORMAL_PARAMETERS, ParameterizedMethod.class, msgs);
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
			case RuntimemodelPackage.METHOD_PARAMETER__KIND:
				return getKind();
			case RuntimemodelPackage.METHOD_PARAMETER__KNOWLEDGE_PATH:
				return getKnowledgePath();
			case RuntimemodelPackage.METHOD_PARAMETER__INDEX:
				return getIndex();
			case RuntimemodelPackage.METHOD_PARAMETER__OWNER:
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
			case RuntimemodelPackage.METHOD_PARAMETER__KIND:
				setKind((ParameterKind)newValue);
				return;
			case RuntimemodelPackage.METHOD_PARAMETER__KNOWLEDGE_PATH:
				setKnowledgePath((String)newValue);
				return;
			case RuntimemodelPackage.METHOD_PARAMETER__INDEX:
				setIndex((Integer)newValue);
				return;
			case RuntimemodelPackage.METHOD_PARAMETER__OWNER:
				setOwner((ParameterizedMethod)newValue);
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
			case RuntimemodelPackage.METHOD_PARAMETER__KIND:
				setKind(KIND_EDEFAULT);
				return;
			case RuntimemodelPackage.METHOD_PARAMETER__KNOWLEDGE_PATH:
				setKnowledgePath(KNOWLEDGE_PATH_EDEFAULT);
				return;
			case RuntimemodelPackage.METHOD_PARAMETER__INDEX:
				setIndex(INDEX_EDEFAULT);
				return;
			case RuntimemodelPackage.METHOD_PARAMETER__OWNER:
				setOwner((ParameterizedMethod)null);
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
			case RuntimemodelPackage.METHOD_PARAMETER__KIND:
				return kind != KIND_EDEFAULT;
			case RuntimemodelPackage.METHOD_PARAMETER__KNOWLEDGE_PATH:
				return KNOWLEDGE_PATH_EDEFAULT == null ? knowledgePath != null : !KNOWLEDGE_PATH_EDEFAULT.equals(knowledgePath);
			case RuntimemodelPackage.METHOD_PARAMETER__INDEX:
				return INDEX_EDEFAULT == null ? index != null : !INDEX_EDEFAULT.equals(index);
			case RuntimemodelPackage.METHOD_PARAMETER__OWNER:
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
		result.append(" (kind: ");
		result.append(kind);
		result.append(", knowledgePath: ");
		result.append(knowledgePath);
		result.append(", index: ");
		result.append(index);
		result.append(')');
		return result.toString();
	}

} //MethodParameterImpl
