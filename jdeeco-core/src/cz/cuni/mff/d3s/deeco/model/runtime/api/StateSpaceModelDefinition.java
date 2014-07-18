/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;
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
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getModel <em>Model</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getComponentInstance <em>Component Instance</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getTriggers <em>Triggers</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#isIsActive <em>Is Active</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getInStates <em>In States</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getDerivationStates <em>Derivation States</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getModelValue <em>Model Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModelDefinition()
 * @model
 * @generated
 */
public interface StateSpaceModelDefinition extends EObject {
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
	 * Returns the value of the '<em><b>Triggers</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger}.
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
	EList<TimeTrigger> getTriggers();

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
	 * Returns the value of the '<em><b>In States</b></em>' reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>In States</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In States</em>' reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModelDefinition_InStates()
	 * @model required="true"
	 * @generated
	 */
	EList<KnowledgePath> getInStates();

	/**
	 * Returns the value of the '<em><b>Derivation States</b></em>' reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Derivation States</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Derivation States</em>' reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModelDefinition_DerivationStates()
	 * @model
	 * @generated
	 */
	EList<KnowledgePath> getDerivationStates();

	/**
	 * Returns the value of the '<em><b>Model Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Value</em>' attribute.
	 * @see #setModelValue(InaccuracyParamHolder)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModelDefinition_ModelValue()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.runtime.api.InaccurateValue" required="true"
	 * @generated
	 */
	InaccuracyParamHolder getModelValue();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getModelValue <em>Model Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Value</em>' attribute.
	 * @see #getModelValue()
	 * @generated
	 */
	void setModelValue(InaccuracyParamHolder value);

} // StateSpaceModelDefinition
