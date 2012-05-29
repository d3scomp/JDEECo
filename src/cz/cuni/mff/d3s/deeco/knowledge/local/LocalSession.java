package cz.cuni.mff.d3s.deeco.knowledge.local;

import cz.cuni.mff.d3s.deeco.exceptions.SessionException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;

class LocalSession implements ISession {

	private boolean succeeded = false;
	private LocalKnowledgeRepository kr;
	
	LocalSession(LocalKnowledgeRepository kr) {
		this.kr = kr;
	}
	
	@Override
	public void begin() {
		kr.lock.lock();
	}

	@Override
	public void end() throws SessionException {
		kr.lock.unlock();
		succeeded = true;
	}

	@Override
	public void cancel() throws SessionException {
		kr.lock.unlock();
	}

	@Override
	public boolean repeat() {
		return !succeeded;
	}

	@Override
	public boolean hasSucceeded() {
		return succeeded;
	}

}
