/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage
 * @generated
 */
public interface RuntimemodelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	RuntimemodelFactory eINSTANCE = cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Component</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Component</em>'.
	 * @generated
	 */
	Component createComponent();

	/**
	 * Returns a new object of class '<em>Process</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Process</em>'.
	 * @generated
	 */
	Process createProcess();

	/**
	 * Returns a new object of class '<em>Structured Knowledge Value Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Structured Knowledge Value Type</em>'.
	 * @generated
	 */
	StructuredKnowledgeValueType createStructuredKnowledgeValueType();

	/**
	 * Returns a new object of class '<em>List Value Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>List Value Type</em>'.
	 * @generated
	 */
	ListValueType createListValueType();

	/**
	 * Returns a new object of class '<em>Map Value Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Map Value Type</em>'.
	 * @generated
	 */
	MapValueType createMapValueType();

	/**
	 * Returns a new object of class '<em>Unstructured Value Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Unstructured Value Type</em>'.
	 * @generated
	 */
	UnstructuredValueType createUnstructuredValueType();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	Model createModel();

	/**
	 * Returns a new object of class '<em>Update Knowledge Structure Command</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Update Knowledge Structure Command</em>'.
	 * @generated
	 */
	UpdateKnowledgeStructureCommand createUpdateKnowledgeStructureCommand();

	/**
	 * Returns a new object of class '<em>Add Component Command</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Add Component Command</em>'.
	 * @generated
	 */
	AddComponentCommand createAddComponentCommand();

	/**
	 * Returns a new object of class '<em>Method Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Method Parameter</em>'.
	 * @generated
	 */
	MethodParameter createMethodParameter();

	/**
	 * Returns a new object of class '<em>Periodic Scheduling</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Periodic Scheduling</em>'.
	 * @generated
	 */
	PeriodicScheduling createPeriodicScheduling();

	/**
	 * Returns a new object of class '<em>Triggered Scheduling</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Triggered Scheduling</em>'.
	 * @generated
	 */
	TriggeredScheduling createTriggeredScheduling();

	/**
	 * Returns a new object of class '<em>Parameter Changed Trigger</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Parameter Changed Trigger</em>'.
	 * @generated
	 */
	ParameterChangedTrigger createParameterChangedTrigger();

	/**
	 * Returns a new object of class '<em>Ensemble</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ensemble</em>'.
	 * @generated
	 */
	Ensemble createEnsemble();

	/**
	 * Returns a new object of class '<em>Top Level Knowledge Definition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Top Level Knowledge Definition</em>'.
	 * @generated
	 */
	TopLevelKnowledgeDefinition createTopLevelKnowledgeDefinition();

	/**
	 * Returns a new object of class '<em>Nested Knowledge Definition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Nested Knowledge Definition</em>'.
	 * @generated
	 */
	NestedKnowledgeDefinition createNestedKnowledgeDefinition();

	/**
	 * Returns a new object of class '<em>Membership Condition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Membership Condition</em>'.
	 * @generated
	 */
	MembershipCondition createMembershipCondition();

	/**
	 * Returns a new object of class '<em>Knowledge Exchange</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Knowledge Exchange</em>'.
	 * @generated
	 */
	KnowledgeExchange createKnowledgeExchange();

	/**
	 * Returns a new object of class '<em>Type Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Type Parameter</em>'.
	 * @generated
	 */
	TypeParameter createTypeParameter();

	/**
	 * Returns a new object of class '<em>Knowledge Type Owner</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Knowledge Type Owner</em>'.
	 * @generated
	 */
	KnowledgeTypeOwner createKnowledgeTypeOwner();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	RuntimemodelPackage getRuntimemodelPackage();

} //RuntimemodelFactory
