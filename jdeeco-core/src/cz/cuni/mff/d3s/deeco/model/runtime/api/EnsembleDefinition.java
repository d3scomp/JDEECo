/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import cz.cuni.mff.d3s.deeco.network.CommunicationBoundaryPredicate;
import org.eclipse.emf.common.util.EList;
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
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getTriggers <em>Triggers</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getCommunicationBoundary <em>Communication Boundary</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getPartitionedBy <em>Partitioned By</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getCoordinatorRole <em>Coordinator Role</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getMemberRole <em>Member Role</em>}</li>
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
	 * Returns the value of the '<em><b>Triggers</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Triggers</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Triggers</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getEnsembleDefinition_Triggers()
	 * @model containment="true"
	 * @generated
	 */
	EList<Trigger> getTriggers();

	/**
	 * Returns the value of the '<em><b>Communication Boundary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Communication Boundary</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Communication Boundary</em>' attribute.
	 * @see #setCommunicationBoundary(CommunicationBoundaryPredicate)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getEnsembleDefinition_CommunicationBoundary()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.runtime.api.CommunicationBoundary"
	 * @generated
	 */
	CommunicationBoundaryPredicate getCommunicationBoundary();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getCommunicationBoundary <em>Communication Boundary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Communication Boundary</em>' attribute.
	 * @see #getCommunicationBoundary()
	 * @generated
	 */
	void setCommunicationBoundary(CommunicationBoundaryPredicate value);

	/**
	 * Returns the value of the '<em><b>Partitioned By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Partitioned By</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Partitioned By</em>' attribute.
	 * @see #setPartitionedBy(String)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getEnsembleDefinition_PartitionedBy()
	 * @model
	 * @generated
	 */
	String getPartitionedBy();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getPartitionedBy <em>Partitioned By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Partitioned By</em>' attribute.
	 * @see #getPartitionedBy()
	 * @generated
	 */
	void setPartitionedBy(String value);

	/**
	 * Returns the value of the '<em><b>Coordinator Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Coordinator Role</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Coordinator Role</em>' attribute.
	 * @see #setCoordinatorRole(Class)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getEnsembleDefinition_CoordinatorRole()
	 * @model
	 * @generated
	 */
	Class getCoordinatorRole();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getCoordinatorRole <em>Coordinator Role</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Coordinator Role</em>' attribute.
	 * @see #getCoordinatorRole()
	 * @generated
	 */
	void setCoordinatorRole(Class value);

	/**
	 * Returns the value of the '<em><b>Member Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Member Role</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Member Role</em>' attribute.
	 * @see #setMemberRole(Class)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getEnsembleDefinition_MemberRole()
	 * @model
	 * @generated
	 */
	Class getMemberRole();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getMemberRole <em>Member Role</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Member Role</em>' attribute.
	 * @see #getMemberRole()
	 * @generated
	 */
	void setMemberRole(Class value);

} // EnsembleDefinition
