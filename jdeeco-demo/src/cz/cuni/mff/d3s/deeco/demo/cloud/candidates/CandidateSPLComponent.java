package cz.cuni.mff.d3s.deeco.demo.cloud.candidates;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

public class CandidateSPLComponent extends CandidateComponentBase {

	public final static long serialVersionUID = 1L;
	
	public Map<String,Long> latencies; 

	public CandidateSPLComponent(String id, Float loadRatio, Float maxLoadRatio) {
		super(id, loadRatio, maxLoadRatio);
		this.latencies = new HashMap<String,Long>();
	}

	@Process
	@PeriodicScheduling(6000)
	public static void process(@In("id") String id, @Out("loadRatio") OutWrapper<Float> loadRatio) {
		loadRatio.value = (new Random()).nextFloat();
		
		System.out.println(id + " new load ratio: "
				+ Math.round(loadRatio.value * 100) + "%");
	}

	@Process
	public static void process2(@In("id") @TriggerOnChange String id, @In("minMemberId") @TriggerOnChange String minMemberId) {
		System.out.println(id + " min loaded node: " + minMemberId);
	}

}
