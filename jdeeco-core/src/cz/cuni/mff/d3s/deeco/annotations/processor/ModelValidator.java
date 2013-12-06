package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
	public Collection<ModelValidationError> getErrors() {
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
					"Process %s.%s has no parameter.",
					cp.getComponentInstance().getName(),
					cp.getName());		
		}
		
		boolean hasInputs = false;
		boolean hasOutputs = false;
		
		for (Parameter pp: cp.getParameters()) {
			if ((pp.getDirection() == ParameterDirection.IN) || (pp.getDirection() == ParameterDirection.INOUT))
				hasInputs = true;
			if ((pp.getDirection() == ParameterDirection.OUT) || (pp.getDirection() == ParameterDirection.INOUT))
				hasOutputs = true;
			
			// The IN/INOUT parameters need to exist in the initial component knowledge (with the correct types)
			if (
					((pp.getDirection() == ParameterDirection.IN) || (pp.getDirection() == ParameterDirection.INOUT))
					&& !hasKnowledgePath(pp.getKnowledgePath(), cp.getComponentInstance().getKnowledgeManager(), pp.getType())					
			) {				
				reportError(
						"Process %s.%s has a non-existing knowledge field %s as its %s parameter.",
						cp.getComponentInstance().getName(),
						cp.getName(),
						pp.getKnowledgePath(), 
						pp.getDirection());		
			}
		}
		
		if (!hasInputs) {
			reportWarning(
					"Process %s.%s has no input parameters", 
					cp.getComponentInstance().getName(),
					cp.getName());
		}
		if (!hasOutputs) {
			reportWarning(
					"Process %s.%s has no output parameters", 
					cp.getComponentInstance().getName(),
					cp.getName());
		}
	}
	
	
	protected boolean hasKnowledgePath(KnowledgePath kp, KnowledgeManager km, Class<?> type) {
		ValueSet vs;
		// check whether the initial knowledge contains the parameter
		try {
			vs = km.get(Arrays.asList(kp));
		} catch (KnowledgeNotFoundException e) {
			return false;
		}
		// if yes, check whether the initial value is of the correct type
		return type.isAssignableFrom(vs.getValue(kp).getClass());
	}
	
	
	protected final void reportWarning(String formatStr, Object... parameters) {
		errors.add(new ModelValidationError(
				Severity.WARNING,
				String.format(formatStr, parameters)));	
	}
	
	protected final void reportError(String formatStr, Object... parameters) {
		errors.add(new ModelValidationError(
				Severity.ERROR,
				String.format(formatStr, parameters)));	
	}
}

