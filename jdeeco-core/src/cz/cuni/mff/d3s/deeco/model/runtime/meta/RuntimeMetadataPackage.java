/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.meta;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory
 * @model kind="package"
 * @generated
 */
public interface RuntimeMetadataPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "runtime";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://cz.cuni.mff.d3s.deeco.model.runtime/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "cz.cuni.mff.d3s.deeco.model.runtime";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	RuntimeMetadataPackage eINSTANCE = cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl.init();

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.TriggerImpl <em>Trigger</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.TriggerImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getTrigger()
	 * @generated
	 */
	int TRIGGER = 1;

	/**
	 * The number of structural features of the '<em>Trigger</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRIGGER_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Trigger</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRIGGER_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeChangeTriggerImpl <em>Knowledge Change Trigger</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeChangeTriggerImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getKnowledgeChangeTrigger()
	 * @generated
	 */
	int KNOWLEDGE_CHANGE_TRIGGER = 2;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgePathImpl <em>Knowledge Path</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgePathImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getKnowledgePath()
	 * @generated
	 */
	int KNOWLEDGE_PATH = 3;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeImpl <em>Path Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getPathNode()
	 * @generated
	 */
	int PATH_NODE = 4;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeFieldImpl <em>Path Node Field</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeFieldImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getPathNodeField()
	 * @generated
	 */
	int PATH_NODE_FIELD = 5;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeMapKeyImpl <em>Path Node Map Key</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeMapKeyImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getPathNodeMapKey()
	 * @generated
	 */
	int PATH_NODE_MAP_KEY = 6;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataImpl <em>Runtime Metadata</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getRuntimeMetadata()
	 * @generated
	 */
	int RUNTIME_METADATA = 7;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl <em>Component Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getComponentInstance()
	 * @generated
	 */
	int COMPONENT_INSTANCE = 8;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.EnsembleDefinitionImpl <em>Ensemble Definition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.EnsembleDefinitionImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getEnsembleDefinition()
	 * @generated
	 */
	int ENSEMBLE_DEFINITION = 9;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.InvocableImpl <em>Invocable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.InvocableImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getInvocable()
	 * @generated
	 */
	int INVOCABLE = 14;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ConditionImpl <em>Condition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.ConditionImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getCondition()
	 * @generated
	 */
	int CONDITION = 10;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ExchangeImpl <em>Exchange</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.ExchangeImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getExchange()
	 * @generated
	 */
	int EXCHANGE = 11;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentProcessImpl <em>Component Process</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentProcessImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getComponentProcess()
	 * @generated
	 */
	int COMPONENT_PROCESS = 12;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ParameterImpl <em>Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.ParameterImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getParameter()
	 * @generated
	 */
	int PARAMETER = 13;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.EnsembleControllerImpl <em>Ensemble Controller</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.EnsembleControllerImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getEnsembleController()
	 * @generated
	 */
	int ENSEMBLE_CONTROLLER = 15;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeCoordinatorImpl <em>Path Node Coordinator</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeCoordinatorImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getPathNodeCoordinator()
	 * @generated
	 */
	int PATH_NODE_COORDINATOR = 16;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeMemberImpl <em>Path Node Member</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeMemberImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getPathNodeMember()
	 * @generated
	 */
	int PATH_NODE_MEMBER = 17;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeComponentIdImpl <em>Path Node Component Id</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeComponentIdImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getPathNodeComponentId()
	 * @generated
	 */
	int PATH_NODE_COMPONENT_ID = 18;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.StringToObjectMapImpl <em>String To Object Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.StringToObjectMapImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getStringToObjectMap()
	 * @generated
	 */
	int STRING_TO_OBJECT_MAP = 19;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.TimeTriggerImpl <em>Time Trigger</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.TimeTriggerImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getTimeTrigger()
	 * @generated
	 */
	int TIME_TRIGGER = 0;

	/**
	 * The feature id for the '<em><b>Period</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_TRIGGER__PERIOD = TRIGGER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_TRIGGER__OFFSET = TRIGGER_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Time Trigger</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_TRIGGER_FEATURE_COUNT = TRIGGER_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Time Trigger</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIME_TRIGGER_OPERATION_COUNT = TRIGGER_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Knowledge Path</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH = TRIGGER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Knowledge Change Trigger</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_CHANGE_TRIGGER_FEATURE_COUNT = TRIGGER_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Knowledge Change Trigger</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_CHANGE_TRIGGER_OPERATION_COUNT = TRIGGER_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Nodes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_PATH__NODES = 0;

	/**
	 * The number of structural features of the '<em>Knowledge Path</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_PATH_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Knowledge Path</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_PATH_OPERATION_COUNT = 0;

	/**
	 * The number of structural features of the '<em>Path Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_NODE_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Path Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_NODE_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_NODE_FIELD__NAME = PATH_NODE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Path Node Field</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_NODE_FIELD_FEATURE_COUNT = PATH_NODE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Path Node Field</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_NODE_FIELD_OPERATION_COUNT = PATH_NODE_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Key Path</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_NODE_MAP_KEY__KEY_PATH = PATH_NODE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Path Node Map Key</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_NODE_MAP_KEY_FEATURE_COUNT = PATH_NODE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Path Node Map Key</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_NODE_MAP_KEY_OPERATION_COUNT = PATH_NODE_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Ensemble Definitions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RUNTIME_METADATA__ENSEMBLE_DEFINITIONS = 0;

	/**
	 * The feature id for the '<em><b>Component Instances</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RUNTIME_METADATA__COMPONENT_INSTANCES = 1;

	/**
	 * The number of structural features of the '<em>Runtime Metadata</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RUNTIME_METADATA_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Runtime Metadata</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RUNTIME_METADATA_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Component Processes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__COMPONENT_PROCESSES = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__NAME = 1;

	/**
	 * The feature id for the '<em><b>Knowledge Manager</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__KNOWLEDGE_MANAGER = 2;

	/**
	 * The feature id for the '<em><b>Shadow Knowledge Manager Registry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__SHADOW_KNOWLEDGE_MANAGER_REGISTRY = 3;

	/**
	 * The feature id for the '<em><b>Ensemble Controllers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__ENSEMBLE_CONTROLLERS = 4;

	/**
	 * The feature id for the '<em><b>Internal Data</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__INTERNAL_DATA = 5;

	/**
	 * The feature id for the '<em><b>State Space Models</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__STATE_SPACE_MODELS = 6;

	/**
	 * The feature id for the '<em><b>Transitions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__TRANSITIONS = 7;

	/**
	 * The number of structural features of the '<em>Component Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE_FEATURE_COUNT = 8;

	/**
	 * The number of operations of the '<em>Component Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_DEFINITION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Membership</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_DEFINITION__MEMBERSHIP = 1;

	/**
	 * The feature id for the '<em><b>Knowledge Exchange</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_DEFINITION__KNOWLEDGE_EXCHANGE = 2;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_DEFINITION__TRIGGERS = 3;

	/**
	 * The feature id for the '<em><b>Communication Boundary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_DEFINITION__COMMUNICATION_BOUNDARY = 4;

	/**
	 * The number of structural features of the '<em>Ensemble Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_DEFINITION_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Ensemble Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_DEFINITION_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVOCABLE__PARAMETERS = 0;

	/**
	 * The feature id for the '<em><b>Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVOCABLE__METHOD = 1;

	/**
	 * The number of structural features of the '<em>Invocable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVOCABLE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Invocable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVOCABLE_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITION__PARAMETERS = INVOCABLE__PARAMETERS;

	/**
	 * The feature id for the '<em><b>Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITION__METHOD = INVOCABLE__METHOD;

	/**
	 * The number of structural features of the '<em>Condition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITION_FEATURE_COUNT = INVOCABLE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Condition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITION_OPERATION_COUNT = INVOCABLE_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCHANGE__PARAMETERS = INVOCABLE__PARAMETERS;

	/**
	 * The feature id for the '<em><b>Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCHANGE__METHOD = INVOCABLE__METHOD;

	/**
	 * The number of structural features of the '<em>Exchange</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCHANGE_FEATURE_COUNT = INVOCABLE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Exchange</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXCHANGE_OPERATION_COUNT = INVOCABLE_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_PROCESS__PARAMETERS = INVOCABLE__PARAMETERS;

	/**
	 * The feature id for the '<em><b>Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_PROCESS__METHOD = INVOCABLE__METHOD;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_PROCESS__NAME = INVOCABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Component Instance</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_PROCESS__COMPONENT_INSTANCE = INVOCABLE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Is Active</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_PROCESS__IS_ACTIVE = INVOCABLE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_PROCESS__TRIGGERS = INVOCABLE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_PROCESS__STATE = INVOCABLE_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Component Process</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_PROCESS_FEATURE_COUNT = INVOCABLE_FEATURE_COUNT + 5;

	/**
	 * The number of operations of the '<em>Component Process</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_PROCESS_OPERATION_COUNT = INVOCABLE_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Knowledge Path</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__KNOWLEDGE_PATH = 0;

	/**
	 * The feature id for the '<em><b>Direction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__DIRECTION = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__TYPE = 2;

	/**
	 * The number of structural features of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Component Instance</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_CONTROLLER__COMPONENT_INSTANCE = 0;

	/**
	 * The feature id for the '<em><b>Ensemble Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_CONTROLLER__ENSEMBLE_DEFINITION = 1;

	/**
	 * The number of structural features of the '<em>Ensemble Controller</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_CONTROLLER_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Ensemble Controller</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_CONTROLLER_OPERATION_COUNT = 0;

	/**
	 * The number of structural features of the '<em>Path Node Coordinator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_NODE_COORDINATOR_FEATURE_COUNT = PATH_NODE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Path Node Coordinator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_NODE_COORDINATOR_OPERATION_COUNT = PATH_NODE_OPERATION_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Path Node Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_NODE_MEMBER_FEATURE_COUNT = PATH_NODE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Path Node Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_NODE_MEMBER_OPERATION_COUNT = PATH_NODE_OPERATION_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Path Node Component Id</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_NODE_COMPONENT_ID_FEATURE_COUNT = PATH_NODE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Path Node Component Id</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATH_NODE_COMPONENT_ID_OPERATION_COUNT = PATH_NODE_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_TO_OBJECT_MAP__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_TO_OBJECT_MAP__VALUE = 1;

	/**
	 * The number of structural features of the '<em>String To Object Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_TO_OBJECT_MAP_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>String To Object Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_TO_OBJECT_MAP_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.StateSpaceModelDefinitionImpl <em>State Space Model Definition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.StateSpaceModelDefinitionImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getStateSpaceModelDefinition()
	 * @generated
	 */
	int STATE_SPACE_MODEL_DEFINITION = 20;

	/**
	 * The feature id for the '<em><b>Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_SPACE_MODEL_DEFINITION__MODEL = 0;

	/**
	 * The feature id for the '<em><b>Component Instance</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_SPACE_MODEL_DEFINITION__COMPONENT_INSTANCE = 1;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_SPACE_MODEL_DEFINITION__TRIGGERS = 2;

	/**
	 * The feature id for the '<em><b>Is Active</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_SPACE_MODEL_DEFINITION__IS_ACTIVE = 3;

	/**
	 * The feature id for the '<em><b>In States</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_SPACE_MODEL_DEFINITION__IN_STATES = 4;

	/**
	 * The feature id for the '<em><b>Derivation States</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_SPACE_MODEL_DEFINITION__DERIVATION_STATES = 5;

	/**
	 * The feature id for the '<em><b>Model Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_SPACE_MODEL_DEFINITION__MODEL_VALUE = 6;

	/**
	 * The number of structural features of the '<em>State Space Model Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_SPACE_MODEL_DEFINITION_FEATURE_COUNT = 7;

	/**
	 * The number of operations of the '<em>State Space Model Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_SPACE_MODEL_DEFINITION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueUnchangeTriggerImpl <em>Knowledge Value Unchange Trigger</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueUnchangeTriggerImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getKnowledgeValueUnchangeTrigger()
	 * @generated
	 */
	int KNOWLEDGE_VALUE_UNCHANGE_TRIGGER = 21;

	/**
	 * The feature id for the '<em><b>Knowledge Path</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__KNOWLEDGE_PATH = KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__VALUE = KNOWLEDGE_CHANGE_TRIGGER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Comparison</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__COMPARISON = KNOWLEDGE_CHANGE_TRIGGER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Meta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__META = KNOWLEDGE_CHANGE_TRIGGER_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Knowledge Value Unchange Trigger</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_VALUE_UNCHANGE_TRIGGER_FEATURE_COUNT = KNOWLEDGE_CHANGE_TRIGGER_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Knowledge Value Unchange Trigger</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_VALUE_UNCHANGE_TRIGGER_OPERATION_COUNT = KNOWLEDGE_CHANGE_TRIGGER_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueChangeTriggerImpl <em>Knowledge Value Change Trigger</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueChangeTriggerImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getKnowledgeValueChangeTrigger()
	 * @generated
	 */
	int KNOWLEDGE_VALUE_CHANGE_TRIGGER = 22;

	/**
	 * The feature id for the '<em><b>Knowledge Path</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_VALUE_CHANGE_TRIGGER__KNOWLEDGE_PATH = KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_VALUE_CHANGE_TRIGGER__VALUE = KNOWLEDGE_CHANGE_TRIGGER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Comparison</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_VALUE_CHANGE_TRIGGER__COMPARISON = KNOWLEDGE_CHANGE_TRIGGER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Meta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_VALUE_CHANGE_TRIGGER__META = KNOWLEDGE_CHANGE_TRIGGER_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Knowledge Value Change Trigger</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_VALUE_CHANGE_TRIGGER_FEATURE_COUNT = KNOWLEDGE_CHANGE_TRIGGER_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Knowledge Value Change Trigger</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_VALUE_CHANGE_TRIGGER_OPERATION_COUNT = KNOWLEDGE_CHANGE_TRIGGER_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.TransitionDefinitionImpl <em>Transition Definition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.TransitionDefinitionImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getTransitionDefinition()
	 * @generated
	 */
	int TRANSITION_DEFINITION = 23;

	/**
	 * The feature id for the '<em><b>From Mode</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSITION_DEFINITION__FROM_MODE = 0;

	/**
	 * The feature id for the '<em><b>To Mode</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSITION_DEFINITION__TO_MODE = 1;

	/**
	 * The feature id for the '<em><b>Init</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSITION_DEFINITION__INIT = 2;

	/**
	 * The feature id for the '<em><b>Trigger</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSITION_DEFINITION__TRIGGER = 3;

	/**
	 * The number of structural features of the '<em>Transition Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSITION_DEFINITION_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Transition Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSITION_DEFINITION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection <em>Parameter Direction</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getParameterDirection()
	 * @generated
	 */
	int PARAMETER_DIRECTION = 24;

	/**
	 * The meta object id for the '<em>Method</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.reflect.Method
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getMethod()
	 * @generated
	 */
	int METHOD = 28;

	/**
	 * The meta object id for the '<em>Knowledge Manager</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getKnowledgeManager()
	 * @generated
	 */
	int KNOWLEDGE_MANAGER = 29;

	/**
	 * The meta object id for the '<em>Shadow Knowledge Manager Registry</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistry
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getShadowKnowledgeManagerRegistry()
	 * @generated
	 */
	int SHADOW_KNOWLEDGE_MANAGER_REGISTRY = 30;

	/**
	 * The meta object id for the '<em>Communication Boundary</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.network.CommunicationBoundaryPredicate
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getCommunicationBoundary()
	 * @generated
	 */
	int COMMUNICATION_BOUNDARY = 31;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType <em>Metadata Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getMetadataType()
	 * @generated
	 */
	int METADATA_TYPE = 25;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComparisonType <em>Comparison Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComparisonType
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getComparisonType()
	 * @generated
	 */
	int COMPARISON_TYPE = 26;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeState <em>Mode State</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ModeState
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getModeState()
	 * @generated
	 */
	int MODE_STATE = 27;

	/**
	 * The meta object id for the '<em>Model Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ModelInterface
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getModelType()
	 * @generated
	 */
	int MODEL_TYPE = 32;

	/**
	 * The meta object id for the '<em>Inaccurate Value</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getInaccurateValue()
	 * @generated
	 */
	int INACCURATE_VALUE = 33;

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger <em>Trigger</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trigger</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger
	 * @generated
	 */
	EClass getTrigger();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger <em>Knowledge Change Trigger</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Knowledge Change Trigger</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger
	 * @generated
	 */
	EClass getKnowledgeChangeTrigger();

	/**
	 * Returns the meta object for the containment reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger#getKnowledgePath <em>Knowledge Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Knowledge Path</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger#getKnowledgePath()
	 * @see #getKnowledgeChangeTrigger()
	 * @generated
	 */
	EReference getKnowledgeChangeTrigger_KnowledgePath();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath <em>Knowledge Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Knowledge Path</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath
	 * @generated
	 */
	EClass getKnowledgePath();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath#getNodes <em>Nodes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Nodes</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath#getNodes()
	 * @see #getKnowledgePath()
	 * @generated
	 */
	EReference getKnowledgePath_Nodes();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode <em>Path Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Path Node</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode
	 * @generated
	 */
	EClass getPathNode();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField <em>Path Node Field</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Path Node Field</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField
	 * @generated
	 */
	EClass getPathNodeField();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField#getName()
	 * @see #getPathNodeField()
	 * @generated
	 */
	EAttribute getPathNodeField_Name();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey <em>Path Node Map Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Path Node Map Key</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey
	 * @generated
	 */
	EClass getPathNodeMapKey();

	/**
	 * Returns the meta object for the containment reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey#getKeyPath <em>Key Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Key Path</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey#getKeyPath()
	 * @see #getPathNodeMapKey()
	 * @generated
	 */
	EReference getPathNodeMapKey_KeyPath();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata <em>Runtime Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Runtime Metadata</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata
	 * @generated
	 */
	EClass getRuntimeMetadata();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata#getEnsembleDefinitions <em>Ensemble Definitions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Ensemble Definitions</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata#getEnsembleDefinitions()
	 * @see #getRuntimeMetadata()
	 * @generated
	 */
	EReference getRuntimeMetadata_EnsembleDefinitions();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata#getComponentInstances <em>Component Instances</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Component Instances</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata#getComponentInstances()
	 * @see #getRuntimeMetadata()
	 * @generated
	 */
	EReference getRuntimeMetadata_ComponentInstances();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance <em>Component Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Component Instance</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance
	 * @generated
	 */
	EClass getComponentInstance();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getComponentProcesses <em>Component Processes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Component Processes</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getComponentProcesses()
	 * @see #getComponentInstance()
	 * @generated
	 */
	EReference getComponentInstance_ComponentProcesses();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getName()
	 * @see #getComponentInstance()
	 * @generated
	 */
	EAttribute getComponentInstance_Name();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getKnowledgeManager <em>Knowledge Manager</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Knowledge Manager</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getKnowledgeManager()
	 * @see #getComponentInstance()
	 * @generated
	 */
	EAttribute getComponentInstance_KnowledgeManager();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getShadowKnowledgeManagerRegistry <em>Shadow Knowledge Manager Registry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Shadow Knowledge Manager Registry</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getShadowKnowledgeManagerRegistry()
	 * @see #getComponentInstance()
	 * @generated
	 */
	EAttribute getComponentInstance_ShadowKnowledgeManagerRegistry();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getEnsembleControllers <em>Ensemble Controllers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Ensemble Controllers</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getEnsembleControllers()
	 * @see #getComponentInstance()
	 * @generated
	 */
	EReference getComponentInstance_EnsembleControllers();

	/**
	 * Returns the meta object for the map '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getInternalData <em>Internal Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Internal Data</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getInternalData()
	 * @see #getComponentInstance()
	 * @generated
	 */
	EReference getComponentInstance_InternalData();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getStateSpaceModels <em>State Space Models</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>State Space Models</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getStateSpaceModels()
	 * @see #getComponentInstance()
	 * @generated
	 */
	EReference getComponentInstance_StateSpaceModels();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getTransitions <em>Transitions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Transitions</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getTransitions()
	 * @see #getComponentInstance()
	 * @generated
	 */
	EReference getComponentInstance_Transitions();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition <em>Ensemble Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ensemble Definition</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition
	 * @generated
	 */
	EClass getEnsembleDefinition();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getName()
	 * @see #getEnsembleDefinition()
	 * @generated
	 */
	EAttribute getEnsembleDefinition_Name();

	/**
	 * Returns the meta object for the containment reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getMembership <em>Membership</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Membership</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getMembership()
	 * @see #getEnsembleDefinition()
	 * @generated
	 */
	EReference getEnsembleDefinition_Membership();

	/**
	 * Returns the meta object for the containment reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getKnowledgeExchange <em>Knowledge Exchange</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Knowledge Exchange</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getKnowledgeExchange()
	 * @see #getEnsembleDefinition()
	 * @generated
	 */
	EReference getEnsembleDefinition_KnowledgeExchange();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getTriggers <em>Triggers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Triggers</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getTriggers()
	 * @see #getEnsembleDefinition()
	 * @generated
	 */
	EReference getEnsembleDefinition_Triggers();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getCommunicationBoundary <em>Communication Boundary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Communication Boundary</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getCommunicationBoundary()
	 * @see #getEnsembleDefinition()
	 * @generated
	 */
	EAttribute getEnsembleDefinition_CommunicationBoundary();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Condition <em>Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Condition</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Condition
	 * @generated
	 */
	EClass getCondition();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange <em>Exchange</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Exchange</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange
	 * @generated
	 */
	EClass getExchange();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess <em>Component Process</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Component Process</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess
	 * @generated
	 */
	EClass getComponentProcess();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess#getName()
	 * @see #getComponentProcess()
	 * @generated
	 */
	EAttribute getComponentProcess_Name();

	/**
	 * Returns the meta object for the container reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess#getComponentInstance <em>Component Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Component Instance</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess#getComponentInstance()
	 * @see #getComponentProcess()
	 * @generated
	 */
	EReference getComponentProcess_ComponentInstance();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess#isIsActive <em>Is Active</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Active</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess#isIsActive()
	 * @see #getComponentProcess()
	 * @generated
	 */
	EAttribute getComponentProcess_IsActive();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess#getTriggers <em>Triggers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Triggers</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess#getTriggers()
	 * @see #getComponentProcess()
	 * @generated
	 */
	EReference getComponentProcess_Triggers();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess#getState <em>State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>State</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess#getState()
	 * @see #getComponentProcess()
	 * @generated
	 */
	EAttribute getComponentProcess_State();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter
	 * @generated
	 */
	EClass getParameter();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getDirection <em>Direction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Direction</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getDirection()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Direction();

	/**
	 * Returns the meta object for the containment reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getKnowledgePath <em>Knowledge Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Knowledge Path</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getKnowledgePath()
	 * @see #getParameter()
	 * @generated
	 */
	EReference getParameter_KnowledgePath();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getType()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Type();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable <em>Invocable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Invocable</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable
	 * @generated
	 */
	EClass getInvocable();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable#getParameters()
	 * @see #getInvocable()
	 * @generated
	 */
	EReference getInvocable_Parameters();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable#getMethod <em>Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Method</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable#getMethod()
	 * @see #getInvocable()
	 * @generated
	 */
	EAttribute getInvocable_Method();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController <em>Ensemble Controller</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ensemble Controller</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController
	 * @generated
	 */
	EClass getEnsembleController();

	/**
	 * Returns the meta object for the container reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController#getComponentInstance <em>Component Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Component Instance</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController#getComponentInstance()
	 * @see #getEnsembleController()
	 * @generated
	 */
	EReference getEnsembleController_ComponentInstance();

	/**
	 * Returns the meta object for the reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController#getEnsembleDefinition <em>Ensemble Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Ensemble Definition</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController#getEnsembleDefinition()
	 * @see #getEnsembleController()
	 * @generated
	 */
	EReference getEnsembleController_EnsembleDefinition();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator <em>Path Node Coordinator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Path Node Coordinator</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator
	 * @generated
	 */
	EClass getPathNodeCoordinator();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember <em>Path Node Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Path Node Member</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember
	 * @generated
	 */
	EClass getPathNodeMember();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId <em>Path Node Component Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Path Node Component Id</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId
	 * @generated
	 */
	EClass getPathNodeComponentId();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>String To Object Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>String To Object Map</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueDataType="org.eclipse.emf.ecore.EJavaObject"
	 * @generated
	 */
	EClass getStringToObjectMap();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getStringToObjectMap()
	 * @generated
	 */
	EAttribute getStringToObjectMap_Key();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getStringToObjectMap()
	 * @generated
	 */
	EAttribute getStringToObjectMap_Value();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition <em>State Space Model Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>State Space Model Definition</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition
	 * @generated
	 */
	EClass getStateSpaceModelDefinition();

	/**
	 * Returns the meta object for the reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getInStates <em>In States</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>In States</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getInStates()
	 * @see #getStateSpaceModelDefinition()
	 * @generated
	 */
	EReference getStateSpaceModelDefinition_InStates();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#isIsActive <em>Is Active</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Active</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#isIsActive()
	 * @see #getStateSpaceModelDefinition()
	 * @generated
	 */
	EAttribute getStateSpaceModelDefinition_IsActive();

	/**
	 * Returns the meta object for the reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getDerivationStates <em>Derivation States</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Derivation States</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getDerivationStates()
	 * @see #getStateSpaceModelDefinition()
	 * @generated
	 */
	EReference getStateSpaceModelDefinition_DerivationStates();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getModelValue <em>Model Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Model Value</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getModelValue()
	 * @see #getStateSpaceModelDefinition()
	 * @generated
	 */
	EAttribute getStateSpaceModelDefinition_ModelValue();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger <em>Knowledge Value Unchange Trigger</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Knowledge Value Unchange Trigger</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger
	 * @generated
	 */
	EClass getKnowledgeValueUnchangeTrigger();

	/**
	 * Returns the meta object for the attribute list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Value</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger#getValue()
	 * @see #getKnowledgeValueUnchangeTrigger()
	 * @generated
	 */
	EAttribute getKnowledgeValueUnchangeTrigger_Value();

	/**
	 * Returns the meta object for the attribute list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger#getComparison <em>Comparison</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Comparison</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger#getComparison()
	 * @see #getKnowledgeValueUnchangeTrigger()
	 * @generated
	 */
	EAttribute getKnowledgeValueUnchangeTrigger_Comparison();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger#getMeta <em>Meta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Meta</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger#getMeta()
	 * @see #getKnowledgeValueUnchangeTrigger()
	 * @generated
	 */
	EAttribute getKnowledgeValueUnchangeTrigger_Meta();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger <em>Knowledge Value Change Trigger</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Knowledge Value Change Trigger</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger
	 * @generated
	 */
	EClass getKnowledgeValueChangeTrigger();

	/**
	 * Returns the meta object for the attribute list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Value</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger#getValue()
	 * @see #getKnowledgeValueChangeTrigger()
	 * @generated
	 */
	EAttribute getKnowledgeValueChangeTrigger_Value();

	/**
	 * Returns the meta object for the attribute list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger#getComparison <em>Comparison</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Comparison</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger#getComparison()
	 * @see #getKnowledgeValueChangeTrigger()
	 * @generated
	 */
	EAttribute getKnowledgeValueChangeTrigger_Comparison();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger#getMeta <em>Meta</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Meta</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger#getMeta()
	 * @see #getKnowledgeValueChangeTrigger()
	 * @generated
	 */
	EAttribute getKnowledgeValueChangeTrigger_Meta();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition <em>Transition Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transition Definition</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition
	 * @generated
	 */
	EClass getTransitionDefinition();

	/**
	 * Returns the meta object for the reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#getFromMode <em>From Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>From Mode</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#getFromMode()
	 * @see #getTransitionDefinition()
	 * @generated
	 */
	EReference getTransitionDefinition_FromMode();

	/**
	 * Returns the meta object for the reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#getToMode <em>To Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>To Mode</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#getToMode()
	 * @see #getTransitionDefinition()
	 * @generated
	 */
	EReference getTransitionDefinition_ToMode();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#isInit <em>Init</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Init</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#isInit()
	 * @see #getTransitionDefinition()
	 * @generated
	 */
	EAttribute getTransitionDefinition_Init();

	/**
	 * Returns the meta object for the reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#getTrigger <em>Trigger</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Trigger</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.TransitionDefinition#getTrigger()
	 * @see #getTransitionDefinition()
	 * @generated
	 */
	EReference getTransitionDefinition_Trigger();

	/**
	 * Returns the meta object for the container reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getComponentInstance <em>Component Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Component Instance</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getComponentInstance()
	 * @see #getStateSpaceModelDefinition()
	 * @generated
	 */
	EReference getStateSpaceModelDefinition_ComponentInstance();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getTriggers <em>Triggers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Triggers</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getTriggers()
	 * @see #getStateSpaceModelDefinition()
	 * @generated
	 */
	EReference getStateSpaceModelDefinition_Triggers();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Model</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition#getModel()
	 * @see #getStateSpaceModelDefinition()
	 * @generated
	 */
	EAttribute getStateSpaceModelDefinition_Model();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger <em>Time Trigger</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Time Trigger</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger
	 * @generated
	 */
	EClass getTimeTrigger();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger#getPeriod <em>Period</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Period</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger#getPeriod()
	 * @see #getTimeTrigger()
	 * @generated
	 */
	EAttribute getTimeTrigger_Period();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger#getOffset <em>Offset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Offset</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger#getOffset()
	 * @see #getTimeTrigger()
	 * @generated
	 */
	EAttribute getTimeTrigger_Offset();

	/**
	 * Returns the meta object for enum '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection <em>Parameter Direction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Parameter Direction</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection
	 * @generated
	 */
	EEnum getParameterDirection();

	/**
	 * Returns the meta object for data type '{@link java.lang.reflect.Method <em>Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Method</em>'.
	 * @see java.lang.reflect.Method
	 * @model instanceClass="java.lang.reflect.Method"
	 * @generated
	 */
	EDataType getMethod();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager <em>Knowledge Manager</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Knowledge Manager</em>'.
	 * @see cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager
	 * @model instanceClass="cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager"
	 * @generated
	 */
	EDataType getKnowledgeManager();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistry <em>Shadow Knowledge Manager Registry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Shadow Knowledge Manager Registry</em>'.
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistry
	 * @model instanceClass="cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistry"
	 * @generated
	 */
	EDataType getShadowKnowledgeManagerRegistry();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.deeco.network.CommunicationBoundaryPredicate <em>Communication Boundary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Communication Boundary</em>'.
	 * @see cz.cuni.mff.d3s.deeco.network.CommunicationBoundaryPredicate
	 * @model instanceClass="cz.cuni.mff.d3s.deeco.network.CommunicationBoundaryPredicate"
	 * @generated
	 */
	EDataType getCommunicationBoundary();

	/**
	 * Returns the meta object for enum '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType <em>Metadata Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Metadata Type</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType
	 * @generated
	 */
	EEnum getMetadataType();

	/**
	 * Returns the meta object for enum '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComparisonType <em>Comparison Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Comparison Type</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComparisonType
	 * @generated
	 */
	EEnum getComparisonType();

	/**
	 * Returns the meta object for enum '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeState <em>Mode State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Mode State</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ModeState
	 * @generated
	 */
	EEnum getModeState();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ModelInterface <em>Model Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Model Type</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ModelInterface
	 * @model instanceClass="cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ModelInterface"
	 * @generated
	 */
	EDataType getModelType();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder <em>Inaccurate Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Inaccurate Value</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder
	 * @model instanceClass="cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder"
	 * @generated
	 */
	EDataType getInaccurateValue();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	RuntimeMetadataFactory getRuntimeMetadataFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.TriggerImpl <em>Trigger</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.TriggerImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getTrigger()
		 * @generated
		 */
		EClass TRIGGER = eINSTANCE.getTrigger();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeChangeTriggerImpl <em>Knowledge Change Trigger</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeChangeTriggerImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getKnowledgeChangeTrigger()
		 * @generated
		 */
		EClass KNOWLEDGE_CHANGE_TRIGGER = eINSTANCE.getKnowledgeChangeTrigger();

		/**
		 * The meta object literal for the '<em><b>Knowledge Path</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference KNOWLEDGE_CHANGE_TRIGGER__KNOWLEDGE_PATH = eINSTANCE.getKnowledgeChangeTrigger_KnowledgePath();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgePathImpl <em>Knowledge Path</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgePathImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getKnowledgePath()
		 * @generated
		 */
		EClass KNOWLEDGE_PATH = eINSTANCE.getKnowledgePath();

		/**
		 * The meta object literal for the '<em><b>Nodes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference KNOWLEDGE_PATH__NODES = eINSTANCE.getKnowledgePath_Nodes();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeImpl <em>Path Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getPathNode()
		 * @generated
		 */
		EClass PATH_NODE = eINSTANCE.getPathNode();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeFieldImpl <em>Path Node Field</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeFieldImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getPathNodeField()
		 * @generated
		 */
		EClass PATH_NODE_FIELD = eINSTANCE.getPathNodeField();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATH_NODE_FIELD__NAME = eINSTANCE.getPathNodeField_Name();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeMapKeyImpl <em>Path Node Map Key</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeMapKeyImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getPathNodeMapKey()
		 * @generated
		 */
		EClass PATH_NODE_MAP_KEY = eINSTANCE.getPathNodeMapKey();

		/**
		 * The meta object literal for the '<em><b>Key Path</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PATH_NODE_MAP_KEY__KEY_PATH = eINSTANCE.getPathNodeMapKey_KeyPath();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataImpl <em>Runtime Metadata</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getRuntimeMetadata()
		 * @generated
		 */
		EClass RUNTIME_METADATA = eINSTANCE.getRuntimeMetadata();

		/**
		 * The meta object literal for the '<em><b>Ensemble Definitions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RUNTIME_METADATA__ENSEMBLE_DEFINITIONS = eINSTANCE.getRuntimeMetadata_EnsembleDefinitions();

		/**
		 * The meta object literal for the '<em><b>Component Instances</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RUNTIME_METADATA__COMPONENT_INSTANCES = eINSTANCE.getRuntimeMetadata_ComponentInstances();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl <em>Component Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getComponentInstance()
		 * @generated
		 */
		EClass COMPONENT_INSTANCE = eINSTANCE.getComponentInstance();

		/**
		 * The meta object literal for the '<em><b>Component Processes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_INSTANCE__COMPONENT_PROCESSES = eINSTANCE.getComponentInstance_ComponentProcesses();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_INSTANCE__NAME = eINSTANCE.getComponentInstance_Name();

		/**
		 * The meta object literal for the '<em><b>Knowledge Manager</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_INSTANCE__KNOWLEDGE_MANAGER = eINSTANCE.getComponentInstance_KnowledgeManager();

		/**
		 * The meta object literal for the '<em><b>Shadow Knowledge Manager Registry</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_INSTANCE__SHADOW_KNOWLEDGE_MANAGER_REGISTRY = eINSTANCE.getComponentInstance_ShadowKnowledgeManagerRegistry();

		/**
		 * The meta object literal for the '<em><b>Ensemble Controllers</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_INSTANCE__ENSEMBLE_CONTROLLERS = eINSTANCE.getComponentInstance_EnsembleControllers();

		/**
		 * The meta object literal for the '<em><b>Internal Data</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_INSTANCE__INTERNAL_DATA = eINSTANCE.getComponentInstance_InternalData();

		/**
		 * The meta object literal for the '<em><b>State Space Models</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_INSTANCE__STATE_SPACE_MODELS = eINSTANCE.getComponentInstance_StateSpaceModels();

		/**
		 * The meta object literal for the '<em><b>Transitions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_INSTANCE__TRANSITIONS = eINSTANCE.getComponentInstance_Transitions();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.EnsembleDefinitionImpl <em>Ensemble Definition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.EnsembleDefinitionImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getEnsembleDefinition()
		 * @generated
		 */
		EClass ENSEMBLE_DEFINITION = eINSTANCE.getEnsembleDefinition();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENSEMBLE_DEFINITION__NAME = eINSTANCE.getEnsembleDefinition_Name();

		/**
		 * The meta object literal for the '<em><b>Membership</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENSEMBLE_DEFINITION__MEMBERSHIP = eINSTANCE.getEnsembleDefinition_Membership();

		/**
		 * The meta object literal for the '<em><b>Knowledge Exchange</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENSEMBLE_DEFINITION__KNOWLEDGE_EXCHANGE = eINSTANCE.getEnsembleDefinition_KnowledgeExchange();

		/**
		 * The meta object literal for the '<em><b>Triggers</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENSEMBLE_DEFINITION__TRIGGERS = eINSTANCE.getEnsembleDefinition_Triggers();

		/**
		 * The meta object literal for the '<em><b>Communication Boundary</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENSEMBLE_DEFINITION__COMMUNICATION_BOUNDARY = eINSTANCE.getEnsembleDefinition_CommunicationBoundary();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ConditionImpl <em>Condition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.ConditionImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getCondition()
		 * @generated
		 */
		EClass CONDITION = eINSTANCE.getCondition();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ExchangeImpl <em>Exchange</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.ExchangeImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getExchange()
		 * @generated
		 */
		EClass EXCHANGE = eINSTANCE.getExchange();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentProcessImpl <em>Component Process</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentProcessImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getComponentProcess()
		 * @generated
		 */
		EClass COMPONENT_PROCESS = eINSTANCE.getComponentProcess();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_PROCESS__NAME = eINSTANCE.getComponentProcess_Name();

		/**
		 * The meta object literal for the '<em><b>Component Instance</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_PROCESS__COMPONENT_INSTANCE = eINSTANCE.getComponentProcess_ComponentInstance();

		/**
		 * The meta object literal for the '<em><b>Is Active</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_PROCESS__IS_ACTIVE = eINSTANCE.getComponentProcess_IsActive();

		/**
		 * The meta object literal for the '<em><b>Triggers</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_PROCESS__TRIGGERS = eINSTANCE.getComponentProcess_Triggers();

		/**
		 * The meta object literal for the '<em><b>State</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_PROCESS__STATE = eINSTANCE.getComponentProcess_State();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ParameterImpl <em>Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.ParameterImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getParameter()
		 * @generated
		 */
		EClass PARAMETER = eINSTANCE.getParameter();

		/**
		 * The meta object literal for the '<em><b>Direction</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__DIRECTION = eINSTANCE.getParameter_Direction();

		/**
		 * The meta object literal for the '<em><b>Knowledge Path</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETER__KNOWLEDGE_PATH = eINSTANCE.getParameter_KnowledgePath();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__TYPE = eINSTANCE.getParameter_Type();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.InvocableImpl <em>Invocable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.InvocableImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getInvocable()
		 * @generated
		 */
		EClass INVOCABLE = eINSTANCE.getInvocable();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INVOCABLE__PARAMETERS = eINSTANCE.getInvocable_Parameters();

		/**
		 * The meta object literal for the '<em><b>Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INVOCABLE__METHOD = eINSTANCE.getInvocable_Method();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.EnsembleControllerImpl <em>Ensemble Controller</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.EnsembleControllerImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getEnsembleController()
		 * @generated
		 */
		EClass ENSEMBLE_CONTROLLER = eINSTANCE.getEnsembleController();

		/**
		 * The meta object literal for the '<em><b>Component Instance</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENSEMBLE_CONTROLLER__COMPONENT_INSTANCE = eINSTANCE.getEnsembleController_ComponentInstance();

		/**
		 * The meta object literal for the '<em><b>Ensemble Definition</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENSEMBLE_CONTROLLER__ENSEMBLE_DEFINITION = eINSTANCE.getEnsembleController_EnsembleDefinition();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeCoordinatorImpl <em>Path Node Coordinator</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeCoordinatorImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getPathNodeCoordinator()
		 * @generated
		 */
		EClass PATH_NODE_COORDINATOR = eINSTANCE.getPathNodeCoordinator();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeMemberImpl <em>Path Node Member</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeMemberImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getPathNodeMember()
		 * @generated
		 */
		EClass PATH_NODE_MEMBER = eINSTANCE.getPathNodeMember();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeComponentIdImpl <em>Path Node Component Id</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeComponentIdImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getPathNodeComponentId()
		 * @generated
		 */
		EClass PATH_NODE_COMPONENT_ID = eINSTANCE.getPathNodeComponentId();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.StringToObjectMapImpl <em>String To Object Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.StringToObjectMapImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getStringToObjectMap()
		 * @generated
		 */
		EClass STRING_TO_OBJECT_MAP = eINSTANCE.getStringToObjectMap();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_TO_OBJECT_MAP__KEY = eINSTANCE.getStringToObjectMap_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_TO_OBJECT_MAP__VALUE = eINSTANCE.getStringToObjectMap_Value();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.StateSpaceModelDefinitionImpl <em>State Space Model Definition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.StateSpaceModelDefinitionImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getStateSpaceModelDefinition()
		 * @generated
		 */
		EClass STATE_SPACE_MODEL_DEFINITION = eINSTANCE.getStateSpaceModelDefinition();

		/**
		 * The meta object literal for the '<em><b>In States</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATE_SPACE_MODEL_DEFINITION__IN_STATES = eINSTANCE.getStateSpaceModelDefinition_InStates();

		/**
		 * The meta object literal for the '<em><b>Is Active</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATE_SPACE_MODEL_DEFINITION__IS_ACTIVE = eINSTANCE.getStateSpaceModelDefinition_IsActive();

		/**
		 * The meta object literal for the '<em><b>Derivation States</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATE_SPACE_MODEL_DEFINITION__DERIVATION_STATES = eINSTANCE.getStateSpaceModelDefinition_DerivationStates();

		/**
		 * The meta object literal for the '<em><b>Model Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATE_SPACE_MODEL_DEFINITION__MODEL_VALUE = eINSTANCE.getStateSpaceModelDefinition_ModelValue();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueUnchangeTriggerImpl <em>Knowledge Value Unchange Trigger</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueUnchangeTriggerImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getKnowledgeValueUnchangeTrigger()
		 * @generated
		 */
		EClass KNOWLEDGE_VALUE_UNCHANGE_TRIGGER = eINSTANCE.getKnowledgeValueUnchangeTrigger();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__VALUE = eINSTANCE.getKnowledgeValueUnchangeTrigger_Value();

		/**
		 * The meta object literal for the '<em><b>Comparison</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__COMPARISON = eINSTANCE.getKnowledgeValueUnchangeTrigger_Comparison();

		/**
		 * The meta object literal for the '<em><b>Meta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KNOWLEDGE_VALUE_UNCHANGE_TRIGGER__META = eINSTANCE.getKnowledgeValueUnchangeTrigger_Meta();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueChangeTriggerImpl <em>Knowledge Value Change Trigger</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgeValueChangeTriggerImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getKnowledgeValueChangeTrigger()
		 * @generated
		 */
		EClass KNOWLEDGE_VALUE_CHANGE_TRIGGER = eINSTANCE.getKnowledgeValueChangeTrigger();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KNOWLEDGE_VALUE_CHANGE_TRIGGER__VALUE = eINSTANCE.getKnowledgeValueChangeTrigger_Value();

		/**
		 * The meta object literal for the '<em><b>Comparison</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KNOWLEDGE_VALUE_CHANGE_TRIGGER__COMPARISON = eINSTANCE.getKnowledgeValueChangeTrigger_Comparison();

		/**
		 * The meta object literal for the '<em><b>Meta</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KNOWLEDGE_VALUE_CHANGE_TRIGGER__META = eINSTANCE.getKnowledgeValueChangeTrigger_Meta();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.TransitionDefinitionImpl <em>Transition Definition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.TransitionDefinitionImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getTransitionDefinition()
		 * @generated
		 */
		EClass TRANSITION_DEFINITION = eINSTANCE.getTransitionDefinition();

		/**
		 * The meta object literal for the '<em><b>From Mode</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSITION_DEFINITION__FROM_MODE = eINSTANCE.getTransitionDefinition_FromMode();

		/**
		 * The meta object literal for the '<em><b>To Mode</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSITION_DEFINITION__TO_MODE = eINSTANCE.getTransitionDefinition_ToMode();

		/**
		 * The meta object literal for the '<em><b>Init</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSITION_DEFINITION__INIT = eINSTANCE.getTransitionDefinition_Init();

		/**
		 * The meta object literal for the '<em><b>Trigger</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSITION_DEFINITION__TRIGGER = eINSTANCE.getTransitionDefinition_Trigger();

		/**
		 * The meta object literal for the '<em><b>Component Instance</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATE_SPACE_MODEL_DEFINITION__COMPONENT_INSTANCE = eINSTANCE.getStateSpaceModelDefinition_ComponentInstance();

		/**
		 * The meta object literal for the '<em><b>Triggers</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATE_SPACE_MODEL_DEFINITION__TRIGGERS = eINSTANCE.getStateSpaceModelDefinition_Triggers();

		/**
		 * The meta object literal for the '<em><b>Model</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATE_SPACE_MODEL_DEFINITION__MODEL = eINSTANCE.getStateSpaceModelDefinition_Model();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.TimeTriggerImpl <em>Time Trigger</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.TimeTriggerImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getTimeTrigger()
		 * @generated
		 */
		EClass TIME_TRIGGER = eINSTANCE.getTimeTrigger();

		/**
		 * The meta object literal for the '<em><b>Period</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIME_TRIGGER__PERIOD = eINSTANCE.getTimeTrigger_Period();

		/**
		 * The meta object literal for the '<em><b>Offset</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIME_TRIGGER__OFFSET = eINSTANCE.getTimeTrigger_Offset();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection <em>Parameter Direction</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getParameterDirection()
		 * @generated
		 */
		EEnum PARAMETER_DIRECTION = eINSTANCE.getParameterDirection();

		/**
		 * The meta object literal for the '<em>Method</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.reflect.Method
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getMethod()
		 * @generated
		 */
		EDataType METHOD = eINSTANCE.getMethod();

		/**
		 * The meta object literal for the '<em>Knowledge Manager</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getKnowledgeManager()
		 * @generated
		 */
		EDataType KNOWLEDGE_MANAGER = eINSTANCE.getKnowledgeManager();

		/**
		 * The meta object literal for the '<em>Shadow Knowledge Manager Registry</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistry
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getShadowKnowledgeManagerRegistry()
		 * @generated
		 */
		EDataType SHADOW_KNOWLEDGE_MANAGER_REGISTRY = eINSTANCE.getShadowKnowledgeManagerRegistry();

		/**
		 * The meta object literal for the '<em>Communication Boundary</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.network.CommunicationBoundaryPredicate
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getCommunicationBoundary()
		 * @generated
		 */
		EDataType COMMUNICATION_BOUNDARY = eINSTANCE.getCommunicationBoundary();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType <em>Metadata Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getMetadataType()
		 * @generated
		 */
		EEnum METADATA_TYPE = eINSTANCE.getMetadataType();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComparisonType <em>Comparison Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComparisonType
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getComparisonType()
		 * @generated
		 */
		EEnum COMPARISON_TYPE = eINSTANCE.getComparisonType();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ModeState <em>Mode State</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ModeState
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getModeState()
		 * @generated
		 */
		EEnum MODE_STATE = eINSTANCE.getModeState();

		/**
		 * The meta object literal for the '<em>Model Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ModelInterface
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getModelType()
		 * @generated
		 */
		EDataType MODEL_TYPE = eINSTANCE.getModelType();

		/**
		 * The meta object literal for the '<em>Inaccurate Value</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getInaccurateValue()
		 * @generated
		 */
		EDataType INACCURATE_VALUE = eINSTANCE.getInaccurateValue();

	}

} //RuntimeMetadataPackage
