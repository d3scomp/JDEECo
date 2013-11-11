package cz.cuni.mff.d3s.deeco.knowledge;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class SynchronizerTest {

	@Mock
	private KnowledgeManager local;

	@Mock
	private KnowledgeManager replica;

	private Synchronizer tested;

	@Before
	public void setUp() {
		initMocks(this);
		this.tested = new Synchronizer(local, replica);
	}

	@Test
	public void testSynchronization() throws KnowledgeNotFoundException {
		// WHEN local knowledge manager changes
		tested.triggered(RuntimeModelHelper.createKnowledgeChangeTrigger());
		// THEN the replica is updated
		verify(local).get(any(List.class));
		verify(replica).update(any(ChangeSet.class));
	}
}
