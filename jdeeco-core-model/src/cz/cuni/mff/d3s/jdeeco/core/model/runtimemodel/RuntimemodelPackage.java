/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelFactory
 * @model kind="package"
 * @generated
 */
public interface RuntimemodelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "runtimemodel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://runtimemetadata/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "cz.cuni.mff.d3s.jdeeco.core.runtimemodel";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	RuntimemodelPackage eINSTANCE = cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl.init();

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ComponentImpl <em>Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ComponentImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getComponent()
	 * @generated
	 */
	int COMPONENT = 0;

	/**
	 * The feature id for the '<em><b>Knowledge</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT__KNOWLEDGE = 0;

	/**
	 * The feature id for the '<em><b>Processes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT__PROCESSES = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT__NAME = 2;

	/**
	 * The number of structural features of the '<em>Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeTypeOwnerImpl <em>Knowledge Type Owner</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeTypeOwnerImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getKnowledgeTypeOwner()
	 * @generated
	 */
	int KNOWLEDGE_TYPE_OWNER = 28;

	/**
	 * The feature id for the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_TYPE_OWNER__TYPE = 0;

	/**
	 * The number of structural features of the '<em>Knowledge Type Owner</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_TYPE_OWNER_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeReferenceImpl <em>Knowledge Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeReferenceImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getKnowledgeReference()
	 * @generated
	 */
	int KNOWLEDGE_REFERENCE = 22;

	/**
	 * The feature id for the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_REFERENCE__TYPE = KNOWLEDGE_TYPE_OWNER__TYPE;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_REFERENCE__NAME = KNOWLEDGE_TYPE_OWNER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Knowledge Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_REFERENCE_FEATURE_COUNT = KNOWLEDGE_TYPE_OWNER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeDefinitionImpl <em>Knowledge Definition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeDefinitionImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getKnowledgeDefinition()
	 * @generated
	 */
	int KNOWLEDGE_DEFINITION = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_DEFINITION__TYPE = KNOWLEDGE_REFERENCE__TYPE;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_DEFINITION__NAME = KNOWLEDGE_REFERENCE__NAME;

	/**
	 * The feature id for the '<em><b>Is Local</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_DEFINITION__IS_LOCAL = KNOWLEDGE_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Knowledge Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_DEFINITION_FEATURE_COUNT = KNOWLEDGE_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeTypeImpl <em>Knowledge Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeTypeImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getKnowledgeType()
	 * @generated
	 */
	int KNOWLEDGE_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Is Structured</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_TYPE__IS_STRUCTURED = 0;

	/**
	 * The feature id for the '<em><b>Is Wrapper</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_TYPE__IS_WRAPPER = 1;

	/**
	 * The feature id for the '<em><b>Clazz</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_TYPE__CLAZZ = 2;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_TYPE__OWNER = 3;

	/**
	 * The number of structural features of the '<em>Knowledge Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_TYPE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ParameterizedMethodImpl <em>Parameterized Method</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ParameterizedMethodImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getParameterizedMethod()
	 * @generated
	 */
	int PARAMETERIZED_METHOD = 14;

	/**
	 * The feature id for the '<em><b>Declaring Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERIZED_METHOD__DECLARING_CLASS = 0;

	/**
	 * The feature id for the '<em><b>Method Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERIZED_METHOD__METHOD_NAME = 1;

	/**
	 * The feature id for the '<em><b>Formal Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERIZED_METHOD__FORMAL_PARAMETERS = 2;

	/**
	 * The feature id for the '<em><b>In Parameters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERIZED_METHOD__IN_PARAMETERS = 3;

	/**
	 * The feature id for the '<em><b>Out Parameters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERIZED_METHOD__OUT_PARAMETERS = 4;

	/**
	 * The feature id for the '<em><b>In Out Parameters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERIZED_METHOD__IN_OUT_PARAMETERS = 5;

	/**
	 * The number of structural features of the '<em>Parameterized Method</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERIZED_METHOD_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ProcessImpl <em>Process</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ProcessImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getProcess()
	 * @generated
	 */
	int PROCESS = 3;

	/**
	 * The feature id for the '<em><b>Declaring Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS__DECLARING_CLASS = PARAMETERIZED_METHOD__DECLARING_CLASS;

	/**
	 * The feature id for the '<em><b>Method Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS__METHOD_NAME = PARAMETERIZED_METHOD__METHOD_NAME;

	/**
	 * The feature id for the '<em><b>Formal Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS__FORMAL_PARAMETERS = PARAMETERIZED_METHOD__FORMAL_PARAMETERS;

	/**
	 * The feature id for the '<em><b>In Parameters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS__IN_PARAMETERS = PARAMETERIZED_METHOD__IN_PARAMETERS;

	/**
	 * The feature id for the '<em><b>Out Parameters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS__OUT_PARAMETERS = PARAMETERIZED_METHOD__OUT_PARAMETERS;

	/**
	 * The feature id for the '<em><b>In Out Parameters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS__IN_OUT_PARAMETERS = PARAMETERIZED_METHOD__IN_OUT_PARAMETERS;

	/**
	 * The feature id for the '<em><b>Scheduling</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS__SCHEDULING = PARAMETERIZED_METHOD_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS__NAME = PARAMETERIZED_METHOD_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Component</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS__COMPONENT = PARAMETERIZED_METHOD_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Process</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_FEATURE_COUNT = PARAMETERIZED_METHOD_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Scheduling <em>Scheduling</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Scheduling
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getScheduling()
	 * @generated
	 */
	int SCHEDULING = 4;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULING__OWNER = 0;

	/**
	 * The number of structural features of the '<em>Scheduling</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULING_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.StructuredKnowledgeValueTypeImpl <em>Structured Knowledge Value Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.StructuredKnowledgeValueTypeImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getStructuredKnowledgeValueType()
	 * @generated
	 */
	int STRUCTURED_KNOWLEDGE_VALUE_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Is Structured</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURED_KNOWLEDGE_VALUE_TYPE__IS_STRUCTURED = KNOWLEDGE_TYPE__IS_STRUCTURED;

	/**
	 * The feature id for the '<em><b>Is Wrapper</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURED_KNOWLEDGE_VALUE_TYPE__IS_WRAPPER = KNOWLEDGE_TYPE__IS_WRAPPER;

	/**
	 * The feature id for the '<em><b>Clazz</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURED_KNOWLEDGE_VALUE_TYPE__CLAZZ = KNOWLEDGE_TYPE__CLAZZ;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURED_KNOWLEDGE_VALUE_TYPE__OWNER = KNOWLEDGE_TYPE__OWNER;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURED_KNOWLEDGE_VALUE_TYPE__CHILDREN = KNOWLEDGE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Structured Knowledge Value Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURED_KNOWLEDGE_VALUE_TYPE_FEATURE_COUNT = KNOWLEDGE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ListValueTypeImpl <em>List Value Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ListValueTypeImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getListValueType()
	 * @generated
	 */
	int LIST_VALUE_TYPE = 6;

	/**
	 * The feature id for the '<em><b>Is Structured</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIST_VALUE_TYPE__IS_STRUCTURED = KNOWLEDGE_TYPE__IS_STRUCTURED;

	/**
	 * The feature id for the '<em><b>Is Wrapper</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIST_VALUE_TYPE__IS_WRAPPER = KNOWLEDGE_TYPE__IS_WRAPPER;

	/**
	 * The feature id for the '<em><b>Clazz</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIST_VALUE_TYPE__CLAZZ = KNOWLEDGE_TYPE__CLAZZ;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIST_VALUE_TYPE__OWNER = KNOWLEDGE_TYPE__OWNER;

	/**
	 * The feature id for the '<em><b>Type Parameter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIST_VALUE_TYPE__TYPE_PARAMETER = KNOWLEDGE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>List Value Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIST_VALUE_TYPE_FEATURE_COUNT = KNOWLEDGE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MapValueTypeImpl <em>Map Value Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MapValueTypeImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getMapValueType()
	 * @generated
	 */
	int MAP_VALUE_TYPE = 7;

	/**
	 * The feature id for the '<em><b>Is Structured</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_VALUE_TYPE__IS_STRUCTURED = KNOWLEDGE_TYPE__IS_STRUCTURED;

	/**
	 * The feature id for the '<em><b>Is Wrapper</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_VALUE_TYPE__IS_WRAPPER = KNOWLEDGE_TYPE__IS_WRAPPER;

	/**
	 * The feature id for the '<em><b>Clazz</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_VALUE_TYPE__CLAZZ = KNOWLEDGE_TYPE__CLAZZ;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_VALUE_TYPE__OWNER = KNOWLEDGE_TYPE__OWNER;

	/**
	 * The feature id for the '<em><b>Key Type Parameter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_VALUE_TYPE__KEY_TYPE_PARAMETER = KNOWLEDGE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Value Type Parameter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_VALUE_TYPE__VALUE_TYPE_PARAMETER = KNOWLEDGE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Map Value Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_VALUE_TYPE_FEATURE_COUNT = KNOWLEDGE_TYPE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.UnstructuredValueTypeImpl <em>Unstructured Value Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.UnstructuredValueTypeImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getUnstructuredValueType()
	 * @generated
	 */
	int UNSTRUCTURED_VALUE_TYPE = 8;

	/**
	 * The feature id for the '<em><b>Is Structured</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNSTRUCTURED_VALUE_TYPE__IS_STRUCTURED = KNOWLEDGE_TYPE__IS_STRUCTURED;

	/**
	 * The feature id for the '<em><b>Is Wrapper</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNSTRUCTURED_VALUE_TYPE__IS_WRAPPER = KNOWLEDGE_TYPE__IS_WRAPPER;

	/**
	 * The feature id for the '<em><b>Clazz</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNSTRUCTURED_VALUE_TYPE__CLAZZ = KNOWLEDGE_TYPE__CLAZZ;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNSTRUCTURED_VALUE_TYPE__OWNER = KNOWLEDGE_TYPE__OWNER;

	/**
	 * The number of structural features of the '<em>Unstructured Value Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNSTRUCTURED_VALUE_TYPE_FEATURE_COUNT = KNOWLEDGE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ModelImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getModel()
	 * @generated
	 */
	int MODEL = 9;

	/**
	 * The feature id for the '<em><b>Components</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL__COMPONENTS = 0;

	/**
	 * The feature id for the '<em><b>Ensembles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL__ENSEMBLES = 1;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ModelUpdateCommandImpl <em>Model Update Command</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ModelUpdateCommandImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getModelUpdateCommand()
	 * @generated
	 */
	int MODEL_UPDATE_COMMAND = 10;

	/**
	 * The number of structural features of the '<em>Model Update Command</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_UPDATE_COMMAND_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.UpdateKnowledgeStructureCommandImpl <em>Update Knowledge Structure Command</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.UpdateKnowledgeStructureCommandImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getUpdateKnowledgeStructureCommand()
	 * @generated
	 */
	int UPDATE_KNOWLEDGE_STRUCTURE_COMMAND = 11;

	/**
	 * The number of structural features of the '<em>Update Knowledge Structure Command</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATE_KNOWLEDGE_STRUCTURE_COMMAND_FEATURE_COUNT = MODEL_UPDATE_COMMAND_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.AddComponentCommandImpl <em>Add Component Command</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.AddComponentCommandImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getAddComponentCommand()
	 * @generated
	 */
	int ADD_COMPONENT_COMMAND = 12;

	/**
	 * The number of structural features of the '<em>Add Component Command</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADD_COMPONENT_COMMAND_FEATURE_COUNT = MODEL_UPDATE_COMMAND_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MethodParameterImpl <em>Method Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MethodParameterImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getMethodParameter()
	 * @generated
	 */
	int METHOD_PARAMETER = 13;

	/**
	 * The feature id for the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METHOD_PARAMETER__TYPE = KNOWLEDGE_REFERENCE__TYPE;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METHOD_PARAMETER__NAME = KNOWLEDGE_REFERENCE__NAME;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METHOD_PARAMETER__KIND = KNOWLEDGE_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Knowledge Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METHOD_PARAMETER__KNOWLEDGE_PATH = KNOWLEDGE_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METHOD_PARAMETER__INDEX = KNOWLEDGE_REFERENCE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METHOD_PARAMETER__OWNER = KNOWLEDGE_REFERENCE_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Method Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METHOD_PARAMETER_FEATURE_COUNT = KNOWLEDGE_REFERENCE_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.PeriodicSchedulingImpl <em>Periodic Scheduling</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.PeriodicSchedulingImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getPeriodicScheduling()
	 * @generated
	 */
	int PERIODIC_SCHEDULING = 15;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIODIC_SCHEDULING__OWNER = SCHEDULING__OWNER;

	/**
	 * The feature id for the '<em><b>Period</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIODIC_SCHEDULING__PERIOD = SCHEDULING_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Periodic Scheduling</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIODIC_SCHEDULING_FEATURE_COUNT = SCHEDULING_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TriggeredSchedulingImpl <em>Triggered Scheduling</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TriggeredSchedulingImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getTriggeredScheduling()
	 * @generated
	 */
	int TRIGGERED_SCHEDULING = 16;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRIGGERED_SCHEDULING__OWNER = SCHEDULING__OWNER;

	/**
	 * The feature id for the '<em><b>Triggers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRIGGERED_SCHEDULING__TRIGGERS = SCHEDULING_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Triggered Scheduling</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRIGGERED_SCHEDULING_FEATURE_COUNT = SCHEDULING_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TriggerImpl <em>Trigger</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TriggerImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getTrigger()
	 * @generated
	 */
	int TRIGGER = 17;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRIGGER__OWNER = 0;

	/**
	 * The number of structural features of the '<em>Trigger</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRIGGER_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ParameterChangedTriggerImpl <em>Parameter Changed Trigger</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ParameterChangedTriggerImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getParameterChangedTrigger()
	 * @generated
	 */
	int PARAMETER_CHANGED_TRIGGER = 18;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_CHANGED_TRIGGER__OWNER = TRIGGER__OWNER;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_CHANGED_TRIGGER__PARAMETERS = TRIGGER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Parameter Changed Trigger</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_CHANGED_TRIGGER_FEATURE_COUNT = TRIGGER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Schedulable <em>Schedulable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Schedulable
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getSchedulable()
	 * @generated
	 */
	int SCHEDULABLE = 25;

	/**
	 * The feature id for the '<em><b>Scheduling</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULABLE__SCHEDULING = 0;

	/**
	 * The number of structural features of the '<em>Schedulable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCHEDULABLE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.EnsembleImpl <em>Ensemble</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.EnsembleImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getEnsemble()
	 * @generated
	 */
	int ENSEMBLE = 19;

	/**
	 * The feature id for the '<em><b>Scheduling</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE__SCHEDULING = SCHEDULABLE__SCHEDULING;

	/**
	 * The feature id for the '<em><b>Membership</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE__MEMBERSHIP = SCHEDULABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Knowledge Exchange</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE__KNOWLEDGE_EXCHANGE = SCHEDULABLE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Ensemble</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_FEATURE_COUNT = SCHEDULABLE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TopLevelKnowledgeDefinitionImpl <em>Top Level Knowledge Definition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TopLevelKnowledgeDefinitionImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getTopLevelKnowledgeDefinition()
	 * @generated
	 */
	int TOP_LEVEL_KNOWLEDGE_DEFINITION = 20;

	/**
	 * The feature id for the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOP_LEVEL_KNOWLEDGE_DEFINITION__TYPE = KNOWLEDGE_DEFINITION__TYPE;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOP_LEVEL_KNOWLEDGE_DEFINITION__NAME = KNOWLEDGE_DEFINITION__NAME;

	/**
	 * The feature id for the '<em><b>Is Local</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOP_LEVEL_KNOWLEDGE_DEFINITION__IS_LOCAL = KNOWLEDGE_DEFINITION__IS_LOCAL;

	/**
	 * The feature id for the '<em><b>Component</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOP_LEVEL_KNOWLEDGE_DEFINITION__COMPONENT = KNOWLEDGE_DEFINITION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Top Level Knowledge Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOP_LEVEL_KNOWLEDGE_DEFINITION_FEATURE_COUNT = KNOWLEDGE_DEFINITION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.NestedKnowledgeDefinitionImpl <em>Nested Knowledge Definition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.NestedKnowledgeDefinitionImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getNestedKnowledgeDefinition()
	 * @generated
	 */
	int NESTED_KNOWLEDGE_DEFINITION = 21;

	/**
	 * The feature id for the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NESTED_KNOWLEDGE_DEFINITION__TYPE = KNOWLEDGE_DEFINITION__TYPE;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NESTED_KNOWLEDGE_DEFINITION__NAME = KNOWLEDGE_DEFINITION__NAME;

	/**
	 * The feature id for the '<em><b>Is Local</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NESTED_KNOWLEDGE_DEFINITION__IS_LOCAL = KNOWLEDGE_DEFINITION__IS_LOCAL;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NESTED_KNOWLEDGE_DEFINITION__PARENT = KNOWLEDGE_DEFINITION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Nested Knowledge Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NESTED_KNOWLEDGE_DEFINITION_FEATURE_COUNT = KNOWLEDGE_DEFINITION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MembershipConditionImpl <em>Membership Condition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MembershipConditionImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getMembershipCondition()
	 * @generated
	 */
	int MEMBERSHIP_CONDITION = 23;

	/**
	 * The feature id for the '<em><b>Declaring Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBERSHIP_CONDITION__DECLARING_CLASS = PARAMETERIZED_METHOD__DECLARING_CLASS;

	/**
	 * The feature id for the '<em><b>Method Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBERSHIP_CONDITION__METHOD_NAME = PARAMETERIZED_METHOD__METHOD_NAME;

	/**
	 * The feature id for the '<em><b>Formal Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBERSHIP_CONDITION__FORMAL_PARAMETERS = PARAMETERIZED_METHOD__FORMAL_PARAMETERS;

	/**
	 * The feature id for the '<em><b>In Parameters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBERSHIP_CONDITION__IN_PARAMETERS = PARAMETERIZED_METHOD__IN_PARAMETERS;

	/**
	 * The feature id for the '<em><b>Out Parameters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBERSHIP_CONDITION__OUT_PARAMETERS = PARAMETERIZED_METHOD__OUT_PARAMETERS;

	/**
	 * The feature id for the '<em><b>In Out Parameters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBERSHIP_CONDITION__IN_OUT_PARAMETERS = PARAMETERIZED_METHOD__IN_OUT_PARAMETERS;

	/**
	 * The feature id for the '<em><b>Ensemble</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBERSHIP_CONDITION__ENSEMBLE = PARAMETERIZED_METHOD_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Membership Condition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEMBERSHIP_CONDITION_FEATURE_COUNT = PARAMETERIZED_METHOD_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeExchangeImpl <em>Knowledge Exchange</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeExchangeImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getKnowledgeExchange()
	 * @generated
	 */
	int KNOWLEDGE_EXCHANGE = 24;

	/**
	 * The feature id for the '<em><b>Declaring Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_EXCHANGE__DECLARING_CLASS = PARAMETERIZED_METHOD__DECLARING_CLASS;

	/**
	 * The feature id for the '<em><b>Method Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_EXCHANGE__METHOD_NAME = PARAMETERIZED_METHOD__METHOD_NAME;

	/**
	 * The feature id for the '<em><b>Formal Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_EXCHANGE__FORMAL_PARAMETERS = PARAMETERIZED_METHOD__FORMAL_PARAMETERS;

	/**
	 * The feature id for the '<em><b>In Parameters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_EXCHANGE__IN_PARAMETERS = PARAMETERIZED_METHOD__IN_PARAMETERS;

	/**
	 * The feature id for the '<em><b>Out Parameters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_EXCHANGE__OUT_PARAMETERS = PARAMETERIZED_METHOD__OUT_PARAMETERS;

	/**
	 * The feature id for the '<em><b>In Out Parameters</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_EXCHANGE__IN_OUT_PARAMETERS = PARAMETERIZED_METHOD__IN_OUT_PARAMETERS;

	/**
	 * The feature id for the '<em><b>Ensemble</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_EXCHANGE__ENSEMBLE = PARAMETERIZED_METHOD_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Knowledge Exchange</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KNOWLEDGE_EXCHANGE_FEATURE_COUNT = PARAMETERIZED_METHOD_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TypeParameterImpl <em>Type Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TypeParameterImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getTypeParameter()
	 * @generated
	 */
	int TYPE_PARAMETER = 26;

	/**
	 * The feature id for the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_PARAMETER__TYPE = KNOWLEDGE_TYPE_OWNER__TYPE;

	/**
	 * The feature id for the '<em><b>Owner</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_PARAMETER__OWNER = KNOWLEDGE_TYPE_OWNER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Type Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_PARAMETER_FEATURE_COUNT = KNOWLEDGE_TYPE_OWNER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParametricKnowledgeType <em>Parametric Knowledge Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParametricKnowledgeType
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getParametricKnowledgeType()
	 * @generated
	 */
	int PARAMETRIC_KNOWLEDGE_TYPE = 27;

	/**
	 * The number of structural features of the '<em>Parametric Knowledge Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETRIC_KNOWLEDGE_TYPE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeType <em>Structured Knowledge Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeType
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getStructuredKnowledgeType()
	 * @generated
	 */
	int STRUCTURED_KNOWLEDGE_TYPE = 29;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterKind <em>Parameter Kind</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterKind
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getParameterKind()
	 * @generated
	 */
	int PARAMETER_KIND = 30;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.SchedulingType <em>Scheduling Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.SchedulingType
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getSchedulingType()
	 * @generated
	 */
	int SCHEDULING_TYPE = 31;


	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Component</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component
	 * @generated
	 */
	EClass getComponent();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component#getKnowledge <em>Knowledge</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Knowledge</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component#getKnowledge()
	 * @see #getComponent()
	 * @generated
	 */
	EReference getComponent_Knowledge();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component#getProcesses <em>Processes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Processes</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component#getProcesses()
	 * @see #getComponent()
	 * @generated
	 */
	EReference getComponent_Processes();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component#getName()
	 * @see #getComponent()
	 * @generated
	 */
	EAttribute getComponent_Name();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeDefinition <em>Knowledge Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Knowledge Definition</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeDefinition
	 * @generated
	 */
	EClass getKnowledgeDefinition();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeDefinition#isIsLocal <em>Is Local</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Local</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeDefinition#isIsLocal()
	 * @see #getKnowledgeDefinition()
	 * @generated
	 */
	EAttribute getKnowledgeDefinition_IsLocal();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType <em>Knowledge Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Knowledge Type</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType
	 * @generated
	 */
	EClass getKnowledgeType();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#isIsStructured <em>Is Structured</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Structured</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#isIsStructured()
	 * @see #getKnowledgeType()
	 * @generated
	 */
	EAttribute getKnowledgeType_IsStructured();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#isIsWrapper <em>Is Wrapper</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Wrapper</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#isIsWrapper()
	 * @see #getKnowledgeType()
	 * @generated
	 */
	EAttribute getKnowledgeType_IsWrapper();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#getClazz <em>Clazz</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Clazz</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#getClazz()
	 * @see #getKnowledgeType()
	 * @generated
	 */
	EAttribute getKnowledgeType_Clazz();

	/**
	 * Returns the meta object for the container reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Owner</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType#getOwner()
	 * @see #getKnowledgeType()
	 * @generated
	 */
	EReference getKnowledgeType_Owner();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process <em>Process</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Process</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process
	 * @generated
	 */
	EClass getProcess();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process#getName()
	 * @see #getProcess()
	 * @generated
	 */
	EAttribute getProcess_Name();

	/**
	 * Returns the meta object for the container reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process#getComponent <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Component</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process#getComponent()
	 * @see #getProcess()
	 * @generated
	 */
	EReference getProcess_Component();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Scheduling <em>Scheduling</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scheduling</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Scheduling
	 * @generated
	 */
	EClass getScheduling();

	/**
	 * Returns the meta object for the container reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Scheduling#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Owner</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Scheduling#getOwner()
	 * @see #getScheduling()
	 * @generated
	 */
	EReference getScheduling_Owner();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeValueType <em>Structured Knowledge Value Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Structured Knowledge Value Type</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeValueType
	 * @generated
	 */
	EClass getStructuredKnowledgeValueType();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeValueType#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeValueType#getChildren()
	 * @see #getStructuredKnowledgeValueType()
	 * @generated
	 */
	EReference getStructuredKnowledgeValueType_Children();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ListValueType <em>List Value Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>List Value Type</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ListValueType
	 * @generated
	 */
	EClass getListValueType();

	/**
	 * Returns the meta object for the containment reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ListValueType#getTypeParameter <em>Type Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Type Parameter</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ListValueType#getTypeParameter()
	 * @see #getListValueType()
	 * @generated
	 */
	EReference getListValueType_TypeParameter();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MapValueType <em>Map Value Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Map Value Type</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MapValueType
	 * @generated
	 */
	EClass getMapValueType();

	/**
	 * Returns the meta object for the containment reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MapValueType#getKeyTypeParameter <em>Key Type Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Key Type Parameter</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MapValueType#getKeyTypeParameter()
	 * @see #getMapValueType()
	 * @generated
	 */
	EReference getMapValueType_KeyTypeParameter();

	/**
	 * Returns the meta object for the containment reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MapValueType#getValueTypeParameter <em>Value Type Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value Type Parameter</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MapValueType#getValueTypeParameter()
	 * @see #getMapValueType()
	 * @generated
	 */
	EReference getMapValueType_ValueTypeParameter();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.UnstructuredValueType <em>Unstructured Value Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Unstructured Value Type</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.UnstructuredValueType
	 * @generated
	 */
	EClass getUnstructuredValueType();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Model <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Model
	 * @generated
	 */
	EClass getModel();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Model#getComponents <em>Components</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Components</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Model#getComponents()
	 * @see #getModel()
	 * @generated
	 */
	EReference getModel_Components();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Model#getEnsembles <em>Ensembles</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Ensembles</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Model#getEnsembles()
	 * @see #getModel()
	 * @generated
	 */
	EReference getModel_Ensembles();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ModelUpdateCommand <em>Model Update Command</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Update Command</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ModelUpdateCommand
	 * @generated
	 */
	EClass getModelUpdateCommand();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.UpdateKnowledgeStructureCommand <em>Update Knowledge Structure Command</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Update Knowledge Structure Command</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.UpdateKnowledgeStructureCommand
	 * @generated
	 */
	EClass getUpdateKnowledgeStructureCommand();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.AddComponentCommand <em>Add Component Command</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Add Component Command</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.AddComponentCommand
	 * @generated
	 */
	EClass getAddComponentCommand();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter <em>Method Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Method Parameter</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter
	 * @generated
	 */
	EClass getMethodParameter();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter#getKind()
	 * @see #getMethodParameter()
	 * @generated
	 */
	EAttribute getMethodParameter_Kind();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter#getKnowledgePath <em>Knowledge Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Knowledge Path</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter#getKnowledgePath()
	 * @see #getMethodParameter()
	 * @generated
	 */
	EAttribute getMethodParameter_KnowledgePath();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Index</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter#getIndex()
	 * @see #getMethodParameter()
	 * @generated
	 */
	EAttribute getMethodParameter_Index();

	/**
	 * Returns the meta object for the container reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Owner</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter#getOwner()
	 * @see #getMethodParameter()
	 * @generated
	 */
	EReference getMethodParameter_Owner();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod <em>Parameterized Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameterized Method</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod
	 * @generated
	 */
	EClass getParameterizedMethod();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getDeclaringClass <em>Declaring Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Declaring Class</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getDeclaringClass()
	 * @see #getParameterizedMethod()
	 * @generated
	 */
	EAttribute getParameterizedMethod_DeclaringClass();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getMethodName <em>Method Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Method Name</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getMethodName()
	 * @see #getParameterizedMethod()
	 * @generated
	 */
	EAttribute getParameterizedMethod_MethodName();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getFormalParameters <em>Formal Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Formal Parameters</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getFormalParameters()
	 * @see #getParameterizedMethod()
	 * @generated
	 */
	EReference getParameterizedMethod_FormalParameters();

	/**
	 * Returns the meta object for the reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getInParameters <em>In Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>In Parameters</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getInParameters()
	 * @see #getParameterizedMethod()
	 * @generated
	 */
	EReference getParameterizedMethod_InParameters();

	/**
	 * Returns the meta object for the reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getOutParameters <em>Out Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Out Parameters</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getOutParameters()
	 * @see #getParameterizedMethod()
	 * @generated
	 */
	EReference getParameterizedMethod_OutParameters();

	/**
	 * Returns the meta object for the reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getInOutParameters <em>In Out Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>In Out Parameters</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getInOutParameters()
	 * @see #getParameterizedMethod()
	 * @generated
	 */
	EReference getParameterizedMethod_InOutParameters();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.PeriodicScheduling <em>Periodic Scheduling</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Periodic Scheduling</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.PeriodicScheduling
	 * @generated
	 */
	EClass getPeriodicScheduling();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.PeriodicScheduling#getPeriod <em>Period</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Period</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.PeriodicScheduling#getPeriod()
	 * @see #getPeriodicScheduling()
	 * @generated
	 */
	EAttribute getPeriodicScheduling_Period();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TriggeredScheduling <em>Triggered Scheduling</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Triggered Scheduling</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TriggeredScheduling
	 * @generated
	 */
	EClass getTriggeredScheduling();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TriggeredScheduling#getTriggers <em>Triggers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Triggers</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TriggeredScheduling#getTriggers()
	 * @see #getTriggeredScheduling()
	 * @generated
	 */
	EReference getTriggeredScheduling_Triggers();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Trigger <em>Trigger</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trigger</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Trigger
	 * @generated
	 */
	EClass getTrigger();

	/**
	 * Returns the meta object for the container reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Trigger#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Owner</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Trigger#getOwner()
	 * @see #getTrigger()
	 * @generated
	 */
	EReference getTrigger_Owner();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterChangedTrigger <em>Parameter Changed Trigger</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter Changed Trigger</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterChangedTrigger
	 * @generated
	 */
	EClass getParameterChangedTrigger();

	/**
	 * Returns the meta object for the reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterChangedTrigger#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Parameters</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterChangedTrigger#getParameters()
	 * @see #getParameterChangedTrigger()
	 * @generated
	 */
	EReference getParameterChangedTrigger_Parameters();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble <em>Ensemble</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ensemble</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble
	 * @generated
	 */
	EClass getEnsemble();

	/**
	 * Returns the meta object for the containment reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble#getMembership <em>Membership</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Membership</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble#getMembership()
	 * @see #getEnsemble()
	 * @generated
	 */
	EReference getEnsemble_Membership();

	/**
	 * Returns the meta object for the containment reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble#getKnowledgeExchange <em>Knowledge Exchange</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Knowledge Exchange</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble#getKnowledgeExchange()
	 * @see #getEnsemble()
	 * @generated
	 */
	EReference getEnsemble_KnowledgeExchange();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TopLevelKnowledgeDefinition <em>Top Level Knowledge Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Top Level Knowledge Definition</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TopLevelKnowledgeDefinition
	 * @generated
	 */
	EClass getTopLevelKnowledgeDefinition();

	/**
	 * Returns the meta object for the container reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TopLevelKnowledgeDefinition#getComponent <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Component</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TopLevelKnowledgeDefinition#getComponent()
	 * @see #getTopLevelKnowledgeDefinition()
	 * @generated
	 */
	EReference getTopLevelKnowledgeDefinition_Component();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.NestedKnowledgeDefinition <em>Nested Knowledge Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Nested Knowledge Definition</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.NestedKnowledgeDefinition
	 * @generated
	 */
	EClass getNestedKnowledgeDefinition();

	/**
	 * Returns the meta object for the container reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.NestedKnowledgeDefinition#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Parent</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.NestedKnowledgeDefinition#getParent()
	 * @see #getNestedKnowledgeDefinition()
	 * @generated
	 */
	EReference getNestedKnowledgeDefinition_Parent();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeReference <em>Knowledge Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Knowledge Reference</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeReference
	 * @generated
	 */
	EClass getKnowledgeReference();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeReference#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeReference#getName()
	 * @see #getKnowledgeReference()
	 * @generated
	 */
	EAttribute getKnowledgeReference_Name();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MembershipCondition <em>Membership Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Membership Condition</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MembershipCondition
	 * @generated
	 */
	EClass getMembershipCondition();

	/**
	 * Returns the meta object for the container reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MembershipCondition#getEnsemble <em>Ensemble</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Ensemble</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MembershipCondition#getEnsemble()
	 * @see #getMembershipCondition()
	 * @generated
	 */
	EReference getMembershipCondition_Ensemble();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeExchange <em>Knowledge Exchange</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Knowledge Exchange</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeExchange
	 * @generated
	 */
	EClass getKnowledgeExchange();

	/**
	 * Returns the meta object for the container reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeExchange#getEnsemble <em>Ensemble</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Ensemble</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeExchange#getEnsemble()
	 * @see #getKnowledgeExchange()
	 * @generated
	 */
	EReference getKnowledgeExchange_Ensemble();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Schedulable <em>Schedulable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Schedulable</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Schedulable
	 * @generated
	 */
	EClass getSchedulable();

	/**
	 * Returns the meta object for the containment reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Schedulable#getScheduling <em>Scheduling</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Scheduling</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Schedulable#getScheduling()
	 * @see #getSchedulable()
	 * @generated
	 */
	EReference getSchedulable_Scheduling();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TypeParameter <em>Type Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Type Parameter</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TypeParameter
	 * @generated
	 */
	EClass getTypeParameter();

	/**
	 * Returns the meta object for the reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TypeParameter#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Owner</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TypeParameter#getOwner()
	 * @see #getTypeParameter()
	 * @generated
	 */
	EReference getTypeParameter_Owner();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParametricKnowledgeType <em>Parametric Knowledge Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parametric Knowledge Type</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParametricKnowledgeType
	 * @generated
	 */
	EClass getParametricKnowledgeType();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeTypeOwner <em>Knowledge Type Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Knowledge Type Owner</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeTypeOwner
	 * @generated
	 */
	EClass getKnowledgeTypeOwner();

	/**
	 * Returns the meta object for the containment reference '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeTypeOwner#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Type</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeTypeOwner#getType()
	 * @see #getKnowledgeTypeOwner()
	 * @generated
	 */
	EReference getKnowledgeTypeOwner_Type();

	/**
	 * Returns the meta object for enum '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeType <em>Structured Knowledge Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Structured Knowledge Type</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeType
	 * @generated
	 */
	EEnum getStructuredKnowledgeType();

	/**
	 * Returns the meta object for enum '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterKind <em>Parameter Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Parameter Kind</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterKind
	 * @generated
	 */
	EEnum getParameterKind();

	/**
	 * Returns the meta object for enum '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.SchedulingType <em>Scheduling Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Scheduling Type</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.SchedulingType
	 * @generated
	 */
	EEnum getSchedulingType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	RuntimemodelFactory getRuntimemodelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ComponentImpl <em>Component</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ComponentImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getComponent()
		 * @generated
		 */
		EClass COMPONENT = eINSTANCE.getComponent();

		/**
		 * The meta object literal for the '<em><b>Knowledge</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT__KNOWLEDGE = eINSTANCE.getComponent_Knowledge();

		/**
		 * The meta object literal for the '<em><b>Processes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT__PROCESSES = eINSTANCE.getComponent_Processes();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT__NAME = eINSTANCE.getComponent_Name();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeDefinitionImpl <em>Knowledge Definition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeDefinitionImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getKnowledgeDefinition()
		 * @generated
		 */
		EClass KNOWLEDGE_DEFINITION = eINSTANCE.getKnowledgeDefinition();

		/**
		 * The meta object literal for the '<em><b>Is Local</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KNOWLEDGE_DEFINITION__IS_LOCAL = eINSTANCE.getKnowledgeDefinition_IsLocal();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeTypeImpl <em>Knowledge Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeTypeImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getKnowledgeType()
		 * @generated
		 */
		EClass KNOWLEDGE_TYPE = eINSTANCE.getKnowledgeType();

		/**
		 * The meta object literal for the '<em><b>Is Structured</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KNOWLEDGE_TYPE__IS_STRUCTURED = eINSTANCE.getKnowledgeType_IsStructured();

		/**
		 * The meta object literal for the '<em><b>Is Wrapper</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KNOWLEDGE_TYPE__IS_WRAPPER = eINSTANCE.getKnowledgeType_IsWrapper();

		/**
		 * The meta object literal for the '<em><b>Clazz</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KNOWLEDGE_TYPE__CLAZZ = eINSTANCE.getKnowledgeType_Clazz();

		/**
		 * The meta object literal for the '<em><b>Owner</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference KNOWLEDGE_TYPE__OWNER = eINSTANCE.getKnowledgeType_Owner();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ProcessImpl <em>Process</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ProcessImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getProcess()
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
		 * The meta object literal for the '<em><b>Component</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROCESS__COMPONENT = eINSTANCE.getProcess_Component();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Scheduling <em>Scheduling</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Scheduling
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getScheduling()
		 * @generated
		 */
		EClass SCHEDULING = eINSTANCE.getScheduling();

		/**
		 * The meta object literal for the '<em><b>Owner</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULING__OWNER = eINSTANCE.getScheduling_Owner();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.StructuredKnowledgeValueTypeImpl <em>Structured Knowledge Value Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.StructuredKnowledgeValueTypeImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getStructuredKnowledgeValueType()
		 * @generated
		 */
		EClass STRUCTURED_KNOWLEDGE_VALUE_TYPE = eINSTANCE.getStructuredKnowledgeValueType();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STRUCTURED_KNOWLEDGE_VALUE_TYPE__CHILDREN = eINSTANCE.getStructuredKnowledgeValueType_Children();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ListValueTypeImpl <em>List Value Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ListValueTypeImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getListValueType()
		 * @generated
		 */
		EClass LIST_VALUE_TYPE = eINSTANCE.getListValueType();

		/**
		 * The meta object literal for the '<em><b>Type Parameter</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LIST_VALUE_TYPE__TYPE_PARAMETER = eINSTANCE.getListValueType_TypeParameter();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MapValueTypeImpl <em>Map Value Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MapValueTypeImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getMapValueType()
		 * @generated
		 */
		EClass MAP_VALUE_TYPE = eINSTANCE.getMapValueType();

		/**
		 * The meta object literal for the '<em><b>Key Type Parameter</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAP_VALUE_TYPE__KEY_TYPE_PARAMETER = eINSTANCE.getMapValueType_KeyTypeParameter();

		/**
		 * The meta object literal for the '<em><b>Value Type Parameter</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAP_VALUE_TYPE__VALUE_TYPE_PARAMETER = eINSTANCE.getMapValueType_ValueTypeParameter();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.UnstructuredValueTypeImpl <em>Unstructured Value Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.UnstructuredValueTypeImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getUnstructuredValueType()
		 * @generated
		 */
		EClass UNSTRUCTURED_VALUE_TYPE = eINSTANCE.getUnstructuredValueType();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ModelImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getModel()
		 * @generated
		 */
		EClass MODEL = eINSTANCE.getModel();

		/**
		 * The meta object literal for the '<em><b>Components</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL__COMPONENTS = eINSTANCE.getModel_Components();

		/**
		 * The meta object literal for the '<em><b>Ensembles</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL__ENSEMBLES = eINSTANCE.getModel_Ensembles();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ModelUpdateCommandImpl <em>Model Update Command</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ModelUpdateCommandImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getModelUpdateCommand()
		 * @generated
		 */
		EClass MODEL_UPDATE_COMMAND = eINSTANCE.getModelUpdateCommand();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.UpdateKnowledgeStructureCommandImpl <em>Update Knowledge Structure Command</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.UpdateKnowledgeStructureCommandImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getUpdateKnowledgeStructureCommand()
		 * @generated
		 */
		EClass UPDATE_KNOWLEDGE_STRUCTURE_COMMAND = eINSTANCE.getUpdateKnowledgeStructureCommand();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.AddComponentCommandImpl <em>Add Component Command</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.AddComponentCommandImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getAddComponentCommand()
		 * @generated
		 */
		EClass ADD_COMPONENT_COMMAND = eINSTANCE.getAddComponentCommand();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MethodParameterImpl <em>Method Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MethodParameterImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getMethodParameter()
		 * @generated
		 */
		EClass METHOD_PARAMETER = eINSTANCE.getMethodParameter();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METHOD_PARAMETER__KIND = eINSTANCE.getMethodParameter_Kind();

		/**
		 * The meta object literal for the '<em><b>Knowledge Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METHOD_PARAMETER__KNOWLEDGE_PATH = eINSTANCE.getMethodParameter_KnowledgePath();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METHOD_PARAMETER__INDEX = eINSTANCE.getMethodParameter_Index();

		/**
		 * The meta object literal for the '<em><b>Owner</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference METHOD_PARAMETER__OWNER = eINSTANCE.getMethodParameter_Owner();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ParameterizedMethodImpl <em>Parameterized Method</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ParameterizedMethodImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getParameterizedMethod()
		 * @generated
		 */
		EClass PARAMETERIZED_METHOD = eINSTANCE.getParameterizedMethod();

		/**
		 * The meta object literal for the '<em><b>Declaring Class</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETERIZED_METHOD__DECLARING_CLASS = eINSTANCE.getParameterizedMethod_DeclaringClass();

		/**
		 * The meta object literal for the '<em><b>Method Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETERIZED_METHOD__METHOD_NAME = eINSTANCE.getParameterizedMethod_MethodName();

		/**
		 * The meta object literal for the '<em><b>Formal Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETERIZED_METHOD__FORMAL_PARAMETERS = eINSTANCE.getParameterizedMethod_FormalParameters();

		/**
		 * The meta object literal for the '<em><b>In Parameters</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETERIZED_METHOD__IN_PARAMETERS = eINSTANCE.getParameterizedMethod_InParameters();

		/**
		 * The meta object literal for the '<em><b>Out Parameters</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETERIZED_METHOD__OUT_PARAMETERS = eINSTANCE.getParameterizedMethod_OutParameters();

		/**
		 * The meta object literal for the '<em><b>In Out Parameters</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETERIZED_METHOD__IN_OUT_PARAMETERS = eINSTANCE.getParameterizedMethod_InOutParameters();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.PeriodicSchedulingImpl <em>Periodic Scheduling</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.PeriodicSchedulingImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getPeriodicScheduling()
		 * @generated
		 */
		EClass PERIODIC_SCHEDULING = eINSTANCE.getPeriodicScheduling();

		/**
		 * The meta object literal for the '<em><b>Period</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PERIODIC_SCHEDULING__PERIOD = eINSTANCE.getPeriodicScheduling_Period();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TriggeredSchedulingImpl <em>Triggered Scheduling</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TriggeredSchedulingImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getTriggeredScheduling()
		 * @generated
		 */
		EClass TRIGGERED_SCHEDULING = eINSTANCE.getTriggeredScheduling();

		/**
		 * The meta object literal for the '<em><b>Triggers</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRIGGERED_SCHEDULING__TRIGGERS = eINSTANCE.getTriggeredScheduling_Triggers();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TriggerImpl <em>Trigger</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TriggerImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getTrigger()
		 * @generated
		 */
		EClass TRIGGER = eINSTANCE.getTrigger();

		/**
		 * The meta object literal for the '<em><b>Owner</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRIGGER__OWNER = eINSTANCE.getTrigger_Owner();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ParameterChangedTriggerImpl <em>Parameter Changed Trigger</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.ParameterChangedTriggerImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getParameterChangedTrigger()
		 * @generated
		 */
		EClass PARAMETER_CHANGED_TRIGGER = eINSTANCE.getParameterChangedTrigger();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETER_CHANGED_TRIGGER__PARAMETERS = eINSTANCE.getParameterChangedTrigger_Parameters();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.EnsembleImpl <em>Ensemble</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.EnsembleImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getEnsemble()
		 * @generated
		 */
		EClass ENSEMBLE = eINSTANCE.getEnsemble();

		/**
		 * The meta object literal for the '<em><b>Membership</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENSEMBLE__MEMBERSHIP = eINSTANCE.getEnsemble_Membership();

		/**
		 * The meta object literal for the '<em><b>Knowledge Exchange</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENSEMBLE__KNOWLEDGE_EXCHANGE = eINSTANCE.getEnsemble_KnowledgeExchange();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TopLevelKnowledgeDefinitionImpl <em>Top Level Knowledge Definition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TopLevelKnowledgeDefinitionImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getTopLevelKnowledgeDefinition()
		 * @generated
		 */
		EClass TOP_LEVEL_KNOWLEDGE_DEFINITION = eINSTANCE.getTopLevelKnowledgeDefinition();

		/**
		 * The meta object literal for the '<em><b>Component</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TOP_LEVEL_KNOWLEDGE_DEFINITION__COMPONENT = eINSTANCE.getTopLevelKnowledgeDefinition_Component();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.NestedKnowledgeDefinitionImpl <em>Nested Knowledge Definition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.NestedKnowledgeDefinitionImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getNestedKnowledgeDefinition()
		 * @generated
		 */
		EClass NESTED_KNOWLEDGE_DEFINITION = eINSTANCE.getNestedKnowledgeDefinition();

		/**
		 * The meta object literal for the '<em><b>Parent</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NESTED_KNOWLEDGE_DEFINITION__PARENT = eINSTANCE.getNestedKnowledgeDefinition_Parent();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeReferenceImpl <em>Knowledge Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeReferenceImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getKnowledgeReference()
		 * @generated
		 */
		EClass KNOWLEDGE_REFERENCE = eINSTANCE.getKnowledgeReference();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KNOWLEDGE_REFERENCE__NAME = eINSTANCE.getKnowledgeReference_Name();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MembershipConditionImpl <em>Membership Condition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.MembershipConditionImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getMembershipCondition()
		 * @generated
		 */
		EClass MEMBERSHIP_CONDITION = eINSTANCE.getMembershipCondition();

		/**
		 * The meta object literal for the '<em><b>Ensemble</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MEMBERSHIP_CONDITION__ENSEMBLE = eINSTANCE.getMembershipCondition_Ensemble();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeExchangeImpl <em>Knowledge Exchange</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeExchangeImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getKnowledgeExchange()
		 * @generated
		 */
		EClass KNOWLEDGE_EXCHANGE = eINSTANCE.getKnowledgeExchange();

		/**
		 * The meta object literal for the '<em><b>Ensemble</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference KNOWLEDGE_EXCHANGE__ENSEMBLE = eINSTANCE.getKnowledgeExchange_Ensemble();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Schedulable <em>Schedulable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Schedulable
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getSchedulable()
		 * @generated
		 */
		EClass SCHEDULABLE = eINSTANCE.getSchedulable();

		/**
		 * The meta object literal for the '<em><b>Scheduling</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCHEDULABLE__SCHEDULING = eINSTANCE.getSchedulable_Scheduling();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TypeParameterImpl <em>Type Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.TypeParameterImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getTypeParameter()
		 * @generated
		 */
		EClass TYPE_PARAMETER = eINSTANCE.getTypeParameter();

		/**
		 * The meta object literal for the '<em><b>Owner</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TYPE_PARAMETER__OWNER = eINSTANCE.getTypeParameter_Owner();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParametricKnowledgeType <em>Parametric Knowledge Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParametricKnowledgeType
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getParametricKnowledgeType()
		 * @generated
		 */
		EClass PARAMETRIC_KNOWLEDGE_TYPE = eINSTANCE.getParametricKnowledgeType();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeTypeOwnerImpl <em>Knowledge Type Owner</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.KnowledgeTypeOwnerImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getKnowledgeTypeOwner()
		 * @generated
		 */
		EClass KNOWLEDGE_TYPE_OWNER = eINSTANCE.getKnowledgeTypeOwner();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference KNOWLEDGE_TYPE_OWNER__TYPE = eINSTANCE.getKnowledgeTypeOwner_Type();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeType <em>Structured Knowledge Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeType
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getStructuredKnowledgeType()
		 * @generated
		 */
		EEnum STRUCTURED_KNOWLEDGE_TYPE = eINSTANCE.getStructuredKnowledgeType();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterKind <em>Parameter Kind</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterKind
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getParameterKind()
		 * @generated
		 */
		EEnum PARAMETER_KIND = eINSTANCE.getParameterKind();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.SchedulingType <em>Scheduling Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.SchedulingType
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl.RuntimemodelPackageImpl#getSchedulingType()
		 * @generated
		 */
		EEnum SCHEDULING_TYPE = eINSTANCE.getSchedulingType();

	}

} //RuntimemodelPackage
