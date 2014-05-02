package cz.cuni.mff.d3s.jdeeco.simulation.jni;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataReceiver;
import cz.cuni.mff.d3s.deeco.simulation.Host;
import cz.cuni.mff.d3s.deeco.simulation.OMNetSimulation;
import cz.cuni.mff.d3s.deeco.simulation.SimulationTimeEventListener;

public class DemoJNIKnowledgeDataManager implements KnowledgeDataReceiver,
		SimulationTimeEventListener {

	private final Host host;
	private final OMNetSimulation simulation;
	private final List<KnowledgeData> toSend;
	private String recipient = "";

	public DemoJNIKnowledgeDataManager(Host host, OMNetSimulation simulation) {
		this.host = host;
		this.simulation = simulation;
		this.host.getPacketReceiver().setKnowledgeDataReceiver(this);
		toSend = new LinkedList<KnowledgeData>();
		this.host.setSimulationTimeEventListener(this);
	}

	@Override
	public void receive(List<? extends KnowledgeData> knowledgeData) {
		if (knowledgeData != null) {
			for (KnowledgeData kd : knowledgeData) {
					DemoKnowledgeMetaData md = (DemoKnowledgeMetaData) kd.getMetaData();
							System.out.print(host.getCurrentTime() + " Node "
									+ host.getHostId() + " has received knowledge from "
									+ md.componentId + ".");		
					System.out.println(" Rebroadcast Counter: "
							+ md.rebroadcastCount + " RSSI: " + kd.getMetaData().rssi);
					if (md.rebroadcastCount < 5) {
						md.rebroadcastCount++;
						toSend.add(new KnowledgeData(new ValueSet(), md));
					} else if (host.getHostId().equals("0")){
						recipient = "node[2]";
						md.rebroadcastCount = 0;
						toSend.add(new KnowledgeData(new ValueSet(), md));
					}
			}
			if (!toSend.isEmpty())
				simulation.callAt(host.getCurrentTime()
						+ new Random().nextInt(2000), host.getHostId());
		}
	}

	@Override
	public void at(long time) {
		System.out.print("Node " + host.getHostId() + " is broadcasting data." + recipient);
		for (KnowledgeData kd : toSend) {
			System.out.println(" Rebroadcast Counter: " + ((DemoKnowledgeMetaData) kd.getMetaData()).rebroadcastCount);
		}
		if (recipient.equals(""))
			host.getPacketSender().sendData(toSend, "");
		else
			host.getPacketSender().sendData(toSend, recipient);
		toSend.clear();
		System.out.println("Node " + host.getHostId() + " is located at: (" + host.getPositionX() + ", " + host.getPositionY() + ")");
	}

	public void sendDummyData() {
		KnowledgeData kd = new KnowledgeData(new ValueSet(), new DemoKnowledgeMetaData(host.getHostId(), 0, host.getHostId(), 0, 0));
		toSend.add(kd);
		simulation.callAt(host.getCurrentTime() + 2000, host.getHostId());
	}

}
