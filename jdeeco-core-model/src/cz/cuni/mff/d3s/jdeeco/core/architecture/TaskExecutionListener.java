/**
 */
package cz.cuni.mff.d3s.jdeeco.core.architecture;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Task Execution Listener</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @generated
 */
public interface TaskExecutionListener {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	void executionStarted(Executable task);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	void executionFinished(Executable task);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	void executionException(Executable task);

} // TaskExecutionListener
