/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl;

import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.AddComponentCommand;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeExchange;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeTypeOwner;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ListValueType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MapValueType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MembershipCondition;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Model;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.NestedKnowledgeDefinition;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterChangedTrigger;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterKind;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.PeriodicScheduling;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelFactory;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.SchedulingType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeValueType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TopLevelKnowledgeDefinition;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TriggeredScheduling;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TypeParameter;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.UnstructuredValueType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.UpdateKnowledgeStructureCommand;

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
public class RuntimemodelFactoryImpl extends EFactoryImpl implements RuntimemodelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static RuntimemodelFactory init() {
		try {
			RuntimemodelFactory theRuntimemodelFactory = (RuntimemodelFactory)EPackage.Registry.INSTANCE.getEFactory("http://runtimemetadata/1.0"); 
			if (theRuntimemodelFactory != null) {
				return theRuntimemodelFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new RuntimemodelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuntimemodelFactoryImpl() {
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
			case RuntimemodelPackage.COMPONENT: return createComponent();
			case RuntimemodelPackage.PROCESS: return createProcess();
			case RuntimemodelPackage.STRUCTURED_KNOWLEDGE_VALUE_TYPE: return createStructuredKnowledgeValueType();
			case RuntimemodelPackage.LIST_VALUE_TYPE: return createListValueType();
			case RuntimemodelPackage.MAP_VALUE_TYPE: return createMapValueType();
			case RuntimemodelPackage.UNSTRUCTURED_VALUE_TYPE: return createUnstructuredValueType();
			case RuntimemodelPackage.MODEL: return createModel();
			case RuntimemodelPackage.UPDATE_KNOWLEDGE_STRUCTURE_COMMAND: return createUpdateKnowledgeStructureCommand();
			case RuntimemodelPackage.ADD_COMPONENT_COMMAND: return createAddComponentCommand();
			case RuntimemodelPackage.METHOD_PARAMETER: return createMethodParameter();
			case RuntimemodelPackage.PERIODIC_SCHEDULING: return createPeriodicScheduling();
			case RuntimemodelPackage.TRIGGERED_SCHEDULING: return createTriggeredScheduling();
			case RuntimemodelPackage.PARAMETER_CHANGED_TRIGGER: return createParameterChangedTrigger();
			case RuntimemodelPackage.ENSEMBLE: return createEnsemble();
			case RuntimemodelPackage.TOP_LEVEL_KNOWLEDGE_DEFINITION: return createTopLevelKnowledgeDefinition();
			case RuntimemodelPackage.NESTED_KNOWLEDGE_DEFINITION: return createNestedKnowledgeDefinition();
			case RuntimemodelPackage.MEMBERSHIP_CONDITION: return createMembershipCondition();
			case RuntimemodelPackage.KNOWLEDGE_EXCHANGE: return createKnowledgeExchange();
			case RuntimemodelPackage.TYPE_PARAMETER: return createTypeParameter();
			case RuntimemodelPackage.KNOWLEDGE_TYPE_OWNER: return createKnowledgeTypeOwner();
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
			case RuntimemodelPackage.STRUCTURED_KNOWLEDGE_TYPE:
				return createStructuredKnowledgeTypeFromString(eDataType, initialValue);
			case RuntimemodelPackage.PARAMETER_KIND:
				return createParameterKindFromString(eDataType, initialValue);
			case RuntimemodelPackage.SCHEDULING_TYPE:
				return createSchedulingTypeFromString(eDataType, initialValue);
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
			case RuntimemodelPackage.STRUCTURED_KNOWLEDGE_TYPE:
				return convertStructuredKnowledgeTypeToString(eDataType, instanceValue);
			case RuntimemodelPackage.PARAMETER_KIND:
				return convertParameterKindToString(eDataType, instanceValue);
			case RuntimemodelPackage.SCHEDULING_TYPE:
				return convertSchedulingTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
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
	public cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process createProcess() {
		ProcessImpl process = new ProcessImpl();
		return process;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StructuredKnowledgeValueType createStructuredKnowledgeValueType() {
		StructuredKnowledgeValueTypeImpl structuredKnowledgeValueType = new StructuredKnowledgeValueTypeImpl();
		return structuredKnowledgeValueType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ListValueType createListValueType() {
		ListValueTypeImpl listValueType = new ListValueTypeImpl();
		return listValueType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MapValueType createMapValueType() {
		MapValueTypeImpl mapValueType = new MapValueTypeImpl();
		return mapValueType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UnstructuredValueType createUnstructuredValueType() {
		UnstructuredValueTypeImpl unstructuredValueType = new UnstructuredValueTypeImpl();
		return unstructuredValueType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Model createModel() {
		ModelImpl model = new ModelImpl();
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UpdateKnowledgeStructureCommand createUpdateKnowledgeStructureCommand() {
		UpdateKnowledgeStructureCommandImpl updateKnowledgeStructureCommand = new UpdateKnowledgeStructureCommandImpl();
		return updateKnowledgeStructureCommand;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AddComponentCommand createAddComponentCommand() {
		AddComponentCommandImpl addComponentCommand = new AddComponentCommandImpl();
		return addComponentCommand;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MethodParameter createMethodParameter() {
		MethodParameterImpl methodParameter = new MethodParameterImpl();
		return methodParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PeriodicScheduling createPeriodicScheduling() {
		PeriodicSchedulingImpl periodicScheduling = new PeriodicSchedulingImpl();
		return periodicScheduling;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TriggeredScheduling createTriggeredScheduling() {
		TriggeredSchedulingImpl triggeredScheduling = new TriggeredSchedulingImpl();
		return triggeredScheduling;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterChangedTrigger createParameterChangedTrigger() {
		ParameterChangedTriggerImpl parameterChangedTrigger = new ParameterChangedTriggerImpl();
		return parameterChangedTrigger;
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
	public TopLevelKnowledgeDefinition createTopLevelKnowledgeDefinition() {
		TopLevelKnowledgeDefinitionImpl topLevelKnowledgeDefinition = new TopLevelKnowledgeDefinitionImpl();
		return topLevelKnowledgeDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NestedKnowledgeDefinition createNestedKnowledgeDefinition() {
		NestedKnowledgeDefinitionImpl nestedKnowledgeDefinition = new NestedKnowledgeDefinitionImpl();
		return nestedKnowledgeDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MembershipCondition createMembershipCondition() {
		MembershipConditionImpl membershipCondition = new MembershipConditionImpl();
		return membershipCondition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgeExchange createKnowledgeExchange() {
		KnowledgeExchangeImpl knowledgeExchange = new KnowledgeExchangeImpl();
		return knowledgeExchange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TypeParameter createTypeParameter() {
		TypeParameterImpl typeParameter = new TypeParameterImpl();
		return typeParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgeTypeOwner createKnowledgeTypeOwner() {
		KnowledgeTypeOwnerImpl knowledgeTypeOwner = new KnowledgeTypeOwnerImpl();
		return knowledgeTypeOwner;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StructuredKnowledgeType createStructuredKnowledgeTypeFromString(EDataType eDataType, String initialValue) {
		StructuredKnowledgeType result = StructuredKnowledgeType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertStructuredKnowledgeTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterKind createParameterKindFromString(EDataType eDataType, String initialValue) {
		ParameterKind result = ParameterKind.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertParameterKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchedulingType createSchedulingTypeFromString(EDataType eDataType, String initialValue) {
		SchedulingType result = SchedulingType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSchedulingTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuntimemodelPackage getRuntimemodelPackage() {
		return (RuntimemodelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static RuntimemodelPackage getPackage() {
		return RuntimemodelPackage.eINSTANCE;
	}

} //RuntimemodelFactoryImpl
