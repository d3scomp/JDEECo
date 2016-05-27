/**
 */
package cz.cuni.mff.d3s.jdeeco.edl.model.edl.impl;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.AdditiveInverse;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.AdditiveOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Aggregation;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.AliasDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BinaryOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BinaryOperatorType;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BoolLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.BoolOperatorType;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.ChildDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.ComparableQuery;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.DataContractDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlDocument;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlFactory;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlPackage;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EquitableQuery;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.ExchangeRule;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QueryVisitor;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FieldDeclaration;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FloatLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.FunctionCall;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.IdDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.KnowledgeVariable;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.LogicalOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.MultiplicativeOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Negation;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.NumericLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.QualifiedName;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RelationOperator;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RelationOperatorType;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.RoleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.StringLiteral;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Sum;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.TypeDefinition;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EdlPackageImpl extends EPackageImpl implements EdlPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass edlDocumentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ensembleDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass roleDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aliasDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass idDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass childDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass exchangeRuleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass qualifiedNameEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass functionCallEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass knowledgeVariableEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass binaryOperatorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass queryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass numericLiteralEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass equitableQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass boolLiteralEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass relationOperatorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass logicalOperatorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass negationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stringLiteralEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass floatLiteralEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass comparableQueryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass additiveOperatorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass multiplicativeOperatorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass additiveInverseEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataContractDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fieldDeclarationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass typeDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass queryVisitorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass aggregationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sumEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum boolOperatorTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum binaryOperatorTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum relationOperatorTypeEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private EdlPackageImpl() {
		super(eNS_URI, EdlFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link EdlPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static EdlPackage init() {
		if (isInited) return (EdlPackage)EPackage.Registry.INSTANCE.getEPackage(EdlPackage.eNS_URI);

		// Obtain or create and register package
		EdlPackageImpl theEdlPackage = (EdlPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EdlPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EdlPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theEdlPackage.createPackageContents();

		// Initialize created meta-data
		theEdlPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theEdlPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(EdlPackage.eNS_URI, theEdlPackage);
		return theEdlPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEdlDocument() {
		return edlDocumentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEdlDocument_Ensembles() {
		return (EReference)edlDocumentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEdlDocument_DataContracts() {
		return (EReference)edlDocumentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEdlDocument_Package() {
		return (EReference)edlDocumentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEdlDocument_KnowledgeTypes() {
		return (EReference)edlDocumentEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnsembleDefinition() {
		return ensembleDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEnsembleDefinition_Name() {
		return (EAttribute)ensembleDefinitionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsembleDefinition_Roles() {
		return (EReference)ensembleDefinitionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsembleDefinition_Aliases() {
		return (EReference)ensembleDefinitionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsembleDefinition_Constraints() {
		return (EReference)ensembleDefinitionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsembleDefinition_Id() {
		return (EReference)ensembleDefinitionEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEnsembleDefinition_ParentEnsemble() {
		return (EAttribute)ensembleDefinitionEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsembleDefinition_ChildEnsembles() {
		return (EReference)ensembleDefinitionEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsembleDefinition_ExchangeRules() {
		return (EReference)ensembleDefinitionEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnsembleDefinition_Fitness() {
		return (EReference)ensembleDefinitionEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRoleDefinition() {
		return roleDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRoleDefinition_Exclusive() {
		return (EAttribute)roleDefinitionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRoleDefinition_WhereFilter() {
		return (EReference)roleDefinitionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAliasDefinition() {
		return aliasDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAliasDefinition_AliasId() {
		return (EAttribute)aliasDefinitionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAliasDefinition_AliasValue() {
		return (EReference)aliasDefinitionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIdDefinition() {
		return idDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIdDefinition_IsAssigned() {
		return (EAttribute)idDefinitionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIdDefinition_FieldName() {
		return (EAttribute)idDefinitionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIdDefinition_Value() {
		return (EReference)idDefinitionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIdDefinition_Type() {
		return (EReference)idDefinitionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getChildDefinition() {
		return childDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChildDefinition_Name() {
		return (EAttribute)childDefinitionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChildDefinition_CardinalityMax() {
		return (EAttribute)childDefinitionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getChildDefinition_CardinalityMin() {
		return (EAttribute)childDefinitionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChildDefinition_Type() {
		return (EReference)childDefinitionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExchangeRule() {
		return exchangeRuleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExchangeRule_Field() {
		return (EReference)exchangeRuleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getExchangeRule_Query() {
		return (EReference)exchangeRuleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getQualifiedName() {
		return qualifiedNameEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQualifiedName_Prefix() {
		return (EAttribute)qualifiedNameEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQualifiedName_Name() {
		return (EAttribute)qualifiedNameEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQualifiedName__ToString() {
		return qualifiedNameEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQualifiedName__ToParts() {
		return qualifiedNameEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFunctionCall() {
		return functionCallEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFunctionCall_Name() {
		return (EAttribute)functionCallEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFunctionCall_Parameters() {
		return (EReference)functionCallEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getKnowledgeVariable() {
		return knowledgeVariableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getKnowledgeVariable_Path() {
		return (EReference)knowledgeVariableEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBinaryOperator() {
		return binaryOperatorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBinaryOperator_Left() {
		return (EReference)binaryOperatorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBinaryOperator_Right() {
		return (EReference)binaryOperatorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBinaryOperator_OperatorType() {
		return (EAttribute)binaryOperatorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getQuery() {
		return queryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQuery__Accept__QueryVisitor() {
		return queryEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNumericLiteral() {
		return numericLiteralEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNumericLiteral_Value() {
		return (EAttribute)numericLiteralEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEquitableQuery() {
		return equitableQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBoolLiteral() {
		return boolLiteralEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBoolLiteral_Value() {
		return (EAttribute)boolLiteralEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRelationOperator() {
		return relationOperatorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRelationOperator_Left() {
		return (EReference)relationOperatorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRelationOperator_Right() {
		return (EReference)relationOperatorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRelationOperator_Type() {
		return (EAttribute)relationOperatorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLogicalOperator() {
		return logicalOperatorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLogicalOperator_Left() {
		return (EReference)logicalOperatorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLogicalOperator_Right() {
		return (EReference)logicalOperatorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLogicalOperator_Type() {
		return (EAttribute)logicalOperatorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNegation() {
		return negationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNegation_Nested() {
		return (EReference)negationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStringLiteral() {
		return stringLiteralEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringLiteral_Value() {
		return (EAttribute)stringLiteralEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFloatLiteral() {
		return floatLiteralEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFloatLiteral_Value() {
		return (EAttribute)floatLiteralEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getComparableQuery() {
		return comparableQueryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAdditiveOperator() {
		return additiveOperatorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMultiplicativeOperator() {
		return multiplicativeOperatorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAdditiveInverse() {
		return additiveInverseEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAdditiveInverse_Nested() {
		return (EReference)additiveInverseEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataContractDefinition() {
		return dataContractDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFieldDeclaration() {
		return fieldDeclarationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFieldDeclaration_Name() {
		return (EAttribute)fieldDeclarationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFieldDeclaration_Type() {
		return (EReference)fieldDeclarationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTypeDefinition() {
		return typeDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTypeDefinition_Name() {
		return (EAttribute)typeDefinitionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTypeDefinition_Fields() {
		return (EReference)typeDefinitionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getQueryVisitor() {
		return queryVisitorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQueryVisitor__Visit__AdditiveInverse() {
		return queryVisitorEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQueryVisitor__Visit__BinaryOperator() {
		return queryVisitorEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQueryVisitor__Visit__BoolLiteral() {
		return queryVisitorEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQueryVisitor__Visit__FloatLiteral() {
		return queryVisitorEClass.getEOperations().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQueryVisitor__Visit__FunctionCall() {
		return queryVisitorEClass.getEOperations().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQueryVisitor__Visit__KnowledgeVariable() {
		return queryVisitorEClass.getEOperations().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQueryVisitor__Visit__LogicalOperator() {
		return queryVisitorEClass.getEOperations().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQueryVisitor__Visit__Negation() {
		return queryVisitorEClass.getEOperations().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQueryVisitor__Visit__NumericLiteral() {
		return queryVisitorEClass.getEOperations().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQueryVisitor__Visit__RelationOperator() {
		return queryVisitorEClass.getEOperations().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQueryVisitor__Visit__StringLiteral() {
		return queryVisitorEClass.getEOperations().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getQueryVisitor__Visit__Sum() {
		return queryVisitorEClass.getEOperations().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAggregation() {
		return aggregationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAggregation_Collection() {
		return (EReference)aggregationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSum() {
		return sumEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSum_Item() {
		return (EReference)sumEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getBoolOperatorType() {
		return boolOperatorTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getBinaryOperatorType() {
		return binaryOperatorTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getRelationOperatorType() {
		return relationOperatorTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EdlFactory getEdlFactory() {
		return (EdlFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		edlDocumentEClass = createEClass(EDL_DOCUMENT);
		createEReference(edlDocumentEClass, EDL_DOCUMENT__ENSEMBLES);
		createEReference(edlDocumentEClass, EDL_DOCUMENT__DATA_CONTRACTS);
		createEReference(edlDocumentEClass, EDL_DOCUMENT__PACKAGE);
		createEReference(edlDocumentEClass, EDL_DOCUMENT__KNOWLEDGE_TYPES);

		ensembleDefinitionEClass = createEClass(ENSEMBLE_DEFINITION);
		createEAttribute(ensembleDefinitionEClass, ENSEMBLE_DEFINITION__NAME);
		createEReference(ensembleDefinitionEClass, ENSEMBLE_DEFINITION__ROLES);
		createEReference(ensembleDefinitionEClass, ENSEMBLE_DEFINITION__ALIASES);
		createEReference(ensembleDefinitionEClass, ENSEMBLE_DEFINITION__CONSTRAINTS);
		createEReference(ensembleDefinitionEClass, ENSEMBLE_DEFINITION__ID);
		createEAttribute(ensembleDefinitionEClass, ENSEMBLE_DEFINITION__PARENT_ENSEMBLE);
		createEReference(ensembleDefinitionEClass, ENSEMBLE_DEFINITION__CHILD_ENSEMBLES);
		createEReference(ensembleDefinitionEClass, ENSEMBLE_DEFINITION__EXCHANGE_RULES);
		createEReference(ensembleDefinitionEClass, ENSEMBLE_DEFINITION__FITNESS);

		roleDefinitionEClass = createEClass(ROLE_DEFINITION);
		createEAttribute(roleDefinitionEClass, ROLE_DEFINITION__EXCLUSIVE);
		createEReference(roleDefinitionEClass, ROLE_DEFINITION__WHERE_FILTER);

		aliasDefinitionEClass = createEClass(ALIAS_DEFINITION);
		createEAttribute(aliasDefinitionEClass, ALIAS_DEFINITION__ALIAS_ID);
		createEReference(aliasDefinitionEClass, ALIAS_DEFINITION__ALIAS_VALUE);

		idDefinitionEClass = createEClass(ID_DEFINITION);
		createEAttribute(idDefinitionEClass, ID_DEFINITION__IS_ASSIGNED);
		createEAttribute(idDefinitionEClass, ID_DEFINITION__FIELD_NAME);
		createEReference(idDefinitionEClass, ID_DEFINITION__VALUE);
		createEReference(idDefinitionEClass, ID_DEFINITION__TYPE);

		childDefinitionEClass = createEClass(CHILD_DEFINITION);
		createEAttribute(childDefinitionEClass, CHILD_DEFINITION__NAME);
		createEAttribute(childDefinitionEClass, CHILD_DEFINITION__CARDINALITY_MAX);
		createEAttribute(childDefinitionEClass, CHILD_DEFINITION__CARDINALITY_MIN);
		createEReference(childDefinitionEClass, CHILD_DEFINITION__TYPE);

		exchangeRuleEClass = createEClass(EXCHANGE_RULE);
		createEReference(exchangeRuleEClass, EXCHANGE_RULE__FIELD);
		createEReference(exchangeRuleEClass, EXCHANGE_RULE__QUERY);

		qualifiedNameEClass = createEClass(QUALIFIED_NAME);
		createEAttribute(qualifiedNameEClass, QUALIFIED_NAME__PREFIX);
		createEAttribute(qualifiedNameEClass, QUALIFIED_NAME__NAME);
		createEOperation(qualifiedNameEClass, QUALIFIED_NAME___TO_STRING);
		createEOperation(qualifiedNameEClass, QUALIFIED_NAME___TO_PARTS);

		functionCallEClass = createEClass(FUNCTION_CALL);
		createEAttribute(functionCallEClass, FUNCTION_CALL__NAME);
		createEReference(functionCallEClass, FUNCTION_CALL__PARAMETERS);

		knowledgeVariableEClass = createEClass(KNOWLEDGE_VARIABLE);
		createEReference(knowledgeVariableEClass, KNOWLEDGE_VARIABLE__PATH);

		binaryOperatorEClass = createEClass(BINARY_OPERATOR);
		createEReference(binaryOperatorEClass, BINARY_OPERATOR__LEFT);
		createEReference(binaryOperatorEClass, BINARY_OPERATOR__RIGHT);
		createEAttribute(binaryOperatorEClass, BINARY_OPERATOR__OPERATOR_TYPE);

		queryEClass = createEClass(QUERY);
		createEOperation(queryEClass, QUERY___ACCEPT__QUERYVISITOR);

		numericLiteralEClass = createEClass(NUMERIC_LITERAL);
		createEAttribute(numericLiteralEClass, NUMERIC_LITERAL__VALUE);

		equitableQueryEClass = createEClass(EQUITABLE_QUERY);

		boolLiteralEClass = createEClass(BOOL_LITERAL);
		createEAttribute(boolLiteralEClass, BOOL_LITERAL__VALUE);

		relationOperatorEClass = createEClass(RELATION_OPERATOR);
		createEReference(relationOperatorEClass, RELATION_OPERATOR__LEFT);
		createEReference(relationOperatorEClass, RELATION_OPERATOR__RIGHT);
		createEAttribute(relationOperatorEClass, RELATION_OPERATOR__TYPE);

		logicalOperatorEClass = createEClass(LOGICAL_OPERATOR);
		createEReference(logicalOperatorEClass, LOGICAL_OPERATOR__LEFT);
		createEReference(logicalOperatorEClass, LOGICAL_OPERATOR__RIGHT);
		createEAttribute(logicalOperatorEClass, LOGICAL_OPERATOR__TYPE);

		negationEClass = createEClass(NEGATION);
		createEReference(negationEClass, NEGATION__NESTED);

		stringLiteralEClass = createEClass(STRING_LITERAL);
		createEAttribute(stringLiteralEClass, STRING_LITERAL__VALUE);

		floatLiteralEClass = createEClass(FLOAT_LITERAL);
		createEAttribute(floatLiteralEClass, FLOAT_LITERAL__VALUE);

		comparableQueryEClass = createEClass(COMPARABLE_QUERY);

		additiveOperatorEClass = createEClass(ADDITIVE_OPERATOR);

		multiplicativeOperatorEClass = createEClass(MULTIPLICATIVE_OPERATOR);

		additiveInverseEClass = createEClass(ADDITIVE_INVERSE);
		createEReference(additiveInverseEClass, ADDITIVE_INVERSE__NESTED);

		dataContractDefinitionEClass = createEClass(DATA_CONTRACT_DEFINITION);

		fieldDeclarationEClass = createEClass(FIELD_DECLARATION);
		createEAttribute(fieldDeclarationEClass, FIELD_DECLARATION__NAME);
		createEReference(fieldDeclarationEClass, FIELD_DECLARATION__TYPE);

		typeDefinitionEClass = createEClass(TYPE_DEFINITION);
		createEAttribute(typeDefinitionEClass, TYPE_DEFINITION__NAME);
		createEReference(typeDefinitionEClass, TYPE_DEFINITION__FIELDS);

		queryVisitorEClass = createEClass(QUERY_VISITOR);
		createEOperation(queryVisitorEClass, QUERY_VISITOR___VISIT__ADDITIVEINVERSE);
		createEOperation(queryVisitorEClass, QUERY_VISITOR___VISIT__BINARYOPERATOR);
		createEOperation(queryVisitorEClass, QUERY_VISITOR___VISIT__BOOLLITERAL);
		createEOperation(queryVisitorEClass, QUERY_VISITOR___VISIT__FLOATLITERAL);
		createEOperation(queryVisitorEClass, QUERY_VISITOR___VISIT__FUNCTIONCALL);
		createEOperation(queryVisitorEClass, QUERY_VISITOR___VISIT__KNOWLEDGEVARIABLE);
		createEOperation(queryVisitorEClass, QUERY_VISITOR___VISIT__LOGICALOPERATOR);
		createEOperation(queryVisitorEClass, QUERY_VISITOR___VISIT__NEGATION);
		createEOperation(queryVisitorEClass, QUERY_VISITOR___VISIT__NUMERICLITERAL);
		createEOperation(queryVisitorEClass, QUERY_VISITOR___VISIT__RELATIONOPERATOR);
		createEOperation(queryVisitorEClass, QUERY_VISITOR___VISIT__STRINGLITERAL);
		createEOperation(queryVisitorEClass, QUERY_VISITOR___VISIT__SUM);

		aggregationEClass = createEClass(AGGREGATION);
		createEReference(aggregationEClass, AGGREGATION__COLLECTION);

		sumEClass = createEClass(SUM);
		createEReference(sumEClass, SUM__ITEM);

		// Create enums
		boolOperatorTypeEEnum = createEEnum(BOOL_OPERATOR_TYPE);
		binaryOperatorTypeEEnum = createEEnum(BINARY_OPERATOR_TYPE);
		relationOperatorTypeEEnum = createEEnum(RELATION_OPERATOR_TYPE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters
		ETypeParameter queryVisitorEClass_T = addETypeParameter(queryVisitorEClass, "T");

		// Set bounds for type parameters

		// Add supertypes to classes
		roleDefinitionEClass.getESuperTypes().add(this.getChildDefinition());
		functionCallEClass.getESuperTypes().add(this.getComparableQuery());
		knowledgeVariableEClass.getESuperTypes().add(this.getComparableQuery());
		binaryOperatorEClass.getESuperTypes().add(this.getComparableQuery());
		numericLiteralEClass.getESuperTypes().add(this.getComparableQuery());
		equitableQueryEClass.getESuperTypes().add(this.getQuery());
		boolLiteralEClass.getESuperTypes().add(this.getEquitableQuery());
		relationOperatorEClass.getESuperTypes().add(this.getEquitableQuery());
		logicalOperatorEClass.getESuperTypes().add(this.getEquitableQuery());
		negationEClass.getESuperTypes().add(this.getEquitableQuery());
		stringLiteralEClass.getESuperTypes().add(this.getEquitableQuery());
		floatLiteralEClass.getESuperTypes().add(this.getComparableQuery());
		comparableQueryEClass.getESuperTypes().add(this.getEquitableQuery());
		additiveOperatorEClass.getESuperTypes().add(this.getBinaryOperator());
		multiplicativeOperatorEClass.getESuperTypes().add(this.getBinaryOperator());
		additiveInverseEClass.getESuperTypes().add(this.getComparableQuery());
		dataContractDefinitionEClass.getESuperTypes().add(this.getTypeDefinition());
		aggregationEClass.getESuperTypes().add(this.getComparableQuery());
		sumEClass.getESuperTypes().add(this.getAggregation());

		// Initialize classes, features, and operations; add parameters
		initEClass(edlDocumentEClass, EdlDocument.class, "EdlDocument", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEdlDocument_Ensembles(), this.getEnsembleDefinition(), null, "ensembles", null, 0, -1, EdlDocument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEdlDocument_DataContracts(), this.getDataContractDefinition(), null, "dataContracts", null, 0, -1, EdlDocument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEdlDocument_Package(), this.getQualifiedName(), null, "package", null, 1, 1, EdlDocument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEdlDocument_KnowledgeTypes(), this.getTypeDefinition(), null, "knowledgeTypes", null, 0, -1, EdlDocument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(ensembleDefinitionEClass, EnsembleDefinition.class, "EnsembleDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEnsembleDefinition_Name(), ecorePackage.getEString(), "name", null, 1, 1, EnsembleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnsembleDefinition_Roles(), this.getRoleDefinition(), null, "roles", null, 1, -1, EnsembleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnsembleDefinition_Aliases(), this.getAliasDefinition(), null, "aliases", null, 0, -1, EnsembleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnsembleDefinition_Constraints(), this.getEquitableQuery(), null, "constraints", null, 0, -1, EnsembleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnsembleDefinition_Id(), this.getIdDefinition(), null, "id", null, 1, 1, EnsembleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEnsembleDefinition_ParentEnsemble(), ecorePackage.getEString(), "parentEnsemble", null, 0, 1, EnsembleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnsembleDefinition_ChildEnsembles(), this.getChildDefinition(), null, "childEnsembles", null, 0, -1, EnsembleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnsembleDefinition_ExchangeRules(), this.getExchangeRule(), null, "exchangeRules", null, 0, -1, EnsembleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnsembleDefinition_Fitness(), this.getQuery(), null, "fitness", null, 0, 1, EnsembleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(roleDefinitionEClass, RoleDefinition.class, "RoleDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRoleDefinition_Exclusive(), ecorePackage.getEBoolean(), "exclusive", "false", 1, 1, RoleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRoleDefinition_WhereFilter(), this.getEquitableQuery(), null, "whereFilter", null, 0, 1, RoleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(aliasDefinitionEClass, AliasDefinition.class, "AliasDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAliasDefinition_AliasId(), ecorePackage.getEString(), "aliasId", null, 1, 1, AliasDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAliasDefinition_AliasValue(), this.getQuery(), null, "aliasValue", null, 1, 1, AliasDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(idDefinitionEClass, IdDefinition.class, "IdDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIdDefinition_IsAssigned(), ecorePackage.getEBoolean(), "isAssigned", null, 1, 1, IdDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIdDefinition_FieldName(), ecorePackage.getEString(), "fieldName", null, 1, 1, IdDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIdDefinition_Value(), this.getQuery(), null, "value", null, 0, 1, IdDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIdDefinition_Type(), this.getQualifiedName(), null, "type", null, 1, 1, IdDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(childDefinitionEClass, ChildDefinition.class, "ChildDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getChildDefinition_Name(), ecorePackage.getEString(), "name", null, 1, 1, ChildDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChildDefinition_CardinalityMax(), ecorePackage.getEInt(), "cardinalityMax", "1", 1, 1, ChildDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChildDefinition_CardinalityMin(), ecorePackage.getEInt(), "cardinalityMin", "1", 1, 1, ChildDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChildDefinition_Type(), this.getQualifiedName(), null, "type", null, 1, 1, ChildDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(exchangeRuleEClass, ExchangeRule.class, "ExchangeRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getExchangeRule_Field(), this.getQualifiedName(), null, "field", null, 1, 1, ExchangeRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExchangeRule_Query(), this.getQuery(), null, "query", null, 1, 1, ExchangeRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(qualifiedNameEClass, QualifiedName.class, "QualifiedName", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getQualifiedName_Prefix(), ecorePackage.getEString(), "prefix", null, 0, -1, QualifiedName.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQualifiedName_Name(), ecorePackage.getEString(), "name", null, 1, 1, QualifiedName.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getQualifiedName__ToString(), ecorePackage.getEString(), "toString", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getQualifiedName__ToParts(), ecorePackage.getEString(), "toParts", 0, -1, IS_UNIQUE, IS_ORDERED);

		initEClass(functionCallEClass, FunctionCall.class, "FunctionCall", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFunctionCall_Name(), ecorePackage.getEString(), "name", null, 1, 1, FunctionCall.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFunctionCall_Parameters(), this.getQuery(), null, "parameters", null, 0, -1, FunctionCall.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(knowledgeVariableEClass, KnowledgeVariable.class, "KnowledgeVariable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getKnowledgeVariable_Path(), this.getQualifiedName(), null, "path", null, 1, 1, KnowledgeVariable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(binaryOperatorEClass, BinaryOperator.class, "BinaryOperator", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBinaryOperator_Left(), this.getQuery(), null, "left", null, 1, 1, BinaryOperator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBinaryOperator_Right(), this.getQuery(), null, "right", null, 1, 1, BinaryOperator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBinaryOperator_OperatorType(), this.getBinaryOperatorType(), "operatorType", null, 0, 1, BinaryOperator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(queryEClass, Query.class, "Query", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		EOperation op = initEOperation(getQuery__Accept__QueryVisitor(), null, "accept", 0, 1, IS_UNIQUE, IS_ORDERED);
		ETypeParameter t1 = addETypeParameter(op, "T");
		EGenericType g1 = createEGenericType(this.getQueryVisitor());
		EGenericType g2 = createEGenericType(t1);
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "visitor", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(t1);
		initEOperation(op, g1);

		initEClass(numericLiteralEClass, NumericLiteral.class, "NumericLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNumericLiteral_Value(), ecorePackage.getEInt(), "value", null, 1, 1, NumericLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(equitableQueryEClass, EquitableQuery.class, "EquitableQuery", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(boolLiteralEClass, BoolLiteral.class, "BoolLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBoolLiteral_Value(), ecorePackage.getEBoolean(), "value", null, 1, 1, BoolLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(relationOperatorEClass, RelationOperator.class, "RelationOperator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRelationOperator_Left(), this.getEquitableQuery(), null, "left", null, 1, 1, RelationOperator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRelationOperator_Right(), this.getEquitableQuery(), null, "right", null, 1, 1, RelationOperator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRelationOperator_Type(), this.getRelationOperatorType(), "type", "==", 1, 1, RelationOperator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(logicalOperatorEClass, LogicalOperator.class, "LogicalOperator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLogicalOperator_Left(), this.getEquitableQuery(), null, "left", null, 1, 1, LogicalOperator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLogicalOperator_Right(), this.getEquitableQuery(), null, "right", null, 1, 1, LogicalOperator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLogicalOperator_Type(), this.getBoolOperatorType(), "type", null, 1, 1, LogicalOperator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(negationEClass, Negation.class, "Negation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNegation_Nested(), this.getEquitableQuery(), null, "nested", null, 1, 1, Negation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(stringLiteralEClass, StringLiteral.class, "StringLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStringLiteral_Value(), ecorePackage.getEString(), "value", null, 1, 1, StringLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(floatLiteralEClass, FloatLiteral.class, "FloatLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFloatLiteral_Value(), ecorePackage.getEDouble(), "value", null, 1, 1, FloatLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(comparableQueryEClass, ComparableQuery.class, "ComparableQuery", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(additiveOperatorEClass, AdditiveOperator.class, "AdditiveOperator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(multiplicativeOperatorEClass, MultiplicativeOperator.class, "MultiplicativeOperator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(additiveInverseEClass, AdditiveInverse.class, "AdditiveInverse", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAdditiveInverse_Nested(), this.getQuery(), null, "nested", null, 1, 1, AdditiveInverse.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataContractDefinitionEClass, DataContractDefinition.class, "DataContractDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(fieldDeclarationEClass, FieldDeclaration.class, "FieldDeclaration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFieldDeclaration_Name(), ecorePackage.getEString(), "name", null, 1, 1, FieldDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFieldDeclaration_Type(), this.getQualifiedName(), null, "type", null, 1, 1, FieldDeclaration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(typeDefinitionEClass, TypeDefinition.class, "TypeDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTypeDefinition_Name(), ecorePackage.getEString(), "name", null, 1, 1, TypeDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTypeDefinition_Fields(), this.getFieldDeclaration(), null, "fields", null, 0, -1, TypeDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(queryVisitorEClass, QueryVisitor.class, "QueryVisitor", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		op = initEOperation(getQueryVisitor__Visit__AdditiveInverse(), null, "visit", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getAdditiveInverse(), "query", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(queryVisitorEClass_T);
		initEOperation(op, g1);

		op = initEOperation(getQueryVisitor__Visit__BinaryOperator(), null, "visit", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getBinaryOperator(), "query", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(queryVisitorEClass_T);
		initEOperation(op, g1);

		op = initEOperation(getQueryVisitor__Visit__BoolLiteral(), null, "visit", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getBoolLiteral(), "query", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(queryVisitorEClass_T);
		initEOperation(op, g1);

		op = initEOperation(getQueryVisitor__Visit__FloatLiteral(), null, "visit", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getFloatLiteral(), "query", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(queryVisitorEClass_T);
		initEOperation(op, g1);

		op = initEOperation(getQueryVisitor__Visit__FunctionCall(), null, "visit", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getFunctionCall(), "query", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(queryVisitorEClass_T);
		initEOperation(op, g1);

		op = initEOperation(getQueryVisitor__Visit__KnowledgeVariable(), null, "visit", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getKnowledgeVariable(), "query", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(queryVisitorEClass_T);
		initEOperation(op, g1);

		op = initEOperation(getQueryVisitor__Visit__LogicalOperator(), null, "visit", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getLogicalOperator(), "query", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(queryVisitorEClass_T);
		initEOperation(op, g1);

		op = initEOperation(getQueryVisitor__Visit__Negation(), null, "visit", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getNegation(), "query", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(queryVisitorEClass_T);
		initEOperation(op, g1);

		op = initEOperation(getQueryVisitor__Visit__NumericLiteral(), null, "visit", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getNumericLiteral(), "query", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(queryVisitorEClass_T);
		initEOperation(op, g1);

		op = initEOperation(getQueryVisitor__Visit__RelationOperator(), null, "visit", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getRelationOperator(), "query", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(queryVisitorEClass_T);
		initEOperation(op, g1);

		op = initEOperation(getQueryVisitor__Visit__StringLiteral(), null, "visit", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getStringLiteral(), "query", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(queryVisitorEClass_T);
		initEOperation(op, g1);

		op = initEOperation(getQueryVisitor__Visit__Sum(), null, "visit", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getSum(), "query", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(queryVisitorEClass_T);
		initEOperation(op, g1);

		initEClass(aggregationEClass, Aggregation.class, "Aggregation", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAggregation_Collection(), this.getQualifiedName(), null, "collection", null, 1, 1, Aggregation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sumEClass, Sum.class, "Sum", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSum_Item(), this.getQuery(), null, "item", null, 1, 1, Sum.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(boolOperatorTypeEEnum, BoolOperatorType.class, "BoolOperatorType");
		addEEnumLiteral(boolOperatorTypeEEnum, BoolOperatorType.AND);
		addEEnumLiteral(boolOperatorTypeEEnum, BoolOperatorType.OR);

		initEEnum(binaryOperatorTypeEEnum, BinaryOperatorType.class, "BinaryOperatorType");
		addEEnumLiteral(binaryOperatorTypeEEnum, BinaryOperatorType.ADDITION);
		addEEnumLiteral(binaryOperatorTypeEEnum, BinaryOperatorType.SUBTRACTION);
		addEEnumLiteral(binaryOperatorTypeEEnum, BinaryOperatorType.MULTIPLICATION);
		addEEnumLiteral(binaryOperatorTypeEEnum, BinaryOperatorType.DIVISION);

		initEEnum(relationOperatorTypeEEnum, RelationOperatorType.class, "RelationOperatorType");
		addEEnumLiteral(relationOperatorTypeEEnum, RelationOperatorType.EQUALITY);
		addEEnumLiteral(relationOperatorTypeEEnum, RelationOperatorType.NON_EQUALITY);
		addEEnumLiteral(relationOperatorTypeEEnum, RelationOperatorType.GREATER_THAN);
		addEEnumLiteral(relationOperatorTypeEEnum, RelationOperatorType.LESS_THAN);
		addEEnumLiteral(relationOperatorTypeEEnum, RelationOperatorType.GREATER_OR_EQUAL);
		addEEnumLiteral(relationOperatorTypeEEnum, RelationOperatorType.LESS_OR_EQUAL);

		// Create resource
		createResource(eNS_URI);
	}

} //EdlPackageImpl
