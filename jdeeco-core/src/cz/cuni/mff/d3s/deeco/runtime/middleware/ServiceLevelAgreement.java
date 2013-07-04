package cz.cuni.mff.d3s.deeco.runtime.middleware;

public class ServiceLevelAgreement {
	
	/**
	 * first scenario
	 */
	public Integer maxLinkLatency;
	
	/**
	 * third scenario
	 */
	public Integer minCpuCores;
	public Long minCpuFrequency;
	
	public ServiceLevelAgreement() {
		maxLinkLatency = Integer.MAX_VALUE;
	}
}
