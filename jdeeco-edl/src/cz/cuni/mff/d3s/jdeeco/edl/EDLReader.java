package cz.cuni.mff.d3s.jdeeco.edl;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.*;

import com.google.inject.Inject;
import com.google.inject.Injector;

import cz.cuni.mff.d3s.jdeeco.edl.EDLStandaloneSetup;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.*;
import cz.cuni.mff.d3s.jdeeco.edl.validation.EDLStandaloneValidator;
import cz.cuni.mff.d3s.jdeeco.edl.validation.EdlValidationException;

public class EDLReader {
	
	@Inject
    private IParser parser;
	
	
	private EDLStandaloneValidator validator;

	public EDLReader() {
		cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlPackage.eINSTANCE.eClass();
		Injector injector = new EDLStandaloneSetup().createInjectorAndDoEMFRegistration();
        injector.injectMembers(this);
        validator = new EDLStandaloneValidator();
        injector.injectMembers(validator);
	}
	
	private EObject parse(Reader reader) {
		IParseResult result = parser.parse(reader);
        if(result.hasSyntaxErrors())
        {
            throw new ParseException("Provided input contains syntax errors.");
        }
        return result.getRootASTElement();
	}

	public EdlDocument readDocument(String fileName) throws IOException, EdlValidationException {
		return readDocument(new FileReader(fileName));
	}
	
	public EdlDocument readDocument(Reader input) throws IOException, EdlValidationException {
		// This must be invoked before working with the model - for some reason, external model registration is done as a side-effect of accessing eInstance					
		cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlPackage.eINSTANCE.eClass();		
		
		EdlDocument model = (EdlDocument) parse(input);
		
		validator.validateDocument(model);
		if (validator.hasValidationErrors()) {
			throw new EdlValidationException(String.join("\n", validator.getErrors()));
		}
		
		return model;
	}
}