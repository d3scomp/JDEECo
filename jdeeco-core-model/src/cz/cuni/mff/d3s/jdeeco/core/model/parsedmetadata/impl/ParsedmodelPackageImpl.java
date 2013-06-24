/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl;

import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedClass;
import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedEntity;
import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedField;
import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedMethod;
import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedParameter;
import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.Annotation;
import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.NamedEntity;
import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedModel;
import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelFactory;
import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ParsedmodelPackageImpl extends EPackageImpl implements ParsedmodelPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass annotatedClassEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass annotatedMethodEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedEntityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parsedModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass annotationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass annotatedEntityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass annotatedFieldEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass annotatedParameterEClass = null;

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
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ParsedmodelPackageImpl() {
		super(eNS_URI, ParsedmodelFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ParsedmodelPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ParsedmodelPackage init() {
		if (isInited) return (ParsedmodelPackage)EPackage.Registry.INSTANCE.getEPackage(ParsedmodelPackage.eNS_URI);

		// Obtain or create and register package
		ParsedmodelPackageImpl theParsedmodelPackage = (ParsedmodelPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ParsedmodelPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ParsedmodelPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theParsedmodelPackage.createPackageContents();

		// Initialize created meta-data
		theParsedmodelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theParsedmodelPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ParsedmodelPackage.eNS_URI, theParsedmodelPackage);
		return theParsedmodelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAnnotatedClass() {
		return annotatedClassEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnnotatedClass_Methods() {
		return (EReference)annotatedClassEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAnnotatedClass_Class() {
		return (EAttribute)annotatedClassEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnnotatedClass_Fields() {
		return (EReference)annotatedClassEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAnnotatedMethod() {
		return annotatedMethodEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAnnotatedMethod_Method() {
		return (EAttribute)annotatedMethodEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnnotatedMethod_Parameters() {
		return (EReference)annotatedMethodEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNamedEntity() {
		return namedEntityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNamedEntity_Name() {
		return (EAttribute)namedEntityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParsedModel() {
		return parsedModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParsedModel_Classes() {
		return (EReference)parsedModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAnnotation() {
		return annotationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAnnotation_Class() {
		return (EAttribute)annotationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAnnotation_Instance() {
		return (EAttribute)annotationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAnnotatedEntity() {
		return annotatedEntityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnnotatedEntity_Annotations() {
		return (EReference)annotatedEntityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAnnotatedField() {
		return annotatedFieldEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAnnotatedField_Type() {
		return (EAttribute)annotatedFieldEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAnnotatedParameter() {
		return annotatedParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAnnotatedParameter_Index() {
		return (EAttribute)annotatedParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAnnotatedParameter_Type() {
		return (EAttribute)annotatedParameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParsedmodelFactory getParsedmodelFactory() {
		return (ParsedmodelFactory)getEFactoryInstance();
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
		annotatedClassEClass = createEClass(ANNOTATED_CLASS);
		createEReference(annotatedClassEClass, ANNOTATED_CLASS__METHODS);
		createEAttribute(annotatedClassEClass, ANNOTATED_CLASS__CLASS);
		createEReference(annotatedClassEClass, ANNOTATED_CLASS__FIELDS);

		annotatedMethodEClass = createEClass(ANNOTATED_METHOD);
		createEAttribute(annotatedMethodEClass, ANNOTATED_METHOD__METHOD);
		createEReference(annotatedMethodEClass, ANNOTATED_METHOD__PARAMETERS);

		namedEntityEClass = createEClass(NAMED_ENTITY);
		createEAttribute(namedEntityEClass, NAMED_ENTITY__NAME);

		parsedModelEClass = createEClass(PARSED_MODEL);
		createEReference(parsedModelEClass, PARSED_MODEL__CLASSES);

		annotationEClass = createEClass(ANNOTATION);
		createEAttribute(annotationEClass, ANNOTATION__CLASS);
		createEAttribute(annotationEClass, ANNOTATION__INSTANCE);

		annotatedEntityEClass = createEClass(ANNOTATED_ENTITY);
		createEReference(annotatedEntityEClass, ANNOTATED_ENTITY__ANNOTATIONS);

		annotatedFieldEClass = createEClass(ANNOTATED_FIELD);
		createEAttribute(annotatedFieldEClass, ANNOTATED_FIELD__TYPE);

		annotatedParameterEClass = createEClass(ANNOTATED_PARAMETER);
		createEAttribute(annotatedParameterEClass, ANNOTATED_PARAMETER__INDEX);
		createEAttribute(annotatedParameterEClass, ANNOTATED_PARAMETER__TYPE);
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
		annotatedClassEClass.getESuperTypes().add(this.getNamedEntity());
		annotatedClassEClass.getESuperTypes().add(this.getAnnotatedEntity());
		annotatedMethodEClass.getESuperTypes().add(this.getNamedEntity());
		annotatedMethodEClass.getESuperTypes().add(this.getAnnotatedEntity());
		annotationEClass.getESuperTypes().add(this.getNamedEntity());
		annotatedFieldEClass.getESuperTypes().add(this.getAnnotatedEntity());
		annotatedFieldEClass.getESuperTypes().add(this.getNamedEntity());
		annotatedParameterEClass.getESuperTypes().add(this.getNamedEntity());
		annotatedParameterEClass.getESuperTypes().add(this.getAnnotatedEntity());

		// Initialize classes and features; add operations and parameters
		initEClass(annotatedClassEClass, AnnotatedClass.class, "AnnotatedClass", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAnnotatedClass_Methods(), this.getAnnotatedMethod(), null, "methods", null, 0, -1, AnnotatedClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAnnotatedClass_Class(), ecorePackage.getEJavaClass(), "class", null, 1, 1, AnnotatedClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnnotatedClass_Fields(), this.getAnnotatedField(), null, "fields", null, 0, -1, AnnotatedClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(annotatedMethodEClass, AnnotatedMethod.class, "AnnotatedMethod", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAnnotatedMethod_Method(), ecorePackage.getEJavaObject(), "method", null, 1, 1, AnnotatedMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAnnotatedMethod_Parameters(), this.getAnnotatedParameter(), null, "parameters", null, 0, -1, AnnotatedMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(namedEntityEClass, NamedEntity.class, "NamedEntity", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedEntity_Name(), ecorePackage.getEString(), "name", null, 1, 1, NamedEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parsedModelEClass, ParsedModel.class, "ParsedModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getParsedModel_Classes(), this.getAnnotatedClass(), null, "classes", null, 0, -1, ParsedModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(parsedModelEClass, this.getAnnotatedClass(), "findByJavaClass", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEJavaClass(), "clazz", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(annotationEClass, Annotation.class, "Annotation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAnnotation_Class(), ecorePackage.getEJavaClass(), "class", null, 1, 1, Annotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAnnotation_Instance(), ecorePackage.getEJavaObject(), "instance", null, 1, 1, Annotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(annotatedEntityEClass, AnnotatedEntity.class, "AnnotatedEntity", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAnnotatedEntity_Annotations(), this.getAnnotation(), null, "annotations", null, 0, -1, AnnotatedEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(annotatedEntityEClass, this.getAnnotation(), "findAnnotationByJavaClass", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEJavaClass(), "class_", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(annotatedFieldEClass, AnnotatedField.class, "AnnotatedField", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAnnotatedField_Type(), ecorePackage.getEJavaClass(), "type", null, 1, 1, AnnotatedField.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(annotatedParameterEClass, AnnotatedParameter.class, "AnnotatedParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAnnotatedParameter_Index(), ecorePackage.getEInt(), "index", null, 1, 1, AnnotatedParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAnnotatedParameter_Type(), ecorePackage.getEJavaClass(), "type", null, 1, 1, AnnotatedParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //ParsedmodelPackageImpl
