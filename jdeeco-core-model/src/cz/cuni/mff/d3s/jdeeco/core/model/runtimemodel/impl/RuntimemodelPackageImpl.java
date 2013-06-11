/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.impl;

import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.AddComponentCommand;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeDefinition;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeExchange;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeReference;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeTypeOwner;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ListValueType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MapValueType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MembershipCondition;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Model;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ModelUpdateCommand;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.NestedKnowledgeDefinition;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterChangedTrigger;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterKind;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParametricKnowledgeType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.PeriodicScheduling;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelFactory;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Schedulable;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Scheduling;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.SchedulingType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeValueType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TopLevelKnowledgeDefinition;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Trigger;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TriggeredScheduling;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TypeParameter;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.UnstructuredValueType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.UpdateKnowledgeStructureCommand;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
public class RuntimemodelPackageImpl extends EPackageImpl implements RuntimemodelPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass componentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass knowledgeDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass knowledgeTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass processEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass schedulingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass structuredKnowledgeValueTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass listValueTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mapValueTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass unstructuredValueTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass modelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass modelUpdateCommandEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass updateKnowledgeStructureCommandEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass addComponentCommandEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass methodParameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parameterizedMethodEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass periodicSchedulingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass triggeredSchedulingEClass = null;

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
	private EClass parameterChangedTriggerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ensembleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass topLevelKnowledgeDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nestedKnowledgeDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass knowledgeReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass membershipConditionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass knowledgeExchangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass schedulableEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass typeParameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parametricKnowledgeTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass knowledgeTypeOwnerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum structuredKnowledgeTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum parameterKindEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum schedulingTypeEEnum = null;

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
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private RuntimemodelPackageImpl() {
		super(eNS_URI, RuntimemodelFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link RuntimemodelPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static RuntimemodelPackage init() {
		if (isInited) return (RuntimemodelPackage)EPackage.Registry.INSTANCE.getEPackage(RuntimemodelPackage.eNS_URI);

		// Obtain or create and register package
		RuntimemodelPackageImpl theRuntimemodelPackage = (RuntimemodelPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof RuntimemodelPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new RuntimemodelPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theRuntimemodelPackage.createPackageContents();

		// Initialize created meta-data
		theRuntimemodelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theRuntimemodelPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(RuntimemodelPackage.eNS_URI, theRuntimemodelPackage);
		return theRuntimemodelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getComponent() {
		return componentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComponent_Knowledge() {
		return (EReference)componentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComponent_Processes() {
		return (EReference)componentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getComponent_Name() {
		return (EAttribute)componentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKnowledgeDefinition() {
		return knowledgeDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKnowledgeDefinition_IsLocal() {
		return (EAttribute)knowledgeDefinitionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKnowledgeType() {
		return knowledgeTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKnowledgeType_IsStructured() {
		return (EAttribute)knowledgeTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKnowledgeType_IsWrapper() {
		return (EAttribute)knowledgeTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKnowledgeType_Clazz() {
		return (EAttribute)knowledgeTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getKnowledgeType_Owner() {
		return (EReference)knowledgeTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProcess() {
		return processEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProcess_Name() {
		return (EAttribute)processEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProcess_Component() {
		return (EReference)processEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getScheduling() {
		return schedulingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getScheduling_Owner() {
		return (EReference)schedulingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStructuredKnowledgeValueType() {
		return structuredKnowledgeValueTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStructuredKnowledgeValueType_Children() {
		return (EReference)structuredKnowledgeValueTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getListValueType() {
		return listValueTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getListValueType_TypeParameter() {
		return (EReference)listValueTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMapValueType() {
		return mapValueTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMapValueType_KeyTypeParameter() {
		return (EReference)mapValueTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMapValueType_ValueTypeParameter() {
		return (EReference)mapValueTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUnstructuredValueType() {
		return unstructuredValueTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getModel() {
		return modelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModel_Components() {
		return (EReference)modelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getModel_Ensembles() {
		return (EReference)modelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getModelUpdateCommand() {
		return modelUpdateCommandEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUpdateKnowledgeStructureCommand() {
		return updateKnowledgeStructureCommandEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAddComponentCommand() {
		return addComponentCommandEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMethodParameter() {
		return methodParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMethodParameter_Kind() {
		return (EAttribute)methodParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMethodParameter_KnowledgePath() {
		return (EAttribute)methodParameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMethodParameter_Index() {
		return (EAttribute)methodParameterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMethodParameter_Owner() {
		return (EReference)methodParameterEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParameterizedMethod() {
		return parameterizedMethodEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameterizedMethod_DeclaringClass() {
		return (EAttribute)parameterizedMethodEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameterizedMethod_MethodName() {
		return (EAttribute)parameterizedMethodEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameterizedMethod_FormalParameters() {
		return (EReference)parameterizedMethodEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameterizedMethod_InParameters() {
		return (EReference)parameterizedMethodEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameterizedMethod_OutParameters() {
		return (EReference)parameterizedMethodEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameterizedMethod_InOutParameters() {
		return (EReference)parameterizedMethodEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPeriodicScheduling() {
		return periodicSchedulingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPeriodicScheduling_Period() {
		return (EAttribute)periodicSchedulingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTriggeredScheduling() {
		return triggeredSchedulingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTriggeredScheduling_Triggers() {
		return (EReference)triggeredSchedulingEClass.getEStructuralFeatures().get(0);
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
	public EReference getTrigger_Owner() {
		return (EReference)triggerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParameterChangedTrigger() {
		return parameterChangedTriggerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameterChangedTrigger_Parameters() {
		return (EReference)parameterChangedTriggerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnsemble() {
		return ensembleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsemble_Membership() {
		return (EReference)ensembleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsemble_KnowledgeExchange() {
		return (EReference)ensembleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTopLevelKnowledgeDefinition() {
		return topLevelKnowledgeDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTopLevelKnowledgeDefinition_Component() {
		return (EReference)topLevelKnowledgeDefinitionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNestedKnowledgeDefinition() {
		return nestedKnowledgeDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNestedKnowledgeDefinition_Parent() {
		return (EReference)nestedKnowledgeDefinitionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKnowledgeReference() {
		return knowledgeReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getKnowledgeReference_Name() {
		return (EAttribute)knowledgeReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMembershipCondition() {
		return membershipConditionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMembershipCondition_Ensemble() {
		return (EReference)membershipConditionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKnowledgeExchange() {
		return knowledgeExchangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getKnowledgeExchange_Ensemble() {
		return (EReference)knowledgeExchangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSchedulable() {
		return schedulableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSchedulable_Scheduling() {
		return (EReference)schedulableEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTypeParameter() {
		return typeParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTypeParameter_Owner() {
		return (EReference)typeParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParametricKnowledgeType() {
		return parametricKnowledgeTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKnowledgeTypeOwner() {
		return knowledgeTypeOwnerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getKnowledgeTypeOwner_Type() {
		return (EReference)knowledgeTypeOwnerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getStructuredKnowledgeType() {
		return structuredKnowledgeTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getParameterKind() {
		return parameterKindEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSchedulingType() {
		return schedulingTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuntimemodelFactory getRuntimemodelFactory() {
		return (RuntimemodelFactory)getEFactoryInstance();
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
		componentEClass = createEClass(COMPONENT);
		createEReference(componentEClass, COMPONENT__KNOWLEDGE);
		createEReference(componentEClass, COMPONENT__PROCESSES);
		createEAttribute(componentEClass, COMPONENT__NAME);

		knowledgeDefinitionEClass = createEClass(KNOWLEDGE_DEFINITION);
		createEAttribute(knowledgeDefinitionEClass, KNOWLEDGE_DEFINITION__IS_LOCAL);

		knowledgeTypeEClass = createEClass(KNOWLEDGE_TYPE);
		createEAttribute(knowledgeTypeEClass, KNOWLEDGE_TYPE__IS_STRUCTURED);
		createEAttribute(knowledgeTypeEClass, KNOWLEDGE_TYPE__IS_WRAPPER);
		createEAttribute(knowledgeTypeEClass, KNOWLEDGE_TYPE__CLAZZ);
		createEReference(knowledgeTypeEClass, KNOWLEDGE_TYPE__OWNER);

		processEClass = createEClass(PROCESS);
		createEAttribute(processEClass, PROCESS__NAME);
		createEReference(processEClass, PROCESS__COMPONENT);

		schedulingEClass = createEClass(SCHEDULING);
		createEReference(schedulingEClass, SCHEDULING__OWNER);

		structuredKnowledgeValueTypeEClass = createEClass(STRUCTURED_KNOWLEDGE_VALUE_TYPE);
		createEReference(structuredKnowledgeValueTypeEClass, STRUCTURED_KNOWLEDGE_VALUE_TYPE__CHILDREN);

		listValueTypeEClass = createEClass(LIST_VALUE_TYPE);
		createEReference(listValueTypeEClass, LIST_VALUE_TYPE__TYPE_PARAMETER);

		mapValueTypeEClass = createEClass(MAP_VALUE_TYPE);
		createEReference(mapValueTypeEClass, MAP_VALUE_TYPE__KEY_TYPE_PARAMETER);
		createEReference(mapValueTypeEClass, MAP_VALUE_TYPE__VALUE_TYPE_PARAMETER);

		unstructuredValueTypeEClass = createEClass(UNSTRUCTURED_VALUE_TYPE);

		modelEClass = createEClass(MODEL);
		createEReference(modelEClass, MODEL__COMPONENTS);
		createEReference(modelEClass, MODEL__ENSEMBLES);

		modelUpdateCommandEClass = createEClass(MODEL_UPDATE_COMMAND);

		updateKnowledgeStructureCommandEClass = createEClass(UPDATE_KNOWLEDGE_STRUCTURE_COMMAND);

		addComponentCommandEClass = createEClass(ADD_COMPONENT_COMMAND);

		methodParameterEClass = createEClass(METHOD_PARAMETER);
		createEAttribute(methodParameterEClass, METHOD_PARAMETER__KIND);
		createEAttribute(methodParameterEClass, METHOD_PARAMETER__KNOWLEDGE_PATH);
		createEAttribute(methodParameterEClass, METHOD_PARAMETER__INDEX);
		createEReference(methodParameterEClass, METHOD_PARAMETER__OWNER);

		parameterizedMethodEClass = createEClass(PARAMETERIZED_METHOD);
		createEAttribute(parameterizedMethodEClass, PARAMETERIZED_METHOD__DECLARING_CLASS);
		createEAttribute(parameterizedMethodEClass, PARAMETERIZED_METHOD__METHOD_NAME);
		createEReference(parameterizedMethodEClass, PARAMETERIZED_METHOD__FORMAL_PARAMETERS);
		createEReference(parameterizedMethodEClass, PARAMETERIZED_METHOD__IN_PARAMETERS);
		createEReference(parameterizedMethodEClass, PARAMETERIZED_METHOD__OUT_PARAMETERS);
		createEReference(parameterizedMethodEClass, PARAMETERIZED_METHOD__IN_OUT_PARAMETERS);

		periodicSchedulingEClass = createEClass(PERIODIC_SCHEDULING);
		createEAttribute(periodicSchedulingEClass, PERIODIC_SCHEDULING__PERIOD);

		triggeredSchedulingEClass = createEClass(TRIGGERED_SCHEDULING);
		createEReference(triggeredSchedulingEClass, TRIGGERED_SCHEDULING__TRIGGERS);

		triggerEClass = createEClass(TRIGGER);
		createEReference(triggerEClass, TRIGGER__OWNER);

		parameterChangedTriggerEClass = createEClass(PARAMETER_CHANGED_TRIGGER);
		createEReference(parameterChangedTriggerEClass, PARAMETER_CHANGED_TRIGGER__PARAMETERS);

		ensembleEClass = createEClass(ENSEMBLE);
		createEReference(ensembleEClass, ENSEMBLE__MEMBERSHIP);
		createEReference(ensembleEClass, ENSEMBLE__KNOWLEDGE_EXCHANGE);

		topLevelKnowledgeDefinitionEClass = createEClass(TOP_LEVEL_KNOWLEDGE_DEFINITION);
		createEReference(topLevelKnowledgeDefinitionEClass, TOP_LEVEL_KNOWLEDGE_DEFINITION__COMPONENT);

		nestedKnowledgeDefinitionEClass = createEClass(NESTED_KNOWLEDGE_DEFINITION);
		createEReference(nestedKnowledgeDefinitionEClass, NESTED_KNOWLEDGE_DEFINITION__PARENT);

		knowledgeReferenceEClass = createEClass(KNOWLEDGE_REFERENCE);
		createEAttribute(knowledgeReferenceEClass, KNOWLEDGE_REFERENCE__NAME);

		membershipConditionEClass = createEClass(MEMBERSHIP_CONDITION);
		createEReference(membershipConditionEClass, MEMBERSHIP_CONDITION__ENSEMBLE);

		knowledgeExchangeEClass = createEClass(KNOWLEDGE_EXCHANGE);
		createEReference(knowledgeExchangeEClass, KNOWLEDGE_EXCHANGE__ENSEMBLE);

		schedulableEClass = createEClass(SCHEDULABLE);
		createEReference(schedulableEClass, SCHEDULABLE__SCHEDULING);

		typeParameterEClass = createEClass(TYPE_PARAMETER);
		createEReference(typeParameterEClass, TYPE_PARAMETER__OWNER);

		parametricKnowledgeTypeEClass = createEClass(PARAMETRIC_KNOWLEDGE_TYPE);

		knowledgeTypeOwnerEClass = createEClass(KNOWLEDGE_TYPE_OWNER);
		createEReference(knowledgeTypeOwnerEClass, KNOWLEDGE_TYPE_OWNER__TYPE);

		// Create enums
		structuredKnowledgeTypeEEnum = createEEnum(STRUCTURED_KNOWLEDGE_TYPE);
		parameterKindEEnum = createEEnum(PARAMETER_KIND);
		schedulingTypeEEnum = createEEnum(SCHEDULING_TYPE);
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
		knowledgeDefinitionEClass.getESuperTypes().add(this.getKnowledgeReference());
		processEClass.getESuperTypes().add(this.getParameterizedMethod());
		processEClass.getESuperTypes().add(this.getSchedulable());
		structuredKnowledgeValueTypeEClass.getESuperTypes().add(this.getKnowledgeType());
		listValueTypeEClass.getESuperTypes().add(this.getKnowledgeType());
		listValueTypeEClass.getESuperTypes().add(this.getParametricKnowledgeType());
		mapValueTypeEClass.getESuperTypes().add(this.getKnowledgeType());
		mapValueTypeEClass.getESuperTypes().add(this.getParametricKnowledgeType());
		unstructuredValueTypeEClass.getESuperTypes().add(this.getKnowledgeType());
		updateKnowledgeStructureCommandEClass.getESuperTypes().add(this.getModelUpdateCommand());
		addComponentCommandEClass.getESuperTypes().add(this.getModelUpdateCommand());
		methodParameterEClass.getESuperTypes().add(this.getKnowledgeReference());
		periodicSchedulingEClass.getESuperTypes().add(this.getScheduling());
		triggeredSchedulingEClass.getESuperTypes().add(this.getScheduling());
		parameterChangedTriggerEClass.getESuperTypes().add(this.getTrigger());
		ensembleEClass.getESuperTypes().add(this.getSchedulable());
		topLevelKnowledgeDefinitionEClass.getESuperTypes().add(this.getKnowledgeDefinition());
		nestedKnowledgeDefinitionEClass.getESuperTypes().add(this.getKnowledgeDefinition());
		knowledgeReferenceEClass.getESuperTypes().add(this.getKnowledgeTypeOwner());
		membershipConditionEClass.getESuperTypes().add(this.getParameterizedMethod());
		knowledgeExchangeEClass.getESuperTypes().add(this.getParameterizedMethod());
		typeParameterEClass.getESuperTypes().add(this.getKnowledgeTypeOwner());

		// Initialize classes and features; add operations and parameters
		initEClass(componentEClass, Component.class, "Component", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getComponent_Knowledge(), this.getTopLevelKnowledgeDefinition(), this.getTopLevelKnowledgeDefinition_Component(), "knowledge", null, 0, -1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getComponent_Processes(), this.getProcess(), this.getProcess_Component(), "processes", null, 0, -1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComponent_Name(), ecorePackage.getEString(), "name", null, 0, 1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(componentEClass, this.getKnowledgeDefinition(), "findDefinition", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "path", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(knowledgeDefinitionEClass, KnowledgeDefinition.class, "KnowledgeDefinition", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getKnowledgeDefinition_IsLocal(), ecorePackage.getEBoolean(), "isLocal", null, 1, 1, KnowledgeDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(knowledgeTypeEClass, KnowledgeType.class, "KnowledgeType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getKnowledgeType_IsStructured(), ecorePackage.getEBoolean(), "isStructured", null, 1, 1, KnowledgeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getKnowledgeType_IsWrapper(), ecorePackage.getEBoolean(), "isWrapper", null, 1, 1, KnowledgeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getKnowledgeType_Clazz(), ecorePackage.getEJavaClass(), "clazz", null, 1, 1, KnowledgeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getKnowledgeType_Owner(), this.getKnowledgeTypeOwner(), this.getKnowledgeTypeOwner_Type(), "owner", null, 1, 1, KnowledgeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(knowledgeTypeEClass, ecorePackage.getEJavaObject(), "createFromRaw", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEJavaObject(), "rawValue", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(knowledgeTypeEClass, ecorePackage.getEJavaObject(), "storeToRaw", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEJavaObject(), "value", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(processEClass, cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process.class, "Process", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProcess_Name(), ecorePackage.getEString(), "name", null, 1, 1, cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProcess_Component(), this.getComponent(), this.getComponent_Processes(), "component", null, 1, 1, cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(schedulingEClass, Scheduling.class, "Scheduling", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getScheduling_Owner(), this.getSchedulable(), this.getSchedulable_Scheduling(), "owner", null, 1, 1, Scheduling.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(structuredKnowledgeValueTypeEClass, StructuredKnowledgeValueType.class, "StructuredKnowledgeValueType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getStructuredKnowledgeValueType_Children(), this.getNestedKnowledgeDefinition(), this.getNestedKnowledgeDefinition_Parent(), "children", null, 0, -1, StructuredKnowledgeValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(listValueTypeEClass, ListValueType.class, "ListValueType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getListValueType_TypeParameter(), this.getTypeParameter(), null, "typeParameter", null, 1, 1, ListValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mapValueTypeEClass, MapValueType.class, "MapValueType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMapValueType_KeyTypeParameter(), this.getTypeParameter(), null, "keyTypeParameter", null, 1, 1, MapValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMapValueType_ValueTypeParameter(), this.getTypeParameter(), null, "valueTypeParameter", null, 1, 1, MapValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(unstructuredValueTypeEClass, UnstructuredValueType.class, "UnstructuredValueType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(modelEClass, Model.class, "Model", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getModel_Components(), this.getComponent(), null, "components", null, 0, -1, Model.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModel_Ensembles(), this.getEnsemble(), null, "ensembles", null, 0, -1, Model.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(modelEClass, null, "update", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getModelUpdateCommand(), "updateCommand", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(modelUpdateCommandEClass, ModelUpdateCommand.class, "ModelUpdateCommand", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = addEOperation(modelUpdateCommandEClass, null, "do_", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getModel(), "model", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(updateKnowledgeStructureCommandEClass, UpdateKnowledgeStructureCommand.class, "UpdateKnowledgeStructureCommand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(addComponentCommandEClass, AddComponentCommand.class, "AddComponentCommand", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(methodParameterEClass, MethodParameter.class, "MethodParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMethodParameter_Kind(), this.getParameterKind(), "kind", null, 1, 1, MethodParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMethodParameter_KnowledgePath(), ecorePackage.getEString(), "knowledgePath", null, 1, 1, MethodParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMethodParameter_Index(), ecorePackage.getEIntegerObject(), "index", null, 1, 1, MethodParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMethodParameter_Owner(), this.getParameterizedMethod(), this.getParameterizedMethod_FormalParameters(), "owner", null, 1, 1, MethodParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parameterizedMethodEClass, ParameterizedMethod.class, "ParameterizedMethod", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getParameterizedMethod_DeclaringClass(), ecorePackage.getEJavaClass(), "declaringClass", null, 1, 1, ParameterizedMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameterizedMethod_MethodName(), ecorePackage.getEString(), "methodName", null, 1, 1, ParameterizedMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getParameterizedMethod_FormalParameters(), this.getMethodParameter(), this.getMethodParameter_Owner(), "formalParameters", null, 0, -1, ParameterizedMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getParameterizedMethod_InParameters(), this.getMethodParameter(), null, "inParameters", null, 0, -1, ParameterizedMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getParameterizedMethod_OutParameters(), this.getMethodParameter(), null, "outParameters", null, 0, -1, ParameterizedMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getParameterizedMethod_InOutParameters(), this.getMethodParameter(), null, "inOutParameters", null, 0, -1, ParameterizedMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(parameterizedMethodEClass, ecorePackage.getEJavaObject(), "invoke", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(periodicSchedulingEClass, PeriodicScheduling.class, "PeriodicScheduling", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPeriodicScheduling_Period(), ecorePackage.getEIntegerObject(), "period", null, 1, 1, PeriodicScheduling.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(triggeredSchedulingEClass, TriggeredScheduling.class, "TriggeredScheduling", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTriggeredScheduling_Triggers(), this.getTrigger(), this.getTrigger_Owner(), "triggers", null, 1, -1, TriggeredScheduling.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(triggerEClass, Trigger.class, "Trigger", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTrigger_Owner(), this.getTriggeredScheduling(), this.getTriggeredScheduling_Triggers(), "owner", null, 1, 1, Trigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parameterChangedTriggerEClass, ParameterChangedTrigger.class, "ParameterChangedTrigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getParameterChangedTrigger_Parameters(), this.getMethodParameter(), null, "parameters", null, 1, -1, ParameterChangedTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(ensembleEClass, Ensemble.class, "Ensemble", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEnsemble_Membership(), this.getMembershipCondition(), this.getMembershipCondition_Ensemble(), "membership", null, 1, 1, Ensemble.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnsemble_KnowledgeExchange(), this.getKnowledgeExchange(), this.getKnowledgeExchange_Ensemble(), "knowledgeExchange", null, 1, 1, Ensemble.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(topLevelKnowledgeDefinitionEClass, TopLevelKnowledgeDefinition.class, "TopLevelKnowledgeDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTopLevelKnowledgeDefinition_Component(), this.getComponent(), this.getComponent_Knowledge(), "component", null, 1, 1, TopLevelKnowledgeDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(nestedKnowledgeDefinitionEClass, NestedKnowledgeDefinition.class, "NestedKnowledgeDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNestedKnowledgeDefinition_Parent(), this.getStructuredKnowledgeValueType(), this.getStructuredKnowledgeValueType_Children(), "parent", null, 1, 1, NestedKnowledgeDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(knowledgeReferenceEClass, KnowledgeReference.class, "KnowledgeReference", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getKnowledgeReference_Name(), ecorePackage.getEString(), "name", null, 1, 1, KnowledgeReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(knowledgeReferenceEClass, ecorePackage.getEJavaObject(), "load", 1, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(knowledgeReferenceEClass, null, "store", 1, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEJavaObject(), "value", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(membershipConditionEClass, MembershipCondition.class, "MembershipCondition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMembershipCondition_Ensemble(), this.getEnsemble(), this.getEnsemble_Membership(), "ensemble", null, 1, 1, MembershipCondition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(knowledgeExchangeEClass, KnowledgeExchange.class, "KnowledgeExchange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getKnowledgeExchange_Ensemble(), this.getEnsemble(), this.getEnsemble_KnowledgeExchange(), "ensemble", null, 1, 1, KnowledgeExchange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(schedulableEClass, Schedulable.class, "Schedulable", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSchedulable_Scheduling(), this.getScheduling(), this.getScheduling_Owner(), "scheduling", null, 1, 1, Schedulable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(typeParameterEClass, TypeParameter.class, "TypeParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTypeParameter_Owner(), this.getParametricKnowledgeType(), null, "owner", null, 1, 1, TypeParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parametricKnowledgeTypeEClass, ParametricKnowledgeType.class, "ParametricKnowledgeType", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(knowledgeTypeOwnerEClass, KnowledgeTypeOwner.class, "KnowledgeTypeOwner", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getKnowledgeTypeOwner_Type(), this.getKnowledgeType(), this.getKnowledgeType_Owner(), "type", null, 1, 1, KnowledgeTypeOwner.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(structuredKnowledgeTypeEEnum, StructuredKnowledgeType.class, "StructuredKnowledgeType");
		addEEnumLiteral(structuredKnowledgeTypeEEnum, StructuredKnowledgeType.KNOWLEDGE);
		addEEnumLiteral(structuredKnowledgeTypeEEnum, StructuredKnowledgeType.LIST);
		addEEnumLiteral(structuredKnowledgeTypeEEnum, StructuredKnowledgeType.MAP);
		addEEnumLiteral(structuredKnowledgeTypeEEnum, StructuredKnowledgeType.UNSTRUCTURED);

		initEEnum(parameterKindEEnum, ParameterKind.class, "ParameterKind");
		addEEnumLiteral(parameterKindEEnum, ParameterKind.IN);
		addEEnumLiteral(parameterKindEEnum, ParameterKind.OUT);
		addEEnumLiteral(parameterKindEEnum, ParameterKind.INOUT);

		initEEnum(schedulingTypeEEnum, SchedulingType.class, "SchedulingType");
		addEEnumLiteral(schedulingTypeEEnum, SchedulingType.PERIODIC);
		addEEnumLiteral(schedulingTypeEEnum, SchedulingType.TRIGGERED);

		// Create resource
		createResource(eNS_URI);
	}

} //RuntimemodelPackageImpl
