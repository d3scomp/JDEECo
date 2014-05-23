package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Exchanger;

import org.matsim.core.mobsim.framework.events.MobsimBeforeSimStepEvent;
import org.matsim.core.mobsim.framework.listeners.MobsimBeforeSimStepListener;

import cz.cuni.mff.d3s.deeco.logging.Log;

public class jDEECoWithinDayMobsimListener implements
		MobsimBeforeSimStepListener {

	private final Exchanger<Map<String, ?>> exchanger;
	private jDEECoAgentProvider agentProvider;

	public jDEECoWithinDayMobsimListener(Exchanger<Map<String, ?>> exchanger) {
		this.exchanger = exchanger;
	}

	public void registerAgentProvider(jDEECoAgentProvider agentProvider) {
		this.agentProvider = agentProvider;
	}

	public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent event) {
		try {
			Map<String, MATSimOutput> matSimOutputs = new HashMap<String, MATSimOutput>();

			// Get agents current positions and jDEECoAgents
			MATSimOutput matSimOutput;
			for (jDEECoAgent agent : agentProvider.getAgents()) {
				matSimOutput = new MATSimOutput(agent.getCurrentLinkId(),
						agent.estimatePosition(event.getSimulationTime()));
				matSimOutputs.put(agent.getId().toString(), matSimOutput);
			}
			System.out.println();
			// Exchange data (Rendezvous)
			System.out.println("MATSim Time: " + event.getSimulationTime());
			Map<String, ?> matSimInputs = exchanger.exchange(matSimOutputs);
			// Update jDEECo agents next link id
			if (matSimInputs != null && !matSimInputs.isEmpty()) {
				MATSimInput mData;
				for (jDEECoAgent agent : agentProvider.getAgents()) {
					if (matSimInputs.containsKey(agent.getId().toString())) {
						mData = (MATSimInput) matSimInputs.get(agent.getId()
								.toString());
						agent.setDestinationLinkId(mData.destination);
						agent.setRoute(mData.route);
						agent.setActivityEndTime(mData.activityEndTime);
					}
				}
			}
		} catch (Exception e) {
			Log.e("jDEECoWithinDayMobsimListener: ", e);
		}
	}

}