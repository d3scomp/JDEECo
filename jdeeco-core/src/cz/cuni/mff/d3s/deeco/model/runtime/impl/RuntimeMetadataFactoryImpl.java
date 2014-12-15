/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistry;

import cz.cuni.mff.d3s.deeco.model.runtime.api.*;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import cz.cuni.mff.d3s.deeco.network.CommunicationBoundaryPredicate;

import java.lang.reflect.Method;

import java.util.Map;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class RuntimeMetadataFactoryImpl extends EFactoryImpl implements RuntimeMetadataFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	public static RuntimeMetadataFactory init() {
		try {
			RuntimeMetadataFactory theRuntimeMetadataFactory = (RuntimeMetadataFactory)EPackage.Registry.INSTANCE.getEFactory("http://cz.cuni.mff.d3s.deeco.model.runtime/1.0"); 
			if (theRuntimeMetadataFactory != null) {
				return theRuntimeMetadataFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuntimeMetadataFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case RuntimeMetadataPackage.TIME_TRIGGER: return createTimeTrigger();
			case RuntimeMetadataPackage.KNOWLEDGE_CHANGE_TRIGGER: return createKnowledgeChangeTrigger();
			case RuntimeMetadataPackage.KNOWLEDGE_PATH: return createKnowledgePath();
			case RuntimeMetadataPackage.PATH_NODE_FIELD: return createPathNodeField();
			case RuntimeMetadataPackage.PATH_NODE_MAP_KEY: return createPathNodeMapKey();
			case RuntimeMetadataPackage.RUNTIME_METADATA: return createRuntimeMetadata();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE: return createComponentInstance();
			case RuntimeMetadataPackage.ENSEMBLE_DEFINITION: return createEnsembleDefinition();
			case RuntimeMetadataPackage.CONDITION: return createCondition();
			case RuntimeMetadataPackage.EXCHANGE: return createExchange();
			case RuntimeMetadataPackage.COMPONENT_PROCESS: return createComponentProcess();
			case RuntimeMetadataPackage.PARAMETER: return createParameter();
			case RuntimeMetadataPackage.INVOCABLE: return createInvocable();
			case RuntimeMetadataPackage.ENSEMBLE_CONTROLLER: return createEnsembleController();
			case RuntimeMetadataPackage.PATH_NODE_COORDINATOR: return createPathNodeCoordinator();
			case RuntimeMetadataPackage.PATH_NODE_MEMBER: return createPathNodeMember();
			case RuntimeMetadataPackage.PATH_NODE_COMPONENT_ID: return createPathNodeComponentId();
			case RuntimeMetadataPackage.STRING_TO_OBJECT_MAP: return (EObject)createStringToObjectMap();
			case RuntimeMetadataPackage.KNOWLEDGE_SECURITY_TAG: return createKnowledgeSecurityTag();
			case RuntimeMetadataPackage.SECURITY_ROLE: return createSecurityRole();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case RuntimeMetadataPackage.PARAMETER_DIRECTION:
				return createParameterDirectionFromString(eDataType, initialValue);
			case RuntimeMetadataPackage.METHOD:
				return createMethodFromString(eDataType, initialValue);
			case RuntimeMetadataPackage.KNOWLEDGE_MANAGER:
				return createKnowledgeManagerFromString(eDataType, initialValue);
			case RuntimeMetadataPackage.SHADOW_KNOWLEDGE_MANAGER_REGISTRY:
				return createShadowKnowledgeManagerRegistryFromString(eDataType, initialValue);
			case RuntimeMetadataPackage.COMMUNICATION_BOUNDARY:
				return createCommunicationBoundaryFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case RuntimeMetadataPackage.PARAMETER_DIRECTION:
				return convertParameterDirectionToString(eDataType, instanceValue);
			case RuntimeMetadataPackage.METHOD:
				return convertMethodToString(eDataType, instanceValue);
			case RuntimeMetadataPackage.KNOWLEDGE_MANAGER:
				return convertKnowledgeManagerToString(eDataType, instanceValue);
			case RuntimeMetadataPackage.SHADOW_KNOWLEDGE_MANAGER_REGISTRY:
				return convertShadowKnowledgeManagerRegistryToString(eDataType, instanceValue);
			case RuntimeMetadataPackage.COMMUNICATION_BOUNDARY:
				return convertCommunicationBoundaryToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TimeTrigger createTimeTrigger() {
		TimeTriggerImpl timeTrigger = new TimeTriggerImpl();
		return timeTrigger;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgeChangeTrigger createKnowledgeChangeTrigger() {
		KnowledgeChangeTriggerImpl knowledgeChangeTrigger = new KnowledgeChangeTriggerImpl();
		return knowledgeChangeTrigger;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgePath createKnowledgePath() {
		KnowledgePathImpl knowledgePath = new KnowledgePathImpl();
		return knowledgePath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PathNodeField createPathNodeField() {
		PathNodeFieldImpl pathNodeField = new PathNodeFieldImpl();
		return pathNodeField;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PathNodeMapKey createPathNodeMapKey() {
		PathNodeMapKeyImpl pathNodeMapKey = new PathNodeMapKeyImpl();
		return pathNodeMapKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuntimeMetadata createRuntimeMetadata() {
		RuntimeMetadataImpl runtimeMetadata = new RuntimeMetadataImpl();
		return runtimeMetadata;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentInstance createComponentInstance() {
		ComponentInstanceImpl componentInstance = new ComponentInstanceImpl();
		return componentInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnsembleDefinition createEnsembleDefinition() {
		EnsembleDefinitionImpl ensembleDefinition = new EnsembleDefinitionImpl();
		return ensembleDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Condition createCondition() {
		ConditionImpl condition = new ConditionImpl();
		return condition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Exchange createExchange() {
		ExchangeImpl exchange = new ExchangeImpl();
		return exchange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentProcess createComponentProcess() {
		ComponentProcessImpl componentProcess = new ComponentProcessImpl();
		return componentProcess;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Parameter createParameter() {
		ParameterImpl parameter = new ParameterImpl();
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Invocable createInvocable() {
		InvocableImpl invocable = new InvocableImpl();
		return invocable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnsembleController createEnsembleController() {
		EnsembleControllerImpl ensembleController = new EnsembleControllerImpl();
		return ensembleController;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PathNodeCoordinator createPathNodeCoordinator() {
		PathNodeCoordinatorImpl pathNodeCoordinator = new PathNodeCoordinatorImpl();
		return pathNodeCoordinator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PathNodeMember createPathNodeMember() {
		PathNodeMemberImpl pathNodeMember = new PathNodeMemberImpl();
		return pathNodeMember;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PathNodeComponentId createPathNodeComponentId() {
		PathNodeComponentIdImpl pathNodeComponentId = new PathNodeComponentIdImpl();
		return pathNodeComponentId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, Object> createStringToObjectMap() {
		StringToObjectMapImpl stringToObjectMap = new StringToObjectMapImpl();
		return stringToObjectMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgeSecurityTag createKnowledgeSecurityTag() {
		KnowledgeSecurityTagImpl knowledgeSecurityTag = new KnowledgeSecurityTagImpl();
		return knowledgeSecurityTag;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SecurityRole createSecurityRole() {
		SecurityRoleImpl securityRole = new SecurityRoleImpl();
		return securityRole;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterDirection createParameterDirectionFromString(EDataType eDataType, String initialValue) {
		ParameterDirection result = ParameterDirection.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertParameterDirectionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Method createMethodFromString(EDataType eDataType, String initialValue) {
		return (Method)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMethodToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgeManager createKnowledgeManagerFromString(EDataType eDataType, String initialValue) {
		return (KnowledgeManager)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertKnowledgeManagerToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ShadowKnowledgeManagerRegistry createShadowKnowledgeManagerRegistryFromString(EDataType eDataType, String initialValue) {
		return (ShadowKnowledgeManagerRegistry)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertShadowKnowledgeManagerRegistryToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommunicationBoundaryPredicate createCommunicationBoundaryFromString(EDataType eDataType, String initialValue) {
		return (CommunicationBoundaryPredicate)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCommunicationBoundaryToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuntimeMetadataPackage getRuntimeMetadataPackage() {
		return (RuntimeMetadataPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static RuntimeMetadataPackage getPackage() {
		return RuntimeMetadataPackage.eINSTANCE;
	}

} //RuntimeMetadataFactoryImpl
