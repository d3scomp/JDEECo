/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Knowledge Change Trigger</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger#getKnowledgePath <em>Knowledge Path</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeChangeTrigger()
 * @model
 * @generated
 */
public interface KnowledgeChangeTrigger extends Trigger {
	/**
	 * Returns the value of the '<em><b>Knowledge Path</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Knowledge Path</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Knowledge Path</em>' containment reference.
	 * @see #setKnowledgePath(KnowledgePath)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeChangeTrigger_KnowledgePath()
	 * @model containment="true" required="true"
	 * @generated
	 */
	KnowledgePath getKnowledgePath();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger#getKnowledgePath <em>Knowledge Path</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Knowledge Path</em>' containment reference.
	 * @see #getKnowledgePath()
	 * @generated
	 */
	void setKnowledgePath(KnowledgePath value);

} // KnowledgeChangeTrigger
