package cz.cuni.mff.d3s.jdeeco.network;

import cz.cuni.mff.d3s.deeco.runtime.DEECoPlugin;

public interface NetworkPlugin extends DEECoPlugin, NetworkToDevice, NetworkToGossip, L1StrategyManager,
		L2StrategyManager {

}
