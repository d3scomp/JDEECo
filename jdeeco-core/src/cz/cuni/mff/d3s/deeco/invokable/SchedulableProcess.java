/*******************************************************************************
 * Copyright 2012 Charles University in Prague
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package cz.cuni.mff.d3s.deeco.invokable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.DeepCopy;
import cz.cuni.mff.d3s.deeco.scheduling.ETriggerType;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessPeriodicSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessTriggeredSchedule;

/**
 * Base class defining common functionalities for all schedulable processes.
 * 
 * @author Michal Kit
 * 
 */
public abstract class SchedulableProcess implements Serializable {

	private static final long serialVersionUID = -642546184205115045L;

	private final InputParametersHelper iph;
	private final OutputParametersHelper oph;
	
	public final KnowledgeManager km;
	public final ClassLoader contextClassLoader;

	public final ProcessSchedule scheduling;

	protected static class ParametersPair {
		public final Object originalValue; // Original value taken from the
											// repository
		public final Object value; // Newly cloned instance of the originalValue

		public ParametersPair(Object originalValue) {
			this.originalValue = originalValue;
			this.value = DeepCopy.copy(originalValue);
		}

		/**
		 * From the source array creates array of {@link ParametersPair#value}
		 * 
		 * @param source
		 *            Array where values are taken
		 * @return Extracted {@link ParametersPair#value} values
		 */
		static public Object[] extractValues(ParametersPair[] source) {
			Object[] result = new Object[source.length];
			for (int i = 0; i < source.length; i++) {
				result[i] = source[i].value;
			}
			return result;
		}
	}

	public SchedulableProcess(KnowledgeManager km, ProcessSchedule scheduling, ClassLoader contextClassLoader) {
		this.iph = new InputParametersHelper();
		this.oph = new OutputParametersHelper();
		this.scheduling = scheduling;
		this.km = km;
		this.contextClassLoader = contextClassLoader;
	}

	protected ParametersPair[] getParameterMethodValues(List<Parameter> in,
			List<Parameter> inOut, List<Parameter> out) throws KMException {
		return getParameterMethodValues(in, out, inOut, null, null, null);
	}

	protected ParametersPair[] getParameterMethodValues(List<Parameter> in,
			List<Parameter> inOut, List<Parameter> out, ISession session)
			throws KMException {
		return getParameterMethodValues(in, inOut, out, session, null, null);
	}

	protected ParametersPair[] getParameterMethodValues(List<Parameter> in,
			List<Parameter> inOut, List<Parameter> out, ISession session,
			String coordinator, String member) throws KMException {
		final List<Parameter> parametersIn = new ArrayList<Parameter>();
		parametersIn.addAll(in);
		parametersIn.addAll(inOut);
		Object value;
		ParametersPair[] result = new ParametersPair[parametersIn.size()
				+ ((out != null) ? out.size() : 0)];
		ISession localSession;
		if (session == null) {
			localSession = km.createSession();
			localSession.begin();
		} else
			localSession = session;
		try {
			while (localSession.repeat()) {
				for (Parameter p : parametersIn) {
					value = km.getKnowledge(p.kPath.getEvaluatedPath(km,
							coordinator, member, localSession), localSession);
					value = iph.getParameterInstance(p.type, value);

					result[p.index] = new ParametersPair(value);
				}
				if (session == null)
					localSession.end();
				else
					break;
			}
			final List<Parameter> parametersOut = out;
			for (Parameter p : parametersOut) {
				value = oph.getParameterInstance(p.type);
				result[p.index] = new ParametersPair(value);
			}
			return result;
		} catch (KMException kme) {
			// System.out.println("Parameter getting error!: " +
			// kme.getMessage());
			if (session == null)
				localSession.cancel();
			throw kme;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Function used to store computed values during the process method
	 * execution in the knowledge repository.
	 * 
	 * @param parameterValues
	 *            list of method all parameterTypes
	 * @param out
	 *            list of output parameter descriptions
	 * @param inOut
	 *            list of both output and input parameter descriptions
	 * @param root
	 *            knowledge level for which parameterTypes should stored.
	 */
	protected void putParameterMethodValues(ParametersPair[] parameterValues,
			List<Parameter> inOut, List<Parameter> out) {
		putParameterMethodValues(parameterValues, inOut, out, null, null, null);
	}

	protected void putParameterMethodValues(ParametersPair[] parameterValues,
			List<Parameter> inOut, List<Parameter> out, ISession session) {
		putParameterMethodValues(parameterValues, inOut, out, session, null,
				null);
	}

	/**
	 * Function used to store computed values during the process method
	 * execution in the knowledge repository. This version is session oriented.
	 * 
	 * @param parameterValues
	 *            list of method all parameterTypes
	 * @param out
	 *            list of output parameter descriptions
	 * @param inOut
	 *            list of both output and input parameter descriptions
	 * @param root
	 *            knowledge level for which parameterTypes should stored
	 * @param session
	 *            session instance within which all the storing operations
	 *            should be performed.
	 */
	protected void putParameterMethodValues(ParametersPair[] parameterValues,
			List<Parameter> inOut, List<Parameter> out, ISession session,
			String coordinator, String member) {
		if (parameterValues != null) {
			final List<Parameter> parameters = new ArrayList<Parameter>();
			parameters.addAll(out);
			parameters.addAll(inOut);
			ISession localSession;
			if (session == null) {
				localSession = km.createSession();
				localSession.begin();
			} else
				localSession = session;
			try {
				while (localSession.repeat()) {
					for (Parameter p : parameters) {
						ParametersPair valuePair = parameterValues[p.index];
						oph.storeOutValue(p.kPath.getEvaluatedPath(km,
								coordinator, member, localSession),
								valuePair.originalValue, valuePair.value, km,
								localSession);
					}
					if (session == null)
						localSession.end();
					else
						break;
				}
			} catch (Exception e) {
				if (session == null)
					localSession.cancel();
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Checks if the process has periodic scheduling.
	 * 
	 * @return true or false depending on the process scheduling.
	 */
	public boolean isPeriodic() {
		return scheduling instanceof ProcessPeriodicSchedule;
	}

	/**
	 * Checks if the process has triggered scheduling.
	 * 
	 * @return true or false depending on the process scheduling.
	 */
	public boolean isTriggered() {
		return scheduling instanceof ProcessTriggeredSchedule;
	}

	/**
	 * Function invokes single process execution.
	 */
	public void invoke() {
		invoke(null, null);
	}

	public abstract void invoke(String triggererId, ETriggerType recipientMode);
}
