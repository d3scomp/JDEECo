/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Process#getName <em>Name</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Process#getSchedulingSpecification <em>Scheduling Specification</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Process#getComponentInstance <em>Component Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getProcess()
 * @model
 * @generated
 */
public interface Process extends Invocable {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getProcess_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Process#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Scheduling Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scheduling Specification</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scheduling Specification</em>' containment reference.
	 * @see #setSchedulingSpecification(SchedulingSpecification)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getProcess_SchedulingSpecification()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SchedulingSpecification getSchedulingSpecification();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Process#getSchedulingSpecification <em>Scheduling Specification</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scheduling Specification</em>' containment reference.
	 * @see #getSchedulingSpecification()
	 * @generated
	 */
	void setSchedulingSpecification(SchedulingSpecification value);

	/**
	 * Returns the value of the '<em><b>Component Instance</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getProcesses <em>Processes</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Component Instance</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component Instance</em>' container reference.
	 * @see #setComponentInstance(ComponentInstance)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getProcess_ComponentInstance()
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getProcesses
	 * @model opposite="processes" required="true" transient="false"
	 * @generated
	 */
	ComponentInstance getComponentInstance();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Process#getComponentInstance <em>Component Instance</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Component Instance</em>' container reference.
	 * @see #getComponentInstance()
	 * @generated
	 */
	void setComponentInstance(ComponentInstance value);

} // Process
