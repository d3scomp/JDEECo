package cz.cuni.mff.d3s.jdeeco.simulation.jni;
import cz.cuni.mff.d3s.deeco.simulation.SimulationHost;
import cz.cuni.mff.d3s.deeco.simulation.omnet.OMNetSimulation;


public class JNITest {

	public static void main(String[] args) throws Exception {
		System.out.println(System.getProperty("java.library.path"));
		OMNetSimulation oMNetSimulation = new OMNetSimulation();
		

		SimulationHost h0 = oMNetSimulation.getHost("0", "node[0]");
		DemoJNIKnowledgeDataManager kr = new DemoJNIKnowledgeDataManager(h0, oMNetSimulation);
		kr.sendDummyData();
		
		SimulationHost h1 = oMNetSimulation.getHost("1", "node[1]");
		kr = new DemoJNIKnowledgeDataManager(h1, oMNetSimulation);
		
		SimulationHost h2 = oMNetSimulation.getHost("2", "node[2]");
		kr = new DemoJNIKnowledgeDataManager(h2, oMNetSimulation);
		
		SimulationHost h3 = oMNetSimulation.getHost("3", "node[3]");
		kr = new DemoJNIKnowledgeDataManager(h3, oMNetSimulation);

		oMNetSimulation.run("Cmdenv", "omnetpp-jni.ini");
		
		System.gc();
		System.out.println("Simulation finished.");
		
	}

}
