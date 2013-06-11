/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ensemble</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble#getMembership <em>Membership</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble#getKnowledgeExchange <em>Knowledge Exchange</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getEnsemble()
 * @model
 * @generated
 */
public interface Ensemble extends Schedulable {
	/**
	 * Returns the value of the '<em><b>Membership</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MembershipCondition#getEnsemble <em>Ensemble</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Membership</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Membership</em>' containment reference.
	 * @see #setMembership(MembershipCondition)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getEnsemble_Membership()
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MembershipCondition#getEnsemble
	 * @model opposite="ensemble" containment="true" required="true"
	 * @generated
	 */
	MembershipCondition getMembership();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble#getMembership <em>Membership</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Membership</em>' containment reference.
	 * @see #getMembership()
	 * @generated
	 */
	void setMembership(MembershipCondition value);

	/**
	 * Returns the value of the '<em><b>Knowledge Exchange</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeExchange#getEnsemble <em>Ensemble</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Knowledge Exchange</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Knowledge Exchange</em>' containment reference.
	 * @see #setKnowledgeExchange(KnowledgeExchange)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getEnsemble_KnowledgeExchange()
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeExchange#getEnsemble
	 * @model opposite="ensemble" containment="true" required="true"
	 * @generated
	 */
	KnowledgeExchange getKnowledgeExchange();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble#getKnowledgeExchange <em>Knowledge Exchange</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Knowledge Exchange</em>' containment reference.
	 * @see #getKnowledgeExchange()
	 * @generated
	 */
	void setKnowledgeExchange(KnowledgeExchange value);

} // Ensemble
