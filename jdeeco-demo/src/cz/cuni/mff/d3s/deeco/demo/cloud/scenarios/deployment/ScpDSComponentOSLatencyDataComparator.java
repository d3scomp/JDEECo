package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment;

import java.util.Comparator;

public class ScpDSComponentOSLatencyDataComparator implements Comparator<ScpDSComponentOSLatencyData> {

	@Override
	public int compare(ScpDSComponentOSLatencyData o1,
			ScpDSComponentOSLatencyData o2) {
		return Long.compare(o1.cache, o2.cache);
	}
}
