package cz.cuni.mff.d3s.deeco.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DEECoSimulationRealm {

	List<DEECo> nodesList;
	DEECoPlugin[] plugins;
	SimulationSchedulerNotifier simulationSchedulerNotifier;
	
	public DEECoSimulationRealm(SimulationSchedulerNotifier simulationSchedulerNotifier, DEECoPlugin... plugins) {
		this.plugins = plugins;
		this.simulationSchedulerNotifier = simulationSchedulerNotifier;
		nodesList = new ArrayList<>();
	}
	
	public void start() {
		simulationSchedulerNotifier.start();
	}

	public DEECo createNode(DEECoPlugin... extraPlugins) throws DEECoException {
		DEECo node = new DEECo(simulationSchedulerNotifier, getAllPlugins(extraPlugins));
		nodesList.add(node);
		return node;
	}

	public void setTerminationTime(long terminationTime) {
		simulationSchedulerNotifier.setTerminationTime(terminationTime);
	}
	
	private DEECoPlugin[] getAllPlugins(DEECoPlugin[] extraPlugins) {
		DEECoPlugin[] ret = new DEECoPlugin[extraPlugins.length+plugins.length];
		int ind=0;
		for (int i=0; i<extraPlugins.length; i++) {
			ret[ind] = extraPlugins[i];
			ind++;
		}
		for (int j=0; j<plugins.length; j++) {
			ret[ind] = extraPlugins[j];
			ind++;			
		}
		return ret;
	}
	
}
