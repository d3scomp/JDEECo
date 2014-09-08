/**
 */
package cz.cuni.mff.d3s.deeco.model.architecture.impl;

import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;

import cz.cuni.mff.d3s.deeco.model.architecture.api.*;

import cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitectureFactory;
import cz.cuni.mff.d3s.deeco.model.architecture.meta.ArchitecturePackage;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;

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
public class ArchitectureFactoryImpl extends EFactoryImpl implements ArchitectureFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ArchitectureFactory init() {
		try {
			ArchitectureFactory theArchitectureFactory = (ArchitectureFactory)EPackage.Registry.INSTANCE.getEFactory(ArchitecturePackage.eNS_URI);
			if (theArchitectureFactory != null) {
				return theArchitectureFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ArchitectureFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ArchitectureFactoryImpl() {
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
			case ArchitecturePackage.ARCHITECTURE: return createArchitecture();
			case ArchitecturePackage.LOCAL_COMPONENT_INSTANCE: return createLocalComponentInstance();
			case ArchitecturePackage.ENSEMBLE_INSTANCE: return createEnsembleInstance();
			case ArchitecturePackage.REMOTE_COMPONENT_INSTANCE: return createRemoteComponentInstance();
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
			case ArchitecturePackage.COMPONENT_INSTANCE_METADATA:
				return createComponentInstanceMetadataFromString(eDataType, initialValue);
			case ArchitecturePackage.ENSEMBLE_DEFINITION_METADATA:
				return createEnsembleDefinitionMetadataFromString(eDataType, initialValue);
			case ArchitecturePackage.READ_ONLY_KNOWLEDGE_MANAGER:
				return createReadOnlyKnowledgeManagerFromString(eDataType, initialValue);
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
			case ArchitecturePackage.COMPONENT_INSTANCE_METADATA:
				return convertComponentInstanceMetadataToString(eDataType, instanceValue);
			case ArchitecturePackage.ENSEMBLE_DEFINITION_METADATA:
				return convertEnsembleDefinitionMetadataToString(eDataType, instanceValue);
			case ArchitecturePackage.READ_ONLY_KNOWLEDGE_MANAGER:
				return convertReadOnlyKnowledgeManagerToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Architecture createArchitecture() {
		ArchitectureImpl architecture = new ArchitectureImpl();
		return architecture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalComponentInstance createLocalComponentInstance() {
		LocalComponentInstanceImpl localComponentInstance = new LocalComponentInstanceImpl();
		return localComponentInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnsembleInstance createEnsembleInstance() {
		EnsembleInstanceImpl ensembleInstance = new EnsembleInstanceImpl();
		return ensembleInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RemoteComponentInstance createRemoteComponentInstance() {
		RemoteComponentInstanceImpl remoteComponentInstance = new RemoteComponentInstanceImpl();
		return remoteComponentInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentInstance createComponentInstanceMetadataFromString(EDataType eDataType, String initialValue) {
		return (ComponentInstance)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertComponentInstanceMetadataToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnsembleDefinition createEnsembleDefinitionMetadataFromString(EDataType eDataType, String initialValue) {
		return (EnsembleDefinition)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEnsembleDefinitionMetadataToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReadOnlyKnowledgeManager createReadOnlyKnowledgeManagerFromString(EDataType eDataType, String initialValue) {
		return (ReadOnlyKnowledgeManager)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertReadOnlyKnowledgeManagerToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ArchitecturePackage getArchitecturePackage() {
		return (ArchitecturePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ArchitecturePackage getPackage() {
		return ArchitecturePackage.eINSTANCE;
	}

} //ArchitectureFactoryImpl
