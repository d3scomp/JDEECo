package cz.cuni.mff.d3s.jdeeco.simulation.jni;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataReceiver;
import cz.cuni.mff.d3s.deeco.simulation.Host;
import cz.cuni.mff.d3s.deeco.simulation.SimulationTimeEventListener;

public class DemoJNIKnowledgeDataManager implements KnowledgeDataReceiver,
		SimulationTimeEventListener {

	private final Host host;
	private final List<KnowledgeData> toSend;

	public DemoJNIKnowledgeDataManager(Host host) {
		this.host = host;
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
									+ host.getId() + " has received knowledge from "
									+ md.componentId + ".");		
					System.out.println(" Rebroadcast Counter: "
							+ md.rebroadcastCount + " RSSI: " + kd.getMetaData().rssi);
					if (md.rebroadcastCount < 5) {
						md.rebroadcastCount++;
						toSend.add(new KnowledgeData(new ValueSet(), md));
					}
			}
			if (!toSend.isEmpty())
				host.callAt(host.getCurrentTime()
						+ new Random().nextInt(2000));
		}
	}

	@Override
	public void at(long time) {
		System.out.print("Node " + host.getId() + " is broadcasting data.");
		for (KnowledgeData kd : toSend) {
			System.out.println(" Rebroadcast Counter: " + ((DemoKnowledgeMetaData) kd.getMetaData()).rebroadcastCount);
		}
		host.sendData(toSend);
		toSend.clear();
		System.out.println("Node " + host.getId() + " is located at: (" + host.getPositionX() + ", " + host.getPositionY() + ")");
	}

	public void sendDummyData() {
		KnowledgeData kd = new KnowledgeData(new ValueSet(), new DemoKnowledgeMetaData(host.getId(), 0, host.getId(), 0, 0));
		toSend.add(kd);
		host.callAt(host.getCurrentTime() + 2000);
	}

}
