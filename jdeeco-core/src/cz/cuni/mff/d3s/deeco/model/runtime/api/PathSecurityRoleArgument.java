/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Path Security Role Argument</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument#getKnowledgePath <em>Knowledge Path</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument#getContextKind <em>Context Kind</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getPathSecurityRoleArgument()
 * @model
 * @generated
 */
public interface PathSecurityRoleArgument extends SecurityRoleArgument {
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
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getPathSecurityRoleArgument_KnowledgePath()
	 * @model containment="true" required="true"
	 * @generated
	 */
	KnowledgePath getKnowledgePath();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument#getKnowledgePath <em>Knowledge Path</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Knowledge Path</em>' containment reference.
	 * @see #getKnowledgePath()
	 * @generated
	 */
	void setKnowledgePath(KnowledgePath value);

	/**
	 * Returns the value of the '<em><b>Context Kind</b></em>' attribute.
	 * The literals are from the enumeration {@link cz.cuni.mff.d3s.deeco.model.runtime.api.ContextKind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Context Kind</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Context Kind</em>' attribute.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ContextKind
	 * @see #setContextKind(ContextKind)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getPathSecurityRoleArgument_ContextKind()
	 * @model required="true"
	 * @generated
	 */
	ContextKind getContextKind();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument#getContextKind <em>Context Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context Kind</em>' attribute.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ContextKind
	 * @see #getContextKind()
	 * @generated
	 */
	void setContextKind(ContextKind value);

} // PathSecurityRoleArgument
