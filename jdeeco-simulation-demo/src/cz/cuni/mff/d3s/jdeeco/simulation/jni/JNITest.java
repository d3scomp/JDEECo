package cz.cuni.mff.d3s.jdeeco.simulation.jni;
import cz.cuni.mff.d3s.deeco.simulation.Host;
import cz.cuni.mff.d3s.deeco.simulation.OMNetSimulation;


public class JNITest {

	public static void main(String[] args) throws Exception {
		System.out.println(System.getProperty("java.library.path"));
		OMNetSimulation simulation = new OMNetSimulation();
		
		simulation.initialize(); //loads Library
		

		Host h0 = simulation.getHost("0", "node[0]");
		DemoJNIKnowledgeDataManager kr = new DemoJNIKnowledgeDataManager(h0, simulation);
		kr.sendDummyData();
		
		Host h1 = simulation.getHost("1", "node[1]");
		kr = new DemoJNIKnowledgeDataManager(h1, simulation);
		
		Host h2 = simulation.getHost("2", "node[2]");
		kr = new DemoJNIKnowledgeDataManager(h2, simulation);
		
		Host h3 = simulation.getHost("3", "node[3]");
		kr = new DemoJNIKnowledgeDataManager(h3, simulation);

		simulation.run("Cmdenv", "omnetpp-jni.ini");
		
		System.gc();
		System.out.println("Simulation finished.");
		
	}

}
