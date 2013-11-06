package cz.cuni.mff.d3s.deeco.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class ComponentInstanceRecordTest {

	ComponentInstance instance;
	ComponentInstanceRecord tested;
	
	@Before
	public void setUp() throws Exception {
		instance = mock(ComponentInstance.class);
		tested = new ComponentInstanceRecord(instance);
	}

	@SuppressWarnings("serial")
	@Test
	public void testGetAllTasks() {
		// WHEN the record is empty
		// THEN getAllTasks() is empty
		assertEquals(0, tested.getAllTasks().size());
		
		
		// WHEN the record contains non-empty list of process tasks
		final Task pt1 = mock(Task.class);
		final Task pt2 = mock(Task.class);
		tested.getProcessTasks().putAll(new HashMap<ComponentProcess, Task>() {{
			put(mock(ComponentProcess.class), pt1);
			put(mock(ComponentProcess.class), pt2);			
		}});
		// AND a non empty list of ensemble tasks
		final Task et1 = mock(Task.class);
		final Task et2 = mock(Task.class);
		tested.getEnsembleTasks().putAll(new HashMap<EnsembleController, Task>() {{
			put(mock(EnsembleController.class), et1);
			put(mock(EnsembleController.class), et2);			
		}});
		// THEN the getAllTasks() returns exactly all the tasks
		assertEquals(4, tested.getAllTasks().size());
		assertTrue(tested.getAllTasks().contains(pt1));
		assertTrue(tested.getAllTasks().contains(pt2));
		assertTrue(tested.getAllTasks().contains(et1));
		assertTrue(tested.getAllTasks().contains(et2));
		
	}

}
