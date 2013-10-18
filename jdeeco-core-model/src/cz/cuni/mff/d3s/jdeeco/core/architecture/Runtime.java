/**
 */
package cz.cuni.mff.d3s.jdeeco.core.architecture;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Runtime</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.architecture.Runtime#getScheduler <em>Scheduler</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.architecture.Runtime#getExecutor <em>Executor</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.architecture.Runtime#getKnowledgeManager <em>Knowledge Manager</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public interface Runtime {
	/**
	 * Returns the value of the '<em><b>Scheduler</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scheduler</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scheduler</em>' reference.
	 * @see #setScheduler(Scheduler)
	 * @generated
	 */
	Scheduler getScheduler();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.architecture.Runtime#getScheduler <em>Scheduler</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scheduler</em>' reference.
	 * @see #getScheduler()
	 * @generated
	 */
	void setScheduler(Scheduler value);

	/**
	 * Returns the value of the '<em><b>Executor</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Executor</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Executor</em>' reference.
	 * @see #setExecutor(Executor)
	 * @generated
	 */
	Executor getExecutor();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.architecture.Runtime#getExecutor <em>Executor</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Executor</em>' reference.
	 * @see #getExecutor()
	 * @generated
	 */
	void setExecutor(Executor value);

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
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.architecture.Runtime#getKnowledgeManager <em>Knowledge Manager</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Knowledge Manager</em>' reference.
	 * @see #getKnowledgeManager()
	 * @generated
	 */
	void setKnowledgeManager(KnowledgeManager value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	void run();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	void shutdown();

} // Runtime
