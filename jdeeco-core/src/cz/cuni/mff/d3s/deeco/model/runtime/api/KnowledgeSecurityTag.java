/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Knowledge Security Tag</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag#getRoleName <em>Role Name</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag#getArguments <em>Arguments</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeSecurityTag()
 * @model
 * @generated
 */
public interface KnowledgeSecurityTag extends EObject {
	/**
	 * Returns the value of the '<em><b>Role Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Role Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Role Name</em>' attribute.
	 * @see #setRoleName(String)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeSecurityTag_RoleName()
	 * @model required="true"
	 * @generated
	 */
	String getRoleName();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag#getRoleName <em>Role Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Role Name</em>' attribute.
	 * @see #getRoleName()
	 * @generated
	 */
	void setRoleName(String value);

	/**
	 * Returns the value of the '<em><b>Arguments</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Arguments</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arguments</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getKnowledgeSecurityTag_Arguments()
	 * @model containment="true"
	 * @generated
	 */
	EList<KnowledgePath> getArguments();

} // KnowledgeSecurityTag
