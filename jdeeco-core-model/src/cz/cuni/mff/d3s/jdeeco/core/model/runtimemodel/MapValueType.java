/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Map Value Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MapValueType#getKeyTypeParameter <em>Key Type Parameter</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MapValueType#getValueTypeParameter <em>Value Type Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getMapValueType()
 * @model
 * @generated
 */
public interface MapValueType extends ParametricKnowledgeType {
	/**
	 * Returns the value of the '<em><b>Key Type Parameter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key Type Parameter</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key Type Parameter</em>' containment reference.
	 * @see #setKeyTypeParameter(TypeParameter)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getMapValueType_KeyTypeParameter()
	 * @model containment="true" required="true"
	 * @generated
	 */
	TypeParameter getKeyTypeParameter();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MapValueType#getKeyTypeParameter <em>Key Type Parameter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key Type Parameter</em>' containment reference.
	 * @see #getKeyTypeParameter()
	 * @generated
	 */
	void setKeyTypeParameter(TypeParameter value);

	/**
	 * Returns the value of the '<em><b>Value Type Parameter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value Type Parameter</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value Type Parameter</em>' containment reference.
	 * @see #setValueTypeParameter(TypeParameter)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getMapValueType_ValueTypeParameter()
	 * @model containment="true" required="true"
	 * @generated
	 */
	TypeParameter getValueTypeParameter();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MapValueType#getValueTypeParameter <em>Value Type Parameter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value Type Parameter</em>' containment reference.
	 * @see #getValueTypeParameter()
	 * @generated
	 */
	void setValueTypeParameter(TypeParameter value);

} // MapValueType
