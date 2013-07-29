package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment;

import java.util.Comparator;

/**
 * The Science Cloud Platform (Scp) Component Operating System Latency Data Comparator for the Deployment Scenario (DS).
 *
 * Compares the latency values of two latency data objects.
 * Enables the graph search to be faster by sorting the scp components links by ascending latencies.
 * 
 * @author Julien Malvot
 *
 */
public class ScpDSComponentOSLatencyDataComparator implements Comparator<ScpDSComponentOSLatencyData> {

	@Override
	public int compare(ScpDSComponentOSLatencyData o1,
			ScpDSComponentOSLatencyData o2) {
		return Long.compare(o1.cache, o2.cache);
	}
}
