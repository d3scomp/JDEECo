package cz.cuni.mff.d3s.deeco.demo.intelligent;

import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;

public class SimpleEnsemble implements EnsembleInstance {

	@Override
	public void performKnowledgeExchange() {
		SimpleEnsembleFactory.outputStream.println("Knowledge exchange performed!");		
	}
}
