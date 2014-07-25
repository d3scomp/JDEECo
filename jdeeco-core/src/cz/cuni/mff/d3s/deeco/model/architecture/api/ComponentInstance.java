/**
 */
package cz.cuni.mff.d3s.deeco.model.architecture.api;

import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance#getId <em>Id</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance#getKnowledgeManager <em>Knowledge Manager</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage#getComponentInstance()
 * @model abstract="true"
 * @generated
 */
public interface ComponentInstance extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage#getComponentInstance_Id()
	 * @model required="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Knowledge Manager</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Knowledge Manager</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Knowledge Manager</em>' attribute.
	 * @see #setKnowledgeManager(ReadOnlyKnowledgeManager)
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage#getComponentInstance_KnowledgeManager()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.architecture.api.ReadOnlyKnowledgeManager"
	 * @generated
	 */
	ReadOnlyKnowledgeManager getKnowledgeManager();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance#getKnowledgeManager <em>Knowledge Manager</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Knowledge Manager</em>' attribute.
	 * @see #getKnowledgeManager()
	 * @generated
	 */
	void setKnowledgeManager(ReadOnlyKnowledgeManager value);

} // ComponentInstance
