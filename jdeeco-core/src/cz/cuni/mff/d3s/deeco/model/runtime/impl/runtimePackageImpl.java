/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.runtimeFactory;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.runtimePackage;

import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class runtimePackageImpl extends EPackageImpl implements runtimePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass schedulingSpecificationEClass = null;

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
	private EDataType methodEDataType = null;

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
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.runtimePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private runtimePackageImpl() {
		super(eNS_URI, runtimeFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link runtimePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static runtimePackage init() {
		if (isInited) return (runtimePackage)EPackage.Registry.INSTANCE.getEPackage(runtimePackage.eNS_URI);

		// Obtain or create and register package
		runtimePackageImpl theruntimePackage = (runtimePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof runtimePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new runtimePackageImpl());

		isInited = true;

		// Create package meta-data objects
		theruntimePackage.createPackageContents();

		// Initialize created meta-data
		theruntimePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theruntimePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(runtimePackage.eNS_URI, theruntimePackage);
		return theruntimePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSchedulingSpecification() {
		return schedulingSpecificationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchedulingSpecification_Triggers() {
		return (EReference)schedulingSpecificationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSchedulingSpecification_Period() {
		return (EAttribute)schedulingSpecificationEClass.getEStructuralFeatures().get(1);
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
	public EDataType getMethod() {
		return methodEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public runtimeFactory getruntimeFactory() {
		return (runtimeFactory)getEFactoryInstance();
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
		schedulingSpecificationEClass = createEClass(SCHEDULING_SPECIFICATION);
		createEReference(schedulingSpecificationEClass, SCHEDULING_SPECIFICATION__TRIGGERS);
		createEAttribute(schedulingSpecificationEClass, SCHEDULING_SPECIFICATION__PERIOD);

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

		// Create data types
		methodEDataType = createEDataType(METHOD);
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
		knowledgeChangeTriggerEClass.getESuperTypes().add(this.getTrigger());
		pathNodeFieldEClass.getESuperTypes().add(this.getPathNode());
		pathNodeMapKeyEClass.getESuperTypes().add(this.getPathNode());

		// Initialize classes, features, and operations; add parameters
		initEClass(schedulingSpecificationEClass, SchedulingSpecification.class, "SchedulingSpecification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSchedulingSpecification_Triggers(), this.getTrigger(), null, "triggers", null, 0, -1, SchedulingSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSchedulingSpecification_Period(), ecorePackage.getELong(), "period", null, 1, 1, SchedulingSpecification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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

		// Initialize data types
		initEDataType(methodEDataType, Method.class, "Method", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //runtimePackageImpl
