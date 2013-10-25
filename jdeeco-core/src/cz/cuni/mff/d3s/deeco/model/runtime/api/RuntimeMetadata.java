/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Runtime Metadata</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata#getInstances <em>Instances</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata#getEnsembles <em>Ensembles</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata#getComponents <em>Components</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.runtimePackage#getRuntimeMetadata()
 * @model
 * @generated
 */
public interface RuntimeMetadata extends EObject {
	/**
	 * Returns the value of the '<em><b>Instances</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Instances</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Instances</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.runtimePackage#getRuntimeMetadata_Instances()
	 * @model containment="true"
	 * @generated
	 */
	EList<ComponentInstance> getInstances();

	/**
	 * Returns the value of the '<em><b>Ensembles</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.Ensemble}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ensembles</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ensembles</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.runtimePackage#getRuntimeMetadata_Ensembles()
	 * @model containment="true"
	 * @generated
	 */
	EList<Ensemble> getEnsembles();

	/**
	 * Returns the value of the '<em><b>Components</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.Component}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Components</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Components</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.runtimePackage#getRuntimeMetadata_Components()
	 * @model containment="true"
	 * @generated
	 */
	EList<Component> getComponents();

} // RuntimeMetadata
