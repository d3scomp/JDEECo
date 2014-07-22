/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Knowledge Value Unchange Trigger</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger#getValue <em>Value</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger#getComparison <em>Comparison</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger#getMeta <em>Meta</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeValueUnchangeTrigger()
 * @model
 * @generated
 */
public interface KnowledgeValueUnchangeTrigger extends KnowledgeChangeTrigger {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Long}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeValueUnchangeTrigger_Value()
	 * @model
	 * @generated
	 */
	EList<Long> getValue();

	/**
	 * Returns the value of the '<em><b>Comparison</b></em>' attribute list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComparisonType}.
	 * The literals are from the enumeration {@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComparisonType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comparison</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Comparison</em>' attribute list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComparisonType
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeValueUnchangeTrigger_Comparison()
	 * @model
	 * @generated
	 */
	EList<ComparisonType> getComparison();

	/**
	 * Returns the value of the '<em><b>Meta</b></em>' attribute.
	 * The literals are from the enumeration {@link cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Meta</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Meta</em>' attribute.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType
	 * @see #setMeta(MetadataType)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeValueUnchangeTrigger_Meta()
	 * @model
	 * @generated
	 */
	MetadataType getMeta();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger#getMeta <em>Meta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Meta</em>' attribute.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType
	 * @see #getMeta()
	 * @generated
	 */
	void setMeta(MetadataType value);

} // KnowledgeValueUnchangeTrigger
