package cz.cuni.mff.d3s.deeco.timer;

import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;


public interface Timer extends CurrentTimeProvider {

	void notifyAt(long time, TimerEventListener listener, DEECoContainer node);
	
}