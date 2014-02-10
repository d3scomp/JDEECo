package cz.cuni.mff.d3s.deeco.network;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;

public class GenericCommunicationBoundaryPredicate implements CommunicationBoundaryPredicate {
	Method method;
	
	public GenericCommunicationBoundaryPredicate(Method method) {		
		this.method = method;
	}

	@Override
	public boolean eval(KnowledgeData data, ReadOnlyKnowledgeManager sender) {
		try {
			return (Boolean) method.invoke(null, data, sender);
		} catch (Exception e) {
			Log.e("GenericCommunicationBoundaryPredicate.eval failed", e);
			return false;
		}
	}
}
