/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Periodic Scheduling</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.PeriodicScheduling#getPeriod <em>Period</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getPeriodicScheduling()
 * @model
 * @generated
 */
public interface PeriodicScheduling extends Scheduling {
	/**
	 * Returns the value of the '<em><b>Period</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Period</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Period</em>' attribute.
	 * @see #setPeriod(Integer)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getPeriodicScheduling_Period()
	 * @model required="true"
	 * @generated
	 */
	Integer getPeriod();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.PeriodicScheduling#getPeriod <em>Period</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Period</em>' attribute.
	 * @see #getPeriod()
	 * @generated
	 */
	void setPeriod(Integer value);

} // PeriodicScheduling
