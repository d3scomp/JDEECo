package cz.cuni.mff.d3s.jdeeco.matsim.plugin;

import cz.cuni.mff.d3s.deeco.network.AbstractHost;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;
import cz.cuni.mff.d3s.deeco.timer.TimerEventListener;

/**
 * DEECo node host representation used in MATSim simulation
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 */
public class MATSimHost extends AbstractHost {
	public TimerEventListener listener;

	public MATSimHost(String id, CurrentTimeProvider timeProvider) {
		super(id, timeProvider);
	}

	public void at(double absoluteTime) {
		listener.at(getCurrentMilliseconds());
	}
}