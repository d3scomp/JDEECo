/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.meta;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
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
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.SchedulingSpecificationImpl <em>Scheduling Specification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.SchedulingSpecificationImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getSchedulingSpecification()
	 * @generated
	 */
	int SCHEDULING_SPECIFICATION = 0;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULING_SPECIFICATION__TRIGGERS = 0;

	/**
	 * The feature id for the '<em><b>Period</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULING_SPECIFICATION__PERIOD = 1;

	/**
	 * The number of structural features of the '<em>Scheduling Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULING_SPECIFICATION_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Scheduling Specification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULING_SPECIFICATION_OPERATION_COUNT = 0;

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
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgePathImpl <em>Knowledge Path</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgePathImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getKnowledgePath()
	 * @generated
	 */
	int KNOWLEDGE_PATH = 3;

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
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeImpl <em>Path Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getPathNode()
	 * @generated
	 */
	int PATH_NODE = 4;

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
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeFieldImpl <em>Path Node Field</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeFieldImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getPathNodeField()
	 * @generated
	 */
	int PATH_NODE_FIELD = 5;

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
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeMapKeyImpl <em>Path Node Map Key</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeMapKeyImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getPathNodeMapKey()
	 * @generated
	 */
	int PATH_NODE_MAP_KEY = 6;

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
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataImpl <em>Runtime Metadata</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getRuntimeMetadata()
	 * @generated
	 */
	int RUNTIME_METADATA = 7;

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
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl <em>Component Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.ComponentInstanceImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getComponentInstance()
	 * @generated
	 */
	int COMPONENT_INSTANCE = 8;

	/**
	 * The feature id for the '<em><b>Processes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__PROCESSES = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__ID = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__NAME = 2;

	/**
	 * The feature id for the '<em><b>Knowledge Manager</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__KNOWLEDGE_MANAGER = 3;

	/**
	 * The feature id for the '<em><b>Other Knowledge Managers Access</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__OTHER_KNOWLEDGE_MANAGERS_ACCESS = 4;

	/**
	 * The feature id for the '<em><b>Ensembling Controllers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__ENSEMBLING_CONTROLLERS = 5;

	/**
	 * The number of structural features of the '<em>Component Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Component Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE_OPERATION_COUNT = 0;

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
	 * The feature id for the '<em><b>Scheduling Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_DEFINITION__SCHEDULING_SPECIFICATION = 3;

	/**
	 * The number of structural features of the '<em>Ensemble Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_DEFINITION_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Ensemble Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_DEFINITION_OPERATION_COUNT = 0;

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
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ConditionImpl <em>Condition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.ConditionImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getCondition()
	 * @generated
	 */
	int CONDITION = 10;

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
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ExchangeImpl <em>Exchange</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.ExchangeImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getExchange()
	 * @generated
	 */
	int EXCHANGE = 11;

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
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ProcessImpl <em>Process</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.ProcessImpl
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getProcess()
	 * @generated
	 */
	int PROCESS = 12;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS__PARAMETERS = INVOCABLE__PARAMETERS;

	/**
	 * The feature id for the '<em><b>Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS__METHOD = INVOCABLE__METHOD;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS__NAME = INVOCABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Scheduling Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS__SCHEDULING_SPECIFICATION = INVOCABLE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Component Instance</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS__COMPONENT_INSTANCE = INVOCABLE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Process</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_FEATURE_COUNT = INVOCABLE_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Process</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_OPERATION_COUNT = INVOCABLE_OPERATION_COUNT + 0;

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
	 * The feature id for the '<em><b>Direction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__DIRECTION = 0;

	/**
	 * The feature id for the '<em><b>Knowledge Path</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__KNOWLEDGE_PATH = 1;

	/**
	 * The number of structural features of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_OPERATION_COUNT = 0;

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
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection <em>Parameter Direction</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getParameterDirection()
	 * @generated
	 */
	int PARAMETER_DIRECTION = 16;

	/**
	 * The meta object id for the '<em>Method</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.reflect.Method
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getMethod()
	 * @generated
	 */
	int METHOD = 17;

	/**
	 * The meta object id for the '<em>Knowledge Manager</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getKnowledgeManager()
	 * @generated
	 */
	int KNOWLEDGE_MANAGER = 18;

	/**
	 * The meta object id for the '<em>Other Knowledge Managers Access</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Object
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getOtherKnowledgeManagersAccess()
	 * @generated
	 */
	int OTHER_KNOWLEDGE_MANAGERS_ACCESS = 19;


	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification <em>Scheduling Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scheduling Specification</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification
	 * @generated
	 */
	EClass getSchedulingSpecification();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification#getTriggers <em>Triggers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Triggers</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification#getTriggers()
	 * @see #getSchedulingSpecification()
	 * @generated
	 */
	EReference getSchedulingSpecification_Triggers();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification#getPeriod <em>Period</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Period</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification#getPeriod()
	 * @see #getSchedulingSpecification()
	 * @generated
	 */
	EAttribute getSchedulingSpecification_Period();

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
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getProcesses <em>Processes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Processes</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getProcesses()
	 * @see #getComponentInstance()
	 * @generated
	 */
	EReference getComponentInstance_Processes();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getId()
	 * @see #getComponentInstance()
	 * @generated
	 */
	EAttribute getComponentInstance_Id();

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
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getOtherKnowledgeManagersAccess <em>Other Knowledge Managers Access</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Other Knowledge Managers Access</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getOtherKnowledgeManagersAccess()
	 * @see #getComponentInstance()
	 * @generated
	 */
	EAttribute getComponentInstance_OtherKnowledgeManagersAccess();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getEnsemblingControllers <em>Ensembling Controllers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Ensembling Controllers</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance#getEnsemblingControllers()
	 * @see #getComponentInstance()
	 * @generated
	 */
	EReference getComponentInstance_EnsemblingControllers();

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
	 * Returns the meta object for the containment reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getSchedulingSpecification <em>Scheduling Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Scheduling Specification</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition#getSchedulingSpecification()
	 * @see #getEnsembleDefinition()
	 * @generated
	 */
	EReference getEnsembleDefinition_SchedulingSpecification();

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
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Process <em>Process</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Process</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Process
	 * @generated
	 */
	EClass getProcess();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Process#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Process#getName()
	 * @see #getProcess()
	 * @generated
	 */
	EAttribute getProcess_Name();

	/**
	 * Returns the meta object for the containment reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Process#getSchedulingSpecification <em>Scheduling Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Scheduling Specification</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Process#getSchedulingSpecification()
	 * @see #getProcess()
	 * @generated
	 */
	EReference getProcess_SchedulingSpecification();

	/**
	 * Returns the meta object for the container reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Process#getComponentInstance <em>Component Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Component Instance</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Process#getComponentInstance()
	 * @see #getProcess()
	 * @generated
	 */
	EReference getProcess_ComponentInstance();

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
	 * Returns the meta object for the reference '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getKnowledgePath <em>Knowledge Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Knowledge Path</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getKnowledgePath()
	 * @see #getParameter()
	 * @generated
	 */
	EReference getParameter_KnowledgePath();

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
	 * Returns the meta object for data type '{@link java.lang.Object <em>Other Knowledge Managers Access</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Other Knowledge Managers Access</em>'.
	 * @see java.lang.Object
	 * @model instanceClass="java.lang.Object"
	 * @generated
	 */
	EDataType getOtherKnowledgeManagersAccess();

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
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.SchedulingSpecificationImpl <em>Scheduling Specification</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.SchedulingSpecificationImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getSchedulingSpecification()
		 * @generated
		 */
		EClass SCHEDULING_SPECIFICATION = eINSTANCE.getSchedulingSpecification();

		/**
		 * The meta object literal for the '<em><b>Triggers</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULING_SPECIFICATION__TRIGGERS = eINSTANCE.getSchedulingSpecification_Triggers();

		/**
		 * The meta object literal for the '<em><b>Period</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCHEDULING_SPECIFICATION__PERIOD = eINSTANCE.getSchedulingSpecification_Period();

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
		 * The meta object literal for the '<em><b>Processes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_INSTANCE__PROCESSES = eINSTANCE.getComponentInstance_Processes();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_INSTANCE__ID = eINSTANCE.getComponentInstance_Id();

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
		 * The meta object literal for the '<em><b>Other Knowledge Managers Access</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_INSTANCE__OTHER_KNOWLEDGE_MANAGERS_ACCESS = eINSTANCE.getComponentInstance_OtherKnowledgeManagersAccess();

		/**
		 * The meta object literal for the '<em><b>Ensembling Controllers</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_INSTANCE__ENSEMBLING_CONTROLLERS = eINSTANCE.getComponentInstance_EnsemblingControllers();

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
		 * The meta object literal for the '<em><b>Scheduling Specification</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENSEMBLE_DEFINITION__SCHEDULING_SPECIFICATION = eINSTANCE.getEnsembleDefinition_SchedulingSpecification();

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
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.runtime.impl.ProcessImpl <em>Process</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.ProcessImpl
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getProcess()
		 * @generated
		 */
		EClass PROCESS = eINSTANCE.getProcess();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS__NAME = eINSTANCE.getProcess_Name();

		/**
		 * The meta object literal for the '<em><b>Scheduling Specification</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROCESS__SCHEDULING_SPECIFICATION = eINSTANCE.getProcess_SchedulingSpecification();

		/**
		 * The meta object literal for the '<em><b>Component Instance</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROCESS__COMPONENT_INSTANCE = eINSTANCE.getProcess_ComponentInstance();

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
		 * The meta object literal for the '<em><b>Knowledge Path</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETER__KNOWLEDGE_PATH = eINSTANCE.getParameter_KnowledgePath();

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
		 * The meta object literal for the '<em>Other Knowledge Managers Access</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Object
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.impl.RuntimeMetadataPackageImpl#getOtherKnowledgeManagersAccess()
		 * @generated
		 */
		EDataType OTHER_KNOWLEDGE_MANAGERS_ACCESS = eINSTANCE.getOtherKnowledgeManagersAccess();

	}

} //RuntimeMetadataPackage
