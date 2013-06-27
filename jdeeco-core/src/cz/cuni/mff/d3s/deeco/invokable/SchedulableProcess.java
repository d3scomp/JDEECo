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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.KMCastException;
import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.IRuntime;
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

	// these are assigned after preprocessing, thus musn't be final
	public KnowledgeManager km;
	public ClassLoader contextClassLoader;

	public final ProcessSchedule scheduling;
        
        public static ThreadLocal<IRuntime> runtime = new ThreadLocal<>();

	public SchedulableProcess(KnowledgeManager km, ProcessSchedule scheduling,
			ClassLoader contextClassLoader) {
		this.scheduling = scheduling;
		this.km = km;
		this.contextClassLoader = contextClassLoader;
	}

	protected Object[] getParameterMethodValues(List<Parameter> in,
			List<Parameter> inOut, List<Parameter> out) throws KMException {
		return getParameterMethodValues(in, out, inOut, null, null, "");
	}

	protected Object[] getParameterMethodValues(List<Parameter> in,
			List<Parameter> inOut, List<Parameter> out, ISession session)
			throws KMException {
		return getParameterMethodValues(in, inOut, out, session, null, "");
	}

	protected Object[] getParameterMethodValues(List<Parameter> in,
			List<Parameter> inOut, List<Parameter> out, ISession session,
			String coordinator, String member) throws KMException {
		final List<Parameter> parametersIn = new ArrayList<Parameter>();
		parametersIn.addAll(in);
		parametersIn.addAll(inOut);
		Object value;
		Object[] result = new Object[parametersIn.size()
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
					value = getParameterInstance(p, coordinator, member, km,
							localSession);
					result[p.index] = value;
				}
				if (session == null)
					localSession.end();
				else
					break;
			}
			final List<Parameter> parametersOut = out;
			for (Parameter p : parametersOut)
				result[p.index] = getParameterInstance(p.type);
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

	/**
	 * Concerns a list of input candidates
	 * @param in
	 * @param inOut
	 * @param out
	 * @param session
	 * @param coordinator
	 * @param candidates
	 * @return
	 * @throws KMException
	 */
	protected Object[] getParameterMethodValues(List<Parameter> in,
			List<Parameter> inOut, List<Parameter> out, ISession session,
			String coordinator, String[] candidates) throws KMException {
		final List<Parameter> parametersIn = new ArrayList<Parameter>();
		parametersIn.addAll(in);
		parametersIn.addAll(inOut);
		Object value;
		Object[] result = new Object[parametersIn.size()
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
					// the change concerns the input array of candidates to the getParameterInstance
					value = getParameterInstance(p, coordinator, candidates, km,
							localSession);
					result[p.index] = value;
				}
				if (session == null)
					localSession.end();
				else
					break;
			}
			final List<Parameter> parametersOut = out;
			for (Parameter p : parametersOut)
				result[p.index] = getParameterInstance(p.type);
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
	protected void putParameterMethodValues(Object[] parameterValues,
			List<Parameter> inOut, List<Parameter> out) {
		putParameterMethodValues(parameterValues, inOut, out, null, null, null);
	}

	protected void putParameterMethodValues(Object[] parameterValues,
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
	protected void putParameterMethodValues(Object[] parameterValues,
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
						Object parameterValue = parameterValues[p.index];
						km.alterKnowledge(
								p.kPath.getEvaluatedPath(km, coordinator,
										member, session),
								p.type.isOutWrapper() ? ((OutWrapper) parameterValue).value
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

	private Object getParameterInstance(TypeDescription expectedParamType)
			throws KMCastException {
		try {
			if (expectedParamType.isMap()) {
				if (expectedParamType.isInterface())
					return new HashMap<String, Object>();
				else
					return expectedParamType.newInstance();
			} else if (expectedParamType.isList()) {
				if (expectedParamType.isInterface())
					return new ArrayList<Object>();
				else
					return expectedParamType.newInstance();
			} else
				return expectedParamType.newInstance();
		} catch (Exception e) {
			throw new KMCastException("Out parameter instantiation exception");
		}
	}

	private Object getParameterInstance(Parameter p, String coordinator,
			String member, KnowledgeManager km, ISession session)
			throws KMException, Exception {
		if (p.type.isOutWrapper()) {
			OutWrapper ow = (OutWrapper) p.type.newInstance();
			ow.value = km.getKnowledge(
					p.kPath.getEvaluatedPath(km, coordinator, member, session),
					p.type.getParametricTypeAt(0), session);
			return ow;
		} else {
			return km.getKnowledge(
					p.kPath.getEvaluatedPath(km, coordinator, member, session),
					p.type, session);
		}
	}
	
	// TODO: CAUTION : consider the type of the structure provided by the user = USE getParameterInstance(type)
	private Object getParameterInstance(Parameter p, String coordinator,
			String[] candidates, KnowledgeManager km, ISession session)
			throws KMException, Exception {
		// TODO: can the getParameterInstance be generic for the 
		// member/candidates for passing arguments to the evaluated path?
		if (p.type.isOutWrapper()) {
			OutWrapper ow = (OutWrapper) p.type.newInstance();
			// in case of candidate paths
			if (p.kPath.isCandidateEnsemblePath()){
				// evaluation of each path into an array
				String[] candidatePaths = p.kPath.getEvaluatedCandidatePaths(km, coordinator, candidates, session);
				Object[] candidatesKnowledge = new Object[candidatePaths.length];
				// retrieve the knowledge for each path into an array
				for (int i = 0; i < candidatePaths.length; i++){
					// take the first element in the knowledge retrievel !
					candidatesKnowledge[i] = ((Object[]) km.getKnowledge(candidatePaths[i], session))[0];
				}
				Object objectValue = null;
				if (p.type.isList())
					objectValue = Arrays.asList(candidatesKnowledge);
				else Log.e("Type for this parameter is not supported yet");
				// array into the outwrapper value object
				ow.value = objectValue;
			}else{
				// no supply of candidate information here as the path is not candidate-related
				ow.value = km.getKnowledge(
						p.kPath.getEvaluatedPath(km, coordinator, null, session),
						p.type.getParametricTypeAt(0), session);
			}
			return ow;
		} else {
			// same case distinction as the outwrapper
			if (p.kPath.isCandidateEnsemblePath()){
				String[] candidatePaths = p.kPath.getEvaluatedCandidatePaths(km, coordinator, candidates, session);
				Object[] candidatesKnowledge = new Object[candidatePaths.length];
				for (int i = 0; i < candidatePaths.length; i++){
					// take the first element in the knowledge retrievel !
					candidatesKnowledge[i] = ((Object[]) km.getKnowledge(candidatePaths[i], session))[0];
				}
				// adaptive process for the input membership parameter type
				Object objectValue = null;
				if (p.type.isList())
					objectValue = Arrays.asList(candidatesKnowledge);
				else Log.e("Type for this parameter is not supported yet");
				return objectValue;
			}else{
				// no supply of candidate information here as the path is not candidate-related
				return km.getKnowledge(
						p.kPath.getEvaluatedPath(km, coordinator, null, session),
						p.type, session);
			}
		}
	}

	public abstract void invoke(String triggererId, ETriggerType recipientMode);
}
