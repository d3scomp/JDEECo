/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Absolute Security Role Argument</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getAbsoluteSecurityRoleArgument()
 * @model
 * @generated
 */
public interface AbsoluteSecurityRoleArgument extends SecurityRoleArgument {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(Object)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getAbsoluteSecurityRoleArgument_Value()
	 * @model required="true"
	 * @generated
	 */
	Object getValue();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(Object value);

} // AbsoluteSecurityRoleArgument
