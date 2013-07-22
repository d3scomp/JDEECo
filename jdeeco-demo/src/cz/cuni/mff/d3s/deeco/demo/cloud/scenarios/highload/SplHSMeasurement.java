package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.highload;

import cz.cuni.mff.spl.SPL;

public class SplHSMeasurement {

	private final static String pkg = "cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.highload.";
	
	/**
	 * something like this
	 */
	@SPL(
			generators={"load="+pkg+"LoadGenerator()#generate()"},
			methods={"ens="+pkg+"DeployHSEnsemble#membership"},
			formula={"A = load", "B = runningScp", "A < B", "ens[app.id, app.isDeployed, app.scpId, B.id, B.load] = true"}
		)
	void measure(){
	}
}
