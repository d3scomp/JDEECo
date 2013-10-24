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
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.runtimePackage#getKnowledgeChangeTrigger()
 * @model
 * @generated
 */
public interface KnowledgeChangeTrigger extends Trigger {
	/**
	 * Returns the value of the '<em><b>Knowledge Path</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Knowledge Path</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Knowledge Path</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.runtimePackage#getKnowledgeChangeTrigger_KnowledgePath()
	 * @model containment="true"
	 * @generated
	 */
	EList<KnowledgePath> getKnowledgePath();

} // KnowledgeChangeTrigger
