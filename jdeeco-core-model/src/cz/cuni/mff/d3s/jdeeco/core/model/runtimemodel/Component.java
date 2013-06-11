/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component#getKnowledge <em>Knowledge</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component#getProcesses <em>Processes</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getComponent()
 * @model
 * @generated
 */
public interface Component extends EObject {
	/**
	 * Returns the value of the '<em><b>Knowledge</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TopLevelKnowledgeDefinition}.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TopLevelKnowledgeDefinition#getComponent <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Knowledge</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Knowledge</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getComponent_Knowledge()
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TopLevelKnowledgeDefinition#getComponent
	 * @model opposite="component" containment="true"
	 * @generated
	 */
	EList<TopLevelKnowledgeDefinition> getKnowledge();

	/**
	 * Returns the value of the '<em><b>Processes</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process}.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process#getComponent <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Processes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Processes</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getComponent_Processes()
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process#getComponent
	 * @model opposite="component" containment="true"
	 * @generated
	 */
	EList<cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process> getProcesses();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getComponent_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	KnowledgeDefinition findDefinition(String path);

} // Component
