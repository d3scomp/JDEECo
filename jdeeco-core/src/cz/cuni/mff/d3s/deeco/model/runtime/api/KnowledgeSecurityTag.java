/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Knowledge Security Tag</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag#getRequiredRole <em>Required Role</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag#getAccessRights <em>Access Rights</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeSecurityTag()
 * @model
 * @generated
 */
public interface KnowledgeSecurityTag extends SecurityTag {
	/**
	 * Returns the value of the '<em><b>Required Role</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Required Role</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Required Role</em>' containment reference.
	 * @see #setRequiredRole(SecurityRole)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeSecurityTag_RequiredRole()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SecurityRole getRequiredRole();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag#getRequiredRole <em>Required Role</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Required Role</em>' containment reference.
	 * @see #getRequiredRole()
	 * @generated
	 */
	void setRequiredRole(SecurityRole value);

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
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeSecurityTag_AccessRights()
	 * @model required="true"
	 * @generated
	 */
	AccessRights getAccessRights();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag#getAccessRights <em>Access Rights</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Access Rights</em>' attribute.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.AccessRights
	 * @see #getAccessRights()
	 * @generated
	 */
	void setAccessRights(AccessRights value);

} // KnowledgeSecurityTag
