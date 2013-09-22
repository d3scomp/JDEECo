package cz.cuni.mff.d3s.deeco.scheduling;

import cz.cuni.mff.d3s.deeco.executor.JobExecutionListener;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.runtime.model.ComponentProcess;
import cz.cuni.mff.d3s.deeco.runtime.model.LockingMode;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;
import cz.cuni.mff.d3s.deeco.runtime.model.Schedule;

public class ComponentProcessJob extends Job {

	private final String componentId;
	private final ComponentProcess componentProcess;
	private final String id;

	public ComponentProcessJob(ComponentProcess componentProcess,
			String componentId, JobExecutionListener listener,
			Runtime runtime) {
		super(runtime, listener);
		this.componentId = componentId;
		this.componentProcess = componentProcess;
		this.id = componentId + componentProcess.getId();
	}

	public String getComponentId() {
		return componentId;
	}

	public ComponentProcess getComponentProcess() {
		return componentProcess;
	}
	
	@Override
	public String getInstanceId() {
		return id;
	}

	@Override
	public String getEvaluatedKnowledgePath(Parameter parameter,
			ISession session) {
		return parameter.getKnowledgePath().getEvaluatedPath(km, null, null,
				componentId, session);
	}

	@Override
	public Schedule getSchedule() {
		return componentProcess.getSchedule();
	}

	@Override
	public void run() {
		jobExecutionStarted();
		LockingMode lm = componentProcess.getLockingMode();
		if (lm.equals(LockingMode.STRONG)) {
			ISession session = km.createSession();
			session.begin();
			try {
				while (session.repeat()) {
					evaluateMethod(componentProcess, session);
					session.end();
				}
			} catch (Exception e) {
				Log.e("", e);
				session.cancel();
				jobExecutionException(e);
			}
		} else {
			try {
				evaluateMethod(componentProcess, null);
			} catch (Exception e) {
				Log.e("Job message error", e);
				jobExecutionException(e);
			}
		}
		jobExecutionFinished();
	}

	@Override
	public String getModelId() {
		return componentProcess.getId();
	}

}
