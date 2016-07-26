package cz.cuni.mff.d3s.deeco.simulation.matsim;

import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.population.algorithms.XY2Links;

/**
 * This is an extension of the MATSim controller that allows for scenario data
 * preloading. Normally the scenario is loaded when the simulation starts. As
 * data such as population, map is necessary beforehand this class has been
 * introduced.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class MATSimPreloadingControler extends Controler {

	public MATSimPreloadingControler(String configFileName) {
		super(configFileName);
		loadData();
		XY2Links xy2Links = new XY2Links(this.getNetwork(),
				((ScenarioImpl) this.getScenario())
						.getActivityFacilities());
		xy2Links.run(getPopulation());
	}

}
