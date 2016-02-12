package cz.cuni.mff.d3s.jdeeco.edl;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.*;

import com.google.inject.Inject;
import com.google.inject.Injector;

import cz.cuni.mff.d3s.jdeeco.edl.EDLStandaloneSetup;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.*;

public class EDLDemo {
	
	@Inject
    private IParser parser;

	public EDLDemo() {
		Injector injector = new EDLStandaloneSetup().createInjectorAndDoEMFRegistration();
        injector.injectMembers(this);
	}
	
	public EObject parse(Reader reader) {
		IParseResult result = parser.parse(reader);
        if(result.hasSyntaxErrors())
        {
            throw new ParseException("Provided input contains syntax errors.");
        }
        return result.getRootASTElement();
	}
	
	// Most likely a candidate for a toString on QualifiedName 
	private static String getFullName(QualifiedName name) {
		StringBuilder b = new StringBuilder();
		for(String part : name.getPrefix()) {
			b.append(part);
			b.append('.');
		}
		
		b.append(name.getName());
		return b.toString();
	}
	
	// Just a quick ugly test hack - should be replaced with a virtual dispatch or, even better, a visitor 
	
	private static String getQuery(Query query) {
		switch(query.eClass().getName()) {
		case "BoolLiteral":
			return ((BoolLiteral) query).isValue() + "";
		case "NumericLiteral":
			return ((NumericLiteral) query).getValue() + "";			
		case "StringLiteral":
			return "\"" + ((StringLiteral) query).getValue() + "\"";
		case "FloatLiteral":
			return ((FloatLiteral) query).getValue() + "";
		case "KnowledgeVariable": 
			return getFullName(((KnowledgeVariable) query).getPath());
		case "LogicalOperator":
			LogicalOperator op = (LogicalOperator) query;
			return '(' + getQuery(op.getLeft()) + ')' + op.getType().toString() + '(' + getQuery(op.getRight()) + ')';
		case "RelationOperator":
			RelationOperator rop = (RelationOperator) query;
			return '(' + getQuery(rop.getLeft()) + ')' + rop.getType().toString() + '(' + getQuery(rop.getRight()) + ')';		
		case "AdditiveOperator":
			AdditiveOperator op1 = (AdditiveOperator) query;
			return '(' + getQuery(op1.getLeft()) + ')' + op1.getOperatorType().toString() + '(' + getQuery(op1.getRight()) + ')';
		case "MultiplicativeOperator":
			MultiplicativeOperator op2 = (MultiplicativeOperator) query;
			return '(' + getQuery(op2.getLeft()) + ')' + op2.getOperatorType().toString() + '(' + getQuery(op2.getRight()) + ')';
		case "AdditiveInverse":
			AdditiveInverse op3 = (AdditiveInverse) query;
			return "(-" + getQuery(op3.getNested()) + ')';
		case "Negation":
			Negation op4 = (Negation) query;
			return "(!" + getQuery(op4.getNested()) + ')';
		case "FunctionCall":
			FunctionCall call = (FunctionCall) query;
			StringBuilder b = new StringBuilder();			
			b.append(call.getName());
			b.append('(');
			
			List<String> paramValues = new ArrayList<String>();
			
			for (Query param : call.getParameters()) {
				paramValues.add(getQuery(param));				
			}
			
			b.append(String.join(",", paramValues));
			
			b.append(')');
			return b.toString();
			
		default:
			return query.eClass().getName();
		}				
	}
	
	
	private static void printEnsemble(EnsembleDefinition ensemble) {
		System.out.println("name: " + ensemble.getName());
		System.out.println("parent: " + ensemble.getParentEnsemble());
		IdDefinition id = ensemble.getId();
		System.out.println("id: field: " + id.getFieldName() + " type: " + getFullName(id.getType()) + " assigned: " + id.isIsAssigned() + " value: " + getQuery(id.getValue()));
		
		System.out.println("child ensembles: ");
		for (ChildDefinition child : ensemble.getChildEnsembles()) {
			System.out.println("name: " + child.getName() + " type: " + getFullName(child.getType()) + " cardinality: " + child.getCardinalityMin()  + ".." + child.getCardinalityMax());
		}
		
		System.out.println("aliases: ");
		for (AliasDefinition alias : ensemble.getAliases()) {
			System.out.println("id: " + alias.getAliasId() + " value: " + getQuery(alias.getAliasValue()));
		}
		
		System.out.println("roles: ");
		for (RoleDefinition role : ensemble.getRoles()) {
			System.out.println("name: " + role.getName() + " exclusive: " + role.isExclusive()  + " type: " + getFullName(role.getType()) + " cardinality: " + role.getCardinalityMin()  + ".." + role.getCardinalityMax());
		}
		
		System.out.println("constraints: ");
		for (EquitableQuery c : ensemble.getConstraints()) {
			System.out.println(getQuery(c));
		}
		
		System.out.println("fitness: " + getQuery(ensemble.getFitness()));
		
		System.out.println("exchange rules: ");
		for (ExchangeRule rule : ensemble.getExchangeRules()) {
			System.out.println(getFullName(rule.getField()) + " := " + getQuery(rule.getQuery()));
		}
	}	

	public static void main(String[] args) throws IOException {		
		EDLDemo demo = new EDLDemo();		
		
		// This must be invoked before working with the model - for some reason, external model registration is done as a side-effect of accessing eInstance
		cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlPackage.eINSTANCE.eClass();		
		
		EdlDocument model = (EdlDocument) demo.parse(new FileReader("test2.edl"));
		
		for(EnsembleDefinition definition : model.getEnsembles())
		{
			printEnsemble(definition);
		}			
	}
}