/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Structured Knowledge Value Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeValueType#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getStructuredKnowledgeValueType()
 * @model
 * @generated
 */
public interface StructuredKnowledgeValueType extends KnowledgeType {
	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.NestedKnowledgeDefinition}.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.NestedKnowledgeDefinition#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getStructuredKnowledgeValueType_Children()
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.NestedKnowledgeDefinition#getParent
	 * @model opposite="parent" containment="true"
	 * @generated
	 */
	EList<NestedKnowledgeDefinition> getChildren();

} // StructuredKnowledgeValueType
