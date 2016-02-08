package cz.cuni.mff.d3s.jdeeco.adaptation.correlation;

import java.util.Collections;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorExtensionPoint;
import cz.cuni.mff.d3s.deeco.annotations.processor.CorrelationAwareAnnotationProcessorExtension;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.DuplicateEnsembleDefinitionException;

public class CorrelationPlugin implements DEECoPlugin {

	private boolean verbose;
	private boolean dumpValues;
	
	/** Plugin dependencies. */
	static private final List<Class<? extends DEECoPlugin>> DEPENDENCIES =
			Collections.emptyList();

	private final List<DEECoNode> deecoNodes;

	public CorrelationPlugin(List<DEECoNode> nodesInRealm){
		deecoNodes = nodesInRealm;
		
		verbose = false;
		dumpValues = false;
	}
	
	/**
	 * Specify the verbosity of the correlation process.
	 * @param verbose True to be verbose, false to be still.
	 * @return The self instance of {@link CorrelationPlugin} 
	 */
	public CorrelationPlugin withVerbosity(boolean verbose){
		this.verbose = verbose;
		return this;
	}

	/**
	 * Specify whether the correlation process should dump values while computing.
	 * @param dumpValues True to dump values, false to hold it.
	 * @return The self instance of {@link CorrelationPlugin} 
	 */
	public CorrelationPlugin withDumping(boolean dumpValues){
		this.dumpValues = dumpValues;
		return this;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return DEPENDENCIES;
	}

	@Override
	public void init(DEECoContainer container) {
		AnnotationProcessorExtensionPoint correlationAwareAnnotationProcessorExtension = new CorrelationAwareAnnotationProcessorExtension();
		container.getProcessor().addExtension(correlationAwareAnnotationProcessorExtension);
		
		try {
			final CorrelationManager manager = new CorrelationManager(deecoNodes);
			CorrelationManager.verbose = verbose;
			CorrelationManager.dumpValues = dumpValues;
			
			container.deployComponent(manager);
			container.deployEnsemble(CorrelationDataAggregation.class);
		} catch (AnnotationProcessorException | DuplicateEnsembleDefinitionException e) {
			Log.e("Error while trying to deploy AdaptationManager", e);
		}
	}

}
