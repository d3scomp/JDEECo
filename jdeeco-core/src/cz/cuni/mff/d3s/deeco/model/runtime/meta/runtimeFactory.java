/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.meta;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;

import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;
import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.runtimePackage
 * @generated
 */
public interface runtimeFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	runtimeFactory eINSTANCE = cz.cuni.mff.d3s.deeco.model.runtime.impl.runtimeFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Scheduling Specification</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Scheduling Specification</em>'.
	 * @generated
	 */
	SchedulingSpecification createSchedulingSpecification();

	/**
	 * Returns a new object of class '<em>Knowledge Change Trigger</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Knowledge Change Trigger</em>'.
	 * @generated
	 */
	KnowledgeChangeTrigger createKnowledgeChangeTrigger();

	/**
	 * Returns a new object of class '<em>Knowledge Path</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Knowledge Path</em>'.
	 * @generated
	 */
	KnowledgePath createKnowledgePath();

	/**
	 * Returns a new object of class '<em>Path Node Field</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Path Node Field</em>'.
	 * @generated
	 */
	PathNodeField createPathNodeField();

	/**
	 * Returns a new object of class '<em>Path Node Map Key</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Path Node Map Key</em>'.
	 * @generated
	 */
	PathNodeMapKey createPathNodeMapKey();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	runtimePackage getruntimePackage();

} //runtimeFactory
