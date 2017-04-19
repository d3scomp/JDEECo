package cz.cuni.mff.d3s.jdeeco.adaptation.componentIsolation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer.StartupListener;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.PluginStartupFailedException;
import cz.cuni.mff.d3s.jdeeco.adaptation.AdaptationPlugin;
import cz.cuni.mff.d3s.metaadaptation.componentisolation.Component;
import cz.cuni.mff.d3s.metaadaptation.componentisolation.ComponentIsolationManager;

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
public class ComponentIsolationPlugin implements DEECoPlugin, StartupListener {

	private boolean verbose;
	
	private final Set<DEECoNode> nodes;

	private AdaptationPlugin adaptationPlugin = null;
	
	
	/** Plugin dependencies. */
	@SuppressWarnings("unchecked")
	static private final List<Class<? extends DEECoPlugin>> DEPENDENCIES =
			Arrays.asList(new Class[]{AdaptationPlugin.class});

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return DEPENDENCIES;
	}

	public ComponentIsolationPlugin(Set<DEECoNode> nodes){
		if(nodes == null){
			throw new IllegalArgumentException(String.format("The %s argument is null.", "nodes"));
		}
		
		verbose = false;
		this.nodes = nodes;
	}
		
	/**
	 * Specify the verbosity of the correlation process.
	 * @param verbose True to be verbose, false to be still.
	 * @return The self instance of {@link ComponentIsolationPlugin} 
	 */
	public ComponentIsolationPlugin withVerbosity(boolean verbose){
		this.verbose = verbose;
		return this;
	}
	
	@Override
	public void init(DEECoContainer container) {
		container.addStartupListener(this);
		
		adaptationPlugin = container.getPluginInstance(AdaptationPlugin.class);
	}

	@Override
	public void onStartup() throws PluginStartupFailedException {
		if(adaptationPlugin == null){
			throw new PluginStartupFailedException(String.format(
					"The %s plugin doesn't have a reference to %s.",
					"NonDeterministicModeSwitching",
					"AdaptationPlugin"));
		}
		
		Set<Component> c = new HashSet<>();
		for(DEECoNode node : nodes){
			for(ComponentInstance ci : node.getRuntimeMetadata().getComponentInstances()){
				ComponentImpl component = new ComponentImpl(ci);
				component.setVerbosity(verbose);
				c.add(component);
			}
		}
		
		ComponentIsolationManager ciManager = new ComponentIsolationManager(new ComponentManagerImpl(c));
		ciManager.setVerbosity(verbose);
		
		adaptationPlugin.registerAdaptation(ciManager);		
	}
}
