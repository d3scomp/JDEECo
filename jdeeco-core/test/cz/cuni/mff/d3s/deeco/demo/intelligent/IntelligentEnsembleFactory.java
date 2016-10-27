package cz.cuni.mff.d3s.deeco.demo.intelligent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFormationException;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.ensembles.intelligent.HeterogeneousArrayException;
import cz.cuni.mff.d3s.deeco.ensembles.intelligent.MiniZincIntelligentEnsembleFactory;
import cz.cuni.mff.d3s.deeco.ensembles.intelligent.MznScriptRunner;
import cz.cuni.mff.d3s.deeco.ensembles.intelligent.OutputVariableParseException;
import cz.cuni.mff.d3s.deeco.ensembles.intelligent.ScriptInputVariableRegistry;
import cz.cuni.mff.d3s.deeco.ensembles.intelligent.ScriptOutputVariableRegistry;
import cz.cuni.mff.d3s.deeco.ensembles.intelligent.UnsupportedVariableTypeException;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainerException;

public class IntelligentEnsembleFactory extends MiniZincIntelligentEnsembleFactory {
	
	private List<Rescuer> rescuers;
	private static final Integer[] trainPositions = {50, 25};
	
	public IntelligentEnsembleFactory() {
		super("test/cz/cuni/mff/d3s/deeco/demo/intelligent/IntelligentEnsemble.mzn");
	}

	@Override
	public int getSchedulingOffset() {
		return 0;
	}

	@Override
	public int getSchedulingPeriod() {
		return 1000;
	}

	@Override
	protected ScriptInputVariableRegistry parseInput(KnowledgeContainer knowledgeContainer) throws EnsembleFormationException {
		try {
			ScriptInputVariableRegistry result = new ScriptInputVariableRegistry();
			rescuers = new ArrayList<>(knowledgeContainer.getTrackedKnowledgeForRole(Rescuer.class));
			result.addVariable("component_count", rescuers.size());
			result.addVariable("train_count", trainPositions.length);
			result.addVariable("component_pos", rescuers.stream().map(r -> r.pos).collect(Collectors.toList()).toArray());
			result.addVariable("train_pos", trainPositions);
			return result;
			
		} catch (UnsupportedVariableTypeException | KnowledgeContainerException | HeterogeneousArrayException e) {
			throw new EnsembleFormationException(e);
		}
	}

	@Override
	protected Collection<EnsembleInstance> createInstancesFromOutput(ScriptOutputVariableRegistry scriptOutput) throws EnsembleFormationException {
		try {
			Integer[] componentPlacement = scriptOutput.getArray1dValue("component_train", Integer.class);
			
			List<EnsembleInstance> result = new ArrayList<>();			
			for (int i = 1; i <= trainPositions.length; i++) {
				IntelligentEnsemble ensemble = new IntelligentEnsemble(i);				
				result.add(ensemble);
			}
			
			for (int j = 0; j < componentPlacement.length; j++) {
				((IntelligentEnsemble) result.get(componentPlacement[j] - 1)).members.add(rescuers.get(j));
			}
			
			return result;
			
		} catch (UnsupportedVariableTypeException | OutputVariableParseException e) {
			throw new EnsembleFormationException(e);
		}
	}

}
