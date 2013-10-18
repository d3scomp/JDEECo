/**
 */
package cz.cuni.mff.d3s.jdeeco.core.architecture;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Executor Service</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.architecture.ExecutorService#getListeners <em>Listeners</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.architecture.ExecutorService#getKnowledgeManager <em>Knowledge Manager</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.architecture.ExecutorService#getExecutables <em>Executables</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public interface ExecutorService extends Executor {
	/**
	 * Returns the value of the '<em><b>Listeners</b></em>' reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.jdeeco.core.architecture.TaskExecutionListener}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Listeners</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Listeners</em>' reference list.
	 * @generated
	 */
	List<TaskExecutionListener> getListeners();

	/**
	 * Returns the value of the '<em><b>Knowledge Manager</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Knowledge Manager</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Knowledge Manager</em>' reference.
	 * @see #setKnowledgeManager(KnowledgeManager)
	 * @generated
	 */
	KnowledgeManager getKnowledgeManager();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.architecture.ExecutorService#getKnowledgeManager <em>Knowledge Manager</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Knowledge Manager</em>' reference.
	 * @see #getKnowledgeManager()
	 * @generated
	 */
	void setKnowledgeManager(KnowledgeManager value);

	/**
	 * Returns the value of the '<em><b>Executables</b></em>' reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.jdeeco.core.architecture.Executable}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Executables</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Executables</em>' reference list.
	 * @generated
	 */
	List<Executable> getExecutables();

} // ExecutorService
