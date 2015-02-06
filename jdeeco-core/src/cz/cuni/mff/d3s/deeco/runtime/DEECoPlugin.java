package cz.cuni.mff.d3s.deeco.runtime;

import java.util.List;

/**
 * Contains the specification of a DEECo plugin.
 * The core is itself a plugin, only it is always present. 
 * Every plugin that has no explicit dependencies implicitly depends on the core plugin. 
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Filip Krijt <krijt@d3s.mff.cuni.cz>
 */
public interface DEECoPlugin {

	/**
	 * The other plugins that this plugin depends on.
	 * If its dependencies cannot be resolved, the plugin cannot be used.  
	 */
	@SuppressWarnings("rawtypes")
	public List<Class> getDependencies();

	/**
	 * Registers all listeners for this plugin to different places in the plugins it depends on. 
	 */
	public void init();
	
}
