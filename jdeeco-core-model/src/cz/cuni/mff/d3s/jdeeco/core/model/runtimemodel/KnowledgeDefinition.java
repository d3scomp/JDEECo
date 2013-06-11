/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Knowledge Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeDefinition#isIsLocal <em>Is Local</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getKnowledgeDefinition()
 * @model abstract="true"
 * @generated
 */
public interface KnowledgeDefinition extends KnowledgeReference {
	/**
	 * Returns the value of the '<em><b>Is Local</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Local</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Local</em>' attribute.
	 * @see #setIsLocal(boolean)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getKnowledgeDefinition_IsLocal()
	 * @model required="true"
	 * @generated
	 */
	boolean isIsLocal();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeDefinition#isIsLocal <em>Is Local</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Local</em>' attribute.
	 * @see #isIsLocal()
	 * @generated
	 */
	void setIsLocal(boolean value);

} // KnowledgeDefinition
