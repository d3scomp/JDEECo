/**
 */
package cz.cuni.mff.d3s.deeco.model.architecture.impl;

import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;

import cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture;
import cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.model.architecture.api.LocalComponentInstance;
import cz.cuni.mff.d3s.deeco.model.architecture.api.RemoteComponentInstance;

import cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitectureFactory;
import cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage;

import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;

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
public class ArchitecturePackageImpl extends EPackageImpl implements ArchitecturePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass architectureEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass localComponentInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ensembleInstanceEClass = null;

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
	private EClass remoteComponentInstanceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType componentInstanceMetadataEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType ensembleDefinitionMetadataEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType readOnlyKnowledgeManagerEDataType = null;

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
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ArchitecturePackageImpl() {
		super(eNS_URI, ArchitectureFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ArchitecturePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ArchitecturePackage init() {
		if (isInited) return (ArchitecturePackage)EPackage.Registry.INSTANCE.getEPackage(ArchitecturePackage.eNS_URI);

		// Obtain or create and register package
		ArchitecturePackageImpl theArchitecturePackage = (ArchitecturePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ArchitecturePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ArchitecturePackageImpl());

		isInited = true;

		// Create package meta-data objects
		theArchitecturePackage.createPackageContents();

		// Initialize created meta-data
		theArchitecturePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theArchitecturePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ArchitecturePackage.eNS_URI, theArchitecturePackage);
		return theArchitecturePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getArchitecture() {
		return architectureEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getArchitecture_EnsembleInstances() {
		return (EReference)architectureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getArchitecture_ComponentInstances() {
		return (EReference)architectureEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLocalComponentInstance() {
		return localComponentInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLocalComponentInstance_RuntimeInstance() {
		return (EAttribute)localComponentInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnsembleInstance() {
		return ensembleInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEnsembleInstance_EnsembleDefinition() {
		return (EAttribute)ensembleInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsembleInstance_Coordinator() {
		return (EReference)ensembleInstanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsembleInstance_Members() {
		return (EReference)ensembleInstanceEClass.getEStructuralFeatures().get(2);
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
	public EAttribute getComponentInstance_Id() {
		return (EAttribute)componentInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getComponentInstance_KnowledgeManager() {
		return (EAttribute)componentInstanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRemoteComponentInstance() {
		return remoteComponentInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getComponentInstanceMetadata() {
		return componentInstanceMetadataEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getEnsembleDefinitionMetadata() {
		return ensembleDefinitionMetadataEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getReadOnlyKnowledgeManager() {
		return readOnlyKnowledgeManagerEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ArchitectureFactory getArchitectureFactory() {
		return (ArchitectureFactory)getEFactoryInstance();
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
		architectureEClass = createEClass(ARCHITECTURE);
		createEReference(architectureEClass, ARCHITECTURE__ENSEMBLE_INSTANCES);
		createEReference(architectureEClass, ARCHITECTURE__COMPONENT_INSTANCES);

		localComponentInstanceEClass = createEClass(LOCAL_COMPONENT_INSTANCE);
		createEAttribute(localComponentInstanceEClass, LOCAL_COMPONENT_INSTANCE__RUNTIME_INSTANCE);

		ensembleInstanceEClass = createEClass(ENSEMBLE_INSTANCE);
		createEAttribute(ensembleInstanceEClass, ENSEMBLE_INSTANCE__ENSEMBLE_DEFINITION);
		createEReference(ensembleInstanceEClass, ENSEMBLE_INSTANCE__COORDINATOR);
		createEReference(ensembleInstanceEClass, ENSEMBLE_INSTANCE__MEMBERS);

		componentInstanceEClass = createEClass(COMPONENT_INSTANCE);
		createEAttribute(componentInstanceEClass, COMPONENT_INSTANCE__ID);
		createEAttribute(componentInstanceEClass, COMPONENT_INSTANCE__KNOWLEDGE_MANAGER);

		remoteComponentInstanceEClass = createEClass(REMOTE_COMPONENT_INSTANCE);

		// Create data types
		componentInstanceMetadataEDataType = createEDataType(COMPONENT_INSTANCE_METADATA);
		ensembleDefinitionMetadataEDataType = createEDataType(ENSEMBLE_DEFINITION_METADATA);
		readOnlyKnowledgeManagerEDataType = createEDataType(READ_ONLY_KNOWLEDGE_MANAGER);
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
		localComponentInstanceEClass.getESuperTypes().add(this.getComponentInstance());
		remoteComponentInstanceEClass.getESuperTypes().add(this.getComponentInstance());

		// Initialize classes, features, and operations; add parameters
		initEClass(architectureEClass, Architecture.class, "Architecture", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getArchitecture_EnsembleInstances(), this.getEnsembleInstance(), null, "ensembleInstances", null, 0, -1, Architecture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getArchitecture_ComponentInstances(), this.getComponentInstance(), null, "componentInstances", null, 0, -1, Architecture.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(localComponentInstanceEClass, LocalComponentInstance.class, "LocalComponentInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLocalComponentInstance_RuntimeInstance(), this.getComponentInstanceMetadata(), "runtimeInstance", null, 1, 1, LocalComponentInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(ensembleInstanceEClass, EnsembleInstance.class, "EnsembleInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEnsembleInstance_EnsembleDefinition(), this.getEnsembleDefinitionMetadata(), "ensembleDefinition", null, 1, 1, EnsembleInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnsembleInstance_Coordinator(), this.getComponentInstance(), null, "coordinator", null, 1, 1, EnsembleInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnsembleInstance_Members(), this.getComponentInstance(), null, "members", null, 0, -1, EnsembleInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(componentInstanceEClass, ComponentInstance.class, "ComponentInstance", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getComponentInstance_Id(), ecorePackage.getEString(), "id", null, 1, 1, ComponentInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponentInstance_KnowledgeManager(), this.getReadOnlyKnowledgeManager(), "knowledgeManager", null, 0, 1, ComponentInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(remoteComponentInstanceEClass, RemoteComponentInstance.class, "RemoteComponentInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Initialize data types
		initEDataType(componentInstanceMetadataEDataType, cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance.class, "ComponentInstanceMetadata", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(ensembleDefinitionMetadataEDataType, EnsembleDefinition.class, "EnsembleDefinitionMetadata", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(readOnlyKnowledgeManagerEDataType, ReadOnlyKnowledgeManager.class, "ReadOnlyKnowledgeManager", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //ArchitecturePackageImpl
