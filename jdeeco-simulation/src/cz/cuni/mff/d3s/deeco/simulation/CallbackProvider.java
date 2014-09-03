package cz.cuni.mff.d3s.deeco.simulation;

public interface CallbackProvider {
	public void callAt(long absoluteTime, String hostId);
}
