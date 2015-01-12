/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.meta;

import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Condition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage
 * @generated
 */
public interface RuntimeMetadataFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	RuntimeMetadataFactory eINSTANCE = cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Time Trigger</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Time Trigger</em>'.
	 * @generated
	 */
	TimeTrigger createTimeTrigger();

	/**
	 * Returns a new object of class '<em>Knowledge Change Trigger</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Knowledge Change Trigger</em>'.
	 * @generated
	 */
	KnowledgeChangeTrigger createKnowledgeChangeTrigger();

	/**
	 * Returns a new object of class '<em>Knowledge Path</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Knowledge Path</em>'.
	 * @generated
	 */
	KnowledgePath createKnowledgePath();

	/**
	 * Returns a new object of class '<em>Path Node Field</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Path Node Field</em>'.
	 * @generated
	 */
	PathNodeField createPathNodeField();

	/**
	 * Returns a new object of class '<em>Path Node Map Key</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Path Node Map Key</em>'.
	 * @generated
	 */
	PathNodeMapKey createPathNodeMapKey();

	/**
	 * Returns a new object of class '<em>Runtime Metadata</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Runtime Metadata</em>'.
	 * @generated
	 */
	RuntimeMetadata createRuntimeMetadata();

	/**
	 * Returns a new object of class '<em>Component Instance</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Component Instance</em>'.
	 * @generated
	 */
	ComponentInstance createComponentInstance();

	/**
	 * Returns a new object of class '<em>Ensemble Definition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ensemble Definition</em>'.
	 * @generated
	 */
	EnsembleDefinition createEnsembleDefinition();

	/**
	 * Returns a new object of class '<em>Condition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Condition</em>'.
	 * @generated
	 */
	Condition createCondition();

	/**
	 * Returns a new object of class '<em>Exchange</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Exchange</em>'.
	 * @generated
	 */
	Exchange createExchange();

	/**
	 * Returns a new object of class '<em>Component Process</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Component Process</em>'.
	 * @generated
	 */
	ComponentProcess createComponentProcess();

	/**
	 * Returns a new object of class '<em>Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Parameter</em>'.
	 * @generated
	 */
	Parameter createParameter();

	/**
	 * Returns a new object of class '<em>Invocable</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Invocable</em>'.
	 * @generated
	 */
	Invocable createInvocable();

	/**
	 * Returns a new object of class '<em>Ensemble Controller</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ensemble Controller</em>'.
	 * @generated
	 */
	EnsembleController createEnsembleController();

	/**
	 * Returns a new object of class '<em>Path Node Coordinator</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Path Node Coordinator</em>'.
	 * @generated
	 */
	PathNodeCoordinator createPathNodeCoordinator();

	/**
	 * Returns a new object of class '<em>Path Node Member</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Path Node Member</em>'.
	 * @generated
	 */
	PathNodeMember createPathNodeMember();

	/**
	 * Returns a new object of class '<em>Path Node Component Id</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Path Node Component Id</em>'.
	 * @generated
	 */
	PathNodeComponentId createPathNodeComponentId();

	/**
	 * Returns a new object of class '<em>Knowledge Security Tag</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Knowledge Security Tag</em>'.
	 * @generated
	 */
	KnowledgeSecurityTag createKnowledgeSecurityTag();

	/**
	 * Returns a new object of class '<em>Security Role</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Security Role</em>'.
	 * @generated
	 */
	SecurityRole createSecurityRole();

	/**
	 * Returns a new object of class '<em>Path Security Role Argument</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Path Security Role Argument</em>'.
	 * @generated
	 */
	PathSecurityRoleArgument createPathSecurityRoleArgument();

	/**
	 * Returns a new object of class '<em>Blank Security Role Argument</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Blank Security Role Argument</em>'.
	 * @generated
	 */
	BlankSecurityRoleArgument createBlankSecurityRoleArgument();

	/**
	 * Returns a new object of class '<em>Absolute Security Role Argument</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Absolute Security Role Argument</em>'.
	 * @generated
	 */
	AbsoluteSecurityRoleArgument createAbsoluteSecurityRoleArgument();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	RuntimeMetadataPackage getRuntimeMetadataPackage();

} //RuntimeMetadataFactory
