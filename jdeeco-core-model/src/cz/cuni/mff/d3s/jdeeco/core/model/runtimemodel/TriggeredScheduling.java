/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Triggered Scheduling</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TriggeredScheduling#getTriggers <em>Triggers</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getTriggeredScheduling()
 * @model
 * @generated
 */
public interface TriggeredScheduling extends Scheduling {
	/**
	 * Returns the value of the '<em><b>Triggers</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Trigger}.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Trigger#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Triggers</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Triggers</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getTriggeredScheduling_Triggers()
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Trigger#getOwner
	 * @model opposite="owner" containment="true" required="true"
	 * @generated
	 */
	EList<Trigger> getTriggers();

} // TriggeredScheduling
