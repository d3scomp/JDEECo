package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.network.DirectGossipStrategy;
import cz.cuni.mff.d3s.deeco.network.DirectRecipientSelector;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.simulation.SimulationRuntimeBuilder;
import cz.cuni.mff.d3s.deeco.simulation.omnet.OMNetSimulationHost;
import cz.cuni.mff.d3s.deeco.simulation.task.SimulationStepTask;

/**
 * MATSim-OMNet simulation builder. An additional functionality of this class is
 * to register the simulation step task at one (i.e. the first) scheduler.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class MATSimOMNetSimulationRuntimeBuilder extends
		SimulationRuntimeBuilder {
	private boolean simulationTaskRegistered = false;

	public RuntimeFramework build(OMNetSimulationHost host,
			MATSimOMNetSimulation simulation, RuntimeMetadata model,
			Collection<DirectRecipientSelector> recipientSelectors,
			DirectGossipStrategy directGossipStrategy) {
		RuntimeFramework result = super.build(host, simulation, model,
				recipientSelectors, directGossipStrategy);
		if (!simulationTaskRegistered && result != null) {
			Scheduler scheduler = result.getScheduler();
			// Set up the simulation step task
			SimulationStepTask simulationStepTask = new SimulationStepTask(
					scheduler, simulation);

			// Add simulation step task to the scheduler
			scheduler.addTask(simulationStepTask);

			simulationTaskRegistered = true;
		}
		return result;
	}

}
