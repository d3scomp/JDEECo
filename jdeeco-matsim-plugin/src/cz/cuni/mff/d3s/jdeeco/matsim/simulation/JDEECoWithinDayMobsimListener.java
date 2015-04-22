package cz.cuni.mff.d3s.jdeeco.matsim.simulation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import org.matsim.core.mobsim.framework.events.MobsimBeforeSimStepEvent;
import org.matsim.core.mobsim.framework.listeners.MobsimBeforeSimStepListener;

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

//	private final Exchanger<Object> exchanger;
	private final CyclicBarrier barrier;
	private final List<JDEECoAgentProvider> agentProviders;
	private final MATSimSimulationStepListener stepListener;
	private final MATSimUpdater updater;
	
	private long remainingExchanges;

	protected JDEECoWithinDayMobsimListener(CyclicBarrier barrier, MATSimSimulationStepListener stepListener, MATSimUpdater updater, long remainingExchanges) {
		this.barrier = barrier;
		this.agentProviders = new LinkedList<JDEECoAgentProvider>();
		this.stepListener = stepListener;
		this.updater = updater;
		this.remainingExchanges = remainingExchanges;
	}
	
	public JDEECoWithinDayMobsimListener(MATSimSimulationStepListener stepListener, CyclicBarrier barrier, MATSimUpdater updater) {
		this(barrier, stepListener, updater, -1);
	}
	
	public JDEECoWithinDayMobsimListener(MATSimSimulationStepListener stepListener, CyclicBarrier barrier, MATSimUpdater updater, long remainingExchanges) {
		this(barrier, stepListener, updater, remainingExchanges);
	}
	
	public JDEECoWithinDayMobsimListener(MATSimSimulationStepListener stepListener, MATSimUpdater updater) {
		this(null, stepListener, updater, -1);
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
	/*	if (this.remainingExchanges == 0) {
			return;
		}*/
		if (barrier != null) {
			try {
				barrier.await();
			} catch (Exception e) {
				// Synchronization is lost, everything is lost
				e.printStackTrace();
				System.exit(-1);
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