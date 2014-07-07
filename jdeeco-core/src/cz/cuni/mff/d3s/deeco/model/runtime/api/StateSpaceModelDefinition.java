/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccurateValueDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ModelInterface;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>State Space Model Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getTriggerKowledgePath <em>Trigger Kowledge Path</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getModel <em>Model</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getDerivationStates <em>Derivation States</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getComponentInstance <em>Component Instance</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getInStates <em>In States</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getTriggers <em>Triggers</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#isIsActive <em>Is Active</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModelDefinition()
 * @model
 * @generated
 */
public interface StateSpaceModelDefinition extends Invocable {
	/**
	 * Returns the value of the '<em><b>Trigger Kowledge Path</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Trigger Kowledge Path</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Trigger Kowledge Path</em>' reference.
	 * @see #setTriggerKowledgePath(KnowledgePath)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModelDefinition_TriggerKowledgePath()
	 * @model required="true"
	 * @generated
	 */
	KnowledgePath getTriggerKowledgePath();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getTriggerKowledgePath <em>Trigger Kowledge Path</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Trigger Kowledge Path</em>' reference.
	 * @see #getTriggerKowledgePath()
	 * @generated
	 */
	void setTriggerKowledgePath(KnowledgePath value);

	/**
	 * Returns the value of the '<em><b>In States</b></em>' attribute list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccurateValueDefinition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>In States</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In States</em>' attribute list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModelDefinition_InStates()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.runtime.api.InaccurateValue" required="true"
	 * @generated
	 */
	EList<InaccurateValueDefinition> getInStates();

	/**
	 * Returns the value of the '<em><b>Triggers</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Triggers</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Triggers</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModelDefinition_Triggers()
	 * @model containment="true"
	 * @generated
	 */
	EList<Trigger> getTriggers();

	/**
	 * Returns the value of the '<em><b>Is Active</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Active</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Active</em>' attribute.
	 * @see #setIsActive(boolean)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModelDefinition_IsActive()
	 * @model required="true"
	 * @generated
	 */
	boolean isIsActive();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#isIsActive <em>Is Active</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Active</em>' attribute.
	 * @see #isIsActive()
	 * @generated
	 */
	void setIsActive(boolean value);

	/**
	 * Returns the value of the '<em><b>Derivation States</b></em>' attribute list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccurateValueDefinition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Derivation States</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Derivation States</em>' attribute list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModelDefinition_DerivationStates()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.runtime.api.InaccurateValue" required="true"
	 * @generated
	 */
	EList<InaccurateValueDefinition> getDerivationStates();

	/**
	 * Returns the value of the '<em><b>Component Instance</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getStateSpaceModels <em>State Space Models</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Component Instance</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component Instance</em>' container reference.
	 * @see #setComponentInstance(ComponentInstance)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModelDefinition_ComponentInstance()
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getStateSpaceModels
	 * @model opposite="stateSpaceModels" required="true" transient="false"
	 * @generated
	 */
	ComponentInstance getComponentInstance();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getComponentInstance <em>Component Instance</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Component Instance</em>' container reference.
	 * @see #getComponentInstance()
	 * @generated
	 */
	void setComponentInstance(ComponentInstance value);

	/**
	 * Returns the value of the '<em><b>Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' attribute.
	 * @see #setModel(ModelInterface)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModelDefinition_Model()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.runtime.api.ModelType"
	 * @generated
	 */
	ModelInterface getModel();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getModel <em>Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' attribute.
	 * @see #getModel()
	 * @generated
	 */
	void setModel(ModelInterface value);

} // StateSpaceModelDefinition
