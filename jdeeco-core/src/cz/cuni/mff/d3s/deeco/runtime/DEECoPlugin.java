package cz.cuni.mff.d3s.deeco.runtime;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorExtensionPoint;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;

/**
 * Contains the specification of a DEECo plugin. The core is itself a plugin of type <code>RuntimeFramework</code>, only
 * it is always present. Every plugin that has no explicit dependencies implicitly depends on the core plugin.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Filip Krijt <krijt@d3s.mff.cuni.cz>
 */
public interface DEECoPlugin {

	/**
	 * The other plugins that this plugin depends on. If its dependencies are not provided, the plugin will not be
	 * initialized and used.
	 */
	public List<Class<? extends DEECoPlugin>> getDependencies();

	/**
	 * Method through which a plugin extends the plugins it depends on (or the DEECo core plugin, if it has no explicit
	 * dependencies).
	 * <p>
	 * It should be used to do one or more of the following:
	 * <ul>
	 * <li>Register an extension via {@link AnnotationProcessor#addExtension(AnnotationProcessorExtensionPoint)}.</li>
	 * <li>Register any other extension to different points of the runtime (similarly to the annotation processor)</li>
	 * <li>Deploy any "system" components or ensembles</li>
	 * <li>Connect the metadata model to its plugin-related extensions (other EMF models)</li>
	 * </ul>
	 * </p>
	 * 
	 * @param container
	 *            main DEECo container that provides the necessary hooks to achieve the above extensions
	 */
	public void init(DEECoContainer container) throws PluginInitFailedException;
}
