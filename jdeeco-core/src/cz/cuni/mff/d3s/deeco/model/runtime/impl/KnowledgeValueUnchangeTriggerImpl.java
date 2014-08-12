/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComparisonType;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger;

import cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

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
 * An implementation of the model object '<em><b>Knowledge Value Unchange Trigger</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueUnchangeTriggerImpl#getCondition <em>Condition</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueUnchangeTriggerImpl#getFrom <em>From</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueUnchangeTriggerImpl#getTo <em>To</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class KnowledgeValueUnchangeTriggerImpl extends KnowledgeChangeTriggerImpl implements KnowledgeValueUnchangeTrigger {
	/**
	 * The cached value of the '{@link #getCondition() <em>Condition</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCondition()
	 * @generated
	 * @ordered
	 */
	protected EList<String> condition;

	/**
	 * The cached value of the '{@link #getFrom() <em>From</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFrom()
	 * @generated
	 * @ordered
	 */
	protected EList<ComponentProcess> from;

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
	protected KnowledgeValueUnchangeTriggerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RuntimeMetadataPackage.Literals.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getCondition() {
		if (condition == null) {
			condition = new EDataTypeUniqueEList<String>(String.class, this, RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__CONDITION);
		}
		return condition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ComponentProcess> getFrom() {
		if (from == null) {
			from = new EObjectResolvingEList<ComponentProcess>(ComponentProcess.class, this, RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__FROM);
		}
		return from;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__TO, oldTo, to));
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
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__TO, oldTo, to));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__CONDITION:
				return getCondition();
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__FROM:
				return getFrom();
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__TO:
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
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__CONDITION:
				getCondition().clear();
				getCondition().addAll((Collection<? extends String>)newValue);
				return;
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__FROM:
				getFrom().clear();
				getFrom().addAll((Collection<? extends ComponentProcess>)newValue);
				return;
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__TO:
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
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__CONDITION:
				getCondition().clear();
				return;
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__FROM:
				getFrom().clear();
				return;
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__TO:
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
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__CONDITION:
				return condition != null && !condition.isEmpty();
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__FROM:
				return from != null && !from.isEmpty();
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__TO:
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
		result.append(" (condition: ");
		result.append(condition);
		result.append(')');
		return result.toString();
	}

} //KnowledgeValueUnchangeTriggerImpl
