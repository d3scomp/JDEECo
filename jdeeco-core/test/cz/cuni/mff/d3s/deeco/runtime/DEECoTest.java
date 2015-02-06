package cz.cuni.mff.d3s.deeco.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import cz.cuni.mff.d3s.deeco.runtime.DEECo;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.deeco.runtime.PluginDependencyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.*;
import org.mockito.InOrder;

/**
 * Test class for the main DEECo application container.
 * @author Filip Krijt <krijt@d3s.mff.cuni.cz>
 *
 */
public class DEECoTest {	
	
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
	
	@Test
	public void testFieldInitialization() throws PluginDependencyException
	{
		DEECo deeco = new DEECo();
		assertNotNull(deeco.knowledgeManagerFactory);
		assertNotNull(deeco.model);
		assertNotNull(deeco.pluginsMap);
		assertNotNull(deeco.processor);
		assertNotNull(deeco.runtime);		
	}
	
	
	@Test
	public void testDependencyOrderSimple() throws PluginDependencyException
	{
		DEECoPlugin plugin1 = mock(P0.class);
		DEECoPlugin plugin2 = mock(P1.class);		
		
		when(plugin1.getDependencies()).thenReturn(Arrays.asList());
		when(plugin2.getDependencies()).thenReturn(Arrays.asList(plugin1.getClass()));
		
		InOrder order = inOrder(plugin1, plugin2);
		
		DEECo deeco = new DEECo(plugin2, plugin1);
		
		order.verify(plugin1).init(deeco);
		order.verify(plugin2).init(deeco);	
	}
	
	@Test
	public void testDependencyOrderDAG() throws PluginDependencyException
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
		
		DEECo deeco = new DEECo(plugin1, pluginBase, plugin2, plugin3);
		
		order1.verify(pluginBase).init(deeco);
		order1.verify(plugin1).init(deeco);
		
		order2.verify(pluginBase).init(deeco);
		order2.verify(plugin2).init(deeco);
		
		order3.verify(plugin1).init(deeco);
		order3.verify(plugin3).init(deeco);
		
		order4.verify(plugin2).init(deeco);
		order4.verify(plugin3).init(deeco);
	}
	
	@Test
	public void testDependencyMultiBase() throws PluginDependencyException
	{
		DEECoPlugin pluginBase1 = mock(P0.class);
		DEECoPlugin pluginBase2 = mock(P1.class);
		DEECoPlugin plugin = mock(P3.class);
		
		when(pluginBase1.getDependencies()).thenReturn(Arrays.asList());
		when(pluginBase2.getDependencies()).thenReturn(Arrays.asList());
		when(plugin.getDependencies()).thenReturn(Arrays.asList(pluginBase1.getClass(), pluginBase2.getClass()));
		
		InOrder order1 = inOrder(pluginBase1, plugin);
		InOrder order2 = inOrder(pluginBase2, plugin);
		
		DEECo deeco = new DEECo(pluginBase2, plugin, pluginBase1);
		
		order1.verify(pluginBase1).init(deeco);
		order1.verify(plugin).init(deeco);
		
		order2.verify(pluginBase2).init(deeco);
		order2.verify(plugin).init(deeco);	
	}
	
	@Test
	public void testDependencyOrderMultiExtension() throws PluginDependencyException
	{
		DEECoPlugin pluginBase = mock(P0.class);
		DEECoPlugin plugin1 = mock(P1.class);
		DEECoPlugin plugin2 = mock(P2.class);
		
		when(pluginBase.getDependencies()).thenReturn(Arrays.asList());
		when(plugin1.getDependencies()).thenReturn(Arrays.asList(pluginBase.getClass()));
		when(plugin2.getDependencies()).thenReturn(Arrays.asList(pluginBase.getClass()));
		
		InOrder order1 = inOrder(pluginBase, plugin1);
		InOrder order2 = inOrder(pluginBase, plugin2);
		
		DEECo deeco = new DEECo(plugin1, pluginBase, plugin2);
		
		order1.verify(pluginBase).init(deeco);
		order1.verify(plugin1).init(deeco);
		
		order2.verify(pluginBase).init(deeco);
		order2.verify(plugin2).init(deeco);
	}
	
	@Test @Ignore
	public void testDependencyOrderRandomized()
	{
		Random generator = new Random(42);
		//TODO
	}
	
	@Test(expected = cz.cuni.mff.d3s.deeco.runtime.MissingDependencyException.class)
	public void testNonExistentDependency() throws PluginDependencyException
	{
		// Correct exception is thrown on detecting non-existent dependency		
		
		DEECoPlugin basePlugin = mock(P1.class);
		DEECoPlugin dependentPlugin = mock(P2.class);
		
		when(dependentPlugin.getDependencies()).thenReturn(Arrays.asList(basePlugin.getClass()));
		
		new DEECo(dependentPlugin);		
	}
	
	@Test(expected = cz.cuni.mff.d3s.deeco.runtime.CycleDetectedException.class)
	public void testCycle() throws PluginDependencyException
	{
		// Tests for a basic (3 plugins) dependency cycle
		DEECoPlugin plugin1 = mock(P0.class);
		DEECoPlugin plugin2 = mock(P1.class);
		DEECoPlugin plugin3 = mock(P2.class);
		
		when(plugin1.getDependencies()).thenReturn(Arrays.asList(plugin2.getClass()));
		when(plugin2.getDependencies()).thenReturn(Arrays.asList(plugin3.getClass()));
		when(plugin3.getDependencies()).thenReturn(Arrays.asList(plugin1.getClass()));
		
		new DEECo(plugin1, plugin2, plugin3);
	}
	
	@Test(expected = cz.cuni.mff.d3s.deeco.runtime.CycleDetectedException.class)
	public void testCycle2() throws PluginDependencyException
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
		
		new DEECo(plugin1, plugin2, plugin3, pluginBase, pluginBase2, pluginOther);
	}
	
	@Test
	public void testNoPlugins() throws PluginDependencyException
	{
		new DEECo();
	}
	
	@Test @Ignore
	public void testStartOnRunning()
	{
				
	}		
	
	@Test @Ignore
	public void testStopOnStopped()
	{
				
	}
	
	@Test
	public void testPluginAccess() throws PluginDependencyException
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
		
		DEECo deeco = new DEECo(pluginArray);
		
		for(DEECoPlugin p : plugins)
		{
			assert(deeco.pluginsMap.containsKey(p.getClass()));
			assertEquals(p, deeco.getPluginInstance(p.getClass()));
		}			
	}
}
