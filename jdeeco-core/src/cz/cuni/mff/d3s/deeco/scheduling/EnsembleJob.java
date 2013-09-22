package cz.cuni.mff.d3s.deeco.scheduling;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.executor.JobExecutionListener;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.runtime.model.BooleanCondition;
import cz.cuni.mff.d3s.deeco.runtime.model.Ensemble;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;
import cz.cuni.mff.d3s.deeco.runtime.model.Schedule;

public class EnsembleJob extends Job {

	private final Ensemble ensemble;
	private final String coordinator;
	private final String member;
	private final Schedule schedule;
	private final String id;

	public EnsembleJob(Ensemble ensemble, String coordinator, String member,
			JobExecutionListener listener, Runtime runtime) {
		super(runtime, listener);
		this.ensemble = ensemble;
		this.coordinator = coordinator;
		this.member = member;
		this.schedule = ensemble.getSchedule();
		this.id = coordinator + member + ensemble.getId();
	}
	
	@Override
	public String getInstanceId() {
		return id;
	}

	public Ensemble getEnsemble() {
		return ensemble;
	}

	public String getCoordinator() {
		return coordinator;
	}

	public String getMember() {
		return member;
	}

	@Override
	public void run() {
		jobExecutionStarted();
		ISession session = km.createSession();
		session.begin();
		while (session.repeat()) {
			try {
				if (evaluateMembership(session))
					evaluateMethod(ensemble.getKnowledgeExchange(), session);
			} catch (Exception e) {
				Log.e("EnsembleJob exception", e);
				session.cancel();
				jobExecutionException(e);
				return;
			}
			session.end();
		}
		jobExecutionFinished();
	}

	@Override
	public Schedule getSchedule() {
		return schedule;
	}

	@Override
	public String getEvaluatedKnowledgePath(Parameter parameter,
			ISession session) {
		return parameter.getKnowledgePath().getEvaluatedPath(km, coordinator,
				member, null, session);
	}

	private boolean evaluateMembership(ISession session) {
		try {
			BooleanCondition membership = ensemble.getMembership();
			Object[] parameters = getParameterMethodValues(membership, session);
			Method m = membership.getMethod();
			return (boolean) m.invoke(null, parameters);
		} catch (Exception e) {
			Log.e("Ensemble exception while membership evaluation", e);
			return false;
		}
	}

	@Override
	public String getModelId() {
		return ensemble.getId();
	}
}
