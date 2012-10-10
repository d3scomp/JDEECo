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
	}

	@Override
	public void end() {
		kr.lock.unlock();
		succeeded = true;
		// JPF - Break transition is necessary here
		//  if we do not brake transition when it is unlocked
		//      kr.lock.unlock() does not break if no other threads
		//      are blocked on it
		// then without this transition break, we can miss some combination
		// of sessions
		Thread.yield();
	}
	

	@Override
	public void cancel() {
		kr.lock.unlock();
		Thread.yield();
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
