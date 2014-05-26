package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Exchanger;

import org.matsim.core.mobsim.framework.events.MobsimBeforeSimStepEvent;
import org.matsim.core.mobsim.framework.listeners.MobsimBeforeSimStepListener;

import cz.cuni.mff.d3s.deeco.logging.Log;

public class JDEECoWithinDayMobsimListener implements
		MobsimBeforeSimStepListener {

	private final Exchanger<Map<String, ?>> exchanger;
	private JDEECoAgentProvider agentProvider;

	public JDEECoWithinDayMobsimListener(Exchanger<Map<String, ?>> exchanger) {
		this.exchanger = exchanger;
	}

	public void registerAgentProvider(JDEECoAgentProvider agentProvider) {
		this.agentProvider = agentProvider;
	}

	public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent event) {
		try {
			Map<String, MATSimOutput> matSimOutputs = new HashMap<String, MATSimOutput>();

			// Get agents current positions and jDEECoAgents
			MATSimOutput matSimOutput;
			for (JDEECoAgent agent : agentProvider.getAgents()) {
				matSimOutput = new MATSimOutput(agent.getCurrentLinkId(),
						agent.estimatePosition(event.getSimulationTime()));
				matSimOutputs.put(agent.getId().toString(), matSimOutput);
			}
			// Exchange data (Rendezvous)
			//Log.w("MATSim Before data exchange at " + event.getSimulationTime());
			Map<String, ?> matSimInputs = exchanger.exchange(matSimOutputs);
			//Log.w("MATSim After data exchange at " + event.getSimulationTime());
			// Update jDEECo agents next link id
			if (matSimInputs != null && !matSimInputs.isEmpty()) {
				MATSimInput mData;
				for (JDEECoAgent agent : agentProvider.getAgents()) {
					if (matSimInputs.containsKey(agent.getId().toString())) {
						mData = (MATSimInput) matSimInputs.get(agent.getId()
								.toString());
						agent.setDestinationLinkId(mData.destination);
						agent.setRoute(mData.route);
						agent.setActivityEndTime(mData.activityEndTime);
						agent.setActivityType(mData.activityType);
					}
				}
			}
		} catch (Exception e) {
			Log.e("jDEECoWithinDayMobsimListener: ", e);
		}
	}

}