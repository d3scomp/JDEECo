package cz.cuni.mff.d3s.deeco.scheduling;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.executor.JobExecutionListener;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.model.ComponentProcess;
import cz.cuni.mff.d3s.deeco.runtime.model.LockingMode;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;
import cz.cuni.mff.d3s.deeco.runtime.model.Schedule;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

public class ComponentProcessJob extends Job {

	private String componentId;
	private ComponentProcess componentProcess;

	public ComponentProcessJob(ComponentProcess componentProcess,
			String componentId, JobExecutionListener listener, Runtime runtime) {
		super(runtime, listener);
		this.componentId = componentId;
		this.componentProcess = componentProcess;
	}

	@Override
	protected String getEvaluatedKnowledgePath(Parameter parameter,
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
		try {
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
				} catch (KMException e) {
					Log.e("", e);
					session.cancel();
				}
			} else {
				try {
					evaluateMethod(componentProcess, null);
				} catch (KMException kme) {
					Log.e("Job message error", kme);
					kme.printStackTrace();
				}
			}
			jobExecutionFinished();
		} catch (Exception e) {
			Log.e("", e);
		}
	}

}
