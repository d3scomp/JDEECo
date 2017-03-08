package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

import java.util.Collection;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFactory;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFormationException;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;

/**
 * An ensemble factory that uses MiniZinc scripts to create ensembles. The script is run together with
 * input arguments by Gecode solver. Input arguments must be extracted from components knowledge,
 * using knowledge containers. Output from the solver is used to construct the ensembles.
 * 
 * @author Zbyněk Jiráček
 *
 * @see EnsembleFactory
 * @see KnowledgeContainer
 */
public abstract class MiniZincIntelligentEnsembleFactory implements EnsembleFactory {
	
	private MznScriptRunner scriptRunner;
	
	public MiniZincIntelligentEnsembleFactory(MznScriptRunner scriptRunner) {
		this.scriptRunner = scriptRunner;
	}
	
	/**
	 * Create new instance of the factory using default script runner.
	 * 
	 * @param scriptPath The mzn script file.
	 * 
	 * @see MznScriptRunner
	 */
	public MiniZincIntelligentEnsembleFactory(String scriptPath) {
		this(new MznScriptRunner(scriptPath));
	}
	
	/**
	 * Prepares input for the MiniZinc script.
	 * 
	 * @param knowledgeContainer A {@link KnowledgeContainer} that wraps all components' knowledge (local and shadow).
	 * @return An instance of variable registry with input variables that should be passed to the script.
	 * @throws EnsembleFormationException
	 * 
	 * @see KnowledgeContainer
	 * @see ScriptInputVariableRegistry
	 */
	protected abstract ScriptInputVariableRegistry parseInput(KnowledgeContainer knowledgeContainer) throws EnsembleFormationException;
	
	/**
	 * Creates ensemble instances from the solver results.
	 * 
	 * @param scriptOutput Variable registry containing all output variables from the script.
	 * @return Collection of ensemble instances.
	 * @throws EnsembleFormationException
	 * 
	 * @see ScriptOutputVariableRegistry
	 * @see EnsembleInstance
	 */
	protected abstract Collection<EnsembleInstance> createInstancesFromOutput(ScriptOutputVariableRegistry scriptOutput) throws EnsembleFormationException;

	@Override
	public Collection<EnsembleInstance> createInstances(KnowledgeContainer container) throws EnsembleFormationException {
		
		ScriptInputVariableRegistry inputVars = parseInput(container);
		
		try {
			Map<String, String> output = scriptRunner.runScript(inputVars);
			ScriptOutputVariableRegistry outputVars = new ScriptOutputVariableRegistry(output);
			return createInstancesFromOutput(outputVars);
			
		} catch (ScriptExecutionException e) {
			throw new EnsembleFormationException("Failed to create ensemble instance (" + this.getClass().getName() + ")", e);
		}
	}
}
