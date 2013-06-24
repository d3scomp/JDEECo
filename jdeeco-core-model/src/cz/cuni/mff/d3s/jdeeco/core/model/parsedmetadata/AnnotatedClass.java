/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annotated Class</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedClass#getMethods <em>Methods</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedClass#getClass_ <em>Class</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedClass#getFields <em>Fields</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage#getAnnotatedClass()
 * @model
 * @generated
 */
public interface AnnotatedClass extends NamedEntity, AnnotatedEntity {
	/**
	 * Returns the value of the '<em><b>Methods</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedMethod}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Methods</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Methods</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage#getAnnotatedClass_Methods()
	 * @model containment="true"
	 * @generated
	 */
	EList<AnnotatedMethod> getMethods();

	/**
	 * Returns the value of the '<em><b>Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Class</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Class</em>' attribute.
	 * @see #setClass(Class)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage#getAnnotatedClass_Class()
	 * @model required="true"
	 * @generated
	 */
	Class getClass_();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedClass#getClass_ <em>Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Class</em>' attribute.
	 * @see #getClass_()
	 * @generated
	 */
	void setClass(Class value);

	/**
	 * Returns the value of the '<em><b>Fields</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedField}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fields</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fields</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage#getAnnotatedClass_Fields()
	 * @model containment="true"
	 * @generated
	 */
	EList<AnnotatedField> getFields();

} // AnnotatedClass
