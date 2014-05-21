package cz.cuni.mff.d3s.deeco.simulation.matsim;

import org.matsim.core.controler.Controler;

public class MATSimPreloadingControler extends Controler {

	public MATSimPreloadingControler(String configFileName) {
		super(configFileName);
		loadData();
	}

}
