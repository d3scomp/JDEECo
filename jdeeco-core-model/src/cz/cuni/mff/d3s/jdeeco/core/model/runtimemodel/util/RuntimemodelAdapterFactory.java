/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.util;

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
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParametricKnowledgeType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.PeriodicScheduling;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Schedulable;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Scheduling;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeValueType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TopLevelKnowledgeDefinition;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Trigger;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TriggeredScheduling;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TypeParameter;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.UnstructuredValueType;
import cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.UpdateKnowledgeStructureCommand;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage
 * @generated
 */
public class RuntimemodelAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static RuntimemodelPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuntimemodelAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = RuntimemodelPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RuntimemodelSwitch<Adapter> modelSwitch =
		new RuntimemodelSwitch<Adapter>() {
			@Override
			public Adapter caseComponent(Component object) {
				return createComponentAdapter();
			}
			@Override
			public Adapter caseKnowledgeDefinition(KnowledgeDefinition object) {
				return createKnowledgeDefinitionAdapter();
			}
			@Override
			public Adapter caseKnowledgeType(KnowledgeType object) {
				return createKnowledgeTypeAdapter();
			}
			@Override
			public Adapter caseProcess(cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process object) {
				return createProcessAdapter();
			}
			@Override
			public Adapter caseScheduling(Scheduling object) {
				return createSchedulingAdapter();
			}
			@Override
			public Adapter caseStructuredKnowledgeValueType(StructuredKnowledgeValueType object) {
				return createStructuredKnowledgeValueTypeAdapter();
			}
			@Override
			public Adapter caseListValueType(ListValueType object) {
				return createListValueTypeAdapter();
			}
			@Override
			public Adapter caseMapValueType(MapValueType object) {
				return createMapValueTypeAdapter();
			}
			@Override
			public Adapter caseUnstructuredValueType(UnstructuredValueType object) {
				return createUnstructuredValueTypeAdapter();
			}
			@Override
			public Adapter caseModel(Model object) {
				return createModelAdapter();
			}
			@Override
			public Adapter caseModelUpdateCommand(ModelUpdateCommand object) {
				return createModelUpdateCommandAdapter();
			}
			@Override
			public Adapter caseUpdateKnowledgeStructureCommand(UpdateKnowledgeStructureCommand object) {
				return createUpdateKnowledgeStructureCommandAdapter();
			}
			@Override
			public Adapter caseAddComponentCommand(AddComponentCommand object) {
				return createAddComponentCommandAdapter();
			}
			@Override
			public Adapter caseMethodParameter(MethodParameter object) {
				return createMethodParameterAdapter();
			}
			@Override
			public Adapter caseParameterizedMethod(ParameterizedMethod object) {
				return createParameterizedMethodAdapter();
			}
			@Override
			public Adapter casePeriodicScheduling(PeriodicScheduling object) {
				return createPeriodicSchedulingAdapter();
			}
			@Override
			public Adapter caseTriggeredScheduling(TriggeredScheduling object) {
				return createTriggeredSchedulingAdapter();
			}
			@Override
			public Adapter caseTrigger(Trigger object) {
				return createTriggerAdapter();
			}
			@Override
			public Adapter caseParameterChangedTrigger(ParameterChangedTrigger object) {
				return createParameterChangedTriggerAdapter();
			}
			@Override
			public Adapter caseEnsemble(Ensemble object) {
				return createEnsembleAdapter();
			}
			@Override
			public Adapter caseTopLevelKnowledgeDefinition(TopLevelKnowledgeDefinition object) {
				return createTopLevelKnowledgeDefinitionAdapter();
			}
			@Override
			public Adapter caseNestedKnowledgeDefinition(NestedKnowledgeDefinition object) {
				return createNestedKnowledgeDefinitionAdapter();
			}
			@Override
			public Adapter caseKnowledgeReference(KnowledgeReference object) {
				return createKnowledgeReferenceAdapter();
			}
			@Override
			public Adapter caseMembershipCondition(MembershipCondition object) {
				return createMembershipConditionAdapter();
			}
			@Override
			public Adapter caseKnowledgeExchange(KnowledgeExchange object) {
				return createKnowledgeExchangeAdapter();
			}
			@Override
			public Adapter caseSchedulable(Schedulable object) {
				return createSchedulableAdapter();
			}
			@Override
			public Adapter caseTypeParameter(TypeParameter object) {
				return createTypeParameterAdapter();
			}
			@Override
			public Adapter caseParametricKnowledgeType(ParametricKnowledgeType object) {
				return createParametricKnowledgeTypeAdapter();
			}
			@Override
			public Adapter caseKnowledgeTypeOwner(KnowledgeTypeOwner object) {
				return createKnowledgeTypeOwnerAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Component
	 * @generated
	 */
	public Adapter createComponentAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeDefinition <em>Knowledge Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeDefinition
	 * @generated
	 */
	public Adapter createKnowledgeDefinitionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType <em>Knowledge Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeType
	 * @generated
	 */
	public Adapter createKnowledgeTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process <em>Process</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process
	 * @generated
	 */
	public Adapter createProcessAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Scheduling <em>Scheduling</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Scheduling
	 * @generated
	 */
	public Adapter createSchedulingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeValueType <em>Structured Knowledge Value Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.StructuredKnowledgeValueType
	 * @generated
	 */
	public Adapter createStructuredKnowledgeValueTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ListValueType <em>List Value Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ListValueType
	 * @generated
	 */
	public Adapter createListValueTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MapValueType <em>Map Value Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MapValueType
	 * @generated
	 */
	public Adapter createMapValueTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.UnstructuredValueType <em>Unstructured Value Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.UnstructuredValueType
	 * @generated
	 */
	public Adapter createUnstructuredValueTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Model <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Model
	 * @generated
	 */
	public Adapter createModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ModelUpdateCommand <em>Model Update Command</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ModelUpdateCommand
	 * @generated
	 */
	public Adapter createModelUpdateCommandAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.UpdateKnowledgeStructureCommand <em>Update Knowledge Structure Command</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.UpdateKnowledgeStructureCommand
	 * @generated
	 */
	public Adapter createUpdateKnowledgeStructureCommandAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.AddComponentCommand <em>Add Component Command</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.AddComponentCommand
	 * @generated
	 */
	public Adapter createAddComponentCommandAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter <em>Method Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter
	 * @generated
	 */
	public Adapter createMethodParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod <em>Parameterized Method</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod
	 * @generated
	 */
	public Adapter createParameterizedMethodAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.PeriodicScheduling <em>Periodic Scheduling</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.PeriodicScheduling
	 * @generated
	 */
	public Adapter createPeriodicSchedulingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TriggeredScheduling <em>Triggered Scheduling</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TriggeredScheduling
	 * @generated
	 */
	public Adapter createTriggeredSchedulingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Trigger <em>Trigger</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Trigger
	 * @generated
	 */
	public Adapter createTriggerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterChangedTrigger <em>Parameter Changed Trigger</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterChangedTrigger
	 * @generated
	 */
	public Adapter createParameterChangedTriggerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble <em>Ensemble</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Ensemble
	 * @generated
	 */
	public Adapter createEnsembleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TopLevelKnowledgeDefinition <em>Top Level Knowledge Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TopLevelKnowledgeDefinition
	 * @generated
	 */
	public Adapter createTopLevelKnowledgeDefinitionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.NestedKnowledgeDefinition <em>Nested Knowledge Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.NestedKnowledgeDefinition
	 * @generated
	 */
	public Adapter createNestedKnowledgeDefinitionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeReference <em>Knowledge Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeReference
	 * @generated
	 */
	public Adapter createKnowledgeReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MembershipCondition <em>Membership Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MembershipCondition
	 * @generated
	 */
	public Adapter createMembershipConditionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeExchange <em>Knowledge Exchange</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeExchange
	 * @generated
	 */
	public Adapter createKnowledgeExchangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Schedulable <em>Schedulable</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Schedulable
	 * @generated
	 */
	public Adapter createSchedulableAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TypeParameter <em>Type Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.TypeParameter
	 * @generated
	 */
	public Adapter createTypeParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParametricKnowledgeType <em>Parametric Knowledge Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParametricKnowledgeType
	 * @generated
	 */
	public Adapter createParametricKnowledgeTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeTypeOwner <em>Knowledge Type Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.KnowledgeTypeOwner
	 * @generated
	 */
	public Adapter createKnowledgeTypeOwnerAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //RuntimemodelAdapterFactory
