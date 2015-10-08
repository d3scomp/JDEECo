package cz.cuni.mff.d3s.jdeeco.adaptation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.adaptation.correlation.CorrelationPlugin;
import cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata.KnowledgeMetadataHolder;
import cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metric.DifferenceMetric;
import cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metric.Metric;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.SimpleBroadcastDevice;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

/**
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class CorrelationTest {

	@SuppressWarnings("unchecked")
	@Test
	public void acceptanceTest() throws DEECoException, AnnotationProcessorException, InstantiationException, IllegalAccessException {

		List<DEECoNode> nodesInRealm = new ArrayList<DEECoNode>();

		SimulationTimer simulationTimer = new DiscreteEventTimer();
		DEECoSimulation realm = new DEECoSimulation(simulationTimer);
		realm.addPlugin(new SimpleBroadcastDevice());
		realm.addPlugin(Network.class);
		realm.addPlugin(DefaultKnowledgePublisher.class);
		realm.addPlugin(KnowledgeInsertingStrategy.class);
		realm.addPlugin(new PositionPlugin(0.0,0.0));

		DEECoNode deeco1 = realm.createNode(1);
		nodesInRealm.add(deeco1);
		deeco1.deployComponent(new GroupMember("1"));

		DEECoNode deeco2 = realm.createNode(2);
		nodesInRealm.add(deeco2);
		deeco2.deployComponent(new GroupMember("2"));

		DEECoNode deeco3 = realm.createNode(3);
		nodesInRealm.add(deeco3);
		deeco3.deployComponent(new GroupLeader("3"));

		/* Register metadata for fields */
		final String positionLabel = "position";
		final String temperatureLabel = "temperature";
		final String batteryLabel = "battery";

		final int positionBoundary = 8;
		final int temperatureBoundary = 3;
		final int batteryBoundary = 2;

		final Metric simpleMetric = new DifferenceMetric();

		final double positionConfidence = 0.9;
		final double temperatureConfidence = 0.9;
		final double batteryConfidence = 0.9;

		KnowledgeMetadataHolder.setBoundAndMetric(positionLabel, positionBoundary, simpleMetric, positionConfidence);
		KnowledgeMetadataHolder.setBoundAndMetric(temperatureLabel, temperatureBoundary, simpleMetric, temperatureConfidence);
		KnowledgeMetadataHolder.setBoundAndMetric(batteryLabel, batteryBoundary, simpleMetric, batteryConfidence);

		/* Create node that holds the correlation component */
		DEECoNode deeco4 = realm.createNode(4,
				new CorrelationPlugin(nodesInRealm));
		// FIXME these two ensembles could be unified if we implement the "member.*" in knowledge exchange parameters
		deeco4.deployEnsemble(GroupMemberDataAggregation.class);
		deeco4.deployEnsemble(GroupLeaderDataAggregation.class);


		/* WHEN simulation is performed */
	
		realm.start(15000);

		// The code bellow serves for testing the ensemble class created at runtime
/*		try{
			@SuppressWarnings("rawtypes")
			Class testClass = CorrelationEnsembleFactory.getEnsembleDefinition("position", "temperature");
			System.out.println(String.format("Class: %s", testClass.toGenericString()));
			for(Annotation a : testClass.getAnnotations()){
				System.out.println(String.format("Annotation: %s", a.toString()));
			}
			Method[] methods = testClass.getMethods();
			for(Method m : methods){
				System.out.println(String.format("\nMethod: %s", m.toString()));

				for(Annotation a : m.getAnnotations()){
					System.out.println(String.format("Annotation: %s", a.toString()));
				}

				for(Parameter param : m.getParameters()){
					System.out.println(String.format("Parameter: %s", param.getName()));

					for(Annotation a : param.getAnnotations()){
						System.out.println(String.format("Annotation: %s", a.toString()));
					}
				}
			}

			MetadataWrapper<Integer> memberPosition = new MetadataWrapper<Integer>(0);
			MetadataWrapper<Integer> memberTemperature = new MetadataWrapper<Integer>(0);
			MetadataWrapper<Integer> coordPosition = new MetadataWrapper<Integer>(0);
			MetadataWrapper<Integer> coordTemperature = new MetadataWrapper<Integer>(0);
			memberTemperature.malfunction();
			Method m = testClass.getMethod("membership", new Class[]{
					MetadataWrapper.class,
					MetadataWrapper.class,
					MetadataWrapper.class,
					MetadataWrapper.class});
			boolean ret = (boolean) m.invoke(testClass.newInstance(), new Object[]{
				memberPosition,
				memberTemperature,
				coordPosition,
				coordTemperature});
			System.out.println(ret);
			System.out.println(!memberTemperature.isOperational()
					&& coordTemperature.isOperational()
					&& KnowledgeMetadataHolder.classifyDistance("position", memberPosition.getValue(), coordPosition.getValue()) == DistanceClass.Close);

			CorrelationEnsembleFactory.setEnsembleMembershipBoundary("position", "temperature", 0.9);
			testClass = CorrelationEnsembleFactory.getEnsembleDefinition("position", "temperature");
			m = testClass.getMethod("membership", new Class[]{
					MetadataWrapper.class,
					MetadataWrapper.class,
					MetadataWrapper.class,
					MetadataWrapper.class});
			ret = (boolean) m.invoke(testClass.newInstance(), new Object[]{
				memberPosition,
				memberTemperature,
				coordPosition,
				coordTemperature});
			System.out.println(ret);
			System.out.println(!memberTemperature.isOperational()
					&& coordTemperature.isOperational()
					&& KnowledgeMetadataHolder.classifyDistance("position", memberPosition.getValue(), coordPosition.getValue()) == DistanceClass.Close);
		} catch(Exception e){
			e.printStackTrace();
		}*/

	}

	public class Variances {
		static final int SMALL_VARIANCE = 10;
		static final int LARGE_VARIANCE = 100;
	}

}
