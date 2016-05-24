package cz.cuni.mff.d3s.deeco.ensembles;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;

public interface EnsembleFactory {
	Collection<EnsembleInstance> createInstances(KnowledgeContainer container);
}
