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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage
 * @generated
 */
public class RuntimemodelSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static RuntimemodelPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuntimemodelSwitch() {
		if (modelPackage == null) {
			modelPackage = RuntimemodelPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case RuntimemodelPackage.COMPONENT: {
				Component component = (Component)theEObject;
				T result = caseComponent(component);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.KNOWLEDGE_DEFINITION: {
				KnowledgeDefinition knowledgeDefinition = (KnowledgeDefinition)theEObject;
				T result = caseKnowledgeDefinition(knowledgeDefinition);
				if (result == null) result = caseKnowledgeReference(knowledgeDefinition);
				if (result == null) result = caseKnowledgeTypeOwner(knowledgeDefinition);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.KNOWLEDGE_TYPE: {
				KnowledgeType knowledgeType = (KnowledgeType)theEObject;
				T result = caseKnowledgeType(knowledgeType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.PROCESS: {
				cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process process = (cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process)theEObject;
				T result = caseProcess(process);
				if (result == null) result = caseParameterizedMethod(process);
				if (result == null) result = caseSchedulable(process);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.SCHEDULING: {
				Scheduling scheduling = (Scheduling)theEObject;
				T result = caseScheduling(scheduling);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.STRUCTURED_KNOWLEDGE_VALUE_TYPE: {
				StructuredKnowledgeValueType structuredKnowledgeValueType = (StructuredKnowledgeValueType)theEObject;
				T result = caseStructuredKnowledgeValueType(structuredKnowledgeValueType);
				if (result == null) result = caseKnowledgeType(structuredKnowledgeValueType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.LIST_VALUE_TYPE: {
				ListValueType listValueType = (ListValueType)theEObject;
				T result = caseListValueType(listValueType);
				if (result == null) result = caseParametricKnowledgeType(listValueType);
				if (result == null) result = caseKnowledgeType(listValueType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.MAP_VALUE_TYPE: {
				MapValueType mapValueType = (MapValueType)theEObject;
				T result = caseMapValueType(mapValueType);
				if (result == null) result = caseParametricKnowledgeType(mapValueType);
				if (result == null) result = caseKnowledgeType(mapValueType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.UNSTRUCTURED_VALUE_TYPE: {
				UnstructuredValueType unstructuredValueType = (UnstructuredValueType)theEObject;
				T result = caseUnstructuredValueType(unstructuredValueType);
				if (result == null) result = caseKnowledgeType(unstructuredValueType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.MODEL: {
				Model model = (Model)theEObject;
				T result = caseModel(model);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.MODEL_UPDATE_COMMAND: {
				ModelUpdateCommand modelUpdateCommand = (ModelUpdateCommand)theEObject;
				T result = caseModelUpdateCommand(modelUpdateCommand);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.UPDATE_KNOWLEDGE_STRUCTURE_COMMAND: {
				UpdateKnowledgeStructureCommand updateKnowledgeStructureCommand = (UpdateKnowledgeStructureCommand)theEObject;
				T result = caseUpdateKnowledgeStructureCommand(updateKnowledgeStructureCommand);
				if (result == null) result = caseModelUpdateCommand(updateKnowledgeStructureCommand);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.ADD_COMPONENT_COMMAND: {
				AddComponentCommand addComponentCommand = (AddComponentCommand)theEObject;
				T result = caseAddComponentCommand(addComponentCommand);
				if (result == null) result = caseModelUpdateCommand(addComponentCommand);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.METHOD_PARAMETER: {
				MethodParameter methodParameter = (MethodParameter)theEObject;
				T result = caseMethodParameter(methodParameter);
				if (result == null) result = caseKnowledgeReference(methodParameter);
				if (result == null) result = caseKnowledgeTypeOwner(methodParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.PARAMETERIZED_METHOD: {
				ParameterizedMethod parameterizedMethod = (ParameterizedMethod)theEObject;
				T result = caseParameterizedMethod(parameterizedMethod);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.PERIODIC_SCHEDULING: {
				PeriodicScheduling periodicScheduling = (PeriodicScheduling)theEObject;
				T result = casePeriodicScheduling(periodicScheduling);
				if (result == null) result = caseScheduling(periodicScheduling);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.TRIGGERED_SCHEDULING: {
				TriggeredScheduling triggeredScheduling = (TriggeredScheduling)theEObject;
				T result = caseTriggeredScheduling(triggeredScheduling);
				if (result == null) result = caseScheduling(triggeredScheduling);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.TRIGGER: {
				Trigger trigger = (Trigger)theEObject;
				T result = caseTrigger(trigger);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.PARAMETER_CHANGED_TRIGGER: {
				ParameterChangedTrigger parameterChangedTrigger = (ParameterChangedTrigger)theEObject;
				T result = caseParameterChangedTrigger(parameterChangedTrigger);
				if (result == null) result = caseTrigger(parameterChangedTrigger);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.ENSEMBLE: {
				Ensemble ensemble = (Ensemble)theEObject;
				T result = caseEnsemble(ensemble);
				if (result == null) result = caseSchedulable(ensemble);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.TOP_LEVEL_KNOWLEDGE_DEFINITION: {
				TopLevelKnowledgeDefinition topLevelKnowledgeDefinition = (TopLevelKnowledgeDefinition)theEObject;
				T result = caseTopLevelKnowledgeDefinition(topLevelKnowledgeDefinition);
				if (result == null) result = caseKnowledgeDefinition(topLevelKnowledgeDefinition);
				if (result == null) result = caseKnowledgeReference(topLevelKnowledgeDefinition);
				if (result == null) result = caseKnowledgeTypeOwner(topLevelKnowledgeDefinition);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.NESTED_KNOWLEDGE_DEFINITION: {
				NestedKnowledgeDefinition nestedKnowledgeDefinition = (NestedKnowledgeDefinition)theEObject;
				T result = caseNestedKnowledgeDefinition(nestedKnowledgeDefinition);
				if (result == null) result = caseKnowledgeDefinition(nestedKnowledgeDefinition);
				if (result == null) result = caseKnowledgeReference(nestedKnowledgeDefinition);
				if (result == null) result = caseKnowledgeTypeOwner(nestedKnowledgeDefinition);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.KNOWLEDGE_REFERENCE: {
				KnowledgeReference knowledgeReference = (KnowledgeReference)theEObject;
				T result = caseKnowledgeReference(knowledgeReference);
				if (result == null) result = caseKnowledgeTypeOwner(knowledgeReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.MEMBERSHIP_CONDITION: {
				MembershipCondition membershipCondition = (MembershipCondition)theEObject;
				T result = caseMembershipCondition(membershipCondition);
				if (result == null) result = caseParameterizedMethod(membershipCondition);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.KNOWLEDGE_EXCHANGE: {
				KnowledgeExchange knowledgeExchange = (KnowledgeExchange)theEObject;
				T result = caseKnowledgeExchange(knowledgeExchange);
				if (result == null) result = caseParameterizedMethod(knowledgeExchange);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.SCHEDULABLE: {
				Schedulable schedulable = (Schedulable)theEObject;
				T result = caseSchedulable(schedulable);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.TYPE_PARAMETER: {
				TypeParameter typeParameter = (TypeParameter)theEObject;
				T result = caseTypeParameter(typeParameter);
				if (result == null) result = caseKnowledgeTypeOwner(typeParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.PARAMETRIC_KNOWLEDGE_TYPE: {
				ParametricKnowledgeType parametricKnowledgeType = (ParametricKnowledgeType)theEObject;
				T result = caseParametricKnowledgeType(parametricKnowledgeType);
				if (result == null) result = caseKnowledgeType(parametricKnowledgeType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case RuntimemodelPackage.KNOWLEDGE_TYPE_OWNER: {
				KnowledgeTypeOwner knowledgeTypeOwner = (KnowledgeTypeOwner)theEObject;
				T result = caseKnowledgeTypeOwner(knowledgeTypeOwner);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseComponent(Component object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Knowledge Definition</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Knowledge Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseKnowledgeDefinition(KnowledgeDefinition object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Knowledge Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Knowledge Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseKnowledgeType(KnowledgeType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Process</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Process</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProcess(cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.Process object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Scheduling</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Scheduling</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScheduling(Scheduling object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Structured Knowledge Value Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Structured Knowledge Value Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStructuredKnowledgeValueType(StructuredKnowledgeValueType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>List Value Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>List Value Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseListValueType(ListValueType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Map Value Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Map Value Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMapValueType(MapValueType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Unstructured Value Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Unstructured Value Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUnstructuredValueType(UnstructuredValueType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModel(Model object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Update Command</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Update Command</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelUpdateCommand(ModelUpdateCommand object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Update Knowledge Structure Command</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Update Knowledge Structure Command</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUpdateKnowledgeStructureCommand(UpdateKnowledgeStructureCommand object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Add Component Command</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Add Component Command</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAddComponentCommand(AddComponentCommand object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Method Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Method Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMethodParameter(MethodParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Parameterized Method</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Parameterized Method</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseParameterizedMethod(ParameterizedMethod object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Periodic Scheduling</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Periodic Scheduling</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePeriodicScheduling(PeriodicScheduling object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Triggered Scheduling</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Triggered Scheduling</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTriggeredScheduling(TriggeredScheduling object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Trigger</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Trigger</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTrigger(Trigger object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Parameter Changed Trigger</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Parameter Changed Trigger</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseParameterChangedTrigger(ParameterChangedTrigger object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Ensemble</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ensemble</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnsemble(Ensemble object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Top Level Knowledge Definition</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Top Level Knowledge Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTopLevelKnowledgeDefinition(TopLevelKnowledgeDefinition object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Nested Knowledge Definition</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Nested Knowledge Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNestedKnowledgeDefinition(NestedKnowledgeDefinition object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Knowledge Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Knowledge Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseKnowledgeReference(KnowledgeReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Membership Condition</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Membership Condition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMembershipCondition(MembershipCondition object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Knowledge Exchange</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Knowledge Exchange</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseKnowledgeExchange(KnowledgeExchange object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Schedulable</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Schedulable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSchedulable(Schedulable object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Type Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Type Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTypeParameter(TypeParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Parametric Knowledge Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Parametric Knowledge Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseParametricKnowledgeType(ParametricKnowledgeType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Knowledge Type Owner</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Knowledge Type Owner</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseKnowledgeTypeOwner(KnowledgeTypeOwner object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //RuntimemodelSwitch
