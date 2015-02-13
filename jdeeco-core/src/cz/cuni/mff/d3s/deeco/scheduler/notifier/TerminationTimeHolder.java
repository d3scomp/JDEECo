package cz.cuni.mff.d3s.deeco.scheduler.notifier;

public interface TerminationTimeHolder {

	/**
	 * To be used to set the intended duration for this DEECo run (in milliseconds).
	 */
	public void setTerminationTime(long duration);
}
