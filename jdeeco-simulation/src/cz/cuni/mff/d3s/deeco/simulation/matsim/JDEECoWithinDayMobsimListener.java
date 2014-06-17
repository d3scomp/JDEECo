package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;

import org.matsim.api.core.v01.Id;
import org.matsim.core.mobsim.framework.events.MobsimBeforeSimStepEvent;
import org.matsim.core.mobsim.framework.listeners.MobsimBeforeSimStepListener;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.simulation.SimulationStepListener;

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

	private final Exchanger<Map<Id, ?>> exchanger;
	private final List<JDEECoAgentProvider> agentProviders;
	private final SimulationStepListener stepListener;

	protected JDEECoWithinDayMobsimListener(Exchanger<Map<Id, ?>> exchanger, SimulationStepListener stepListener) {
		this.exchanger = exchanger;
		this.agentProviders = new LinkedList<JDEECoAgentProvider>();
		this.stepListener = stepListener;
	}
	
	public JDEECoWithinDayMobsimListener(Exchanger<Map<Id, ?>> exchanger) {
		this(exchanger, null);
	}
	
	public JDEECoWithinDayMobsimListener(SimulationStepListener stepListener) {
		this(null, stepListener);
	}

	public void registerAgentProvider(JDEECoAgentProvider agentProvider) {
		if (!agentProviders.contains(agentProvider)) {
			agentProviders.add(agentProvider);
		}
	}

	public Map<Id, MATSimOutput> getOutputs(double currentSeconds) {
		Map<Id, MATSimOutput> matSimOutputs = new HashMap<Id, MATSimOutput>();

		// Get agents current positions and jDEECoAgents
		MATSimOutput matSimOutput;
		for (JDEECoAgentProvider agentProvider : agentProviders) {
			for (JDEECoAgent agent : agentProvider.getAgents()) {
				matSimOutput = new MATSimOutput(agent.getCurrentLinkId(),
						agent.estimatePosition(currentSeconds));
				matSimOutputs.put(agent.getId(), matSimOutput);
			}
		}
		return matSimOutputs;
	}
	
	public void setInputs(Map<Id, ?> matSimInputs) {
		if (matSimInputs != null && !matSimInputs.isEmpty()) {
			MATSimInput mData;
			for (JDEECoAgentProvider agentProvider : agentProviders) {
				for (JDEECoAgent agent : agentProvider.getAgents()) {
					if (matSimInputs.containsKey(agent.getId())) {
						mData = (MATSimInput) matSimInputs.get(agent.getId());
						agent.setInput(mData);
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent event) {
		if (exchanger != null) {
			try {
				Map<Id, MATSimOutput> matSimOutputs = getOutputs(event
						.getSimulationTime());
				// Exchange data (Rendezvous)
				Map<Id, ?> matSimInputs = exchanger.exchange(matSimOutputs);
				// Log.w("MATSim After data exchange at " +
				// event.getSimulationTime() + " " + remainingExchanges );

				// Update jDEECo agents next link id
				setInputs(matSimInputs);
			} catch (Exception e) {
				Log.e("jDEECoWithinDayMobsimListener: ", e);
			}
		} 
		if (stepListener != null) {
			stepListener.at(Math.round(event.getSimulationTime()), null);
		}
	}

}