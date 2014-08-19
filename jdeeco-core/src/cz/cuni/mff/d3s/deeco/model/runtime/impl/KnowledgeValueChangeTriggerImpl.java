/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComparisonType;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger;

import cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ConditionType;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Knowledge Value Change Trigger</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueChangeTriggerImpl#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueChangeTriggerImpl#getTo <em>To</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class KnowledgeValueChangeTriggerImpl extends KnowledgeChangeTriggerImpl implements KnowledgeValueChangeTrigger {
	/**
	 * The cached value of the '{@link #getConstraints() <em>Constraints</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList<ConditionType> constraints;
	/**
	 * The cached value of the '{@link #getTo() <em>To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTo()
	 * @generated
	 * @ordered
	 */
	protected ComponentProcess to;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected KnowledgeValueChangeTriggerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.KNOWLEDGE_VALUE_CHANGE_TRIGGER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ConditionType> getConstraints() {
		if (constraints == null) {
			constraints = new EDataTypeUniqueEList<ConditionType>(ConditionType.class, this, RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__CONSTRAINTS);
		}
		return constraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentProcess getTo() {
		if (to != null && to.eIsProxy()) {
			InternalEObject oldTo = (InternalEObject)to;
			to = (ComponentProcess)eResolveProxy(oldTo);
			if (to != oldTo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__TO, oldTo, to));
			}
		}
		return to;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentProcess basicGetTo() {
		return to;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTo(ComponentProcess newTo) {
		ComponentProcess oldTo = to;
		to = newTo;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__TO, oldTo, to));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__CONSTRAINTS:
				return getConstraints();
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__TO:
				if (resolve) return getTo();
				return basicGetTo();
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
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__CONSTRAINTS:
				getConstraints().clear();
				getConstraints().addAll((Collection<? extends ConditionType>)newValue);
				return;
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__TO:
				setTo((ComponentProcess)newValue);
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
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__CONSTRAINTS:
				getConstraints().clear();
				return;
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__TO:
				setTo((ComponentProcess)null);
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
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__CONSTRAINTS:
				return constraints != null && !constraints.isEmpty();
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__TO:
				return to != null;
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
		result.append(" (constraints: ");
		result.append(constraints);
		result.append(')');
		return result.toString();
	}

} //KnowledgeValueChangeTriggerImpl
