/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagersView;

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
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getComponentProcesses <em>Component Processes</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getName <em>Name</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getKnowledgeManager <em>Knowledge Manager</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getOtherKnowledgeManagersAccess <em>Other Knowledge Managers Access</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getEnsembleControllers <em>Ensemble Controllers</em>}</li>
 * </ul>
 * </p>
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
         * Returns the value of the '<em><b>Other Knowledge Managers Access</b></em>' attribute.
         * <!-- begin-user-doc -->
         * <p>
         * If the meaning of the '<em>Other Knowledge Managers Access</em>' attribute isn't clear,
         * there really should be more of a description here...
         * </p>
         * <!-- end-user-doc -->
         * @return the value of the '<em>Other Knowledge Managers Access</em>' attribute.
         * @see #setOtherKnowledgeManagersAccess(KnowledgeManagersView)
         * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getComponentInstance_OtherKnowledgeManagersAccess()
         * @model dataType="cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeManagersView" required="true"
         * @generated
         */
        KnowledgeManagersView getOtherKnowledgeManagersAccess();

        /**
         * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getOtherKnowledgeManagersAccess <em>Other Knowledge Managers Access</em>}' attribute.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @param value the new value of the '<em>Other Knowledge Managers Access</em>' attribute.
         * @see #getOtherKnowledgeManagersAccess()
         * @generated
         */
        void setOtherKnowledgeManagersAccess(KnowledgeManagersView value);

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

} // ComponentInstance