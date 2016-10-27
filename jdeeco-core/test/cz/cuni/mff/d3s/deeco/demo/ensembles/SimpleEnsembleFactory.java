package cz.cuni.mff.d3s.deeco.demo.ensembles;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFactory;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;

public class SimpleEnsembleFactory implements EnsembleFactory {
	public static PrintStream outputStream = System.out;
	int formationCount = 0;
	
	@Override
	public Collection<EnsembleInstance> createInstances(KnowledgeContainer container) {		
		++formationCount;
		outputStream.println("Ensemble formation requested!");
		return Arrays.asList(new SimpleEnsemble());
	}

	@Override
	public int getSchedulingOffset() {		
		return 42;
	}

	@Override
	public int getSchedulingPeriod() {
		return 400;
	}
}
