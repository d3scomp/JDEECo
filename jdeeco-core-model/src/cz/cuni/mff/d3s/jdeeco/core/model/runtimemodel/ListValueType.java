/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>List Value Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ListValueType#getTypeParameter <em>Type Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getListValueType()
 * @model
 * @generated
 */
public interface ListValueType extends ParametricKnowledgeType {
	/**
	 * Returns the value of the '<em><b>Type Parameter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Parameter</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Parameter</em>' containment reference.
	 * @see #setTypeParameter(TypeParameter)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getListValueType_TypeParameter()
	 * @model containment="true" required="true"
	 * @generated
	 */
	TypeParameter getTypeParameter();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ListValueType#getTypeParameter <em>Type Parameter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Parameter</em>' containment reference.
	 * @see #getTypeParameter()
	 * @generated
	 */
	void setTypeParameter(TypeParameter value);

} // ListValueType
