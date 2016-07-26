package cz.cuni.mff.d3s.jdeeco.adaptation;

import java.util.Random;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.CorrelationData;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;
import cz.cuni.mff.d3s.jdeeco.adaptation.CorrelationTest.Variances;
import cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.CorrelationMetadataWrapper;
import cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metric.DifferenceMetric;

@Component
public class GroupMember {

	public String id;

	@CorrelationData(metric=DifferenceMetric.class,boundary=8,confidence=0.9)
	public CorrelationMetadataWrapper<Integer> position;
	@CorrelationData(metric=DifferenceMetric.class,boundary=3,confidence=0.9)
	public CorrelationMetadataWrapper<Integer> temperature;
	@CorrelationData(metric=DifferenceMetric.class,boundary=2,confidence=0.9)
	public CorrelationMetadataWrapper<Integer> battery;

	public GroupMember(String id) {
		this.id = id;
		position = new CorrelationMetadataWrapper<>(0,"position");
		temperature = new CorrelationMetadataWrapper<>(0,"temperature");
		battery = new CorrelationMetadataWrapper<>(0,"battery");
	}

	@Process
	@PeriodicScheduling(period=500)
	public static void changePosition(
			@In("id") String id,
			@InOut("position") ParamHolder<CorrelationMetadataWrapper<Integer>> position) {

		Random rand = new Random();
		int seed = rand.nextInt(Variances.SMALL_VARIANCE);
		position.value.setValue(seed, ProcessContext.getTimeProvider().getCurrentMilliseconds());

		System.out.println("GM#" + id + ",\tposition : " + position.value.getValue() + " (" + position.value.getTimestamp() + ")");
	}

	@Process
	@PeriodicScheduling(period=500)
	public static void changeTemperature(
			@In("id") String id,
			@InOut("temperature") ParamHolder<CorrelationMetadataWrapper<Integer>> temperature) {

		if(id.equals("1") && ProcessContext.getTimeProvider().getCurrentMilliseconds() > 10000){
			temperature.value.malfunction();
		} else {
			Random rand = new Random();
			int seed = rand.nextInt(Variances.SMALL_VARIANCE);
			// Setting fixed value of temperature to ensure the correlation (should be random,
			// the variance should reflect the boundary for temperature defined in KnowledgeMetadataHolder)
			temperature.value.setValue(seed, ProcessContext.getTimeProvider().getCurrentMilliseconds());
		}

		System.out.println("GM#" + id + ",\ttemperature : " + temperature.value.getValue() + " (" + temperature.value.getTimestamp() + ")");
	}

	@Process
	@PeriodicScheduling(period=500)
	public static void changeBattery(
			@In("id") String id,
			@InOut("battery") ParamHolder<CorrelationMetadataWrapper<Integer>> battery) {

		Random rand = new Random();
		int seed = rand.nextInt(Variances.LARGE_VARIANCE);
		battery.value.setValue(seed, ProcessContext.getTimeProvider().getCurrentMilliseconds());

		System.out.println("GM#" + id + ",\tbattery : " + battery.value.getValue() + " (" + battery.value.getTimestamp() + ")");
	}

}
