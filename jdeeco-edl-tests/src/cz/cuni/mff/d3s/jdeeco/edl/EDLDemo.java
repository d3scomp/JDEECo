package cz.cuni.mff.d3s.jdeeco.edl;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Inject;
import com.google.inject.Injector;

import cz.cuni.mff.d3s.jdeeco.edl.EDLStandaloneSetup;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlDocument;

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
            //throw new ParseException("Provided input contains syntax errors.");
        }
        return result.getRootASTElement();
	}
	

	public static void main(String[] args) throws IOException {		
		EDLDemo demo = new EDLDemo();		
		
		// This must be invoked before working with the model - for some reason, external model registration is done as a side-effect of accessing eInstance
		cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlPackage.eINSTANCE.eClass();
		
		//new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("../");
		//Injector injector = new EDLStandaloneSetup().createInjectorAndDoEMFRegistration();		
		//XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		//resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		//Resource resource = resourceSet.getResource(
		//URI.createURI("test.edl"), true);
		//EdlDocument model = (EdlDocument) resource.getContents().get(0);
		
		EdlDocument model = (EdlDocument) demo.parse(new FileReader("test.edl"));
		
		System.out.println(model);		
	}
}