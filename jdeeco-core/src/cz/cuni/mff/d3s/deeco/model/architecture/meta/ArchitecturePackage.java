/**
 */
package cz.cuni.mff.d3s.deeco.model.architecture.meta;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * @see cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitectureFactory
 * @model kind="package"
 * @generated
 */
public interface ArchitecturePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "architecture";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://cz.cuni.mff.d3s.deeco.model.architecture/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "cz.cuni.mff.d3s.deeco.model.architecture";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ArchitecturePackage eINSTANCE = cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl.init();

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitectureImpl <em>Architecture</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitectureImpl
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getArchitecture()
	 * @generated
	 */
	int ARCHITECTURE = 0;

	/**
	 * The feature id for the '<em><b>Ensemble Instances</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARCHITECTURE__ENSEMBLE_INSTANCES = 0;

	/**
	 * The feature id for the '<em><b>Component Instances</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARCHITECTURE__COMPONENT_INSTANCES = 1;

	/**
	 * The number of structural features of the '<em>Architecture</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARCHITECTURE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Architecture</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARCHITECTURE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.ComponentInstanceImpl <em>Component Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ComponentInstanceImpl
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getComponentInstance()
	 * @generated
	 */
	int COMPONENT_INSTANCE = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__ID = 0;

	/**
	 * The feature id for the '<em><b>Knowledge Manager</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE__KNOWLEDGE_MANAGER = 1;

	/**
	 * The number of structural features of the '<em>Component Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Component Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_INSTANCE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.LocalComponentInstanceImpl <em>Local Component Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.LocalComponentInstanceImpl
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getLocalComponentInstance()
	 * @generated
	 */
	int LOCAL_COMPONENT_INSTANCE = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_COMPONENT_INSTANCE__ID = COMPONENT_INSTANCE__ID;

	/**
	 * The feature id for the '<em><b>Knowledge Manager</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_COMPONENT_INSTANCE__KNOWLEDGE_MANAGER = COMPONENT_INSTANCE__KNOWLEDGE_MANAGER;

	/**
	 * The feature id for the '<em><b>Runtime Instance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_COMPONENT_INSTANCE__RUNTIME_INSTANCE = COMPONENT_INSTANCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Local Component Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_COMPONENT_INSTANCE_FEATURE_COUNT = COMPONENT_INSTANCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Local Component Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCAL_COMPONENT_INSTANCE_OPERATION_COUNT = COMPONENT_INSTANCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.EnsembleInstanceImpl <em>Ensemble Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.EnsembleInstanceImpl
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getEnsembleInstance()
	 * @generated
	 */
	int ENSEMBLE_INSTANCE = 2;

	/**
	 * The feature id for the '<em><b>Ensemble Definition</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_INSTANCE__ENSEMBLE_DEFINITION = 0;

	/**
	 * The feature id for the '<em><b>Coordinator</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_INSTANCE__COORDINATOR = 1;

	/**
	 * The feature id for the '<em><b>Members</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_INSTANCE__MEMBERS = 2;

	/**
	 * The number of structural features of the '<em>Ensemble Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_INSTANCE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Ensemble Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENSEMBLE_INSTANCE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.RemoteComponentInstanceImpl <em>Remote Component Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.RemoteComponentInstanceImpl
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getRemoteComponentInstance()
	 * @generated
	 */
	int REMOTE_COMPONENT_INSTANCE = 4;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOTE_COMPONENT_INSTANCE__ID = COMPONENT_INSTANCE__ID;

	/**
	 * The feature id for the '<em><b>Knowledge Manager</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOTE_COMPONENT_INSTANCE__KNOWLEDGE_MANAGER = COMPONENT_INSTANCE__KNOWLEDGE_MANAGER;

	/**
	 * The number of structural features of the '<em>Remote Component Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOTE_COMPONENT_INSTANCE_FEATURE_COUNT = COMPONENT_INSTANCE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Remote Component Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOTE_COMPONENT_INSTANCE_OPERATION_COUNT = COMPONENT_INSTANCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '<em>Component Instance Metadata</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getComponentInstanceMetadata()
	 * @generated
	 */
	int COMPONENT_INSTANCE_METADATA = 5;

	/**
	 * The meta object id for the '<em>Ensemble Definition Metadata</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getEnsembleDefinitionMetadata()
	 * @generated
	 */
	int ENSEMBLE_DEFINITION_METADATA = 6;

	/**
	 * The meta object id for the '<em>Read Only Knowledge Manager</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getReadOnlyKnowledgeManager()
	 * @generated
	 */
	int READ_ONLY_KNOWLEDGE_MANAGER = 7;


	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture <em>Architecture</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Architecture</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture
	 * @generated
	 */
	EClass getArchitecture();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture#getEnsembleInstances <em>Ensemble Instances</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Ensemble Instances</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture#getEnsembleInstances()
	 * @see #getArchitecture()
	 * @generated
	 */
	EReference getArchitecture_EnsembleInstances();

	/**
	 * Returns the meta object for the containment reference list '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture#getComponentInstances <em>Component Instances</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Component Instances</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.Architecture#getComponentInstances()
	 * @see #getArchitecture()
	 * @generated
	 */
	EReference getArchitecture_ComponentInstances();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.LocalComponentInstance <em>Local Component Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Local Component Instance</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.LocalComponentInstance
	 * @generated
	 */
	EClass getLocalComponentInstance();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.LocalComponentInstance#getRuntimeInstance <em>Runtime Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Runtime Instance</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.LocalComponentInstance#getRuntimeInstance()
	 * @see #getLocalComponentInstance()
	 * @generated
	 */
	EAttribute getLocalComponentInstance_RuntimeInstance();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance <em>Ensemble Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ensemble Instance</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance
	 * @generated
	 */
	EClass getEnsembleInstance();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance#getEnsembleDefinition <em>Ensemble Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ensemble Definition</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance#getEnsembleDefinition()
	 * @see #getEnsembleInstance()
	 * @generated
	 */
	EAttribute getEnsembleInstance_EnsembleDefinition();

	/**
	 * Returns the meta object for the reference '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance#getCoordinator <em>Coordinator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Coordinator</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance#getCoordinator()
	 * @see #getEnsembleInstance()
	 * @generated
	 */
	EReference getEnsembleInstance_Coordinator();

	/**
	 * Returns the meta object for the reference list '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance#getMembers <em>Members</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Members</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.EnsembleInstance#getMembers()
	 * @see #getEnsembleInstance()
	 * @generated
	 */
	EReference getEnsembleInstance_Members();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance <em>Component Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Component Instance</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance
	 * @generated
	 */
	EClass getComponentInstance();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance#getId()
	 * @see #getComponentInstance()
	 * @generated
	 */
	EAttribute getComponentInstance_Id();

	/**
	 * Returns the meta object for the attribute '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance#getKnowledgeManager <em>Knowledge Manager</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Knowledge Manager</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.ComponentInstance#getKnowledgeManager()
	 * @see #getComponentInstance()
	 * @generated
	 */
	EAttribute getComponentInstance_KnowledgeManager();

	/**
	 * Returns the meta object for class '{@link cz.cuni.mff.d3s.deeco.model.architecture.api.RemoteComponentInstance <em>Remote Component Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Remote Component Instance</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.architecture.api.RemoteComponentInstance
	 * @generated
	 */
	EClass getRemoteComponentInstance();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance <em>Component Instance Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Component Instance Metadata</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance
	 * @model instanceClass="cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance"
	 * @generated
	 */
	EDataType getComponentInstanceMetadata();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition <em>Ensemble Definition Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Ensemble Definition Metadata</em>'.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition
	 * @model instanceClass="cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition"
	 * @generated
	 */
	EDataType getEnsembleDefinitionMetadata();

	/**
	 * Returns the meta object for data type '{@link cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager <em>Read Only Knowledge Manager</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Read Only Knowledge Manager</em>'.
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager
	 * @model instanceClass="cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager"
	 * @generated
	 */
	EDataType getReadOnlyKnowledgeManager();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ArchitectureFactory getArchitectureFactory();

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
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitectureImpl <em>Architecture</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitectureImpl
		 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getArchitecture()
		 * @generated
		 */
		EClass ARCHITECTURE = eINSTANCE.getArchitecture();

		/**
		 * The meta object literal for the '<em><b>Ensemble Instances</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ARCHITECTURE__ENSEMBLE_INSTANCES = eINSTANCE.getArchitecture_EnsembleInstances();

		/**
		 * The meta object literal for the '<em><b>Component Instances</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ARCHITECTURE__COMPONENT_INSTANCES = eINSTANCE.getArchitecture_ComponentInstances();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.LocalComponentInstanceImpl <em>Local Component Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.LocalComponentInstanceImpl
		 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getLocalComponentInstance()
		 * @generated
		 */
		EClass LOCAL_COMPONENT_INSTANCE = eINSTANCE.getLocalComponentInstance();

		/**
		 * The meta object literal for the '<em><b>Runtime Instance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCAL_COMPONENT_INSTANCE__RUNTIME_INSTANCE = eINSTANCE.getLocalComponentInstance_RuntimeInstance();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.EnsembleInstanceImpl <em>Ensemble Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.EnsembleInstanceImpl
		 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getEnsembleInstance()
		 * @generated
		 */
		EClass ENSEMBLE_INSTANCE = eINSTANCE.getEnsembleInstance();

		/**
		 * The meta object literal for the '<em><b>Ensemble Definition</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENSEMBLE_INSTANCE__ENSEMBLE_DEFINITION = eINSTANCE.getEnsembleInstance_EnsembleDefinition();

		/**
		 * The meta object literal for the '<em><b>Coordinator</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENSEMBLE_INSTANCE__COORDINATOR = eINSTANCE.getEnsembleInstance_Coordinator();

		/**
		 * The meta object literal for the '<em><b>Members</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENSEMBLE_INSTANCE__MEMBERS = eINSTANCE.getEnsembleInstance_Members();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.ComponentInstanceImpl <em>Component Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ComponentInstanceImpl
		 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getComponentInstance()
		 * @generated
		 */
		EClass COMPONENT_INSTANCE = eINSTANCE.getComponentInstance();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_INSTANCE__ID = eINSTANCE.getComponentInstance_Id();

		/**
		 * The meta object literal for the '<em><b>Knowledge Manager</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_INSTANCE__KNOWLEDGE_MANAGER = eINSTANCE.getComponentInstance_KnowledgeManager();

		/**
		 * The meta object literal for the '{@link cz.cuni.mff.d3s.deeco.model.architecture.impl.RemoteComponentInstanceImpl <em>Remote Component Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.RemoteComponentInstanceImpl
		 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getRemoteComponentInstance()
		 * @generated
		 */
		EClass REMOTE_COMPONENT_INSTANCE = eINSTANCE.getRemoteComponentInstance();

		/**
		 * The meta object literal for the '<em>Component Instance Metadata</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance
		 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getComponentInstanceMetadata()
		 * @generated
		 */
		EDataType COMPONENT_INSTANCE_METADATA = eINSTANCE.getComponentInstanceMetadata();

		/**
		 * The meta object literal for the '<em>Ensemble Definition Metadata</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition
		 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getEnsembleDefinitionMetadata()
		 * @generated
		 */
		EDataType ENSEMBLE_DEFINITION_METADATA = eINSTANCE.getEnsembleDefinitionMetadata();

		/**
		 * The meta object literal for the '<em>Read Only Knowledge Manager</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager
		 * @see cz.cuni.mff.d3s.deeco.model.architecture.impl.ArchitecturePackageImpl#getReadOnlyKnowledgeManager()
		 * @generated
		 */
		EDataType READ_ONLY_KNOWLEDGE_MANAGER = eINSTANCE.getReadOnlyKnowledgeManager();

	}

} //ArchitecturePackage
