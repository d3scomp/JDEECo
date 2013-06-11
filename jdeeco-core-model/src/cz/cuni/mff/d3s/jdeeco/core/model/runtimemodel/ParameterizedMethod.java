/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameterized Method</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getDeclaringClass <em>Declaring Class</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getMethodName <em>Method Name</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getFormalParameters <em>Formal Parameters</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getInParameters <em>In Parameters</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getOutParameters <em>Out Parameters</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getInOutParameters <em>In Out Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getParameterizedMethod()
 * @model abstract="true"
 * @generated
 */
public interface ParameterizedMethod extends EObject {
	/**
	 * Returns the value of the '<em><b>Declaring Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Declaring Class</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Declaring Class</em>' attribute.
	 * @see #setDeclaringClass(Class)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getParameterizedMethod_DeclaringClass()
	 * @model required="true"
	 * @generated
	 */
	Class getDeclaringClass();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getDeclaringClass <em>Declaring Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Declaring Class</em>' attribute.
	 * @see #getDeclaringClass()
	 * @generated
	 */
	void setDeclaringClass(Class value);

	/**
	 * Returns the value of the '<em><b>Method Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Method Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Method Name</em>' attribute.
	 * @see #setMethodName(String)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getParameterizedMethod_MethodName()
	 * @model required="true"
	 * @generated
	 */
	String getMethodName();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.ParameterizedMethod#getMethodName <em>Method Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Method Name</em>' attribute.
	 * @see #getMethodName()
	 * @generated
	 */
	void setMethodName(String value);

	/**
	 * Returns the value of the '<em><b>Formal Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter}.
	 * It is bidirectional and its opposite is '{@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Formal Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Formal Parameters</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getParameterizedMethod_FormalParameters()
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter#getOwner
	 * @model opposite="owner" containment="true"
	 * @generated
	 */
	EList<MethodParameter> getFormalParameters();

	/**
	 * Returns the value of the '<em><b>In Parameters</b></em>' reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>In Parameters</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In Parameters</em>' reference list.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getParameterizedMethod_InParameters()
	 * @model
	 * @generated
	 */
	EList<MethodParameter> getInParameters();

	/**
	 * Returns the value of the '<em><b>Out Parameters</b></em>' reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Out Parameters</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Out Parameters</em>' reference list.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getParameterizedMethod_OutParameters()
	 * @model
	 * @generated
	 */
	EList<MethodParameter> getOutParameters();

	/**
	 * Returns the value of the '<em><b>In Out Parameters</b></em>' reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.MethodParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>In Out Parameters</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In Out Parameters</em>' reference list.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.runtimemodel.RuntimemodelPackage#getParameterizedMethod_InOutParameters()
	 * @model
	 * @generated
	 */
	EList<MethodParameter> getInOutParameters();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	Object invoke();

} // ParameterizedMethod
