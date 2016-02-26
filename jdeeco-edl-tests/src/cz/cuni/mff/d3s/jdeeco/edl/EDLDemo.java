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
	 
	private static String getFullName(QualifiedName name) {
		return name.toString();
	}
	
	private static String getQuery(Query query) {
		return query.accept(new ToStringVisitor());		
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