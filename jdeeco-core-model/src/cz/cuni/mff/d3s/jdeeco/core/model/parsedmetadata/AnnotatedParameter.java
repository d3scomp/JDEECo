/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annotated Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedParameter#getIndex <em>Index</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedParameter#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage#getAnnotatedParameter()
 * @model
 * @generated
 */
public interface AnnotatedParameter extends NamedEntity, AnnotatedEntity {
	/**
	 * Returns the value of the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' attribute.
	 * @see #setIndex(int)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage#getAnnotatedParameter_Index()
	 * @model required="true"
	 * @generated
	 */
	int getIndex();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedParameter#getIndex <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' attribute.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(int value);

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
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage#getAnnotatedParameter_Type()
	 * @model required="true"
	 * @generated
	 */
	Class getType();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedParameter#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(Class value);

} // AnnotatedParameter
