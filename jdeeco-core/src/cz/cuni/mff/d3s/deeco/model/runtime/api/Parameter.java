/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.api;

import java.lang.reflect.Type;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getKind <em>Kind</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getKnowledgePath <em>Knowledge Path</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getType <em>Type</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getGenericType <em>Generic Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getParameter()
 * @model
 * @generated
 */
public interface Parameter extends EObject {
	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * The default value is <code>"INOUT"</code>.
	 * The literals are from the enumeration {@link cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Kind</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind
	 * @see #isSetKind()
	 * @see #unsetKind()
	 * @see #setKind(ParameterKind)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getParameter_Kind()
	 * @model default="INOUT" unsettable="true" required="true"
	 * @generated
	 */
	ParameterKind getKind();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind
	 * @see #isSetKind()
	 * @see #unsetKind()
	 * @see #getKind()
	 * @generated
	 */
	void setKind(ParameterKind value);

	/**
	 * Unsets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetKind()
	 * @see #getKind()
	 * @see #setKind(ParameterKind)
	 * @generated
	 */
	void unsetKind();

	/**
	 * Returns whether the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getKind <em>Kind</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Kind</em>' attribute is set.
	 * @see #unsetKind()
	 * @see #getKind()
	 * @see #setKind(ParameterKind)
	 * @generated
	 */
	boolean isSetKind();

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
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getParameter_KnowledgePath()
	 * @model containment="true" required="true"
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
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getParameter_Type()
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

	/**
	 * Returns the value of the '<em><b>Generic Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generic Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generic Type</em>' attribute.
	 * @see #setGenericType(Type)
	 * @see cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage#getParameter_GenericType()
	 * @model dataType="cz.cuni.mff.d3s.deeco.model.runtime.api.Type" required="true"
	 * @generated
	 */
	Type getGenericType();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter#getGenericType <em>Generic Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generic Type</em>' attribute.
	 * @see #getGenericType()
	 * @generated
	 */
	void setGenericType(Type value);

} // Parameter
