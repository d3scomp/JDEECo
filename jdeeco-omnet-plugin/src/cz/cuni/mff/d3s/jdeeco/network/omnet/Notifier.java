package cz.cuni.mff.d3s.jdeeco.network.omnet;

public interface Notifier {
	void notifyAt(long time, DummyListener listener);

	long getCurrentTime();
}
