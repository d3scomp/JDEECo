package cz.cuni.mff.d3s.deeco.demo.intelligent;

import java.util.Arrays;
import java.util.Collection;

import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFactory;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;

public class SimpleEnsembleFactory implements EnsembleFactory {

	@Override
	public Collection<EnsembleInstance> createInstances(KnowledgeContainer container) {
		//System.out.println("Formation requested!");
		return Arrays.asList(new SimpleEnsemble());
	}

	@Override
	public int getOffset() {		
		return 42;
	}

	@Override
	public int getPeriod() {
		return 1000;
	}
}
