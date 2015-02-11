package cz.cuni.mff.d3s.jdeeco.gossip;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;

public class DummyGossip implements Gossip {
	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		// No dependencies so far
		return new LinkedList<Class<? extends DEECoPlugin>>();
	}

	@Override
	public void init(DEECoContainer container) {
		// TODO Register for knowledge updates

	}

	@Override
	public void processKnowledgeUpdate(Object knowledge) {
		throw new UnsupportedOperationException();
	}
}
