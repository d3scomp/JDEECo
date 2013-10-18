/**
 */
package cz.cuni.mff.d3s.jdeeco.core.architecture;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Knowledge Manager</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.architecture.KnowledgeManager#getListeners <em>Listeners</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public interface KnowledgeManager {
	/**
	 * Returns the value of the '<em><b>Listeners</b></em>' reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.jdeeco.core.architecture.KnowledgeTriggerListener}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Listeners</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Listeners</em>' reference list.
	 * @generated
	 */
	List<KnowledgeTriggerListener> getListeners();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	void put(KnowledgeChangeset changeset);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Knowledge get(KnowledgeRef KnowledgeReference);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	void registerTrigger(KnowledgeTriggerListener listener, KnowledgeTrigger trigger);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	void unregisterTrigger(KnowledgeTrigger trigger);

} // KnowledgeManager
