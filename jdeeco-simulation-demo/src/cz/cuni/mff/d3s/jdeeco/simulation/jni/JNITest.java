package cz.cuni.mff.d3s.jdeeco.simulation.jni;
import cz.cuni.mff.d3s.deeco.simulation.Host;
import cz.cuni.mff.d3s.deeco.simulation.Simulation;


public class JNITest {

	public static void main(String[] args) throws Exception {
		System.out.println(System.getProperty("java.library.path"));
		Simulation simulation = new Simulation();
		
		simulation.initialize(); //loads Library
		

		Host h0 = simulation.getHost("0");
		KnowledgeDataManager kr = new KnowledgeDataManager(h0);
		kr.sendDummyData();
		
		Host h1 = simulation.getHost("1");
		kr = new KnowledgeDataManager(h1);
		
		Host h2 = simulation.getHost("2");
		kr = new KnowledgeDataManager(h2);
		
		Host h3 = simulation.getHost("3");
		kr = new KnowledgeDataManager(h3);

		simulation.run("Cmdenv", "omnetpp-jni.ini");
		
		System.gc();
		System.out.println("Simulation finished.");
		
	}

}
