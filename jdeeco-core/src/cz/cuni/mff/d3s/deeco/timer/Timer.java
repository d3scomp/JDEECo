package cz.cuni.mff.d3s.deeco.timer;


public interface Timer extends CurrentTimeProvider {

	void notifyAt(long time, TimerEventListener listener);
	
}