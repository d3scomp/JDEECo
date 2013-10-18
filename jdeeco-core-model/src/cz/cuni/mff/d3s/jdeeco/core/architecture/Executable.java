/**
 */
package cz.cuni.mff.d3s.jdeeco.core.architecture;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Executable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.architecture.Executable#getInput <em>Input</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public interface Executable {
	/**
	 * Returns the value of the '<em><b>Input</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input</em>' reference.
	 * @see #setInput(KnowledgeRef)
	 * @generated
	 */
	KnowledgeRef getInput();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.architecture.Executable#getInput <em>Input</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Input</em>' reference.
	 * @see #getInput()
	 * @generated
	 */
	void setInput(KnowledgeRef value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ExecutionResult execute(Knowledge input);

} // Executable
