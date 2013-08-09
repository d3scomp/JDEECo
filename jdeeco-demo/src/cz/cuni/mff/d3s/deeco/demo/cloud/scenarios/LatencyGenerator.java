package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios;

import java.util.List;

import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment.ScpDSComponent;
import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment.ScpDSComponentOSLatencyData;

/**
 * Generates statically the Operating System latency data of each component
 * extending the ScpDSComponent class.
 * The generationg is based on the network ownership constraint
 * as well as latency interval regarding the context.
 * The generation is symetric between two scp components.
 * 
 * @author Julien Malvot
 *
 * @param <T> the scp component class whom the latency must be generated
 */
public class LatencyGenerator<T extends ScpDSComponent> {

	public static <T> void generate(List<T> scpComponents, boolean verbose) {
		
		for (int i = 0; i < scpComponents.size(); i++) {
			ScpDSComponent c1 = (ScpDSComponent) scpComponents.get(i);
			// generate the list of distances from one component to the others
			for (int j = 0; j < i; j++){
				// no loop on same index, symetric between source and destination
				if (i != j){
					ScpDSComponent c2 = (ScpDSComponent) scpComponents.get(j);
					Long min, max;
					// different treatment for NetworkComponents
					if (!c1.networkId.equals(c2.networkId)){
						min = 150L;
						max = 200L;
					}else{
						min = 0L;
						max = 70L;
					}
					// create the latency link between the two components
					ScpDSComponentOSLatencyData latencyData = new ScpDSComponentOSLatencyData(c1.id, c2.id, min, max);
					latencyData.generate();
					c1.latencies.put(c2.id, latencyData);
					c2.latencies.put(c1.id, latencyData);
					if (verbose)
						System.out.println(c1.id + " - " + c2.id);
				}
			}
		}
	}
}
