package cz.cuni.mff.d3s.jdeeco.simulation.jni;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeData;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeDataReceiver;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.simulation.Host;
import cz.cuni.mff.d3s.deeco.simulation.SimulationTimeEventListener;

public class KnowledgeDataManager implements KnowledgeDataReceiver,
		SimulationTimeEventListener {

	private final Host host;
	private final List<DemoKnowledgeData> toSend;

	public KnowledgeDataManager(Host host) {
		this.host = host;
		this.host.getPacketReceiver().setKnowledgeDataReceiver(this);
		toSend = new LinkedList<DemoKnowledgeData>();
		this.host.setSimulationTimeEventListener(this);
	}

	@Override
	public void receive(List<? extends KnowledgeData> knowledgeData) {
		if (knowledgeData != null) {
			for (KnowledgeData kd : knowledgeData) {
				System.out.print(host.getSimulationTime() + " Node "
						+ host.getId() + " has received knowledge from "
						+ kd.getComponentId() + ".");
				if (kd instanceof DemoKnowledgeData) {
					DemoKnowledgeData dkd = (DemoKnowledgeData) kd;
					System.out.println(" Rebroadcast Counter: "
							+ dkd.rebroadcastCount);
					if (dkd.rebroadcastCount < 5) {
						dkd.rebroadcastCount++;
						toSend.add(dkd);
					}
				}
			}
			if (!toSend.isEmpty())
				host.callAt(host.getSimulationTime()
						+ new Random().nextInt(2000));
		}
	}

	@Override
	public void at(long time) {
		System.out.print("Node " + host.getId() + " is broadcasting data.");
		for (DemoKnowledgeData dkd : toSend) {
			System.out.println(" Rebroadcast Counter: " + dkd.rebroadcastCount);
		}
		host.sendData(toSend);
		toSend.clear();
		System.out.println("Node " + host.getId() + " is located at: (" + host.getPositionX() + ", " + host.getPositionY() + ")");
	}

	public void sendDummyData() {
		DemoKnowledgeData dkd = new DemoKnowledgeData(host.getId(),
				new ValueSet());
		toSend.add(dkd);
		host.callAt(host.getSimulationTime() + 2000);
	}

}
