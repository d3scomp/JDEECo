package cz.cuni.mff.d3s.deeco.runners;

public interface DEECoSimulationRunner {

	public void start() throws TerminationTimeNotSetException;
	
	public void setTerminationTime(long terminationTime);

}
