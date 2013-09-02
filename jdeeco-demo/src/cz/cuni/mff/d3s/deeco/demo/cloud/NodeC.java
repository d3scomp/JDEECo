/* $Id$ 
 * 
 */

package cz.cuni.mff.d3s.deeco.demo.cloud;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.definitions.ComponentDefinition;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;
import cz.cuni.mff.d3s.deeco.provider.InstanceRuntimeMetadataProvider;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeUtil;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

/**
 * 
 * @author Petr Hnetynka
 */
public class NodeC extends ComponentDefinition {
	public final static long serialVersionUID = 1L;

	public Float loadRatio;
	public Float maxLoadRatio;
	public Integer networkId;
	public String targetNode;
	public Integer counter;

	public NodeC() {
		this.id = "NodeC";
		this.loadRatio = 0.0f;
		this.maxLoadRatio = 0.2f;
		this.networkId = 1;
		this.targetNode = null;
		this.counter = 0;
	}

	@cz.cuni.mff.d3s.deeco.annotations.Process
	@PeriodicScheduling(6000)
	public static void process(@Out("loadRatio") OutWrapper<Float> loadRatio) {
		loadRatio.value = new Random().nextFloat();

		System.out.println("Node C new load ratio: "
				+ Math.round(loadRatio.value * 100) + "%");
	}

	@cz.cuni.mff.d3s.deeco.annotations.Process
	@PeriodicScheduling(6000)
	public static void process2(@InOut("counter") OutWrapper<Integer> counter) {
		if (counter.value.intValue() == 0) {
			counter.value = new Integer(1);
		} else if (counter.value.intValue() == 1) {
			Runtime rt = RuntimeUtil.getRuntime();
			System.out.println("Introducing new component...");
			InstanceRuntimeMetadataProvider provider = new InstanceRuntimeMetadataProvider();
			provider.fromComponentInstance(new NodeD());
			rt.deployRuntimeMetadata(provider.getRuntimeMetadata());
			System.out.println("New component successfully introduced ;-)");
			counter.value = new Integer(2);
		}
	}
}
