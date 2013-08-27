package cz.cuni.mff.d3s.deeco.scheduling;

import java.lang.reflect.Method;

import cz.cuni.mff.d3s.deeco.executor.JobExecutionListener;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.model.Condition;
import cz.cuni.mff.d3s.deeco.runtime.model.Ensemble;
import cz.cuni.mff.d3s.deeco.runtime.model.Exchange;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;
import cz.cuni.mff.d3s.deeco.runtime.model.Schedule;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

public class EnsembleJob extends Job {

	private Condition membership;
	private Exchange knowledgeExchange;
	private String coordinator;
	private String member;
	private Schedule schedule;

	public EnsembleJob(Ensemble ensemble, String coordinator, String member, JobExecutionListener listener, Runtime runtime) {
		super(runtime, listener);
		this.membership = ensemble.getMembership();
		this.knowledgeExchange = ensemble.getKnowledgeExchange();
		this.coordinator = coordinator;
		this.member = member;
		this.schedule = ensemble.getSchedule();
	}

	@Override
	public void run() {
		jobExecutionStarted();
		ISession session = km.createSession();
		session.begin();
		while (session.repeat()) {
			try {
				if (evaluateMembership(session))
					evaluateMethod(knowledgeExchange, session);
			} catch (Exception e) {
				Log.e("EnsembleJob exception", e);
				session.cancel();
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
	protected String getEvaluatedKnowledgePath(Parameter parameter,
			ISession session) {
		return parameter.getKnowledgePath().getEvaluatedPath(km, coordinator,
				member, null, session);
	}

	private boolean evaluateMembership(ISession session) {
		try {
			Object[] parameters = getParameterMethodValues(membership, session);
			Method m = membership.getMethod();
			return (boolean) m.invoke(null, parameters);
		} catch (Exception e) {
			Log.e("Ensemble exception while membership evaluation", e);
			return false;
		}
	}
}
