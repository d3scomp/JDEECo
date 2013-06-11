/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Membership Condition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MembershipCondition#getEnsemble <em>Ensemble</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getMembershipCondition()
 * @model
 * @generated
 */
public interface MembershipCondition extends ParameterizedMethod {
	/**
	 * Returns the value of the '<em><b>Ensemble</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble#getMembership <em>Membership</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ensemble</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ensemble</em>' container reference.
	 * @see #setEnsemble(Ensemble)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getMembershipCondition_Ensemble()
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble#getMembership
	 * @model opposite="membership" required="true" transient="false"
	 * @generated
	 */
	Ensemble getEnsemble();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MembershipCondition#getEnsemble <em>Ensemble</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ensemble</em>' container reference.
	 * @see #getEnsemble()
	 * @generated
	 */
	void setEnsemble(Ensemble value);

} // MembershipCondition
