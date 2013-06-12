package cz.cuni.mff.d3s.deeco.knowledge.local;

import cz.cuni.mff.d3s.deeco.knowledge.ISession;

class LocalSession implements ISession {

	private boolean succeeded = false;
	private final LocalKnowledgeRepository kr;
	
	LocalSession(LocalKnowledgeRepository kr) {
		this.kr = kr;
	}
	
	@Override
	public void begin() {
		kr.lock.lock();

		// we must break transition here because there may be no thread choices at "park/unpark"
		Thread.yield();
	}

	@Override
	public void end() {
		kr.lock.unlock();
		succeeded = true;
	}
	

	@Override
	public void cancel() {
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
