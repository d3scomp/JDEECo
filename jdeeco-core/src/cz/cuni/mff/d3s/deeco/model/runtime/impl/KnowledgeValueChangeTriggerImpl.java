/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComparisonType;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger;

import cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Knowledge Value Change Trigger</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueChangeTriggerImpl#getValue <em>Value</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueChangeTriggerImpl#getComparison <em>Comparison</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueChangeTriggerImpl#getMeta <em>Meta</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class KnowledgeValueChangeTriggerImpl extends KnowledgeChangeTriggerImpl implements KnowledgeValueChangeTrigger {
	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected EList<Object> value;
	/**
	 * The cached value of the '{@link #getComparison() <em>Comparison</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComparison()
	 * @generated
	 * @ordered
	 */
	protected EList<ComparisonType> comparison;
	/**
	 * The default value of the '{@link #getMeta() <em>Meta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMeta()
	 * @generated
	 * @ordered
	 */
	protected static final MetadataType META_EDEFAULT = MetadataType.EMPTY;
	/**
	 * The cached value of the '{@link #getMeta() <em>Meta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMeta()
	 * @generated
	 * @ordered
	 */
	protected MetadataType meta = META_EDEFAULT;

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
	public EList<Object> getValue() {
		if (value == null) {
			value = new EDataTypeUniqueEList<Object>(Object.class, this, RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__VALUE);
		}
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ComparisonType> getComparison() {
		if (comparison == null) {
			comparison = new EDataTypeUniqueEList<ComparisonType>(ComparisonType.class, this, RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__COMPARISON);
		}
		return comparison;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetadataType getMeta() {
		return meta;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMeta(MetadataType newMeta) {
		MetadataType oldMeta = meta;
		meta = newMeta == null ? META_EDEFAULT : newMeta;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__META, oldMeta, meta));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__VALUE:
				return getValue();
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__COMPARISON:
				return getComparison();
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__META:
				return getMeta();
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
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__VALUE:
				getValue().clear();
				getValue().addAll((Collection<? extends Object>)newValue);
				return;
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__COMPARISON:
				getComparison().clear();
				getComparison().addAll((Collection<? extends ComparisonType>)newValue);
				return;
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__META:
				setMeta((MetadataType)newValue);
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
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__VALUE:
				getValue().clear();
				return;
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__COMPARISON:
				getComparison().clear();
				return;
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__META:
				setMeta(META_EDEFAULT);
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
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__VALUE:
				return value != null && !value.isEmpty();
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__COMPARISON:
				return comparison != null && !comparison.isEmpty();
			case RuntimeMetadataPackage.KNOWLEDGE_VALUE_CHANGE_TRIGGER__META:
				return meta != META_EDEFAULT;
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
		result.append(" (value: ");
		result.append(value);
		result.append(", comparison: ");
		result.append(comparison);
		result.append(", meta: ");
		result.append(meta);
		result.append(')');
		return result.toString();
	}

} //KnowledgeValueChangeTriggerImpl
