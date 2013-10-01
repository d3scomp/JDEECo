package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.KMCastException;
import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.model.KnowledgeType;
import cz.cuni.mff.d3s.deeco.runtime.model.OutWrapperValueType;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;
import cz.cuni.mff.d3s.deeco.runtime.model.ParameterDirection;

public abstract class ParametrizedInstance {

	protected KnowledgeManager km;

	public ParametrizedInstance(KnowledgeManager km) {
		this.km = km;
	}

	public abstract String getEvaluatedKnowledgePath(Parameter parameter,
			ISession session);

	protected void putParameterMethodValues(List<Parameter> parameters,
			Object[] parameterValues) {
		putParameterMethodValues(parameters, parameterValues, null);
	}

	/**
	 * Function used to store computed values during the process method
	 * execution in the knowledge repository. This version is session oriented.
	 * 
	 */
	protected void putParameterMethodValues(List<Parameter> parameters,
			Object[] parameterValues, ISession session) {
		assert (parameterValues != null);
		ISession localSession;
		if (session == null) {
			localSession = km.createSession();
			localSession.begin();
		} else
			localSession = session;
		try {
			Parameter p;
			Object parameterValue;
			while (localSession.repeat()) {
				for (int i = 0; i < parameters.size(); i++) {
					p = parameters.get(i);
					parameterValue = parameterValues[i];
					km.alterKnowledge(
							getEvaluatedKnowledgePath(p, session),
							parameterValue instanceof OutWrapper<?> ? ((OutWrapper<?>) parameterValue).value
									: parameterValue, session);
				}
				if (session == null)
					localSession.end();
				else
					break;
			}
		} catch (Exception e) {
			if (session == null)
				localSession.cancel();
			Log.e("", e);
		}
	}

	protected Object[] getParameterMethodValues(List<Parameter> parameters)
			throws KMException {
		return getParameterMethodValues(parameters, null);
	}

	protected Object[] getParameterMethodValues(List<Parameter> parameters,
			ISession session) throws KMException {
		Object[] result = new Object[parameters.size()];
		ISession localSession;
		if (session == null) {
			localSession = km.createSession();
			localSession.begin();
		} else
			localSession = session;
		try {
			while (localSession.repeat()) {

				for (int i = 0; i < parameters.size(); i++)
					result[i] = getParameterInstance(parameters.get(i), km,
							localSession);
				if (session == null)
					localSession.end();
				else
					break;
			}
			return result;
		} catch (KMException kme) {
			if (kme instanceof KMCastException)
				Log.e(kme.getMessage());
			if (session == null)
				localSession.cancel();
			throw kme;
		} catch (Exception e) {
			Log.e("", e);
			return null;
		}
	}

	private Object getParameterInstance(Parameter p, KnowledgeManager km,
			ISession session) throws KMException, Exception {
		KnowledgeType t = p.getType();
		if (p.getDirection().equals(ParameterDirection.OUT)) {
			try {
				return t.newInstance();
			} catch (Exception e) {
				throw new KMCastException(
						"Out parameter instantiation exception");
			}
		} else {
			if (t instanceof OutWrapperValueType) {
				OutWrapper ow = (OutWrapper) ((OutWrapperValueType) t)
						.newInstance();
				ow.value = km.getKnowledge(
						getEvaluatedKnowledgePath(p, session),
						((OutWrapperValueType) t).getWrappedType(), session);
				return ow;
			} else {
				return km.getKnowledge(getEvaluatedKnowledgePath(p, session),
						t, session);
			}
		}
	}
}