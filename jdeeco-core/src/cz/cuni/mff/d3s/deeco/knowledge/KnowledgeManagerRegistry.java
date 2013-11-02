package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;

/**
 * This class allows to create/remove or get the KnowledgeManagers of the local
 * or remote components
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 * 
 */

public class KnowledgeManagerRegistry {

	private static volatile KnowledgeManagerRegistry instance;

	private KnowledgeManagerRegistry() {}

	public static KnowledgeManagerRegistry getInstance() {
		if (instance == null) {
			synchronized (KnowledgeManagerRegistry.class) {
				if (instance == null) {
					instance = new KnowledgeManagerRegistry();
				}
			}
		}
		return instance;
	}

	public KnowledgeManager createLocal() {
		return null;
	}

	public KnowledgeManager createShadow() {
		return null;
	}

	public void removeLocal(KnowledgeManager km) {

	}

	public void removeShadow(KnowledgeManager km) {

	}

	public Collection<KnowledgeManager> getLocals() {
		return null;
	}

	public Collection<KnowledgeManager> getShadows() {
		return null;
	}

}
