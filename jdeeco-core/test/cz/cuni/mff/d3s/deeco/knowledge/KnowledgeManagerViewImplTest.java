package cz.cuni.mff.d3s.deeco.knowledge;

import org.junit.Test;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class KnowledgeManagerViewImplTest {
	
	@Test
	public void getOtherKnowledgeManagersTest() {
		// WHEN the container already consists of some local and replica
		// knowledge managers

		// and WHEN the 'getOtherKnowledgeManagers' is called on the
		// KnowledgeManagerView instance

		// THEN the KnowledgeManagerView instance returns all container's
		// knowledge managers apart of the one referenced by the
		// KnowledgeManagerView instance
	}

	@Test
	public void registerTest() {
		// WHEN the container already consists of some local and replica
		// knowledge managers

		// and WHEN the 'register' is called on the
		// KnowledgeManagerView instance

		// THEN each of the KnowledgeManager instance hosted by the container is
		// registered with the trigger

		// and any other knowledge manager added later is also registered with
		// the respective trigger
	}

	@Test
	public void unregisterTest() {
		// WHEN the container already consists of some local and replica
		// knowledge managers

		// and WHEN the 'unregister' is called on the
		// KnowledgeManagerView instance

		// THEN each of the KnowledgeManager instance hosted by the container is
		// unregistered with the respective trigger

		// and any other knowledge manager added later is not registered with
		// the trigger

	}

	@Test
	public void localCreatedTest() {
		// WHEN the container has created a new local knowledge manager

		// THEN the KnowledgeManagerView instance is notified about this

		// and all memorised triggers are registered with the new knowledge
		// manager
	}

	@Test
	public void replicaCreatedTest() {
		// WHEN the container has created a new replica knowledge manager

		// THEN the KnowledgeManagerView instance is notified about this

		// and all memorised triggers are registered with the new knowledge
		// manager
	}

	@Test
	public void localRemovedTest() {
		// WHEN the container has removed a local knowledge manager

		// THEN the KnowledgeManagerView instance is notified about this

		// and all triggers registered by the KnowledgeManagerView instance are
		// removed from the knowledge manager being removed
	}

	@Test
	public void replicaRemovedTest() {
		// WHEN the container has removed a replica knowledge manager

		// THEN the KnowledgeManagerView instance is notified about this

		// and all triggers registered by the KnowledgeManagerView instance are
		// removed from the knowledge manager being removed
	}
}
