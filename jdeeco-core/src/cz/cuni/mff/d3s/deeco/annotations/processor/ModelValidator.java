package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import cz.cuni.mff.d3s.deeco.annotations.processor.ModelValidationError.Severity;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;

/**
 * Class enabling compile-time/runti-time validation of the
 * {@link RuntimeMetadata} model.
 * 
 * <p>
 * After creating an instance, use {@link #validate(RuntimeMetadata)} to
 * validate the model, the return value indicates whether the model is valid. If
 * invalid, {@link #getErrors()} will provide a collection of found errors.
 * </p>
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * 
 */
public class ModelValidator {
	
	List<ModelValidationError> errors = Collections.emptyList();
	
	/**
	 * Validates the model, if invalid, the found errors will be accessible via
	 * {@link #getErrors()}.
	 * 
	 * @param model
	 *            the model to be validated
	 * @return true iff the model is valid
	 */
	public boolean validate(RuntimeMetadata model) {
		if (model == null)
			return true;
		
		errors = new ArrayList<>();
		
		
		for (ComponentInstance ci: model.getComponentInstances())
			validateComponentInstance(ci);
		
		for (EnsembleDefinition ed: model.getEnsembleDefinitions())
			validateEnsembleDefinition(ed);
		
		return errors.isEmpty();
	}
	
	
	/**
	 * Returns the collection of errors found during the previous run of
	 * {@link #validate(RuntimeMetadata)}.
	 * 
	 * @return the errors found during the previous validaiton.
	 */
	public List<ModelValidationError> getErrors() {
		return errors;
	}
	
	
	/**
	 * Validates the given {@link EnsembleDefinition}.
	 * 
	 */
	protected void validateEnsembleDefinition(EnsembleDefinition ed) {
		
	}
	
	/**
	 * Validates the given {@link ComponentInstance}.
	 * 
	 * Validates the instance's processes.
	 * 
	 * @see ModelValidator#validateProcess(ComponentProcess)
	 */
	protected void validateComponentInstance(ComponentInstance ci) {
		for (ComponentProcess cp: ci.getComponentProcesses())
			validateProcess(cp);
	}
	
	/**
	 * Validates the given {@link ComponentProcess}.
	 * 
	 * Validates the processes's parameters.
	 * 
	 * @see #validateProcessParameters(ComponentProcess)
	 */
	protected void validateProcess(ComponentProcess cp) {
		validateProcessParameters(cp);
	}
	
	/**
	 * Validates the parameters of a {@link ComponentProcess}.
	 * 
	 * <p>
	 * An error is reported, if the process has an IN/INOUT parameter that is
	 * not in the initial knowledge (with the correct the type).
	 * </p>
	 * 
	 * <p>
	 * An error is reported, if the process has no parameters.
	 * </p>
	 * 
	 * <p>
	 * A warning is reported if the process has no input/output parameters.
	 * </p>
	 * 
	 */
	protected void validateProcessParameters(ComponentProcess cp) {
		if (cp.getParameters().isEmpty()) {
			reportError(
					cp,
					"Process %s.%s has no parameters.",
					cp.getComponentInstance().getName(),
					cp.getName());		
			return;
		}
		
		boolean hasInputs = false;
		boolean hasOutputs = false;
		
		int idx = 1;
		for (Parameter pp: cp.getParameters()) {
			if ((pp.getDirection() == ParameterDirection.IN) || (pp.getDirection() == ParameterDirection.INOUT))
				hasInputs = true;
			if ((pp.getDirection() == ParameterDirection.OUT) || (pp.getDirection() == ParameterDirection.INOUT))
				hasOutputs = true;
			
			// The IN/INOUT parameters need to exist in the initial component knowledge with the correct type
			if ((pp.getDirection() == ParameterDirection.IN) || (pp.getDirection() == ParameterDirection.INOUT)) {				
				if (!hasKnowledgePath(pp.getKnowledgePath(), cp.getComponentInstance().getKnowledgeManager())) {
					reportError(				
						cp,
						"%s parameter \"%s\" (%d) of process %s.%s refers to non-existent knowledge field.\n" 
						+ "Check that all the corresponding knowledge fields are declared as public class fields.",
						pp.getDirection(),
						pp.getKnowledgePath(),
						idx,
						cp.getComponentInstance().getName(),
						cp.getName());
				}				
				else if (!typeMatches(pp.getKnowledgePath(), cp.getComponentInstance().getKnowledgeManager(), pp.getType())) {
					reportError(				
							cp,
							"%s parameter \"%s\" (%d) of process %s.%s does not match the type of the corresponding knowledge field.",
							pp.getDirection(),
							pp.getKnowledgePath(),
							idx,
							cp.getComponentInstance().getName(),
							cp.getName());
				}				
			}
			
			// TODO: store the type information of knowledge (recursively) so
			// that we can check type match of both IN and OUT parameters
			
			idx++;			
		}
		
		if (!hasInputs) {
			reportWarning(
					cp,
					"Process %s.%s has no input parameters.", 
					cp.getComponentInstance().getName(),
					cp.getName());
		}
		if (!hasOutputs) {
			reportWarning(
					cp,
					"Process %s.%s has no output parameters.", 
					cp.getComponentInstance().getName(),
					cp.getName());
		}
	}
	
	
	protected boolean hasKnowledgePath(KnowledgePath kp, KnowledgeManager km) {
		
		// check whether the initial knowledge contains the parameter
		try {
			km.get(Arrays.asList(kp));
		} catch (KnowledgeNotFoundException e) {
			return false;
		}
		return true;
	}
	
	protected boolean typeMatches(KnowledgePath kp, KnowledgeManager km, Class<?> type) {
		ValueSet vs = null;
		try {
			vs = km.get(Arrays.asList(kp));
			// check whether the initial value is of the correct type
			return type.isAssignableFrom(vs.getValue(kp).getClass());
		} catch (KnowledgeNotFoundException e) {
			return true;
		}
	}
	
	
	protected final void reportWarning(EObject where, String formatStr, Object... parameters) {
		errors.add(new ModelValidationError(
				where, 
				Severity.WARNING,
				String.format(formatStr, parameters)));	
	}
	
	protected final void reportError(EObject where, String formatStr, Object... parameters) {
		errors.add(new ModelValidationError(
				where,
				Severity.ERROR,
				String.format(formatStr, parameters)));	
	}
}

