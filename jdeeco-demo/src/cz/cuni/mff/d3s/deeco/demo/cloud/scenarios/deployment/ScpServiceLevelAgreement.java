package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.deployment;

import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;


public class ScpServiceLevelAgreement extends Knowledge {
	
	/**
	 * first scenario
	 */
	public Long maxLinkLatency;
	
	/**
	 * third scenario
	 */
	public Integer minCpuCores;
	public Long minCpuFrequency;
	
	public ScpServiceLevelAgreement() {
		maxLinkLatency = Long.MAX_VALUE;
	}
}
