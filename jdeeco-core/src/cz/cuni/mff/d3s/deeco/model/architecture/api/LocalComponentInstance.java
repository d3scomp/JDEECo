/**
 */
package cz.cuni.mff.d3s.deeco.model.architecture.api;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Local Component Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.architecture.api.LocalComponentInstance#getRuntimeInstance <em>Runtime Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage#getLocalComponentInstance()
 * @model
 * @generated
 */
public interface LocalComponentInstance extends ComponentInstance {
	/**
	 * Returns the value of the '<em><b>Runtime Instance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Runtime Instance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Runtime Instance</em>' attribute.
	 * @see #setRuntimeInstance(cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance)
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage#getLocalComponentInstance_RuntimeInstance()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstanceMetadata" required="true"
	 * @generated
	 */
	cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance getRuntimeInstance();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.LocalComponentInstance#getRuntimeInstance <em>Runtime Instance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Runtime Instance</em>' attribute.
	 * @see #getRuntimeInstance()
	 * @generated
	 */
	void setRuntimeInstance(cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance value);

} // LocalComponentInstance
