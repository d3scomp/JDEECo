package cz.cuni.mff.d3s.deeco.knowledge.local;

import cz.cuni.mff.d3s.deeco.exceptions.SessionException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;

class LocalSession implements ISession {

	private boolean succeeded = false;
	private LocalKnowledgeRepository kr;
	
	LocalSession(LocalKnowledgeRepository kr) {
		this.kr = kr;
		System.out.println("New local session " + this);
	}
	
	@Override
	public void begin() {
		System.out.println("About to lock local session " + this);
		kr.lock.lock();
	}

	@Override
	public void end() throws SessionException {
		kr.lock.unlock();
		System.out.println("Unlocked local session " + this);
		succeeded = true;
	}

	@Override
	public void cancel() throws SessionException {
		kr.lock.unlock();
		System.out.println("Cancelled local session " + this);
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
