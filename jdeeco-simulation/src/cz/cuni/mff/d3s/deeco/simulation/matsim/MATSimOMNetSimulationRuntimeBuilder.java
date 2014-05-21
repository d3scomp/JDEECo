package cz.cuni.mff.d3s.deeco.simulation.matsim;

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.network.DirectGossipStrategy;
import cz.cuni.mff.d3s.deeco.network.DirectRecipientSelector;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.simulation.SimulationHost;
import cz.cuni.mff.d3s.deeco.simulation.omnet.OMNetSimulationRuntimeBuilder;
import cz.cuni.mff.d3s.deeco.simulation.task.SimulationStepTask;

public class MATSimOMNetSimulationRuntimeBuilder extends
		OMNetSimulationRuntimeBuilder {
	private boolean simulationTaskRegistered = false;
	
	public RuntimeFramework build(SimulationHost host, MATSimOMNetSimulation simulation, RuntimeMetadata model, Collection<DirectRecipientSelector> recipientSelectors, DirectGossipStrategy directGossipStrategy) {
		RuntimeFramework result = super.build(host, simulation, model, recipientSelectors, directGossipStrategy);
		if (!simulationTaskRegistered && result != null) {
			Scheduler scheduler = result.getScheduler();
			// Set up the simulation step task
			SimulationStepTask simulationStepTask = new SimulationStepTask(scheduler, 
					simulation, 
					simulation.getSimulationStep(), 
					host.getHostId());
			
			// Add simulation step task to the scheduler
			scheduler.addTask(simulationStepTask);
			
			simulationTaskRegistered = true;
		}
		return result;
	}
	
}
