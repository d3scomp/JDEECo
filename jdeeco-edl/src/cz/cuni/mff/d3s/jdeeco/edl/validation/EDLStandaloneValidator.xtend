package cz.cuni.mff.d3s.jdeeco.edl.validation

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EdlDocument
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.TypeDefinition
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EStructuralFeature
import java.util.List
import java.util.ArrayList

class EDLStandaloneValidator extends EDLValidator {
	// TODO Refactor - should be containment, not inheritance
	private List<String> errors;	
	
	new() {
		errors = new ArrayList();
	}
	
	def public void validateDocument(EdlDocument document) {
		
		generateDocumentInfo(document);
		
		for (TypeDefinition t : document.knowledgeTypes) {
			validateType(t);
		}
		
		for (TypeDefinition t : document.dataContracts) {
			validateType(t);
		}
				
		for (EnsembleDefinition e : document.ensembles) {
			validateEnsembleDefinition(e);
		}
	}
	
	def public boolean hasValidationErrors() {
		return errors.length > 0
	}
	
	def public List<String> getErrors() {
		return errors;
	}
	
	override reportError(String message, EObject source, EStructuralFeature feature) {
		errors.add(message);
	}
	
	override reportError(String message, EObject source, EStructuralFeature feature, int index) {
		errors.add(message);
	}	
}