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
 * @author Filip Krijt<krijt@d3s.mff.cuni.cz>
 *
 */
public class DEECoTest {	
	
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
		DEECoPlugin plugin1 = mock(DEECoPlugin.class);
		DEECoPlugin plugin2 = mock(DEECoPlugin.class);		
		
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
		DEECoPlugin pluginBase = mock(DEECoPlugin.class);
		DEECoPlugin plugin1 = mock(DEECoPlugin.class);
		DEECoPlugin plugin2 = mock(DEECoPlugin.class);
		DEECoPlugin plugin3 = mock(DEECoPlugin.class);
		
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
		DEECoPlugin pluginBase1 = mock(DEECoPlugin.class);
		DEECoPlugin pluginBase2 = mock(DEECoPlugin.class);
		DEECoPlugin plugin = mock(DEECoPlugin.class);
		
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
		DEECoPlugin pluginBase = mock(DEECoPlugin.class);
		DEECoPlugin plugin1 = mock(DEECoPlugin.class);
		DEECoPlugin plugin2 = mock(DEECoPlugin.class);
		
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
	
	@Test void testDependencyOrderRandomized()
	{
		Random generator = new Random(42);
		//TODO
	}
	
	@Test(expected = cz.cuni.mff.d3s.deeco.runtime.MissingDependencyException.class)
	public void testNonExistentDependency() throws PluginDependencyException
	{
		// Correct exception is thrown on detecting non-existent dependency		
		
		DEECoPlugin basePlugin = mock(DEECoPlugin.class);
		DEECoPlugin dependentPlugin = mock(DEECoPlugin.class);
		
		when(dependentPlugin.getDependencies()).thenReturn(Arrays.asList(basePlugin.getClass()));
		
		DEECo deeco = new DEECo(dependentPlugin);		
	}
	
	@Test(expected = cz.cuni.mff.d3s.deeco.runtime.CycleDetectedException.class)
	public void testCycle() throws PluginDependencyException
	{
		// Tests for a basic (3 plugins) dependency cycle
		DEECoPlugin plugin1 = mock(DEECoPlugin.class);
		DEECoPlugin plugin2 = mock(DEECoPlugin.class);
		DEECoPlugin plugin3 = mock(DEECoPlugin.class);
		
		when(plugin1.getDependencies()).thenReturn(Arrays.asList(plugin2.getClass()));
		when(plugin2.getDependencies()).thenReturn(Arrays.asList(plugin3.getClass()));
		when(plugin3.getDependencies()).thenReturn(Arrays.asList(plugin1.getClass()));
		
		DEECo deeco = new DEECo(plugin1, plugin2, plugin3);
	}
	
	@Test(expected = cz.cuni.mff.d3s.deeco.runtime.CycleDetectedException.class)
	public void testCycle2() throws PluginDependencyException
	{
		// Test for a more elaborate plugin dependency cycle, decorated with a base dependency and an additional independent branch.
		DEECoPlugin pluginBase = mock(DEECoPlugin.class);
		DEECoPlugin plugin1 = mock(DEECoPlugin.class);
		DEECoPlugin plugin2 = mock(DEECoPlugin.class);
		DEECoPlugin plugin3 = mock(DEECoPlugin.class);		
		
		DEECoPlugin pluginBase2 = mock(DEECoPlugin.class);
		DEECoPlugin pluginOther = mock(DEECoPlugin.class);
			
		when(plugin1.getDependencies()).thenReturn(Arrays.asList(plugin2.getClass(), pluginBase.getClass()));
		when(plugin2.getDependencies()).thenReturn(Arrays.asList(plugin3.getClass()));
		when(plugin3.getDependencies()).thenReturn(Arrays.asList(plugin1.getClass()));
		
		when(pluginOther.getDependencies()).thenReturn(Arrays.asList(pluginBase2.getClass()));
		
		when(pluginBase.getDependencies()).thenReturn(Arrays.asList());
		when(pluginBase2.getDependencies()).thenReturn(Arrays.asList());
		
		DEECo deeco = new DEECo(plugin1, plugin2, plugin3, pluginBase, pluginBase2, pluginOther);
	}
	
	@Test
	public void testNoPlugins() throws PluginDependencyException
	{
		DEECo deeco = new DEECo();
	}
	
	@Test
	public void testStartOnRunning()
	{
				
	}		
	
	@Test
	public void testStopOnStopped()
	{
				
	}
	
	@Test
	public void testPluginAccess() throws PluginDependencyException
	{
		List<DEECoPlugin> plugins = new ArrayList<>();
		
		int pluginCount = 10;
		for(int i = 0; i < pluginCount; ++i)
		{
			plugins.add(mock(DEECoPlugin.class));
		}
		
		
		DEECoPlugin[] pluginArray = plugins.toArray(new DEECoPlugin[0]);
		
		DEECo deeco = new DEECo(pluginArray);
		
		for(DEECoPlugin p : plugins)
		{
			assert(deeco.pluginsMap.containsKey(p.getClass()));
			assertEquals(p, deeco.getPluginInstance(p.getClass()));
		}			
	}
}
