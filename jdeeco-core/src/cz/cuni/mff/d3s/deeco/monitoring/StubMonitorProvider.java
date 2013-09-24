package cz.cuni.mff.d3s.deeco.monitoring;

import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.runtime.model.BooleanCondition;
import cz.cuni.mff.d3s.deeco.runtime.model.Ensemble;
import cz.cuni.mff.d3s.deeco.scheduling.ComponentProcessJob;
import cz.cuni.mff.d3s.deeco.scheduling.EnsembleJob;

public class StubMonitorProvider implements MonitorProvider {

	@Override
	public boolean getExchangeMonitorEvaluation(String eInvariantId,
			String coordinatorId, String memberId) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean getProcessMonitorEvaluation(String pInvariantId,
			String ownerId) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean getAssumptionMonitorEvaluation(String assumptionId,
			Map<String, String> roleAssignment) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void monitorAssumption(BooleanCondition assumption) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void monitorAssumptions(List<BooleanCondition> assumptions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void monitorProcessInvariant(BooleanCondition activeMonitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void monitorExchangeInvariant(String eInvariantId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void monitorProcessInvariants(List<BooleanCondition> activeMonitors) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProcessMonitor getProcessMonitor(ComponentProcessJob job) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExchangeMonitor getExchangeMonitor(EnsembleJob job) {
		// TODO Auto-generated method stub
		return null;
	}


}
