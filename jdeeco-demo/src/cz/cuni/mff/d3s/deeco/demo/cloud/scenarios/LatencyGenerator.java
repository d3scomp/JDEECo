package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios;

import java.util.List;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment.ScpDSComponent;

public class LatencyGenerator<T extends ScpDSComponent> {
	
	private static Random rand = new Random();

	public static <T> void generate(List<T> scpComponents, Integer maxLatency, boolean verbose) {
		
		for (int i = 0; i < scpComponents.size(); i++) {
			ScpDSComponent c1 = (ScpDSComponent) scpComponents.get(i);
			// generate the list of distances from one component to the others
			for (int j = 0; j < i; j++){
				// no loop on same index, symetric between source and destination
				if (i != j){
					Long val = null;
					ScpDSComponent c2 = (ScpDSComponent) scpComponents.get(j);
					// different treatment for NetworkComponents
					if (!c1.networkId.equals(c2.networkId)){
						val = 150L;
					}else{
						val = ((Integer) rand.nextInt(maxLatency)).longValue();
					}
					c1.latencies.put(c2.id, val);
					c2.latencies.put(c1.id, val);
					if (verbose)
						System.out.println(c1.id + " - " + c2.id + " - " + val);
				}
			}
		}
	}
}
