package cz.cuni.mff.d3s.jdeeco.adaptation.correlation;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorExtensionPoint;
import cz.cuni.mff.d3s.deeco.annotations.processor.CorrelationAwareAnnotationProcessorExtension;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.DuplicateEnsembleDefinitionException;
import cz.cuni.mff.d3s.jdeeco.adaptation.AdaptationPlugin;

/**
 * Correlation plugin deploys a component that monitors and correlates data
 * of other components in the system and deploys new ensembles based on the
 * results of the correlation.
 * 
 * <p>It is desirable to have only one instance of the correlation component
 * since its processes are resource demanding and there is no benefit of having
 * more than one instance.</p>
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class CorrelationPlugin implements DEECoPlugin {

	private boolean verbose;
	private boolean dumpValues;
	private boolean logGeneratedEnsembles;
	
	/** Plugin dependencies. */
	@SuppressWarnings("unchecked")
	static private final List<Class<? extends DEECoPlugin>> DEPENDENCIES =
			Arrays.asList(new Class[]{AdaptationPlugin.class});;

	private final List<DEECoNode> deecoNodes;

	public CorrelationPlugin(List<DEECoNode> nodesInRealm){
		deecoNodes = nodesInRealm;
		
		verbose = false;
		dumpValues = false;
		logGeneratedEnsembles = false;
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
	
	public CorrelationPlugin withGeneratedEnsemblesLogging(boolean enableLogging){
		this.logGeneratedEnsembles = enableLogging;
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
			CorrelationManager.logGeneratedEnsembles = logGeneratedEnsembles;
			
			container.deployComponent(manager);
			container.deployEnsemble(CorrelationDataAggregation.class);
			
			AdaptationPlugin adaptationPlugin = container.getPluginInstance(AdaptationPlugin.class);
			adaptationPlugin.registerAdaptation(manager);
		} catch (AnnotationProcessorException | DuplicateEnsembleDefinitionException e) {
			Log.e("Error while trying to deploy AdaptationManager", e);
		}
	}

}
