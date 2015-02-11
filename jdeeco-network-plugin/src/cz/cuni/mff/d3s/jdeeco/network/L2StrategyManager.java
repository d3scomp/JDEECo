package cz.cuni.mff.d3s.jdeeco.network;

import java.util.Collection;

import cz.cuni.mff.d3s.jdeeco.network.l2.L2Strategy;

/**
 * Interface for Layer 2 network
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public interface L2StrategyManager {
	/**
	 * Registers L2 strategy for processing L2 packets
	 * 
	 * @param strategy
	 *            Strategy to register
	 */
	public void registerL2Strategy(L2Strategy strategy);

	/**
	 * Gets registered L2 strategies
	 * 
	 * @return Read-only collection of registered strategies
	 */
	public Collection<L2Strategy> getRegisteredL2Strategies();

	/**
	 * Removes L2 strategy
	 * 
	 * @param strategy
	 *            Strategy to remove
	 */
	public boolean unregisterL2Strategy(L2Strategy strategy);
}
