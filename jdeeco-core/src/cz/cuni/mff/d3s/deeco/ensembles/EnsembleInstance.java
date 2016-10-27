package cz.cuni.mff.d3s.deeco.ensembles;

/**
 * Provides functionality for performing the knowledge exchange, represents a currently existing ensemble instance. 
 * All ensembles using the {@link cz.cuni.mff.d3s.deeco.ensembles.EnsembleFactory} approach of deployment should 
 * implement this interface. 
 * 
 * @author Filip Krijt
 * @author Zbyněk Jiráček
 */
public interface EnsembleInstance {
	/**
	 * Performs knowledge exchange on this ensemble instance.
	 */
	void performKnowledgeExchange();
}
