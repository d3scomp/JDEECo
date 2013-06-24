/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annotated Method</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedMethod#getMethod <em>Method</em>}</li>
 *   <li>{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedMethod#getParameters <em>Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage#getAnnotatedMethod()
 * @model
 * @generated
 */
public interface AnnotatedMethod extends NamedEntity, AnnotatedEntity {
	/**
	 * Returns the value of the '<em><b>Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Method</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Method</em>' attribute.
	 * @see #setMethod(Object)
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage#getAnnotatedMethod_Method()
	 * @model required="true"
	 * @generated
	 */
	Object getMethod();

	/**
	 * Sets the value of the '{@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedMethod#getMethod <em>Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Method</em>' attribute.
	 * @see #getMethod()
	 * @generated
	 */
	void setMethod(Object value);

	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.AnnotatedParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference list.
	 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage#getAnnotatedMethod_Parameters()
	 * @model containment="true"
	 * @generated
	 */
	EList<AnnotatedParameter> getParameters();

} // AnnotatedMethod
