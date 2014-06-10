package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;

import org.matsim.core.mobsim.framework.events.MobsimBeforeSimStepEvent;
import org.matsim.core.mobsim.framework.listeners.MobsimBeforeSimStepListener;

import cz.cuni.mff.d3s.deeco.logging.Log;

/**
 * 
 * This class represents the interface (on the MATSim side) between jDEECo and
 * MATSim. It's notifyMobsimBeforeSimStep is executed before each simulation
 * step and data exchange between MATSim and jDEECo happens via the exchanger
 * instance.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class JDEECoWithinDayMobsimListener implements
		MobsimBeforeSimStepListener {

	private final Exchanger<Map<String, ?>> exchanger;
	private final List<JDEECoAgentProvider> agentProviders;
	private long remainingExchanges;

	public JDEECoWithinDayMobsimListener(Exchanger<Map<String, ?>> exchanger,
			long remainingExchanges) {
		this.exchanger = exchanger;
		this.agentProviders = new LinkedList<JDEECoAgentProvider>();
		this.remainingExchanges = remainingExchanges;
	}

	public void registerAgentProvider(JDEECoAgentProvider agentProvider) {
		if (!agentProviders.contains(agentProvider)) {
			agentProviders.add(agentProvider);
		}
	}

	public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent event) {
		try {
//			if (this.remainingExchanges > 0) {
				Map<String, MATSimOutput> matSimOutputs = new HashMap<String, MATSimOutput>();

				// Get agents current positions and jDEECoAgents
				MATSimOutput matSimOutput;
				for (JDEECoAgentProvider agentProvider : agentProviders) {
					for (JDEECoAgent agent : agentProvider.getAgents()) {
						matSimOutput = new MATSimOutput(
								agent.getCurrentLinkId(),
								agent.estimatePosition(event
										.getSimulationTime()));
						matSimOutputs.put(agent.getId().toString(),
								matSimOutput);
					}
				}

				// Exchange data (Rendezvous)
				Map<String, ?> matSimInputs = exchanger.exchange(matSimOutputs);
				this.remainingExchanges--;
//				Log.w("MATSim After data exchange at " +
//				event.getSimulationTime() + " " + remainingExchanges );

				// Update jDEECo agents next link id
				if (matSimInputs != null && !matSimInputs.isEmpty()) {
					MATSimInput mData;
					for (JDEECoAgentProvider agentProvider : agentProviders) {
						for (JDEECoAgent agent : agentProvider.getAgents()) {
							if (matSimInputs.containsKey(agent.getId()
									.toString())) {
								mData = (MATSimInput) matSimInputs.get(agent
										.getId().toString());
								agent.setInput(mData);
							}
						}
					}
				}
//			}
		} catch (Exception e) {
			Log.e("jDEECoWithinDayMobsimListener: ", e);
		}
	}

}