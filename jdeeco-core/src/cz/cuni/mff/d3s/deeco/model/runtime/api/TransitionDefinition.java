/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transition Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#getFromMode <em>From Mode</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#getToMode <em>To Mode</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#isInit <em>Init</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#getTrigger <em>Trigger</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getTransitionDefinition()
 * @model
 * @generated
 */
public interface TransitionDefinition extends EObject {
	/**
	 * Returns the value of the '<em><b>From Mode</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>From Mode</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From Mode</em>' reference.
	 * @see #setFromMode(ComponentProcess)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getTransitionDefinition_FromMode()
	 * @model
	 * @generated
	 */
	ComponentProcess getFromMode();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#getFromMode <em>From Mode</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From Mode</em>' reference.
	 * @see #getFromMode()
	 * @generated
	 */
	void setFromMode(ComponentProcess value);

	/**
	 * Returns the value of the '<em><b>To Mode</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>To Mode</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To Mode</em>' reference.
	 * @see #setToMode(ComponentProcess)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getTransitionDefinition_ToMode()
	 * @model
	 * @generated
	 */
	ComponentProcess getToMode();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#getToMode <em>To Mode</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To Mode</em>' reference.
	 * @see #getToMode()
	 * @generated
	 */
	void setToMode(ComponentProcess value);

	/**
	 * Returns the value of the '<em><b>Init</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Init</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Init</em>' attribute.
	 * @see #setInit(boolean)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getTransitionDefinition_Init()
	 * @model required="true"
	 * @generated
	 */
	boolean isInit();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#isInit <em>Init</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Init</em>' attribute.
	 * @see #isInit()
	 * @generated
	 */
	void setInit(boolean value);

	/**
	 * Returns the value of the '<em><b>Trigger</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Trigger</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Trigger</em>' reference.
	 * @see #setTrigger(KnowledgeChangeTrigger)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getTransitionDefinition_Trigger()
	 * @model
	 * @generated
	 */
	KnowledgeChangeTrigger getTrigger();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#getTrigger <em>Trigger</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Trigger</em>' reference.
	 * @see #getTrigger()
	 * @generated
	 */
	void setTrigger(KnowledgeChangeTrigger value);

} // TransitionDefinition
