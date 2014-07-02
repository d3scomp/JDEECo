/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.MetadataType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mode Transition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeTransition#getFromMode <em>From Mode</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeTransition#getToMode <em>To Mode</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeTransition#getTransitionCondition <em>Transition Condition</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeTransition#getMeta <em>Meta</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getModeTransition()
 * @model
 * @generated
 */
public interface ModeTransition extends EObject {
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
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getModeTransition_FromMode()
	 * @model required="true"
	 * @generated
	 */
	ComponentProcess getFromMode();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeTransition#getFromMode <em>From Mode</em>}' reference.
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
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getModeTransition_ToMode()
	 * @model required="true"
	 * @generated
	 */
	ComponentProcess getToMode();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeTransition#getToMode <em>To Mode</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To Mode</em>' reference.
	 * @see #getToMode()
	 * @generated
	 */
	void setToMode(ComponentProcess value);

	/**
	 * Returns the value of the '<em><b>Transition Condition</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Transition Condition</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transition Condition</em>' attribute.
	 * @see #setTransitionCondition(String)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getModeTransition_TransitionCondition()
	 * @model
	 * @generated
	 */
	String getTransitionCondition();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeTransition#getTransitionCondition <em>Transition Condition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transition Condition</em>' attribute.
	 * @see #getTransitionCondition()
	 * @generated
	 */
	void setTransitionCondition(String value);

	/**
	 * Returns the value of the '<em><b>Meta</b></em>' attribute.
	 * The literals are from the enumeration {@link cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Meta</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Meta</em>' attribute.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType
	 * @see #setMeta(cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getModeTransition_Meta()
	 * @model
	 * @generated
	 */
	cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType getMeta();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeTransition#getMeta <em>Meta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Meta</em>' attribute.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType
	 * @see #getMeta()
	 * @generated
	 */
	void setMeta(cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType value);

} // ModeTransition
