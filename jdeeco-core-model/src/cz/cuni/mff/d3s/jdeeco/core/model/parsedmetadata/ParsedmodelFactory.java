/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.ParsedmodelPackage
 * @generated
 */
public interface ParsedmodelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ParsedmodelFactory eINSTANCE = cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl.ParsedmodelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Annotated Class</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Annotated Class</em>'.
	 * @generated
	 */
	AnnotatedClass createAnnotatedClass();

	/**
	 * Returns a new object of class '<em>Annotated Method</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Annotated Method</em>'.
	 * @generated
	 */
	AnnotatedMethod createAnnotatedMethod();

	/**
	 * Returns a new object of class '<em>Parsed Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Parsed Model</em>'.
	 * @generated
	 */
	ParsedModel createParsedModel();

	/**
	 * Returns a new object of class '<em>Annotation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Annotation</em>'.
	 * @generated
	 */
	Annotation createAnnotation();

	/**
	 * Returns a new object of class '<em>Annotated Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Annotated Parameter</em>'.
	 * @generated
	 */
	AnnotatedParameter createAnnotatedParameter();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ParsedmodelPackage getParsedmodelPackage();

} //ParsedmodelFactory
