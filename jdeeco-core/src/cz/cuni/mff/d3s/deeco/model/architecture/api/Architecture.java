/**
 */
package cz.cuni.mff.d3s.deeco.model.architecture.api;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Architecture</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture#getEnsembleInstances <em>Ensemble Instances</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture#getComponentInstances <em>Component Instances</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage#getArchitecture()
 * @model
 * @generated
 */
public interface Architecture extends EObject {
	/**
	 * Returns the value of the '<em><b>Ensemble Instances</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ensemble Instances</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ensemble Instances</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage#getArchitecture_EnsembleInstances()
	 * @model containment="true"
	 * @generated
	 */
	EList<EnsembleInstance> getEnsembleInstances();

	/**
	 * Returns the value of the '<em><b>Component Instances</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Component Instances</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component Instances</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage#getArchitecture_ComponentInstances()
	 * @model containment="true"
	 * @generated
	 */
	EList<ComponentInstance> getComponentInstances();

} // Architecture
