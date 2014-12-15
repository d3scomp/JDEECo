/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistry;

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
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import cz.cuni.mff.d3s.deeco.network.CommunicationBoundaryPredicate;

import java.lang.reflect.Method;

import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class RuntimeMetadataPackageImpl extends EPackageImpl implements RuntimeMetadataPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass timeTriggerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass triggerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass knowledgeChangeTriggerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass knowledgePathEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pathNodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pathNodeFieldEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pathNodeMapKeyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass runtimeMetadataEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass componentInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ensembleDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass conditionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass exchangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass componentProcessEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass invocableEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ensembleControllerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pathNodeCoordinatorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pathNodeMemberEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pathNodeComponentIdEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stringToObjectMapEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass knowledgeSecurityTagEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum parameterDirectionEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType methodEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType knowledgeManagerEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType shadowKnowledgeManagerRegistryEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType communicationBoundaryEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private RuntimeMetadataPackageImpl() {
		super(eNS_URI, RuntimeMetadataFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link RuntimeMetadataPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static RuntimeMetadataPackage init() {
		if (isInited) return (RuntimeMetadataPackage)EPackage.Registry.INSTANCE.getEPackage(RuntimeMetadataPackage.eNS_URI);

		// Obtain or create and register package
		RuntimeMetadataPackageImpl theRuntimeMetadataPackage = (RuntimeMetadataPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof RuntimeMetadataPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new RuntimeMetadataPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theRuntimeMetadataPackage.createPackageContents();

		// Initialize created meta-data
		theRuntimeMetadataPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theRuntimeMetadataPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(RuntimeMetadataPackage.eNS_URI, theRuntimeMetadataPackage);
		return theRuntimeMetadataPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTimeTrigger() {
		return timeTriggerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTimeTrigger_Period() {
		return (EAttribute)timeTriggerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTimeTrigger_Offset() {
		return (EAttribute)timeTriggerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTimeTrigger_Order() {
		return (EAttribute)timeTriggerEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTimeTrigger_Wcet() {
		return (EAttribute)timeTriggerEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTrigger() {
		return triggerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKnowledgeChangeTrigger() {
		return knowledgeChangeTriggerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getKnowledgeChangeTrigger_KnowledgePath() {
		return (EReference)knowledgeChangeTriggerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKnowledgePath() {
		return knowledgePathEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getKnowledgePath_Nodes() {
		return (EReference)knowledgePathEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPathNode() {
		return pathNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPathNodeField() {
		return pathNodeFieldEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPathNodeField_Name() {
		return (EAttribute)pathNodeFieldEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPathNodeMapKey() {
		return pathNodeMapKeyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPathNodeMapKey_KeyPath() {
		return (EReference)pathNodeMapKeyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRuntimeMetadata() {
		return runtimeMetadataEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRuntimeMetadata_EnsembleDefinitions() {
		return (EReference)runtimeMetadataEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRuntimeMetadata_ComponentInstances() {
		return (EReference)runtimeMetadataEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getComponentInstance() {
		return componentInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComponentInstance_ComponentProcesses() {
		return (EReference)componentInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getComponentInstance_Name() {
		return (EAttribute)componentInstanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getComponentInstance_KnowledgeManager() {
		return (EAttribute)componentInstanceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getComponentInstance_ShadowKnowledgeManagerRegistry() {
		return (EAttribute)componentInstanceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComponentInstance_EnsembleControllers() {
		return (EReference)componentInstanceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComponentInstance_InternalData() {
		return (EReference)componentInstanceEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getComponentInstance_SystemComponent() {
		return (EAttribute)componentInstanceEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnsembleDefinition() {
		return ensembleDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEnsembleDefinition_Name() {
		return (EAttribute)ensembleDefinitionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsembleDefinition_Membership() {
		return (EReference)ensembleDefinitionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsembleDefinition_KnowledgeExchange() {
		return (EReference)ensembleDefinitionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsembleDefinition_Triggers() {
		return (EReference)ensembleDefinitionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEnsembleDefinition_CommunicationBoundary() {
		return (EAttribute)ensembleDefinitionEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEnsembleDefinition_PartitionedBy() {
		return (EAttribute)ensembleDefinitionEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCondition() {
		return conditionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExchange() {
		return exchangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getComponentProcess() {
		return componentProcessEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getComponentProcess_Name() {
		return (EAttribute)componentProcessEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComponentProcess_ComponentInstance() {
		return (EReference)componentProcessEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getComponentProcess_Active() {
		return (EAttribute)componentProcessEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComponentProcess_Triggers() {
		return (EReference)componentProcessEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParameter() {
		return parameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_Direction() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameter_KnowledgePath() {
		return (EReference)parameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_Type() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInvocable() {
		return invocableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInvocable_Parameters() {
		return (EReference)invocableEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInvocable_Method() {
		return (EAttribute)invocableEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnsembleController() {
		return ensembleControllerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsembleController_ComponentInstance() {
		return (EReference)ensembleControllerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsembleController_EnsembleDefinition() {
		return (EReference)ensembleControllerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEnsembleController_Active() {
		return (EAttribute)ensembleControllerEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPathNodeCoordinator() {
		return pathNodeCoordinatorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPathNodeMember() {
		return pathNodeMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPathNodeComponentId() {
		return pathNodeComponentIdEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStringToObjectMap() {
		return stringToObjectMapEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringToObjectMap_Key() {
		return (EAttribute)stringToObjectMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringToObjectMap_Value() {
		return (EAttribute)stringToObjectMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKnowledgeSecurityTag() {
		return knowledgeSecurityTagEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKnowledgeSecurityTag_RoleName() {
		return (EAttribute)knowledgeSecurityTagEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getKnowledgeSecurityTag_Arguments() {
		return (EReference)knowledgeSecurityTagEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getParameterDirection() {
		return parameterDirectionEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getMethod() {
		return methodEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getKnowledgeManager() {
		return knowledgeManagerEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getShadowKnowledgeManagerRegistry() {
		return shadowKnowledgeManagerRegistryEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getCommunicationBoundary() {
		return communicationBoundaryEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuntimeMetadataFactory getRuntimeMetadataFactory() {
		return (RuntimeMetadataFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		timeTriggerEClass = createEClass(TIME_TRIGGER);
		createEAttribute(timeTriggerEClass, TIME_TRIGGER__PERIOD);
		createEAttribute(timeTriggerEClass, TIME_TRIGGER__OFFSET);
		createEAttribute(timeTriggerEClass, TIME_TRIGGER__ORDER);
		createEAttribute(timeTriggerEClass, TIME_TRIGGER__WCET);

		triggerEClass = createEClass(TRIGGER);

		knowledgeChangeTriggerEClass = createEClass(KNOWLEDGE_CHANGE_TRIGGER);
		createEReference(knowledgeChangeTriggerEClass, KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH);

		knowledgePathEClass = createEClass(KNOWLEDGE_PATH);
		createEReference(knowledgePathEClass, KNOWLEDGE_PATH__NODES);

		pathNodeEClass = createEClass(PATH_NODE);

		pathNodeFieldEClass = createEClass(PATH_NODE_FIELD);
		createEAttribute(pathNodeFieldEClass, PATH_NODE_FIELD__NAME);

		pathNodeMapKeyEClass = createEClass(PATH_NODE_MAP_KEY);
		createEReference(pathNodeMapKeyEClass, PATH_NODE_MAP_KEY__KEY_PATH);

		runtimeMetadataEClass = createEClass(RUNTIME_METADATA);
		createEReference(runtimeMetadataEClass, RUNTIME_METADATA__ENSEMBLE_DEFINITIONS);
		createEReference(runtimeMetadataEClass, RUNTIME_METADATA__COMPONENT_INSTANCES);

		componentInstanceEClass = createEClass(COMPONENT_INSTANCE);
		createEReference(componentInstanceEClass, COMPONENT_INSTANCE__COMPONENT_PROCESSES);
		createEAttribute(componentInstanceEClass, COMPONENT_INSTANCE__NAME);
		createEAttribute(componentInstanceEClass, COMPONENT_INSTANCE__KNOWLEDGE_MANAGER);
		createEAttribute(componentInstanceEClass, COMPONENT_INSTANCE__SHADOW_KNOWLEDGE_MANAGER_REGISTRY);
		createEReference(componentInstanceEClass, COMPONENT_INSTANCE__ENSEMBLE_CONTROLLERS);
		createEReference(componentInstanceEClass, COMPONENT_INSTANCE__INTERNAL_DATA);
		createEAttribute(componentInstanceEClass, COMPONENT_INSTANCE__SYSTEM_COMPONENT);

		ensembleDefinitionEClass = createEClass(ENSEMBLE_DEFINITION);
		createEAttribute(ensembleDefinitionEClass, ENSEMBLE_DEFINITION__NAME);
		createEReference(ensembleDefinitionEClass, ENSEMBLE_DEFINITION__MEMBERSHIP);
		createEReference(ensembleDefinitionEClass, ENSEMBLE_DEFINITION__KNOWLEDGE_EXCHANGE);
		createEReference(ensembleDefinitionEClass, ENSEMBLE_DEFINITION__TRIGGERS);
		createEAttribute(ensembleDefinitionEClass, ENSEMBLE_DEFINITION__COMMUNICATION_BOUNDARY);
		createEAttribute(ensembleDefinitionEClass, ENSEMBLE_DEFINITION__PARTITIONED_BY);

		conditionEClass = createEClass(CONDITION);

		exchangeEClass = createEClass(EXCHANGE);

		componentProcessEClass = createEClass(COMPONENT_PROCESS);
		createEAttribute(componentProcessEClass, COMPONENT_PROCESS__NAME);
		createEReference(componentProcessEClass, COMPONENT_PROCESS__COMPONENT_INSTANCE);
		createEAttribute(componentProcessEClass, COMPONENT_PROCESS__ACTIVE);
		createEReference(componentProcessEClass, COMPONENT_PROCESS__TRIGGERS);

		parameterEClass = createEClass(PARAMETER);
		createEAttribute(parameterEClass, PARAMETER__DIRECTION);
		createEReference(parameterEClass, PARAMETER__KNOWLEDGE_PATH);
		createEAttribute(parameterEClass, PARAMETER__TYPE);

		invocableEClass = createEClass(INVOCABLE);
		createEReference(invocableEClass, INVOCABLE__PARAMETERS);
		createEAttribute(invocableEClass, INVOCABLE__METHOD);

		ensembleControllerEClass = createEClass(ENSEMBLE_CONTROLLER);
		createEReference(ensembleControllerEClass, ENSEMBLE_CONTROLLER__COMPONENT_INSTANCE);
		createEReference(ensembleControllerEClass, ENSEMBLE_CONTROLLER__ENSEMBLE_DEFINITION);
		createEAttribute(ensembleControllerEClass, ENSEMBLE_CONTROLLER__ACTIVE);

		pathNodeCoordinatorEClass = createEClass(PATH_NODE_COORDINATOR);

		pathNodeMemberEClass = createEClass(PATH_NODE_MEMBER);

		pathNodeComponentIdEClass = createEClass(PATH_NODE_COMPONENT_ID);

		stringToObjectMapEClass = createEClass(STRING_TO_OBJECT_MAP);
		createEAttribute(stringToObjectMapEClass, STRING_TO_OBJECT_MAP__KEY);
		createEAttribute(stringToObjectMapEClass, STRING_TO_OBJECT_MAP__VALUE);

		knowledgeSecurityTagEClass = createEClass(KNOWLEDGE_SECURITY_TAG);
		createEAttribute(knowledgeSecurityTagEClass, KNOWLEDGE_SECURITY_TAG__ROLE_NAME);
		createEReference(knowledgeSecurityTagEClass, KNOWLEDGE_SECURITY_TAG__ARGUMENTS);

		// Create enums
		parameterDirectionEEnum = createEEnum(PARAMETER_DIRECTION);

		// Create data types
		methodEDataType = createEDataType(METHOD);
		knowledgeManagerEDataType = createEDataType(KNOWLEDGE_MANAGER);
		shadowKnowledgeManagerRegistryEDataType = createEDataType(SHADOW_KNOWLEDGE_MANAGER_REGISTRY);
		communicationBoundaryEDataType = createEDataType(COMMUNICATION_BOUNDARY);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		timeTriggerEClass.getESuperTypes().add(this.getTrigger());
		knowledgeChangeTriggerEClass.getESuperTypes().add(this.getTrigger());
		pathNodeFieldEClass.getESuperTypes().add(this.getPathNode());
		pathNodeMapKeyEClass.getESuperTypes().add(this.getPathNode());
		conditionEClass.getESuperTypes().add(this.getInvocable());
		exchangeEClass.getESuperTypes().add(this.getInvocable());
		componentProcessEClass.getESuperTypes().add(this.getInvocable());
		pathNodeCoordinatorEClass.getESuperTypes().add(this.getPathNode());
		pathNodeMemberEClass.getESuperTypes().add(this.getPathNode());
		pathNodeComponentIdEClass.getESuperTypes().add(this.getPathNode());

		// Initialize classes, features, and operations; add parameters
		initEClass(timeTriggerEClass, TimeTrigger.class, "TimeTrigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTimeTrigger_Period(), ecorePackage.getELong(), "period", "0", 1, 1, TimeTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTimeTrigger_Offset(), ecorePackage.getELong(), "offset", "0", 1, 1, TimeTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTimeTrigger_Order(), ecorePackage.getEInt(), "order", null, 1, 1, TimeTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTimeTrigger_Wcet(), ecorePackage.getELong(), "wcet", null, 1, 1, TimeTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(triggerEClass, Trigger.class, "Trigger", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(knowledgeChangeTriggerEClass, KnowledgeChangeTrigger.class, "KnowledgeChangeTrigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getKnowledgeChangeTrigger_KnowledgePath(), this.getKnowledgePath(), null, "knowledgePath", null, 1, 1, KnowledgeChangeTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(knowledgePathEClass, KnowledgePath.class, "KnowledgePath", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getKnowledgePath_Nodes(), this.getPathNode(), null, "nodes", null, 1, -1, KnowledgePath.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(pathNodeEClass, PathNode.class, "PathNode", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(pathNodeFieldEClass, PathNodeField.class, "PathNodeField", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPathNodeField_Name(), ecorePackage.getEString(), "name", null, 1, 1, PathNodeField.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(pathNodeMapKeyEClass, PathNodeMapKey.class, "PathNodeMapKey", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPathNodeMapKey_KeyPath(), this.getKnowledgePath(), null, "keyPath", null, 1, 1, PathNodeMapKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(runtimeMetadataEClass, RuntimeMetadata.class, "RuntimeMetadata", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRuntimeMetadata_EnsembleDefinitions(), this.getEnsembleDefinition(), null, "ensembleDefinitions", null, 0, -1, RuntimeMetadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRuntimeMetadata_ComponentInstances(), this.getComponentInstance(), null, "componentInstances", null, 0, -1, RuntimeMetadata.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(componentInstanceEClass, ComponentInstance.class, "ComponentInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getComponentInstance_ComponentProcesses(), this.getComponentProcess(), this.getComponentProcess_ComponentInstance(), "componentProcesses", null, 0, -1, ComponentInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponentInstance_Name(), ecorePackage.getEString(), "name", null, 1, 1, ComponentInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponentInstance_KnowledgeManager(), this.getKnowledgeManager(), "knowledgeManager", null, 1, 1, ComponentInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponentInstance_ShadowKnowledgeManagerRegistry(), this.getShadowKnowledgeManagerRegistry(), "shadowKnowledgeManagerRegistry", null, 1, 1, ComponentInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getComponentInstance_EnsembleControllers(), this.getEnsembleController(), this.getEnsembleController_ComponentInstance(), "ensembleControllers", null, 0, -1, ComponentInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getComponentInstance_InternalData(), this.getStringToObjectMap(), null, "internalData", null, 0, -1, ComponentInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponentInstance_SystemComponent(), ecorePackage.getEBoolean(), "systemComponent", "false", 0, 1, ComponentInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(ensembleDefinitionEClass, EnsembleDefinition.class, "EnsembleDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEnsembleDefinition_Name(), ecorePackage.getEString(), "name", null, 1, 1, EnsembleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnsembleDefinition_Membership(), this.getCondition(), null, "membership", null, 1, 1, EnsembleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnsembleDefinition_KnowledgeExchange(), this.getExchange(), null, "knowledgeExchange", null, 1, 1, EnsembleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnsembleDefinition_Triggers(), this.getTrigger(), null, "triggers", null, 0, -1, EnsembleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEnsembleDefinition_CommunicationBoundary(), this.getCommunicationBoundary(), "communicationBoundary", null, 0, 1, EnsembleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEnsembleDefinition_PartitionedBy(), ecorePackage.getEString(), "partitionedBy", null, 0, 1, EnsembleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(conditionEClass, Condition.class, "Condition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(exchangeEClass, Exchange.class, "Exchange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(componentProcessEClass, ComponentProcess.class, "ComponentProcess", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getComponentProcess_Name(), ecorePackage.getEString(), "name", null, 1, 1, ComponentProcess.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getComponentProcess_ComponentInstance(), this.getComponentInstance(), this.getComponentInstance_ComponentProcesses(), "componentInstance", null, 1, 1, ComponentProcess.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponentProcess_Active(), ecorePackage.getEBoolean(), "active", "true", 1, 1, ComponentProcess.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getComponentProcess_Triggers(), this.getTrigger(), null, "triggers", null, 0, -1, ComponentProcess.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parameterEClass, Parameter.class, "Parameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getParameter_Direction(), this.getParameterDirection(), "direction", "INOUT", 1, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getParameter_KnowledgePath(), this.getKnowledgePath(), null, "knowledgePath", null, 1, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameter_Type(), ecorePackage.getEJavaClass(), "type", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(invocableEClass, Invocable.class, "Invocable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInvocable_Parameters(), this.getParameter(), null, "parameters", null, 0, -1, Invocable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInvocable_Method(), this.getMethod(), "method", null, 1, 1, Invocable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(ensembleControllerEClass, EnsembleController.class, "EnsembleController", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEnsembleController_ComponentInstance(), this.getComponentInstance(), this.getComponentInstance_EnsembleControllers(), "componentInstance", null, 1, 1, EnsembleController.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnsembleController_EnsembleDefinition(), this.getEnsembleDefinition(), null, "ensembleDefinition", null, 1, 1, EnsembleController.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEnsembleController_Active(), ecorePackage.getEBoolean(), "active", "true", 1, 1, EnsembleController.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(pathNodeCoordinatorEClass, PathNodeCoordinator.class, "PathNodeCoordinator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(pathNodeMemberEClass, PathNodeMember.class, "PathNodeMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(pathNodeComponentIdEClass, PathNodeComponentId.class, "PathNodeComponentId", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(stringToObjectMapEClass, Map.Entry.class, "StringToObjectMap", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStringToObjectMap_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStringToObjectMap_Value(), ecorePackage.getEJavaObject(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(knowledgeSecurityTagEClass, KnowledgeSecurityTag.class, "KnowledgeSecurityTag", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getKnowledgeSecurityTag_RoleName(), ecorePackage.getEString(), "roleName", null, 1, 1, KnowledgeSecurityTag.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getKnowledgeSecurityTag_Arguments(), this.getKnowledgePath(), null, "arguments", null, 0, -1, KnowledgeSecurityTag.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(parameterDirectionEEnum, ParameterDirection.class, "ParameterDirection");
		addEEnumLiteral(parameterDirectionEEnum, ParameterDirection.IN);
		addEEnumLiteral(parameterDirectionEEnum, ParameterDirection.OUT);
		addEEnumLiteral(parameterDirectionEEnum, ParameterDirection.INOUT);

		// Initialize data types
		initEDataType(methodEDataType, Method.class, "Method", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(knowledgeManagerEDataType, KnowledgeManager.class, "KnowledgeManager", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(shadowKnowledgeManagerRegistryEDataType, ShadowKnowledgeManagerRegistry.class, "ShadowKnowledgeManagerRegistry", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(communicationBoundaryEDataType, CommunicationBoundaryPredicate.class, "CommunicationBoundary", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //RuntimeMetadataPackageImpl
