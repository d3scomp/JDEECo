package cz.cuni.mff.d3s.deeco.knowledge;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

/**
 * KnowledgeManagersView testing.
 * The test checks the correctness of getting the KnowledgeManagers and register/unregister 
 * the triggers of the others.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */
public class KnowledgeManagersViewTest {
	
	@Mock
	private Trigger trigger;
	@Mock
	private TriggerListener triggerListener;
	@Mock
	private ValueSet valueSet;
	@Mock
	private ReadOnlyKnowledgeManager km;
 
	private KnowledgeManagersView kmView;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.kmView =mock(KnowledgeManagersView.class);
 		this.km = mock(ReadOnlyKnowledgeManager.class);
		this.trigger = mock(Trigger.class);
		this.triggerListener = mock(TriggerListener.class);
	}

 	@Test 
	public void testGetOtherKnowledgeManagerExcludingSelf(){
		
 		Collection<ReadOnlyKnowledgeManager> colKM = new LinkedList<ReadOnlyKnowledgeManager>();
		when(kmView.getOthersKnowledgeManagers()).thenReturn(colKM);
		
		// WHEN call getOthersKnowledgeManagers()
		// THEN check the returned collection do not have the current knowledge manager 
		assertEquals(false, kmView.getOthersKnowledgeManagers().contains(km));
		
	}
	
	@Ignore
 	@Test
	public void testRegisterOthersTriggerAndTriggerListener() {
		
		// WHEN registering a trigger to triggerListener
		kmView.register(trigger,triggerListener);
		// THEN ...
		// TODO : ...
		verifyNoMoreInteractions(trigger);
	}
	
	@Ignore
 	@Test
	public void testUnregisterOthersTriggerAndTriggerListener() {

		// WHEN registering a trigger to triggerListener
		kmView.unregister(trigger,triggerListener);
		// THEN ...
		// TODO : ...
		verifyNoMoreInteractions(trigger);

	}
	
}
