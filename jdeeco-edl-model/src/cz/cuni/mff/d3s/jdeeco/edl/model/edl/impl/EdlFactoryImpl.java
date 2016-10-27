/**
 */
package cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
public class EdlFactoryImpl extends EFactoryImpl implements EdlFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EdlFactory init() {
		try {
			EdlFactory theEdlFactory = (EdlFactory)EPackage.Registry.INSTANCE.getEFactory(EdlPackage.eNS_URI);
			if (theEdlFactory != null) {
				return theEdlFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new EdlFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EdlFactoryImpl() {
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
			case EdlPackage.EDL_DOCUMENT: return createEdlDocument();
			case EdlPackage.ENSEMBLE_DEFINITION: return createEnsembleDefinition();
			case EdlPackage.ROLE_DEFINITION: return createRoleDefinition();
			case EdlPackage.ALIAS_DEFINITION: return createAliasDefinition();
			case EdlPackage.ID_DEFINITION: return createIdDefinition();
			case EdlPackage.CHILD_DEFINITION: return createChildDefinition();
			case EdlPackage.EXCHANGE_RULE: return createExchangeRule();
			case EdlPackage.QUALIFIED_NAME: return createQualifiedName();
			case EdlPackage.FUNCTION_CALL: return createFunctionCall();
			case EdlPackage.KNOWLEDGE_VARIABLE: return createKnowledgeVariable();
			case EdlPackage.NUMERIC_LITERAL: return createNumericLiteral();
			case EdlPackage.BOOL_LITERAL: return createBoolLiteral();
			case EdlPackage.RELATION_OPERATOR: return createRelationOperator();
			case EdlPackage.LOGICAL_OPERATOR: return createLogicalOperator();
			case EdlPackage.NEGATION: return createNegation();
			case EdlPackage.STRING_LITERAL: return createStringLiteral();
			case EdlPackage.FLOAT_LITERAL: return createFloatLiteral();
			case EdlPackage.ADDITIVE_OPERATOR: return createAdditiveOperator();
			case EdlPackage.MULTIPLICATIVE_OPERATOR: return createMultiplicativeOperator();
			case EdlPackage.ADDITIVE_INVERSE: return createAdditiveInverse();
			case EdlPackage.DATA_CONTRACT_DEFINITION: return createDataContractDefinition();
			case EdlPackage.FIELD_DECLARATION: return createFieldDeclaration();
			case EdlPackage.TYPE_DEFINITION: return createTypeDefinition();
			case EdlPackage.SUM: return createSum();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case EdlPackage.BOOL_OPERATOR_TYPE:
				return createBoolOperatorTypeFromString(eDataType, initialValue);
			case EdlPackage.BINARY_OPERATOR_TYPE:
				return createBinaryOperatorTypeFromString(eDataType, initialValue);
			case EdlPackage.RELATION_OPERATOR_TYPE:
				return createRelationOperatorTypeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case EdlPackage.BOOL_OPERATOR_TYPE:
				return convertBoolOperatorTypeToString(eDataType, instanceValue);
			case EdlPackage.BINARY_OPERATOR_TYPE:
				return convertBinaryOperatorTypeToString(eDataType, instanceValue);
			case EdlPackage.RELATION_OPERATOR_TYPE:
				return convertRelationOperatorTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EdlDocument createEdlDocument() {
		EdlDocumentImpl edlDocument = new EdlDocumentImpl();
		return edlDocument;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnsembleDefinition createEnsembleDefinition() {
		EnsembleDefinitionImpl ensembleDefinition = new EnsembleDefinitionImpl();
		return ensembleDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RoleDefinition createRoleDefinition() {
		RoleDefinitionImpl roleDefinition = new RoleDefinitionImpl();
		return roleDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AliasDefinition createAliasDefinition() {
		AliasDefinitionImpl aliasDefinition = new AliasDefinitionImpl();
		return aliasDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IdDefinition createIdDefinition() {
		IdDefinitionImpl idDefinition = new IdDefinitionImpl();
		return idDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChildDefinition createChildDefinition() {
		ChildDefinitionImpl childDefinition = new ChildDefinitionImpl();
		return childDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExchangeRule createExchangeRule() {
		ExchangeRuleImpl exchangeRule = new ExchangeRuleImpl();
		return exchangeRule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QualifiedName createQualifiedName() {
		QualifiedNameImpl qualifiedName = new QualifiedNameImpl();
		return qualifiedName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionCall createFunctionCall() {
		FunctionCallImpl functionCall = new FunctionCallImpl();
		return functionCall;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KnowledgeVariable createKnowledgeVariable() {
		KnowledgeVariableImpl knowledgeVariable = new KnowledgeVariableImpl();
		return knowledgeVariable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NumericLiteral createNumericLiteral() {
		NumericLiteralImpl numericLiteral = new NumericLiteralImpl();
		return numericLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BoolLiteral createBoolLiteral() {
		BoolLiteralImpl boolLiteral = new BoolLiteralImpl();
		return boolLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RelationOperator createRelationOperator() {
		RelationOperatorImpl relationOperator = new RelationOperatorImpl();
		return relationOperator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LogicalOperator createLogicalOperator() {
		LogicalOperatorImpl logicalOperator = new LogicalOperatorImpl();
		return logicalOperator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Negation createNegation() {
		NegationImpl negation = new NegationImpl();
		return negation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StringLiteral createStringLiteral() {
		StringLiteralImpl stringLiteral = new StringLiteralImpl();
		return stringLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FloatLiteral createFloatLiteral() {
		FloatLiteralImpl floatLiteral = new FloatLiteralImpl();
		return floatLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AdditiveOperator createAdditiveOperator() {
		AdditiveOperatorImpl additiveOperator = new AdditiveOperatorImpl();
		return additiveOperator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MultiplicativeOperator createMultiplicativeOperator() {
		MultiplicativeOperatorImpl multiplicativeOperator = new MultiplicativeOperatorImpl();
		return multiplicativeOperator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AdditiveInverse createAdditiveInverse() {
		AdditiveInverseImpl additiveInverse = new AdditiveInverseImpl();
		return additiveInverse;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataContractDefinition createDataContractDefinition() {
		DataContractDefinitionImpl dataContractDefinition = new DataContractDefinitionImpl();
		return dataContractDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FieldDeclaration createFieldDeclaration() {
		FieldDeclarationImpl fieldDeclaration = new FieldDeclarationImpl();
		return fieldDeclaration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TypeDefinition createTypeDefinition() {
		TypeDefinitionImpl typeDefinition = new TypeDefinitionImpl();
		return typeDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sum createSum() {
		SumImpl sum = new SumImpl();
		return sum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BoolOperatorType createBoolOperatorTypeFromString(EDataType eDataType, String initialValue) {
		BoolOperatorType result = BoolOperatorType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertBoolOperatorTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BinaryOperatorType createBinaryOperatorTypeFromString(EDataType eDataType, String initialValue) {
		BinaryOperatorType result = BinaryOperatorType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertBinaryOperatorTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RelationOperatorType createRelationOperatorTypeFromString(EDataType eDataType, String initialValue) {
		RelationOperatorType result = RelationOperatorType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRelationOperatorTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EdlPackage getEdlPackage() {
		return (EdlPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static EdlPackage getPackage() {
		return EdlPackage.eINSTANCE;
	}

} //EdlFactoryImpl
