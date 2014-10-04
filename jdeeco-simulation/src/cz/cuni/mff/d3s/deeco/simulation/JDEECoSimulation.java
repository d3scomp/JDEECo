package cz.cuni.mff.d3s.deeco.simulation;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class JDEECoSimulation extends Simulation {

	private long currentMilliseconds;
	private final long simulationStartTime;
	private final long simulationEndTime;
	private final TreeSet<Callback> callbacks;
	private final Map<String, Callback> hostIdToCallback;
	private final Map<String, DirectSimulationHost> hosts;

	private final NetworkKnowledgeDataHandler knowledgeDataHandler;

	public JDEECoSimulation(long simulationStartTime, long simulationEndTime, NetworkKnowledgeDataHandler knowledgeDataHandler) {
		this.knowledgeDataHandler = knowledgeDataHandler;
		this.simulationStartTime = simulationStartTime;
		this.simulationEndTime = simulationEndTime;
		this.currentMilliseconds = -1;
		this.callbacks = new TreeSet<>();
		this.hostIdToCallback = new HashMap<>();
		this.hosts = new HashMap<>();
	}

	public DirectSimulationHost getHost(String id) {
		
		DirectSimulationHost host = hosts.get(id);
		if (host == null) {
			host = new DirectSimulationHost(id, this, knowledgeDataHandler);
			hosts.put(id, host);
		}
		return host;
	}

	public long getMATSimMilliseconds() {
		return currentMilliseconds;
	}

	@Override
	public long getCurrentMilliseconds() {
		return currentMilliseconds;
	}

	@Override
	public synchronized void callAt(long absoluteTime, String hostId) {
		Callback callback = hostIdToCallback.remove(hostId);
		if (callback != null) {
			callbacks.remove(callback);
		}
		callback = new Callback(hostId, absoluteTime);
		hostIdToCallback.put(hostId, callback);
		//System.out.println("For " + absoluteTime);
		callbacks.add(callback);
	}

	public synchronized void run() {
		currentMilliseconds = simulationStartTime;
		DirectSimulationHost host;
		Callback callback;
		// Iterate through all the callbacks until the MATSim callback.
		while (!callbacks.isEmpty()) {
			callback = callbacks.pollFirst();
			currentMilliseconds = callback.getAbsoluteTime();
			if (currentMilliseconds > simulationEndTime) {
				break;
			}
			//System.out.println("At: " + currentMilliseconds);
			host = (DirectSimulationHost) hosts.get(callback.hostId);
			host.at(millisecondsToSeconds(currentMilliseconds));
		}
	}

	private class Callback implements Comparable<Callback> {

		private final long milliseconds;
		private final String hostId;

		public Callback(String hostId, long milliseconds) {
			this.hostId = hostId;
			this.milliseconds = milliseconds;
		}

		public long getAbsoluteTime() {
			return milliseconds;
		}

		@Override
		public int compareTo(Callback c) {
			if (c.getAbsoluteTime() < milliseconds) {
				return 1;
			} else if (c.getAbsoluteTime() > milliseconds) {
				return -1;
			} else if (this == c) {
				return 0;
			} else {
				return this.hashCode() < c.hashCode() ? 1 : -1;
			}
		}

		public String toString() {
			return hostId + " " + milliseconds;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((hostId == null) ? 0 : hostId.hashCode());
			result = prime * result
					+ (int) (milliseconds ^ (milliseconds >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Callback other = (Callback) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (hostId == null) {
				if (other.hostId != null)
					return false;
			} else if (!hostId.equals(other.hostId))
				return false;
			if (milliseconds != other.milliseconds)
				return false;
			return true;
		}

		private JDEECoSimulation getOuterType() {
			return JDEECoSimulation.this;
		}
	}
}
