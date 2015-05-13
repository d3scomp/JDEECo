/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Wildcard Security Tag</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.WildcardSecurityTag#getAccessRights <em>Access Rights</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getWildcardSecurityTag()
 * @model
 * @generated
 */
public interface WildcardSecurityTag extends SecurityTag {
	/**
	 * Returns the value of the '<em><b>Access Rights</b></em>' attribute.
	 * The literals are from the enumeration {@link cz.cuni.mff.d3s.deeco.model.runtime.api.AccessRights}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Access Rights</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Access Rights</em>' attribute.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.AccessRights
	 * @see #setAccessRights(AccessRights)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getWildcardSecurityTag_AccessRights()
	 * @model required="true"
	 * @generated
	 */
	AccessRights getAccessRights();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.WildcardSecurityTag#getAccessRights <em>Access Rights</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Access Rights</em>' attribute.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.AccessRights
	 * @see #getAccessRights()
	 * @generated
	 */
	void setAccessRights(AccessRights value);

} // WildcardSecurityTag
