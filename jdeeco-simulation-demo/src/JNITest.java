import cz.cuni.mff.d3s.deeco.simulation.Host;


public class JNITest {

	public static void main(String[] args) throws Exception {
		System.out.println(System.getProperty("java.library.path"));
		TestSimulation sim = new TestSimulation();
		
		sim.initialize(); //loads Library
		
		TestIncomingKnowledgeListener ikl = new TestIncomingKnowledgeListener();
		TestPacketReceiver pr = new TestPacketReceiver(1000, ikl);
		Host h0 = sim.getHost("0", 1000, pr);
		Host h1 = sim.getHost("1", 1000, pr);
		Host h2 = sim.getHost("2", 1000, pr);
		
		h0.callAt(1000);

		sim.run("Cmdenv");
		
		System.gc();
		System.out.println("Simulation finished.");
		
	}

}
