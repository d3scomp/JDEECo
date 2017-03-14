package cz.cuni.mff.d3s.jdeeco.adaptation.correlation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorExtensionPoint;
import cz.cuni.mff.d3s.deeco.annotations.processor.CorrelationAwareAnnotationProcessorExtension;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.DuplicateEnsembleDefinitionException;
import cz.cuni.mff.d3s.jdeeco.adaptation.AdaptationPlugin;
import cz.cuni.mff.d3s.metaadaptation.correlation.Component;
import cz.cuni.mff.d3s.metaadaptation.correlation.CorrelationManager;

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
	
	private final EnsembleManagerImpl ensembleManager;
	private final CorrelationManager manager;
	
	/** Plugin dependencies. */
	@SuppressWarnings("unchecked")
	static private final List<Class<? extends DEECoPlugin>> DEPENDENCIES =
			Arrays.asList(new Class[]{AdaptationPlugin.class});;


	public CorrelationPlugin(){		
		verbose = false;
		dumpValues = false;
		
		ensembleManager = new EnsembleManagerImpl();
		manager = new CorrelationManager(ensembleManager, 
				new CorrelationEnsembleFactory());
	}
	
	public void setDEECoNodes(Set<DEECoNode> nodes){
		ensembleManager.setNodes(nodes);
		
		Set<Component> components = new HashSet<>();
		for(DEECoNode node : nodes){
			for(ComponentInstance ci : node.getRuntimeMetadata().getComponentInstances()){
				components.add(new ComponentImpl(ci.getKnowledgeManager().getId(), ci));
			}
		}
		manager.setComponents(components);
	}
	
	/**
	 * Specify the verbosity of the correlation process.
	 * @param verbose True to be verbose, false to be still.
	 * @return The self instance of {@link CorrelationPlugin} 
	 */
	public CorrelationPlugin withVerbosity(boolean verbose){
		this.verbose = verbose;
		CorrelationManager.verbose = verbose;
		return this;
	}

	/**
	 * Specify whether the correlation process should dump values while computing.
	 * @param dumpValues True to dump values, false to hold it.
	 * @return The self instance of {@link CorrelationPlugin} 
	 */
	public CorrelationPlugin withDumping(boolean dumpValues){
		this.dumpValues = dumpValues;
		CorrelationManager.dumpValues = dumpValues;
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
			CorrelationManager.verbose = verbose;
			CorrelationManager.dumpValues = dumpValues;
			
			container.deployComponent(new CorrelationKnowledgeData());
			container.deployEnsemble(CorrelationDataAggregation.class);
			
			AdaptationPlugin adaptationPlugin = container.getPluginInstance(AdaptationPlugin.class);
			adaptationPlugin.registerAdaptation(manager);
			
		} catch (AnnotationProcessorException | DuplicateEnsembleDefinitionException e) {
			Log.e("Error while trying to deploy AdaptationManager", e);
		}
	}

}
