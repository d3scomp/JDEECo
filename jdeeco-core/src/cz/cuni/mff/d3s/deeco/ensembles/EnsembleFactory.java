package cz.cuni.mff.d3s.deeco.ensembles;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;

/**
 * Provides functionality for creating ensemble instances (ensemble formation) and getting information about the 
 * desired scheduling of the ensemble formation and the consequential knowledge exchange. Ensemble factories
 * are intended to be used with {@link cz.cuni.mff.d3s.deeco.runtime.DEECoNode} as an alternative way of deploying ensembles, 
 * especially in scenarios requiring custom ensemble formation functionality.
 * 
 * @author Filip Krijt
 * @author Zbyněk Jiráček
 */
public interface EnsembleFactory {
	/**
	 * Returns a collection of ensemble instances that should exist in the system at the moment, given the knowledge stored in the container parameter.
	 * @param container - a knowledge container containing the current state of the system (i.e. knowledge from the POV of a single component).
	 * @return Currently existing ensemble instances.
	 * @throws EnsembleFormationException
	 */
	Collection<EnsembleInstance> createInstances(KnowledgeContainer container) throws EnsembleFormationException;
	/**
	 * Gets the initial scheduling offset of the ensemble formation.
	 * @return Ensemble formation offset.
	 */
	int getSchedulingOffset();
	/**
	 * Gets the period after which the ensemble formation is scheduled again.
	 * @return Ensemble formation period.
	 */
	int getSchedulingPeriod();
}
