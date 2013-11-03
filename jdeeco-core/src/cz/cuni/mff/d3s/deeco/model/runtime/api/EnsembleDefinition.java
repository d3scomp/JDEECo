/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ensemble Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getName <em>Name</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getMembership <em>Membership</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getKnowledgeExchange <em>Knowledge Exchange</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getCoordinatorSchedulingSpecification <em>Coordinator Scheduling Specification</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getMemberSchedulingSpecification <em>Member Scheduling Specification</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getEnsembleDefinition()
 * @model
 * @generated
 */
public interface EnsembleDefinition extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getEnsembleDefinition_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Membership</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Membership</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Membership</em>' containment reference.
	 * @see #setMembership(Condition)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getEnsembleDefinition_Membership()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Condition getMembership();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getMembership <em>Membership</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Membership</em>' containment reference.
	 * @see #getMembership()
	 * @generated
	 */
	void setMembership(Condition value);

	/**
	 * Returns the value of the '<em><b>Knowledge Exchange</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Knowledge Exchange</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Knowledge Exchange</em>' containment reference.
	 * @see #setKnowledgeExchange(Exchange)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getEnsembleDefinition_KnowledgeExchange()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Exchange getKnowledgeExchange();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getKnowledgeExchange <em>Knowledge Exchange</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Knowledge Exchange</em>' containment reference.
	 * @see #getKnowledgeExchange()
	 * @generated
	 */
	void setKnowledgeExchange(Exchange value);

	/**
	 * Returns the value of the '<em><b>Coordinator Scheduling Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Coordinator Scheduling Specification</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Coordinator Scheduling Specification</em>' containment reference.
	 * @see #setCoordinatorSchedulingSpecification(SchedulingSpecification)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getEnsembleDefinition_CoordinatorSchedulingSpecification()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SchedulingSpecification getCoordinatorSchedulingSpecification();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getCoordinatorSchedulingSpecification <em>Coordinator Scheduling Specification</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Coordinator Scheduling Specification</em>' containment reference.
	 * @see #getCoordinatorSchedulingSpecification()
	 * @generated
	 */
	void setCoordinatorSchedulingSpecification(SchedulingSpecification value);

	/**
	 * Returns the value of the '<em><b>Member Scheduling Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Member Scheduling Specification</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Member Scheduling Specification</em>' containment reference.
	 * @see #setMemberSchedulingSpecification(SchedulingSpecification)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getEnsembleDefinition_MemberSchedulingSpecification()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SchedulingSpecification getMemberSchedulingSpecification();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getMemberSchedulingSpecification <em>Member Scheduling Specification</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Member Scheduling Specification</em>' containment reference.
	 * @see #getMemberSchedulingSpecification()
	 * @generated
	 */
	void setMemberSchedulingSpecification(SchedulingSpecification value);

} // EnsembleDefinition
