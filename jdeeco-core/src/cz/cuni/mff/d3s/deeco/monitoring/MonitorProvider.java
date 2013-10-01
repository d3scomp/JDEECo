package cz.cuni.mff.d3s.deeco.monitoring;

import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.runtime.model.BooleanCondition;
import cz.cuni.mff.d3s.deeco.scheduling.ComponentProcessTask;
import cz.cuni.mff.d3s.deeco.scheduling.EnsembleTask;

public interface MonitorProvider {
	boolean getExchangeMonitorEvaluation(String eInvariantId,
			String coordinatorId, String memberId);

	boolean getProcessMonitorEvaluation(String pInvariantId, String ownerId);

	boolean getAssumptionMonitorEvaluation(String assumptionId,
			Map<String, String> roleAssignment);

	void monitorAssumption(BooleanCondition assumption);

	void monitorAssumptions(List<BooleanCondition> assumptions);

	void monitorProcessInvariant(BooleanCondition activeMonitor);

	void monitorProcessInvariants(List<BooleanCondition> activeMonitors);

	void monitorExchangeInvariant(String eInvariantId);

	ProcessMonitor getProcessMonitor(ComponentProcessTask task);

	ExchangeMonitor getExchangeMonitor(EnsembleTask task);
}
