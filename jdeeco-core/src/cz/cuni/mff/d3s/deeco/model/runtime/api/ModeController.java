/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mode Controller</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeController#getInitMode <em>Init Mode</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeController#getParentMode <em>Parent Mode</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeController#getAllModes <em>All Modes</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeController#getComponentInstance <em>Component Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getModeController()
 * @model
 * @generated
 */
public interface ModeController extends EObject {
	/**
	 * Returns the value of the '<em><b>Init Mode</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Init Mode</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Init Mode</em>' reference.
	 * @see #setInitMode(ComponentProcess)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getModeController_InitMode()
	 * @model required="true"
	 * @generated
	 */
	ComponentProcess getInitMode();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeController#getInitMode <em>Init Mode</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Init Mode</em>' reference.
	 * @see #getInitMode()
	 * @generated
	 */
	void setInitMode(ComponentProcess value);

	/**
	 * Returns the value of the '<em><b>Parent Mode</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent Mode</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent Mode</em>' reference.
	 * @see #setParentMode(ComponentProcess)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getModeController_ParentMode()
	 * @model
	 * @generated
	 */
	ComponentProcess getParentMode();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeController#getParentMode <em>Parent Mode</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent Mode</em>' reference.
	 * @see #getParentMode()
	 * @generated
	 */
	void setParentMode(ComponentProcess value);

	/**
	 * Returns the value of the '<em><b>All Modes</b></em>' reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>All Modes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>All Modes</em>' reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getModeController_AllModes()
	 * @model required="true"
	 * @generated
	 */
	EList<ComponentProcess> getAllModes();

	/**
	 * Returns the value of the '<em><b>Component Instance</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getModeControllers <em>Mode Controllers</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Component Instance</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component Instance</em>' container reference.
	 * @see #setComponentInstance(ComponentInstance)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getModeController_ComponentInstance()
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getModeControllers
	 * @model opposite="modeControllers" required="true" transient="false"
	 * @generated
	 */
	ComponentInstance getComponentInstance();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeController#getComponentInstance <em>Component Instance</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Component Instance</em>' container reference.
	 * @see #getComponentInstance()
	 * @generated
	 */
	void setComponentInstance(ComponentInstance value);

} // ModeController
