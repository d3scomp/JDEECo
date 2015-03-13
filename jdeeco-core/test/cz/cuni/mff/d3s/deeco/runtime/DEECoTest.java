package cz.cuni.mff.d3s.deeco.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.CorrectE1;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.Timer;

/**
 * Test class for the main DEECo application container.
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Filip Krijt <krijt@d3s.mff.cuni.cz>
 *
 */
public class DEECoTest {	
	
	// The following is a workaround for the Mockito problem of mocking subclasses - all mocks of the same interface share a common class,
	// which breaks the plugin identification in DEECo (where each plugin is assumed to be represented by a different class).
	
	interface P0 extends DEECoPlugin{};
	interface P1 extends DEECoPlugin{};
	interface P2 extends DEECoPlugin{};
	interface P3 extends DEECoPlugin{};
	interface P4 extends DEECoPlugin{};
	interface P5 extends DEECoPlugin{};
	interface P6 extends DEECoPlugin{};
	interface P7 extends DEECoPlugin{};
	interface P8 extends DEECoPlugin{};
	interface P9 extends DEECoPlugin{};
	
	Timer timer = new DiscreteEventTimer();
	
	/**
	 * Tests if the object fields have been initialized correctly. 
	 * @throws DEECoException 
	 */
	@Test
	public void testFieldInitialization() throws DEECoException
	{
		DEECoNode deeco = new DEECoNode(0, timer);
		assertNotNull(deeco.knowledgeManagerFactory);
		assertNotNull(deeco.model);
		assertNotNull(deeco.pluginsMap);
		assertNotNull(deeco.processor);
		assertNotNull(deeco.runtime);	
	}
	
	/** 
	 * Verifies that the dependency and the plugin extending it are initialized in the correct order.
	 */
	private void verifyPluginInitOrder(InOrder order, DEECoContainer deeco, DEECoPlugin dependency, DEECoPlugin extension)
	{
		order.verify(dependency).init(deeco);
		order.verify(extension).init(deeco);
	}
	
	/**
	 * Tests if the DEECo can initialize two plugins in the correct order, with one of them being dependent on the other. 
	 * @throws DEECoException 
	 */
	@Test
	public void testDependencyOrderSimple() throws DEECoException
	{
		DEECoPlugin plugin1 = mock(P0.class);
		DEECoPlugin plugin2 = mock(P1.class);		
		
		when(plugin1.getDependencies()).thenReturn(Arrays.asList());
		when(plugin2.getDependencies()).thenReturn(Arrays.asList(plugin1.getClass()));
		
		InOrder order = inOrder(plugin1, plugin2);
		
		DEECoNode deeco = new DEECoNode(1, timer, plugin2, plugin1);
		
		verifyPluginInitOrder(order, deeco, plugin1, plugin2);
	}
	
	/**
	 * Tests if the DEECo can initialize several plugins in the correct order, with their dependency graph being a basic DAG
	 * @throws DEECoException 
	 */
	@Test
	public void testDependencyOrderDAG() throws DEECoException
	{
		DEECoPlugin pluginBase = mock(P0.class);
		DEECoPlugin plugin1 = mock(P1.class);
		DEECoPlugin plugin2 = mock(P2.class);
		DEECoPlugin plugin3 = mock(P3.class);
		
		when(pluginBase.getDependencies()).thenReturn(Arrays.asList());
		when(plugin1.getDependencies()).thenReturn(Arrays.asList(pluginBase.getClass()));
		when(plugin2.getDependencies()).thenReturn(Arrays.asList(pluginBase.getClass()));
		when(plugin3.getDependencies()).thenReturn(Arrays.asList(plugin1.getClass(), plugin1.getClass()));
		
		InOrder order1 = inOrder(pluginBase, plugin1);
		InOrder order2 = inOrder(pluginBase, plugin2);
		InOrder order3 = inOrder(plugin1, plugin3);
		InOrder order4 = inOrder(plugin2, plugin3);
		
		DEECoNode deeco = new DEECoNode(2, timer, plugin1, pluginBase, plugin2, plugin3);
		
		verifyPluginInitOrder(order1, deeco, pluginBase, plugin1);
		verifyPluginInitOrder(order2, deeco, pluginBase, plugin2);
		verifyPluginInitOrder(order3, deeco, plugin1, plugin3);
		verifyPluginInitOrder(order4, deeco, plugin2, plugin3);		
	}
	
	/**
	 * Tests if the DEECo can initialize three plugins in the correct order, with one of them being dependent on the other two base plugins.
	 * @throws DEECoException 
	 */
	@Test
	public void testDependencyMultiBase() throws DEECoException
	{
		DEECoPlugin pluginBase1 = mock(P0.class);
		DEECoPlugin pluginBase2 = mock(P1.class);
		DEECoPlugin plugin = mock(P3.class);
		
		when(pluginBase1.getDependencies()).thenReturn(Arrays.asList());
		when(pluginBase2.getDependencies()).thenReturn(Arrays.asList());
		when(plugin.getDependencies()).thenReturn(Arrays.asList(pluginBase1.getClass(), pluginBase2.getClass()));
		
		InOrder order1 = inOrder(pluginBase1, plugin);
		InOrder order2 = inOrder(pluginBase2, plugin);
		
		DEECoNode deeco = new DEECoNode(3, timer, pluginBase2, plugin, pluginBase1);
		
		verifyPluginInitOrder(order1, deeco, pluginBase1, plugin);
		verifyPluginInitOrder(order2, deeco, pluginBase2, plugin);			
	}
	
	/**
	 * Tests if the DEECo can initialize three plugins in the correct order, with one of them being a common base dependency of the other two.
	 * @throws DEECoException 
	 */
	@Test
	public void testDependencyOrderMultiExtension() throws DEECoException
	{
		DEECoPlugin pluginBase = mock(P0.class);
		DEECoPlugin plugin1 = mock(P1.class);
		DEECoPlugin plugin2 = mock(P2.class);
		
		when(pluginBase.getDependencies()).thenReturn(Arrays.asList());
		when(plugin1.getDependencies()).thenReturn(Arrays.asList(pluginBase.getClass()));
		when(plugin2.getDependencies()).thenReturn(Arrays.asList(pluginBase.getClass()));
		
		InOrder order1 = inOrder(pluginBase, plugin1);
		InOrder order2 = inOrder(pluginBase, plugin2);
		
		DEECoNode deeco = new DEECoNode(4, timer, plugin1, pluginBase, plugin2);
		
		verifyPluginInitOrder(order1, deeco, pluginBase, plugin1);
		verifyPluginInitOrder(order2, deeco, pluginBase, plugin2);
	}
	
	/**
	 * Tests if the DEECo can initialize a fairly complex 10-plugin scenario in the correct order.
	 * @throws DEECoException 
	 */
	@Test
	public void testDependencyOrderComplex() throws DEECoException
	{
		
		// Create all the plugins
		DEECoPlugin basePlugin1 = mock(P0.class);
		DEECoPlugin basePlugin2 = mock(P1.class);
		DEECoPlugin basePlugin3 = mock(P2.class);
		
		DEECoPlugin tier1Plugin1 = mock(P3.class);
		DEECoPlugin tier1Plugin2 = mock(P4.class);
		
		DEECoPlugin tier2Plugin1 = mock(P5.class);		
		DEECoPlugin tier2Plugin2 = mock(P6.class);
		
		DEECoPlugin tier3Plugin1 = mock(P7.class);	
		
		DEECoPlugin independentBase = mock(P8.class);
		DEECoPlugin independentExtension = mock(P9.class);
		
		// Define dependencies
		when(basePlugin1.getDependencies()).thenReturn(Arrays.asList());
		when(basePlugin2.getDependencies()).thenReturn(Arrays.asList());
		when(basePlugin3.getDependencies()).thenReturn(Arrays.asList());
		when(independentBase.getDependencies()).thenReturn(Arrays.asList());
		
		when(tier1Plugin1.getDependencies()).thenReturn(Arrays.asList(basePlugin1.getClass()));
		when(tier1Plugin2.getDependencies()).thenReturn(Arrays.asList(basePlugin1.getClass(), basePlugin2.getClass()));
		
		when(tier2Plugin1.getDependencies()).thenReturn(Arrays.asList(tier1Plugin1.getClass()));
		when(tier2Plugin2.getDependencies()).thenReturn(Arrays.asList(tier1Plugin1.getClass(), tier1Plugin2.getClass(), basePlugin3.getClass()));
		
		when(tier3Plugin1.getDependencies()).thenReturn(Arrays.asList(tier2Plugin2.getClass()));
		
		when(independentExtension.getDependencies()).thenReturn(Arrays.asList(independentBase.getClass()));
		
		
		// Create ordering verifiers
		InOrder order1 = inOrder(basePlugin1, tier1Plugin1);
		InOrder order2 = inOrder(basePlugin1, tier1Plugin2);
		InOrder order3 = inOrder(basePlugin2, tier1Plugin2);
		InOrder order4 = inOrder(basePlugin3, tier2Plugin2);
		InOrder order5 = inOrder(tier1Plugin1, tier2Plugin1);
		InOrder order6 = inOrder(tier1Plugin1, tier2Plugin2);
		InOrder order7 = inOrder(tier1Plugin2, tier2Plugin2);
		InOrder order8 = inOrder(tier2Plugin2, tier3Plugin1);
		InOrder order9 = inOrder(independentBase, independentExtension);
		
		// Create DEECo
		DEECoNode deeco = new DEECoNode(5, timer, basePlugin1, basePlugin2, basePlugin3, tier1Plugin1, tier1Plugin2, tier2Plugin1, tier2Plugin2, tier3Plugin1, independentBase, independentExtension);
		
		// Verify ordering
		verifyPluginInitOrder(order1, deeco, basePlugin1, tier1Plugin1);
		verifyPluginInitOrder(order2, deeco, basePlugin1, tier1Plugin2);
		verifyPluginInitOrder(order3, deeco, basePlugin2, tier1Plugin2);
		verifyPluginInitOrder(order4, deeco, basePlugin3, tier2Plugin2);
		verifyPluginInitOrder(order5, deeco, tier1Plugin1, tier2Plugin1);
		verifyPluginInitOrder(order6, deeco, tier1Plugin1, tier2Plugin2);
		verifyPluginInitOrder(order7, deeco, tier1Plugin2, tier2Plugin2);
		verifyPluginInitOrder(order8, deeco, tier2Plugin2, tier3Plugin1);
		verifyPluginInitOrder(order9, deeco, independentBase, independentExtension);
	}

	/**
	 * Tests if the DEECo can detect a missing plugin dependency and react by throwing a correct exception.
	 * @throws DEECoException 
	 */
	@Test(expected = cz.cuni.mff.d3s.deeco.runtime.MissingDependencyException.class)
	public void testNonExistentDependency() throws DEECoException
	{
		// Correct exception is thrown on detecting non-existent dependency		
		
		DEECoPlugin basePlugin = mock(P1.class);
		DEECoPlugin dependentPlugin = mock(P2.class);
		
		when(dependentPlugin.getDependencies()).thenReturn(Arrays.asList(basePlugin.getClass()));
		
		new DEECoNode(7, timer, dependentPlugin);		
	}
	
	/**
	 * Tests if the DEECo can detect a three-plugin dependency cycle and react by throwing a correct exception.
	 * @throws DEECoException 
	 */
	@Test(expected = cz.cuni.mff.d3s.deeco.runtime.CycleDetectedException.class)
	public void testCycle() throws DEECoException
	{
		// Tests for a basic (3 plugins) dependency cycle
		DEECoPlugin plugin1 = mock(P0.class);
		DEECoPlugin plugin2 = mock(P1.class);
		DEECoPlugin plugin3 = mock(P2.class);
		
		when(plugin1.getDependencies()).thenReturn(Arrays.asList(plugin2.getClass()));
		when(plugin2.getDependencies()).thenReturn(Arrays.asList(plugin3.getClass()));
		when(plugin3.getDependencies()).thenReturn(Arrays.asList(plugin1.getClass()));
		
		new DEECoNode(8, timer, plugin1, plugin2, plugin3);
	}
	
	/**
	 * Tests if the DEECo can detect a more elaborate plugin dependency cycle, decorated with a base dependency and an additional independent branch. 
	 * Should react by throwing a correct exception. 
	 * @throws DEECoException 
	 */
	@Test(expected = cz.cuni.mff.d3s.deeco.runtime.CycleDetectedException.class)
	public void testCycle2() throws DEECoException
	{
		// Test for a more elaborate plugin dependency cycle, decorated with a base dependency and an additional independent branch.
		DEECoPlugin pluginBase = mock(P0.class);
		DEECoPlugin plugin1 = mock(P1.class);
		DEECoPlugin plugin2 = mock(P2.class);
		DEECoPlugin plugin3 = mock(P3.class);		
		
		DEECoPlugin pluginBase2 = mock(P9.class);
		DEECoPlugin pluginOther = mock(P8.class);
			
		when(plugin1.getDependencies()).thenReturn(Arrays.asList(plugin2.getClass(), pluginBase.getClass()));
		when(plugin2.getDependencies()).thenReturn(Arrays.asList(plugin3.getClass()));
		when(plugin3.getDependencies()).thenReturn(Arrays.asList(plugin1.getClass()));
		
		when(pluginOther.getDependencies()).thenReturn(Arrays.asList(pluginBase2.getClass()));
		
		when(pluginBase.getDependencies()).thenReturn(Arrays.asList());
		when(pluginBase2.getDependencies()).thenReturn(Arrays.asList());
		
		new DEECoNode(9, timer, plugin1, plugin2, plugin3, pluginBase, pluginBase2, pluginOther);
	}
	
	/**
	 * Tests if a DEECo instance can be created without any plugins.
	 * @throws DEECoException 
	 */
	@Test
	public void testNoPlugins() throws DEECoException
	{
		new DEECoNode(10, timer);		
	}	
	
	/**
	 * Tests if the plugins injected into the DEECo are visible to the other plugins.
	 * @throws DEECoException 
	 */
	@Test
	public void testPluginAccess() throws DEECoException
	{		
		List<DEECoPlugin> plugins = new ArrayList<>();
		plugins.add(mock(P0.class));
		plugins.add(mock(P1.class));
		plugins.add(mock(P2.class));
		plugins.add(mock(P3.class));
		plugins.add(mock(P4.class));
		plugins.add(mock(P5.class));
		plugins.add(mock(P6.class));
		plugins.add(mock(P7.class));
		plugins.add(mock(P8.class));
		plugins.add(mock(P9.class));
		
		DEECoPlugin[] pluginArray = plugins.toArray(new DEECoPlugin[0]);
		
		DEECoNode deeco = new DEECoNode(11, timer, pluginArray);
		
		for(DEECoPlugin p : plugins)
		{
			assertTrue(deeco.pluginsMap.containsKey(p.getClass()));
			assertEquals(p, deeco.getPluginInstance(p.getClass()));
		}			
	}
}
