/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;


import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ConditionType;
import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Knowledge Value Change Trigger</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger#getConstraints <em>Constraints</em>}</li>
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
	 * Returns the value of the '<em><b>Constraints</b></em>' attribute list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ConditionType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraints</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraints</em>' attribute list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeValueChangeTrigger_Constraints()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.runtime.api.ConditionType"
	 * @generated
	 */
	EList<ConditionType> getConstraints();

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
