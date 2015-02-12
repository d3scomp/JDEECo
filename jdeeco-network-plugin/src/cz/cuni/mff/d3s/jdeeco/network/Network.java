package cz.cuni.mff.d3s.jdeeco.network;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;
import cz.cuni.mff.d3s.jdeeco.network.l2.Layer2;

/**
 * Provides network plug-in for jDEECO
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */

public class Network implements DEECoPlugin {
	private Layer1 l1;
	private Layer2 l2;

	public Layer1 getL1() {
		return l1;
	}

	public Layer2 getL2() {
		return l2;
	}

	@Override
	public List<Class<? extends DEECoPlugin>> getDependencies() {
		return new LinkedList<Class<? extends DEECoPlugin>>();
	}

	@Override
	public void init(DEECoContainer container) {
		// TODO Register at Gossip for data
	}
}
