package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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

	private final Exchanger<Object> exchanger;
	private final List<JDEECoAgentProvider> agentProviders;
	private final MATSimSimulationStepListener stepListener;
	private final MATSimUpdater updater;
	private final MATSimExtractor extractor;
	
	private long remainingExchanges;

	protected JDEECoWithinDayMobsimListener(Exchanger<Object> exchanger, MATSimSimulationStepListener stepListener, MATSimUpdater updater, MATSimExtractor extractor, long remainingExchanges) {
		this.exchanger = exchanger;
		this.agentProviders = new LinkedList<JDEECoAgentProvider>();
		this.stepListener = stepListener;
		this.updater = updater;
		this.extractor = extractor;
		this.remainingExchanges = remainingExchanges;
	}
	
	public JDEECoWithinDayMobsimListener(Exchanger<Object> exchanger, MATSimUpdater updater, MATSimExtractor extractor) {
		this(exchanger, null, updater, extractor, -1);
	}
	
	public JDEECoWithinDayMobsimListener(Exchanger<Object> exchanger, MATSimUpdater updater, MATSimExtractor extractor, long remainingExchanges) {
		this(exchanger, null, updater, extractor, remainingExchanges);
	}
	
	public JDEECoWithinDayMobsimListener(MATSimSimulationStepListener stepListener, MATSimUpdater updater, MATSimExtractor extractor) {
		this(null, stepListener, updater, extractor, -1);
	}

	public void registerAgentProvider(JDEECoAgentProvider agentProvider) {
		if (!agentProviders.contains(agentProvider)) {
			agentProviders.add(agentProvider);
		}
	}
	
	public void updateJDEECoAgents(Object input) {
		updater.updateJDEECoAgents(input, getAllJDEECoAgents());
	}
	
	public Collection<JDEECoAgent> getAllJDEECoAgents() {
		List<JDEECoAgent> agents = new LinkedList<>();
		for (JDEECoAgentProvider agentProvider : agentProviders) {
			agents.addAll(agentProvider.getAgents());
		}
		return agents;
	}

	@SuppressWarnings("rawtypes")
	public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent event) {
		if (this.remainingExchanges == 0) {
			return;
		}
		if (exchanger != null) {
			try {
				//System.out.println("MATSIM before: " + event.getSimulationTime());
				updateJDEECoAgents(exchanger.exchange(extractor.extractFromMATSim(getAllJDEECoAgents(), event.getQueueSimulation())));
				//System.out.println("MATSIM after: " + event.getSimulationTime());
			} catch (Exception e) {
				Log.e("jDEECoWithinDayMobsimListener: ", e);
			}
		} 
		if (stepListener != null) {
			stepListener.at(event.getSimulationTime(), event.getQueueSimulation());
		}
		if (this.remainingExchanges > 0) {
			this.remainingExchanges--;
		}
	}

}