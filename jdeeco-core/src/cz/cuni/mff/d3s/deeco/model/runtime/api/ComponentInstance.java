/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getProcesses <em>Processes</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getId <em>Id</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getName <em>Name</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getKnowledgeManager <em>Knowledge Manager</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getOtherKnowledgeManagersAccess <em>Other Knowledge Managers Access</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getEnsemblingControllers <em>Ensembling Controllers</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance()
 * @model
 * @generated
 */
public interface ComponentInstance extends EObject {
	/**
	 * Returns the value of the '<em><b>Processes</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.Process}.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Process#getComponentInstance <em>Component Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Processes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Processes</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance_Processes()
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Process#getComponentInstance
	 * @model opposite="componentInstance" containment="true"
	 * @generated
	 */
	EList<cz.cuni.mff.d3s.deeco.model.runtime.api.Process> getProcesses();

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
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance_Id()
	 * @model required="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

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
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Knowledge Manager</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Knowledge Manager</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Knowledge Manager</em>' attribute.
	 * @see #setKnowledgeManager(KnowledgeManager)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance_KnowledgeManager()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeManager" required="true"
	 * @generated
	 */
	KnowledgeManager getKnowledgeManager();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getKnowledgeManager <em>Knowledge Manager</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Knowledge Manager</em>' attribute.
	 * @see #getKnowledgeManager()
	 * @generated
	 */
	void setKnowledgeManager(KnowledgeManager value);

	/**
	 * Returns the value of the '<em><b>Other Knowledge Managers Access</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Other Knowledge Managers Access</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Other Knowledge Managers Access</em>' attribute.
	 * @see #setOtherKnowledgeManagersAccess(Object)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance_OtherKnowledgeManagersAccess()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.runtime.api.OtherKnowledgeManagersAccess" required="true"
	 * @generated
	 */
	Object getOtherKnowledgeManagersAccess();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getOtherKnowledgeManagersAccess <em>Other Knowledge Managers Access</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Other Knowledge Managers Access</em>' attribute.
	 * @see #getOtherKnowledgeManagersAccess()
	 * @generated
	 */
	void setOtherKnowledgeManagersAccess(Object value);

	/**
	 * Returns the value of the '<em><b>Ensembling Controllers</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController}.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController#getComponentInstance <em>Component Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ensembling Controllers</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ensembling Controllers</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance_EnsemblingControllers()
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController#getComponentInstance
	 * @model opposite="componentInstance" containment="true"
	 * @generated
	 */
	EList<EnsembleController> getEnsemblingControllers();

} // ComponentInstance
