package cz.cuni.mff.d3s.jdeeco.adaptation;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.jdeeco.adaptation.CorrelationTest.Variances;
import cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.CorrelationMetadataWrapper;

@Component
public class GroupLeader {

	public String id;
	public Integer leaderId;

	public CorrelationMetadataWrapper<Integer> position;
	public CorrelationMetadataWrapper<Integer> temperature;

	public GroupLeader(String id) {
		this.id = id;
		position = new CorrelationMetadataWrapper<>(0);
		temperature = new CorrelationMetadataWrapper<>(0);
	}

	@Process
	@PeriodicScheduling(period=500)
	public static void changePosition(
			@In("id") String id,
			@InOut("position") ParamHolder<CorrelationMetadataWrapper<Integer>> position) {

		Random rand = new Random();
		int seed = rand.nextInt(Variances.SMALL_VARIANCE);
		position.value.setValue(seed, ProcessContext.getTimeProvider().getCurrentMilliseconds());

		System.out.println("GL#" + id + ",\tposition : " + position.value.getValue() + " (" + position.value.getTimestamp() + ")");
	}

	@Process
	@PeriodicScheduling(period=500)
	public static void changeTemperature(
			@In("id") String id,
			@InOut("temperature") ParamHolder<CorrelationMetadataWrapper<Integer>> temperature) {

		Random rand = new Random();
		int seed = rand.nextInt(Variances.SMALL_VARIANCE);
		// Setting fixed value of temperature to ensure the correlation (should be random,
		// the variance should reflect the boundary for temperature defined in KnowledgeMetadataHolder)
		temperature.value.setValue(seed, ProcessContext.getTimeProvider().getCurrentMilliseconds());

		System.out.println("GL#" + id + ",\ttemperature : " + temperature.value.getValue() + " (" + temperature.value.getTimestamp() + ")");
	}

}
