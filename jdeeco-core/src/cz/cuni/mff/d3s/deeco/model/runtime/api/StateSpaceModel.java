/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.Model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>State Space Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModel#getInStates <em>In States</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModel#getDerivationStates <em>Derivation States</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModel#getModel <em>Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModel()
 * @model
 * @generated
 */
public interface StateSpaceModel extends EObject {
	/**
	 * Returns the value of the '<em><b>In States</b></em>' reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>In States</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In States</em>' reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModel_InStates()
	 * @model required="true"
	 * @generated
	 */
	EList<KnowledgePath> getInStates();

	/**
	 * Returns the value of the '<em><b>Derivation States</b></em>' reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Derivation States</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Derivation States</em>' reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModel_DerivationStates()
	 * @model
	 * @generated
	 */
	EList<KnowledgePath> getDerivationStates();

	/**
	 * Returns the value of the '<em><b>Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' attribute.
	 * @see #setModel(Model)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getStateSpaceModel_Model()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.runtime.api.ModelType"
	 * @generated
	 */
	Model getModel();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModel#getModel <em>Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' attribute.
	 * @see #getModel()
	 * @generated
	 */
	void setModel(Model value);

} // StateSpaceModel
