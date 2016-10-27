package cz.cuni.mff.d3s.jdeeco.edl.validation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class NullErrorReportingService implements IErrorReportingService {

	@Override
	public void reportError(String message, EObject source,
			EStructuralFeature feature) {
		// Left intentionally empty

	}

	@Override
	public void reportError(String message, EObject source,
			EStructuralFeature feature, int index) {
		// Left intentionally empty

	}

}
