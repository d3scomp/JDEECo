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
import cz.cuni.mff.d3s.deeco.knowledge.TypeUtils;
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
		return getParameterMethodValues(in, inOut, out, session, coordinator, new String[] {member});
	}

	/**
	 * Concerns a list of input candidates which has been created inside the membership function.
	 * The parameter instances are gotten via the getParameterInstance but this time with the
	 * input candidate ids.
	 * @param in
	 * @param inOut
	 * @param out
	 * @param session
	 * @param coordinator
	 * @param candidates the input set of candidate ids to base the process on
	 * @return the parameter values
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
					//Log.i(p.kPath.toString());
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
		putParameterMethodValues(parameterValues, inOut, out, null, null, "");
	}

	protected void putParameterMethodValues(Object[] parameterValues,
			List<Parameter> inOut, List<Parameter> out, ISession session) {
		putParameterMethodValues(parameterValues, inOut, out, session, null, "");
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
		
		putParameterMethodValues(parameterValues, inOut, out, session, coordinator, new String[]{member});
	}
	
	protected void putParameterMethodValues(Object[] parameterValues,
			List<Parameter> inOut, List<Parameter> out, ISession session,
			String coordinator, String[] candidates) {
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
						putParameterInstance(p, parameterValues, coordinator, candidates, km, localSession);
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
	
	private void putParameterInstance(Parameter p, Object[] parameterValues, String coordinator,
			String[] candidates, KnowledgeManager km, ISession session) throws KMException, Exception {
		Object parameterValue = parameterValues[p.index];
		Boolean isOutWrapper = p.type.isOutWrapper();
		// the treatment of the parameter can be different regarding the type of the output object (OutWrapper...)
		Object iteratedValue = (isOutWrapper ? ((OutWrapper<?>) parameterValue).value : parameterValue);
		// retrieve the knowledge for each path into an array
		for (int i = 0; i < candidates.length; i++){
			// select the right object value regarding the type of the output object (OutWrapper...)
			Object value = null;
				
			if (p.kPath.isCandidateEnsemblePath() && TypeUtils.isList(iteratedValue.getClass())){
				value = ((List<?>) iteratedValue).get(i);
			}else{
				value = iteratedValue;
			}
			// alter the knowledge of coordinator-candidate pairs with the given value (from the list or as a primitive)
			// the coordinator will then collect all the related candidate knowledge in lists
			km.alterKnowledge(
				p.kPath.getEvaluatedPath(km, coordinator, candidates[i], session), 
				value, session);
		}
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
			String[] candidates, KnowledgeManager km, ISession session)
			throws KMException, Exception {
		OutWrapper ow = null;
		Object objectReturn = null;
		Object objectValue = null;
		TypeDescription tdUnit = null;
		Boolean isOutWrapper = p.type.isOutWrapper();
		// Caution: the type description is the p.type when manipulating a knowledge parameter
		// when we manipulate an outwrapper, we must take the type description which is wrapped !
		// that affects the process of knowledge retrieving
		TypeDescription td = null;
		// in case of an out wrapper
		if (isOutWrapper){
			ow = (OutWrapper) p.type.newInstance();
			td = p.type.getParametricTypeAt(0);
		}else{
			td = p.type;
		}
		// define some states of the parameter
		Boolean isCandidateEnsemblePath = p.kPath.isCandidateEnsemblePath();
		Boolean isList = td.isList();
		// the type description unit is the type to be retrieved for one iteration on the loop
		// can differ regarding the input type (for instance in an OutWrapper<List<T>>)
		// the treatment for candidates is iterative
		if (isCandidateEnsemblePath && isList){
			tdUnit = p.type.getParametricTypeAt(0);
		}else{
			tdUnit = td;
		}
		Object[] candidatesKnowledge = new Object[candidates.length];
		// retrieve the knowledge for each path into an array
		for (int i = 0; i < candidates.length; i++){
			candidatesKnowledge[i] = km.getKnowledge(p.kPath.getEvaluatedPath(km, coordinator, candidates[i], session), tdUnit, session);
		}
		// different treatment for the result regarding the knowledge type
		if (isCandidateEnsemblePath && td.isList()){
			objectValue = Arrays.asList(candidatesKnowledge);
		}else if (isCandidateEnsemblePath){
			throw new Exception("getParameterInstance : Type for this parameter is not supported yet");
		}else{
			objectValue = candidatesKnowledge[0];
		}
		// assign the object value to the proper returned object
		if (isOutWrapper){
			ow.value = objectValue;
			objectReturn = ow;
		}else{
			objectReturn = objectValue;
		}
		// return the OutWrapper or the object to be returned
		return objectReturn;
	}

	public abstract void invoke(String triggererId, ETriggerType recipientMode);
}
