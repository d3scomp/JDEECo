/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getDirection <em>Direction</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getKnowledgePath <em>Knowledge Path</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimePackage#getParameter()
 * @model
 * @generated
 */
public interface Parameter extends EObject {
	/**
	 * Returns the value of the '<em><b>Direction</b></em>' attribute.
	 * The default value is <code>"INOUT"</code>.
	 * The literals are from the enumeration {@link cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Direction</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Direction</em>' attribute.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection
	 * @see #isSetDirection()
	 * @see #unsetDirection()
	 * @see #setDirection(ParameterDirection)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimePackage#getParameter_Direction()
	 * @model default="INOUT" unsettable="true" required="true"
	 * @generated
	 */
	ParameterDirection getDirection();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getDirection <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Direction</em>' attribute.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection
	 * @see #isSetDirection()
	 * @see #unsetDirection()
	 * @see #getDirection()
	 * @generated
	 */
	void setDirection(ParameterDirection value);

	/**
	 * Unsets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getDirection <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDirection()
	 * @see #getDirection()
	 * @see #setDirection(ParameterDirection)
	 * @generated
	 */
	void unsetDirection();

	/**
	 * Returns whether the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getDirection <em>Direction</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Direction</em>' attribute is set.
	 * @see #unsetDirection()
	 * @see #getDirection()
	 * @see #setDirection(ParameterDirection)
	 * @generated
	 */
	boolean isSetDirection();

	/**
	 * Returns the value of the '<em><b>Knowledge Path</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Knowledge Path</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Knowledge Path</em>' containment reference.
	 * @see #setKnowledgePath(KnowledgePath)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimePackage#getParameter_KnowledgePath()
	 * @model containment="true"
	 * @generated
	 */
	KnowledgePath getKnowledgePath();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getKnowledgePath <em>Knowledge Path</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Knowledge Path</em>' containment reference.
	 * @see #getKnowledgePath()
	 * @generated
	 */
	void setKnowledgePath(KnowledgePath value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(Class)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimePackage#getParameter_Type()
	 * @model
	 * @generated
	 */
	Class getType();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(Class value);

} // Parameter
