package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFactory;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;

/**
 * 
 * @author Zbyněk Jiráček
 *
 */
public abstract class MiniZincIntelligentEnsembleFactory implements EnsembleFactory {
	
	private MznScriptRunner scriptRunner;
	
	public MiniZincIntelligentEnsembleFactory(MznScriptRunner scriptRunner) {
		this.scriptRunner = scriptRunner;
	}
			
	protected abstract ScriptInputVariableRegistry parseInput(KnowledgeContainer knowledgeContainer);
	
	protected abstract Collection<EnsembleInstance> createInstancesFromOutput(ScriptOutputVariableRegistry scriptOutput);


	@Override
	public Collection<EnsembleInstance> createInstances(KnowledgeContainer container) {
		
		ScriptInputVariableRegistry inputVars = parseInput(container);
		
		ScriptOutputVariableRegistry outputVars = scriptRunner.runScript(inputVars);
		
		createInstancesFromOutput(outputVars);
		
		return null;
	}
}
