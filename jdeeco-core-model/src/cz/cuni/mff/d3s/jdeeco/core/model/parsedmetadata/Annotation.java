/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annotation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.Annotation#getClass_ <em>Class</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.Annotation#getInstance <em>Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage#getAnnotation()
 * @model
 * @generated
 */
public interface Annotation extends NamedEntity {
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
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage#getAnnotation_Class()
	 * @model required="true"
	 * @generated
	 */
	Class getClass_();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.Annotation#getClass_ <em>Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Class</em>' attribute.
	 * @see #getClass_()
	 * @generated
	 */
	void setClass(Class value);

	/**
	 * Returns the value of the '<em><b>Instance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Instance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Instance</em>' attribute.
	 * @see #setInstance(Object)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage#getAnnotation_Instance()
	 * @model required="true"
	 * @generated
	 */
	Object getInstance();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.Annotation#getInstance <em>Instance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Instance</em>' attribute.
	 * @see #getInstance()
	 * @generated
	 */
	void setInstance(Object value);

} // Annotation
