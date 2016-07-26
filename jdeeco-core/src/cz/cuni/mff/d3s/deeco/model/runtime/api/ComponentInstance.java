/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistry;
import cz.cuni.mff.d3s.deeco.modes.ModeChart;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getComponentProcesses <em>Component Processes</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getName <em>Name</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getKnowledgeManager <em>Knowledge Manager</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getShadowKnowledgeManagerRegistry <em>Shadow Knowledge Manager Registry</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getEnsembleControllers <em>Ensemble Controllers</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getInternalData <em>Internal Data</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#isSystemComponent <em>System Component</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getSecurityRoles <em>Security Roles</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getRatingsProcess <em>Ratings Process</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getModeChart <em>Mode Chart</em>}</li>
 * </ul>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance()
 * @model
 * @generated
 */
public interface ComponentInstance extends EObject {
	/**
	 * Returns the value of the '<em><b>Component Processes</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess}.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess#getComponentInstance <em>Component Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Component Processes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component Processes</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance_ComponentProcesses()
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess#getComponentInstance
	 * @model opposite="componentInstance" containment="true"
	 * @generated
	 */
	EList<ComponentProcess> getComponentProcesses();

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
	 * Returns the value of the '<em><b>Shadow Knowledge Manager Registry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shadow Knowledge Manager Registry</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shadow Knowledge Manager Registry</em>' attribute.
	 * @see #setShadowKnowledgeManagerRegistry(ShadowKnowledgeManagerRegistry)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance_ShadowKnowledgeManagerRegistry()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.runtime.api.ShadowKnowledgeManagerRegistry" required="true"
	 * @generated
	 */
	ShadowKnowledgeManagerRegistry getShadowKnowledgeManagerRegistry();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getShadowKnowledgeManagerRegistry <em>Shadow Knowledge Manager Registry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shadow Knowledge Manager Registry</em>' attribute.
	 * @see #getShadowKnowledgeManagerRegistry()
	 * @generated
	 */
	void setShadowKnowledgeManagerRegistry(ShadowKnowledgeManagerRegistry value);

	/**
	 * Returns the value of the '<em><b>Ensemble Controllers</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController}.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController#getComponentInstance <em>Component Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ensemble Controllers</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ensemble Controllers</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance_EnsembleControllers()
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController#getComponentInstance
	 * @model opposite="componentInstance" containment="true"
	 * @generated
	 */
	EList<EnsembleController> getEnsembleControllers();

	/**
	 * Returns the value of the '<em><b>Internal Data</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.Object},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Internal Data</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Internal Data</em>' map.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance_InternalData()
	 * @model mapType="cz.cuni.mff.d3s.deeco.model.runtime.api.StringToObjectMap<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EJavaObject>"
	 * @generated
	 */
	EMap<String, Object> getInternalData();

	/**
	 * Returns the value of the '<em><b>System Component</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>System Component</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>System Component</em>' attribute.
	 * @see #setSystemComponent(boolean)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance_SystemComponent()
	 * @model default="false"
	 * @generated
	 */
	boolean isSystemComponent();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#isSystemComponent <em>System Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>System Component</em>' attribute.
	 * @see #isSystemComponent()
	 * @generated
	 */
	void setSystemComponent(boolean value);

	/**
	 * Returns the value of the '<em><b>Security Roles</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Security Roles</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Security Roles</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance_SecurityRoles()
	 * @model containment="true"
	 * @generated
	 */
	EList<SecurityRole> getSecurityRoles();

	/**
	 * Returns the value of the '<em><b>Ratings Process</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.RatingsProcess#getComponentInstance <em>Component Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ratings Process</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ratings Process</em>' containment reference.
	 * @see #setRatingsProcess(RatingsProcess)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance_RatingsProcess()
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.RatingsProcess#getComponentInstance
	 * @model opposite="componentInstance" containment="true"
	 * @generated
	 */
	RatingsProcess getRatingsProcess();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getRatingsProcess <em>Ratings Process</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ratings Process</em>' containment reference.
	 * @see #getRatingsProcess()
	 * @generated
	 */
	void setRatingsProcess(RatingsProcess value);

	/**
	 * Returns the value of the '<em><b>Mode Chart</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mode Chart</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mode Chart</em>' attribute.
	 * @see #setModeChart(ModeChart)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance_ModeChart()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.runtime.api.ModeChart"
	 * @generated
	 */
	ModeChart getModeChart();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getModeChart <em>Mode Chart</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mode Chart</em>' attribute.
	 * @see #getModeChart()
	 * @generated
	 */
	void setModeChart(ModeChart value);

} // ComponentInstance
