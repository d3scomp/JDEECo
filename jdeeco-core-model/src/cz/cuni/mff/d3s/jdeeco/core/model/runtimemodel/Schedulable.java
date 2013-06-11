/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Schedulable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Schedulable#getScheduling <em>Scheduling</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getSchedulable()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface Schedulable extends EObject {
	/**
	 * Returns the value of the '<em><b>Scheduling</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Scheduling#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scheduling</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scheduling</em>' containment reference.
	 * @see #setScheduling(Scheduling)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getSchedulable_Scheduling()
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Scheduling#getOwner
	 * @model opposite="owner" containment="true" required="true"
	 * @generated
	 */
	Scheduling getScheduling();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Schedulable#getScheduling <em>Scheduling</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scheduling</em>' containment reference.
	 * @see #getScheduling()
	 * @generated
	 */
	void setScheduling(Scheduling value);

} // Schedulable
