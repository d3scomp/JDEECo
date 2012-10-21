package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
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
		System.out.println("LoggingKnowledgeManager.getKnowledge(knowledgePath=" + knowledgePath +", session=" + session + ")");
		return decoratedKm.getKnowledge(knowledgePath, session);
	}

	@Override
	public Object takeKnowledge(String knowledgePath, ISession session)
			throws KMException {
		System.out.println("LoggingKnowledgeManager.takeKnowledge(knowledgePath=" + knowledgePath +", session=" + session + ")");
		return decoratedKm.takeKnowledge(knowledgePath, session);
	}

	@Override
	public void alterKnowledge(String knowledgePath, Object value,
			ISession session) throws KMException {
		System.out.println("LoggingKnowledgeManager.alterKnowledge(knowledgePath=" + knowledgePath +", value=" + value + ", session=" + session + ")");
		decoratedKm.alterKnowledge(knowledgePath, value, session);
	}

	@Override
	public void putKnowledge(String knowledgePath, Object value,
			ISession session) throws KMException {
		System.out.println("LoggingKnowledgeManager.putKnowledge(knowledgePath=" + knowledgePath +", value=" + value + ", session=" + session + ")");
		decoratedKm.putKnowledge(knowledgePath, value, session);
	}

	@Override
	public ISession createSession() {
		System.out.println("LoggingKnowledgeManager.createSession()");
		return decoratedKm.createSession();
	}

	@Override
	public boolean registerListener(IKnowledgeChangeListener listener) {
		System.out.println("LoggingKnowledgeManager.registerListener(listener=" + listener + ")");
		return decoratedKm.unregisterListener(listener);
	}
	
	@Override
	public boolean unregisterListener(IKnowledgeChangeListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setListenersActive(boolean on) {
		System.out.println("LoggingKnowledgeManager.switchListening(on=" + on + ")");
		decoratedKm.setListenersActive(on);
	}

	@Override
	public Object getKnowledge(String knowledgePath) throws KMException {
		System.out.println("LoggingKnowledgeManager.getKnowledge(knowledgePath=" + knowledgePath + ")");
		return decoratedKm.getKnowledge(knowledgePath);
	}

	@Override
	public Object takeKnowledge(String knowledgePath) throws KMException {
		System.out.println("LoggingKnowledgeManager.takeKnowledge(knowledgePath=" + knowledgePath + ")");
		return decoratedKm.takeKnowledge(knowledgePath);
	}

	@Override
	public void alterKnowledge(String knowledgePath, Object value)
			throws KMException {
		System.out.println("LoggingKnowledgeManager.alterKnowledge(knowledgePath=" + knowledgePath +", value=" + value + ")");
		decoratedKm.alterKnowledge(knowledgePath, value);
	}

	@Override
	public void putKnowledge(String knowledgePath, Object value)
			throws KMException {
		System.out.println("LoggingKnowledgeManager.putKnowledge(knowledgePath=" + knowledgePath +", value=" + value + ")");
		decoratedKm.putKnowledge(knowledgePath, value);
	}

}
