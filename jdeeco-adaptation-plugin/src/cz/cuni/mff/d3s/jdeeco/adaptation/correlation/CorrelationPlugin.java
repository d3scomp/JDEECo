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

public class CorrelationPlugin implements DEECoPlugin {

	/** Plugin dependencies. */
	static private final List<Class<? extends DEECoPlugin>> DEPENDENCIES =
			Collections.emptyList();

	private final List<DEECoNode> deecoNodes;

	public CorrelationPlugin(List<DEECoNode> nodesInRealm){
		deecoNodes = nodesInRealm;
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
			container.deployComponent(manager);
		} catch (AnnotationProcessorException e) {
			Log.e("Error while trying to deploy AdaptationManager", e);
		}
	}

}
