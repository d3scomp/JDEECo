/**
 */
package cz.cuni.mff.d3s.deeco.model.architecture.api;

import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ensemble Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance#getEnsembleDefinition <em>Ensemble Definition</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance#getCoordinator <em>Coordinator</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance#getMembers <em>Members</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage#getEnsembleInstance()
 * @model
 * @generated
 */
public interface EnsembleInstance extends EObject {
	/**
	 * Returns the value of the '<em><b>Ensemble Definition</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ensemble Definition</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ensemble Definition</em>' attribute.
	 * @see #setEnsembleDefinition(EnsembleDefinition)
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage#getEnsembleInstance_EnsembleDefinition()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleDefinitionMetadata" required="true"
	 * @generated
	 */
	EnsembleDefinition getEnsembleDefinition();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance#getEnsembleDefinition <em>Ensemble Definition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ensemble Definition</em>' attribute.
	 * @see #getEnsembleDefinition()
	 * @generated
	 */
	void setEnsembleDefinition(EnsembleDefinition value);

	/**
	 * Returns the value of the '<em><b>Coordinator</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Coordinator</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Coordinator</em>' reference.
	 * @see #setCoordinator(ComponentInstance)
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage#getEnsembleInstance_Coordinator()
	 * @model required="true"
	 * @generated
	 */
	ComponentInstance getCoordinator();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance#getCoordinator <em>Coordinator</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Coordinator</em>' reference.
	 * @see #getCoordinator()
	 * @generated
	 */
	void setCoordinator(ComponentInstance value);

	/**
	 * Returns the value of the '<em><b>Members</b></em>' reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Members</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Members</em>' reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage#getEnsembleInstance_Members()
	 * @model
	 * @generated
	 */
	EList<ComponentInstance> getMembers();

} // EnsembleInstance
