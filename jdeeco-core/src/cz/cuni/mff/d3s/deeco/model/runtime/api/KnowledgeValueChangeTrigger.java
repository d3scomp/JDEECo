/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;


import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Knowledge Value Change Trigger</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger#getCondition <em>Condition</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger#getFrom <em>From</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger#getTo <em>To</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeValueChangeTrigger()
 * @model
 * @generated
 */
public interface KnowledgeValueChangeTrigger extends KnowledgeChangeTrigger {

	/**
	 * Returns the value of the '<em><b>Condition</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Condition</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Condition</em>' attribute list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeValueChangeTrigger_Condition()
	 * @model
	 * @generated
	 */
	EList<String> getCondition();

	/**
	 * Returns the value of the '<em><b>From</b></em>' reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>From</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From</em>' reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeValueChangeTrigger_From()
	 * @model
	 * @generated
	 */
	EList<ComponentProcess> getFrom();

	/**
	 * Returns the value of the '<em><b>To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>To</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To</em>' reference.
	 * @see #setTo(ComponentProcess)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeValueChangeTrigger_To()
	 * @model required="true"
	 * @generated
	 */
	ComponentProcess getTo();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger#getTo <em>To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To</em>' reference.
	 * @see #getTo()
	 * @generated
	 */
	void setTo(ComponentProcess value);
} // KnowledgeValueChangeTrigger
