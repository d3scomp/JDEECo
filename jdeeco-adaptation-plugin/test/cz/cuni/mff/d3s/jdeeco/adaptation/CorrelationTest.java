package cz.cuni.mff.d3s.jdeeco.adaptation;


import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.runners.DEECoSimulation;
import cz.cuni.mff.d3s.deeco.runtime.DEECoException;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.runtimelog.RuntimeLogWriters;
import cz.cuni.mff.d3s.deeco.timer.DiscreteEventTimer;
import cz.cuni.mff.d3s.deeco.timer.SimulationTimer;
import cz.cuni.mff.d3s.jdeeco.adaptation.correlation.CorrelationPlugin;
import cz.cuni.mff.d3s.jdeeco.network.Network;
import cz.cuni.mff.d3s.jdeeco.network.device.SimpleBroadcastDevice;
import cz.cuni.mff.d3s.jdeeco.network.l2.strategy.KnowledgeInsertingStrategy;
import cz.cuni.mff.d3s.jdeeco.position.PositionPlugin;
import cz.cuni.mff.d3s.jdeeco.publishing.DefaultKnowledgePublisher;

/**
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 */
public class CorrelationTest {

	RuntimeLogWriters runtimeLogWriters;
	
	@Before
	public void setUp() throws IOException{
		Writer mockOut = mock(Writer.class);
		runtimeLogWriters = new RuntimeLogWriters(mockOut, mockOut, mockOut);
	}
	
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
		realm.addPlugin(AdaptationPlugin.class);

		DEECoNode deeco1 = realm.createNode(1, runtimeLogWriters);
		nodesInRealm.add(deeco1);
		deeco1.deployComponent(new GroupMember("1"));

		DEECoNode deeco2 = realm.createNode(2, runtimeLogWriters);
		nodesInRealm.add(deeco2);
		deeco2.deployComponent(new GroupMember("2"));

		DEECoNode deeco3 = realm.createNode(3, runtimeLogWriters);
		nodesInRealm.add(deeco3);
		deeco3.deployComponent(new GroupLeader("3"));

		/* Create node that holds the correlation component */
		DEECoNode deeco4 = realm.createNode(4, runtimeLogWriters,
				new CorrelationPlugin(nodesInRealm));
		nodesInRealm.add(deeco4);

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
