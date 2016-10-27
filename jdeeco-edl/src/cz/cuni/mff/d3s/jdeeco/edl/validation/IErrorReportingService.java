package cz.cuni.mff.d3s.jdeeco.edl.validation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public interface IErrorReportingService {
	void reportError(String message, EObject source, EStructuralFeature feature);
	void reportError(String message, EObject source, EStructuralFeature feature, int index);	
}
