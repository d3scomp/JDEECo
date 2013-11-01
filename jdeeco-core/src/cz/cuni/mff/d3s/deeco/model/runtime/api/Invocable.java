/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import java.lang.reflect.Method;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Invocable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable#getParameters <em>Parameters</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable#getMethod <em>Method</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getInvocable()
 * @model
 * @generated
 */
public interface Invocable extends EObject {
	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getInvocable_Parameters()
	 * @model containment="true"
	 * @generated
	 */
	EList<Parameter> getParameters();

	/**
	 * Returns the value of the '<em><b>Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Method</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Method</em>' attribute.
	 * @see #setMethod(Method)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getInvocable_Method()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.runtime.api.Method" required="true"
	 * @generated
	 */
	Method getMethod();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable#getMethod <em>Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Method</em>' attribute.
	 * @see #getMethod()
	 * @generated
	 */
	void setMethod(Method value);

} // Invocable
