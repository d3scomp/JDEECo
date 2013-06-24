/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelFactory
 * @model kind="package"
 * @generated
 */
public interface ParsedmodelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "parsedmetadata";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://parsedmetadata/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "cz.cuni.mff.d3s.deeco.core.mode.parsed";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ParsedmodelPackage eINSTANCE = cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl.init();

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.NamedEntityImpl <em>Named Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.NamedEntityImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getNamedEntity()
	 * @generated
	 */
	int NAMED_ENTITY = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ENTITY__NAME = 0;

	/**
	 * The number of structural features of the '<em>Named Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ENTITY_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedClassImpl <em>Annotated Class</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedClassImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getAnnotatedClass()
	 * @generated
	 */
	int ANNOTATED_CLASS = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CLASS__NAME = NAMED_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CLASS__ANNOTATIONS = NAMED_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CLASS__METHODS = NAMED_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CLASS__CLASS = NAMED_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Fields</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CLASS__FIELDS = NAMED_ENTITY_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Annotated Class</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_CLASS_FEATURE_COUNT = NAMED_ENTITY_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedMethodImpl <em>Annotated Method</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedMethodImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getAnnotatedMethod()
	 * @generated
	 */
	int ANNOTATED_METHOD = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_METHOD__NAME = NAMED_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_METHOD__ANNOTATIONS = NAMED_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_METHOD__METHOD = NAMED_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_METHOD__PARAMETERS = NAMED_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Annotated Method</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_METHOD_FEATURE_COUNT = NAMED_ENTITY_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedModelImpl <em>Parsed Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedModelImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getParsedModel()
	 * @generated
	 */
	int PARSED_MODEL = 3;

	/**
	 * The feature id for the '<em><b>Classes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARSED_MODEL__CLASSES = 0;

	/**
	 * The number of structural features of the '<em>Parsed Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARSED_MODEL_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotationImpl <em>Annotation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotationImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getAnnotation()
	 * @generated
	 */
	int ANNOTATION = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION__NAME = NAMED_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION__CLASS = NAMED_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Instance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION__INSTANCE = NAMED_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Annotation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_FEATURE_COUNT = NAMED_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedEntityImpl <em>Annotated Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedEntityImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getAnnotatedEntity()
	 * @generated
	 */
	int ANNOTATED_ENTITY = 5;

	/**
	 * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_ENTITY__ANNOTATIONS = 0;

	/**
	 * The number of structural features of the '<em>Annotated Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_ENTITY_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedFieldImpl <em>Annotated Field</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedFieldImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getAnnotatedField()
	 * @generated
	 */
	int ANNOTATED_FIELD = 6;

	/**
	 * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_FIELD__ANNOTATIONS = ANNOTATED_ENTITY__ANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_FIELD__NAME = ANNOTATED_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_FIELD__TYPE = ANNOTATED_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Annotated Field</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_FIELD_FEATURE_COUNT = ANNOTATED_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedParameterImpl <em>Annotated Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedParameterImpl
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getAnnotatedParameter()
	 * @generated
	 */
	int ANNOTATED_PARAMETER = 7;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_PARAMETER__NAME = NAMED_ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_PARAMETER__ANNOTATIONS = NAMED_ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_PARAMETER__INDEX = NAMED_ENTITY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_PARAMETER__TYPE = NAMED_ENTITY_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Annotated Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATED_PARAMETER_FEATURE_COUNT = NAMED_ENTITY_FEATURE_COUNT + 3;


	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedClass <em>Annotated Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotated Class</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedClass
	 * @generated
	 */
	EClass getAnnotatedClass();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedClass#getMethods <em>Methods</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Methods</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedClass#getMethods()
	 * @see #getAnnotatedClass()
	 * @generated
	 */
	EReference getAnnotatedClass_Methods();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedClass#getClass_ <em>Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Class</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedClass#getClass_()
	 * @see #getAnnotatedClass()
	 * @generated
	 */
	EAttribute getAnnotatedClass_Class();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedClass#getFields <em>Fields</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fields</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedClass#getFields()
	 * @see #getAnnotatedClass()
	 * @generated
	 */
	EReference getAnnotatedClass_Fields();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedMethod <em>Annotated Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotated Method</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedMethod
	 * @generated
	 */
	EClass getAnnotatedMethod();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedMethod#getMethod <em>Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Method</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedMethod#getMethod()
	 * @see #getAnnotatedMethod()
	 * @generated
	 */
	EAttribute getAnnotatedMethod_Method();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedMethod#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedMethod#getParameters()
	 * @see #getAnnotatedMethod()
	 * @generated
	 */
	EReference getAnnotatedMethod_Parameters();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.NamedEntity <em>Named Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Entity</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.NamedEntity
	 * @generated
	 */
	EClass getNamedEntity();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.NamedEntity#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.NamedEntity#getName()
	 * @see #getNamedEntity()
	 * @generated
	 */
	EAttribute getNamedEntity_Name();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedModel <em>Parsed Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parsed Model</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedModel
	 * @generated
	 */
	EClass getParsedModel();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedModel#getClasses <em>Classes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Classes</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedModel#getClasses()
	 * @see #getParsedModel()
	 * @generated
	 */
	EReference getParsedModel_Classes();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.Annotation <em>Annotation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotation</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.Annotation
	 * @generated
	 */
	EClass getAnnotation();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.Annotation#getClass_ <em>Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Class</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.Annotation#getClass_()
	 * @see #getAnnotation()
	 * @generated
	 */
	EAttribute getAnnotation_Class();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.Annotation#getInstance <em>Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Instance</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.Annotation#getInstance()
	 * @see #getAnnotation()
	 * @generated
	 */
	EAttribute getAnnotation_Instance();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedEntity <em>Annotated Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotated Entity</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedEntity
	 * @generated
	 */
	EClass getAnnotatedEntity();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedEntity#getAnnotations <em>Annotations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Annotations</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedEntity#getAnnotations()
	 * @see #getAnnotatedEntity()
	 * @generated
	 */
	EReference getAnnotatedEntity_Annotations();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedField <em>Annotated Field</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotated Field</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedField
	 * @generated
	 */
	EClass getAnnotatedField();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedField#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedField#getType()
	 * @see #getAnnotatedField()
	 * @generated
	 */
	EAttribute getAnnotatedField_Type();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedParameter <em>Annotated Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotated Parameter</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedParameter
	 * @generated
	 */
	EClass getAnnotatedParameter();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedParameter#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Index</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedParameter#getIndex()
	 * @see #getAnnotatedParameter()
	 * @generated
	 */
	EAttribute getAnnotatedParameter_Index();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedParameter#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedParameter#getType()
	 * @see #getAnnotatedParameter()
	 * @generated
	 */
	EAttribute getAnnotatedParameter_Type();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ParsedmodelFactory getParsedmodelFactory();

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
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedClassImpl <em>Annotated Class</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedClassImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getAnnotatedClass()
		 * @generated
		 */
		EClass ANNOTATED_CLASS = eINSTANCE.getAnnotatedClass();

		/**
		 * The meta object literal for the '<em><b>Methods</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANNOTATED_CLASS__METHODS = eINSTANCE.getAnnotatedClass_Methods();

		/**
		 * The meta object literal for the '<em><b>Class</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATED_CLASS__CLASS = eINSTANCE.getAnnotatedClass_Class();

		/**
		 * The meta object literal for the '<em><b>Fields</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANNOTATED_CLASS__FIELDS = eINSTANCE.getAnnotatedClass_Fields();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedMethodImpl <em>Annotated Method</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedMethodImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getAnnotatedMethod()
		 * @generated
		 */
		EClass ANNOTATED_METHOD = eINSTANCE.getAnnotatedMethod();

		/**
		 * The meta object literal for the '<em><b>Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATED_METHOD__METHOD = eINSTANCE.getAnnotatedMethod_Method();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANNOTATED_METHOD__PARAMETERS = eINSTANCE.getAnnotatedMethod_Parameters();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.NamedEntityImpl <em>Named Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.NamedEntityImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getNamedEntity()
		 * @generated
		 */
		EClass NAMED_ENTITY = eINSTANCE.getNamedEntity();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_ENTITY__NAME = eINSTANCE.getNamedEntity_Name();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedModelImpl <em>Parsed Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedModelImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getParsedModel()
		 * @generated
		 */
		EClass PARSED_MODEL = eINSTANCE.getParsedModel();

		/**
		 * The meta object literal for the '<em><b>Classes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARSED_MODEL__CLASSES = eINSTANCE.getParsedModel_Classes();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotationImpl <em>Annotation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotationImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getAnnotation()
		 * @generated
		 */
		EClass ANNOTATION = eINSTANCE.getAnnotation();

		/**
		 * The meta object literal for the '<em><b>Class</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATION__CLASS = eINSTANCE.getAnnotation_Class();

		/**
		 * The meta object literal for the '<em><b>Instance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATION__INSTANCE = eINSTANCE.getAnnotation_Instance();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedEntityImpl <em>Annotated Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedEntityImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getAnnotatedEntity()
		 * @generated
		 */
		EClass ANNOTATED_ENTITY = eINSTANCE.getAnnotatedEntity();

		/**
		 * The meta object literal for the '<em><b>Annotations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANNOTATED_ENTITY__ANNOTATIONS = eINSTANCE.getAnnotatedEntity_Annotations();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedFieldImpl <em>Annotated Field</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedFieldImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getAnnotatedField()
		 * @generated
		 */
		EClass ANNOTATED_FIELD = eINSTANCE.getAnnotatedField();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATED_FIELD__TYPE = eINSTANCE.getAnnotatedField_Type();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedParameterImpl <em>Annotated Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.AnnotatedParameterImpl
		 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelPackageImpl#getAnnotatedParameter()
		 * @generated
		 */
		EClass ANNOTATED_PARAMETER = eINSTANCE.getAnnotatedParameter();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATED_PARAMETER__INDEX = eINSTANCE.getAnnotatedParameter_Index();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATED_PARAMETER__TYPE = eINSTANCE.getAnnotatedParameter_Type();

	}

} //ParsedmodelPackage
