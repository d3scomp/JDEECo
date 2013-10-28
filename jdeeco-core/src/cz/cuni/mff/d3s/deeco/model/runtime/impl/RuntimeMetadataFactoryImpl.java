/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.impl;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Component;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Condition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Ensemble;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange;
import cz.cuni.mff.d3s.deeco.model.runtime.api.InstanceEnsemblingController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.InstanceProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;

import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;

import java.lang.reflect.Method;

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
public class RuntimeMetadataFactoryImpl extends EFactoryImpl implements RuntimeMetadataFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static RuntimeMetadataFactory init() {
		try {
			RuntimeMetadataFactory theRuntimeMetadataFactory = (RuntimeMetadataFactory)EPackage.Registry.INSTANCE.getEFactory("http://cz.cuni.mff.d3s.deeco.model.runtime/1.0"); 
			if (theRuntimeMetadataFactory != null) {
				return theRuntimeMetadataFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuntimeMetadataFactoryImpl() {
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
			case RuntimeMetadataPackage.SCHEDULING_SPECIFICATION: return createSchedulingSpecification();
			case RuntimeMetadataPackage.KNOWLEDGE_CHANGE_TRIGGER: return createKnowledgeChangeTrigger();
			case RuntimeMetadataPackage.KNOWLEDGE_PATH: return createKnowledgePath();
			case RuntimeMetadataPackage.PATH_NODE_FIELD: return createPathNodeField();
			case RuntimeMetadataPackage.PATH_NODE_MAP_KEY: return createPathNodeMapKey();
			case RuntimeMetadataPackage.RUNTIME_METADATA: return createRuntimeMetadata();
			case RuntimeMetadataPackage.COMPONENT_INSTANCE: return createComponentInstance();
			case RuntimeMetadataPackage.COMPONENT: return createComponent();
			case RuntimeMetadataPackage.ENSEMBLE: return createEnsemble();
			case RuntimeMetadataPackage.CONDITION: return createCondition();
			case RuntimeMetadataPackage.EXCHANGE: return createExchange();
			case RuntimeMetadataPackage.PROCESS: return createProcess();
			case RuntimeMetadataPackage.PARAMETER: return createParameter();
			case RuntimeMetadataPackage.INVOCABLE: return createInvocable();
			case RuntimeMetadataPackage.INSTANCE_PROCESS: return createInstanceProcess();
			case RuntimeMetadataPackage.INSTANCE_ENSEMBLING_CONTROLLER: return createInstanceEnsemblingController();
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
			case RuntimeMetadataPackage.PARAMETER_DIRECTION:
				return createParameterDirectionFromString(eDataType, initialValue);
			case RuntimeMetadataPackage.METHOD:
				return createMethodFromString(eDataType, initialValue);
			case RuntimeMetadataPackage.KNOWLEDGE_MANAGER:
				return createKnowledgeManagerFromString(eDataType, initialValue);
			case RuntimeMetadataPackage.OTHER_KNOWLEDGE_MANAGERS_ACCESS:
				return createOtherKnowledgeManagersAccessFromString(eDataType, initialValue);
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
			case RuntimeMetadataPackage.PARAMETER_DIRECTION:
				return convertParameterDirectionToString(eDataType, instanceValue);
			case RuntimeMetadataPackage.METHOD:
				return convertMethodToString(eDataType, instanceValue);
			case RuntimeMetadataPackage.KNOWLEDGE_MANAGER:
				return convertKnowledgeManagerToString(eDataType, instanceValue);
			case RuntimeMetadataPackage.OTHER_KNOWLEDGE_MANAGERS_ACCESS:
				return convertOtherKnowledgeManagersAccessToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchedulingSpecification createSchedulingSpecification() {
		SchedulingSpecificationImpl schedulingSpecification = new SchedulingSpecificationImpl();
		return schedulingSpecification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgeChangeTrigger createKnowledgeChangeTrigger() {
		KnowledgeChangeTriggerImpl knowledgeChangeTrigger = new KnowledgeChangeTriggerImpl();
		return knowledgeChangeTrigger;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgePath createKnowledgePath() {
		KnowledgePathImpl knowledgePath = new KnowledgePathImpl();
		return knowledgePath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PathNodeField createPathNodeField() {
		PathNodeFieldImpl pathNodeField = new PathNodeFieldImpl();
		return pathNodeField;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PathNodeMapKey createPathNodeMapKey() {
		PathNodeMapKeyImpl pathNodeMapKey = new PathNodeMapKeyImpl();
		return pathNodeMapKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuntimeMetadata createRuntimeMetadata() {
		RuntimeMetadataImpl runtimeMetadata = new RuntimeMetadataImpl();
		return runtimeMetadata;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentInstance createComponentInstance() {
		ComponentInstanceImpl componentInstance = new ComponentInstanceImpl();
		return componentInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Component createComponent() {
		ComponentImpl component = new ComponentImpl();
		return component;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Ensemble createEnsemble() {
		EnsembleImpl ensemble = new EnsembleImpl();
		return ensemble;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Condition createCondition() {
		ConditionImpl condition = new ConditionImpl();
		return condition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Exchange createExchange() {
		ExchangeImpl exchange = new ExchangeImpl();
		return exchange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public cz.cuni.mff.d3s.deeco.model.runtime.api.Process createProcess() {
		ProcessImpl process = new ProcessImpl();
		return process;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Parameter createParameter() {
		ParameterImpl parameter = new ParameterImpl();
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Invocable createInvocable() {
		InvocableImpl invocable = new InvocableImpl();
		return invocable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InstanceProcess createInstanceProcess() {
		InstanceProcessImpl instanceProcess = new InstanceProcessImpl();
		return instanceProcess;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InstanceEnsemblingController createInstanceEnsemblingController() {
		InstanceEnsemblingControllerImpl instanceEnsemblingController = new InstanceEnsemblingControllerImpl();
		return instanceEnsemblingController;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterDirection createParameterDirectionFromString(EDataType eDataType, String initialValue) {
		ParameterDirection result = ParameterDirection.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertParameterDirectionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Method createMethodFromString(EDataType eDataType, String initialValue) {
		return (Method)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMethodToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgeManager createKnowledgeManagerFromString(EDataType eDataType, String initialValue) {
		return (KnowledgeManager)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertKnowledgeManagerToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createOtherKnowledgeManagersAccessFromString(EDataType eDataType, String initialValue) {
		return super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOtherKnowledgeManagersAccessToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuntimeMetadataPackage getRuntimeMetadataPackage() {
		return (RuntimeMetadataPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static RuntimeMetadataPackage getPackage() {
		return RuntimeMetadataPackage.eINSTANCE;
	}

} //RuntimeMetadataFactoryImpl
