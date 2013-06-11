/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Type Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TypeParameter#getOwner <em>Owner</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getTypeParameter()
 * @model
 * @generated
 */
public interface TypeParameter extends KnowledgeTypeOwner {
	/**
	 * Returns the value of the '<em><b>Owner</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owner</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owner</em>' reference.
	 * @see #setOwner(ParametricKnowledgeType)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getTypeParameter_Owner()
	 * @model required="true"
	 * @generated
	 */
	ParametricKnowledgeType getOwner();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TypeParameter#getOwner <em>Owner</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owner</em>' reference.
	 * @see #getOwner()
	 * @generated
	 */
	void setOwner(ParametricKnowledgeType value);

} // TypeParameter
