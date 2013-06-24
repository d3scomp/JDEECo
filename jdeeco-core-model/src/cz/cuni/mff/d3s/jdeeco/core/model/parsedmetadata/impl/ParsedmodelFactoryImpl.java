/**
 */
package cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.impl;

import cz.cuni.mff.d3s.jdeeco.core.model.parsedmetadata.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ParsedmodelFactoryImpl extends EFactoryImpl implements ParsedmodelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ParsedmodelFactory init() {
		try {
			ParsedmodelFactory theParsedmodelFactory = (ParsedmodelFactory)EPackage.Registry.INSTANCE.getEFactory("http://parsedmetadata/1.0"); 
			if (theParsedmodelFactory != null) {
				return theParsedmodelFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ParsedmodelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParsedmodelFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ParsedmodelPackage.ANNOTATED_CLASS: return createAnnotatedClass();
			case ParsedmodelPackage.ANNOTATED_METHOD: return createAnnotatedMethod();
			case ParsedmodelPackage.PARSED_MODEL: return createParsedModel();
			case ParsedmodelPackage.ANNOTATION: return createAnnotation();
			case ParsedmodelPackage.ANNOTATED_PARAMETER: return createAnnotatedParameter();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnnotatedClass createAnnotatedClass() {
		AnnotatedClassImpl annotatedClass = new AnnotatedClassImpl();
		return annotatedClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnnotatedMethod createAnnotatedMethod() {
		AnnotatedMethodImpl annotatedMethod = new AnnotatedMethodImpl();
		return annotatedMethod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParsedModel createParsedModel() {
		ParsedModelImpl parsedModel = new ParsedModelImpl();
		return parsedModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Annotation createAnnotation() {
		AnnotationImpl annotation = new AnnotationImpl();
		return annotation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnnotatedParameter createAnnotatedParameter() {
		AnnotatedParameterImpl annotatedParameter = new AnnotatedParameterImpl();
		return annotatedParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParsedmodelPackage getParsedmodelPackage() {
		return (ParsedmodelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ParsedmodelPackage getPackage() {
		return ParsedmodelPackage.eINSTANCE;
	}

} //ParsedmodelFactoryImpl
