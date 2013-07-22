package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.invokable.TypeDescription;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.scheduling.IKnowledgeChangeListener;

public class LoggingKnowledgeManager extends KnowledgeManager {

	final KnowledgeManager decoratedKm;
	
	public LoggingKnowledgeManager(KnowledgeManager decoratedKm) {
		super();
		this.decoratedKm = decoratedKm;
	}

	@Override
	public Object getKnowledge(String knowledgePath, ISession session)
			throws KMException {
		Log.d("LoggingKnowledgeManager.getKnowledge(knowledgePath=" + knowledgePath +", session=" + session + ")");
		return decoratedKm.getKnowledge(knowledgePath, session);
	}
	
	@Override
	public Object getKnowledge(String knowledgePath,
			TypeDescription expectedType, ISession session) throws KMException {
		Log.d("LoggingKnowledgeManager.getKnowledge(knowledgePath=" + knowledgePath + ", expectedType=" + expectedType + ", session=" + session + ")");
		return decoratedKm.getKnowledge(knowledgePath, expectedType, session);
	}

	@Override
	public Object takeKnowledge(String knowledgePath, ISession session)
			throws KMException {
		Log.d("LoggingKnowledgeManager.takeKnowledge(knowledgePath=" + knowledgePath +", session=" + session + ")");
		return decoratedKm.takeKnowledge(knowledgePath, session);
	}
	
	@Override
	public Object[] takeAllKnowledge(String knowledgeId, ISession session)
			throws KMException {
		Log.d("LoggingKnowledgeManager.takeKnowledge(knowledgeId=" + knowledgeId +", session=" + session + ")");
		return decoratedKm.takeAllKnowledge(knowledgeId, session);
	}

	@Override
	public void alterKnowledge(String knowledgePath, Object value,
			ISession session) throws KMException {
		Log.d("LoggingKnowledgeManager.alterKnowledge(knowledgePath=" + knowledgePath +", value=" + value + ", session=" + session + ")");
		decoratedKm.alterKnowledge(knowledgePath, value, session);
	}

	@Override
	public void putKnowledge(String knowledgePath, Object value,
			ISession session) throws KMException {
		Log.d("LoggingKnowledgeManager.putKnowledge(knowledgePath=" + knowledgePath +", value=" + value + ", session=" + session + ")");
		decoratedKm.putKnowledge(knowledgePath, value, session);
	}
	
	@Override
	public boolean containsKnowledge(String knowledgePath, ISession session) throws KMException {
		Log.d("LoggingKnowledgeManager.isKnowledge(knowledgePath=" + knowledgePath +", session=" + session + ")");
		return decoratedKm.containsKnowledge(knowledgePath, session);
	}

	@Override
	public ISession createSession() {
		Log.d("LoggingKnowledgeManager.createSession()");
		return decoratedKm.createSession();
	}

	@Override
	public boolean registerListener(IKnowledgeChangeListener listener) {
		Log.d("LoggingKnowledgeManager.registerListener(listener=" + listener + ")");
		return decoratedKm.unregisterListener(listener);
	}
	
	@Override
	public boolean unregisterListener(IKnowledgeChangeListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setListenersActive(boolean on) {
		Log.d("LoggingKnowledgeManager.switchListening(on=" + on + ")");
		decoratedKm.setListenersActive(on);
	}

	@Override
	public Object getKnowledge(String knowledgePath) throws KMException {
		Log.d("LoggingKnowledgeManager.getKnowledge(knowledgePath=" + knowledgePath + ")");
		return decoratedKm.getKnowledge(knowledgePath);
	}

	@Override
	public Object takeKnowledge(String knowledgePath) throws KMException {
		Log.d("LoggingKnowledgeManager.takeKnowledge(knowledgePath=" + knowledgePath + ")");
		return decoratedKm.takeKnowledge(knowledgePath);
	}
	
	@Override
	public Object[] takeAllKnowledge(String knowledgeId) throws KMException {
		Log.d("LoggingKnowledgeManager.takeKnowledge(knowledgeId=" + knowledgeId + ")");
		return decoratedKm.takeAllKnowledge(knowledgeId);
	}

	@Override
	public void alterKnowledge(String knowledgePath, Object value)
			throws KMException {
		Log.d("LoggingKnowledgeManager.alterKnowledge(knowledgePath=" + knowledgePath +", value=" + value + ")");
		decoratedKm.alterKnowledge(knowledgePath, value);
	}

	@Override
	public void putKnowledge(String knowledgePath, Object value)
			throws KMException {
		Log.d("LoggingKnowledgeManager.putKnowledge(knowledgePath=" + knowledgePath +", value=" + value + ")");
		decoratedKm.putKnowledge(knowledgePath, value);
	}
	
	@Override
	public boolean containsKnowledge(String knowledgePath)
			throws KMException {
		Log.d("LoggingKnowledgeManager.isKnowledge(knowledgePath=" + knowledgePath + ")");
		return decoratedKm.containsKnowledge(knowledgePath);
	}

}
