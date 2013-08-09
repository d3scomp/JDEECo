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
import cz.cuni.mff.d3s.deeco.invokable.parameters.Parameter;
import cz.cuni.mff.d3s.deeco.invokable.parameters.SelectorParameter;
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

	/**
	 * Concerns a list of input candidates which has been created inside the membership function.
	 * The parameter instances are gotten via the getParameterInstance but this time with the
	 * input candidate ids.
	 */
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
					// the change concerns the input array of candidates to the getParameterInstance
					value = getNodeParameterInstance(p, coordinator, member, km,
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
	 * Check if the coordinator fits the provided method parameters prior to the full processing of the ensemble.
	 * This uses duck-typing by checking if the knowledge is contained in the knowledge repository
	 * 
	 * @param in 
	 * 			input parameters
	 * @param inOut 
	 * 			input/output parameters
	 * @param session 
	 * 			current session
	 * @param coordinator 
	 * 			the coordinator id to be checked
	 * @return true 
	 * 			if the coordinator respects the provided method parameters
	 */
	protected Boolean isMembershipCoordinator(List<Parameter> in, List<Parameter> inOut, ISession session, String coordinator){
		Boolean result = false;
		final List<Parameter> coordinatorParameters = new ArrayList<Parameter>();
		coordinatorParameters.addAll(in);
		coordinatorParameters.addAll(inOut);
		//identifiedParameters.addAll(filterParameters(parametersOut, identifier));
		// start session and loop over the identified parametersIn
		ISession localSession;
		if (session == null) {
			localSession = km.createSession();
			localSession.begin();
		} else
			localSession = session;
		try {
			while (localSession.repeat()) {
				// pack all paths from the id perspective into an array
				Integer index = 0;
				while (index < coordinatorParameters.size()){
					String knowledgePath = coordinatorParameters.get(index).kPath.getEvaluatedPath(km, (String)coordinator, null, localSession);
					// check in the knowledge repository if the set of knowledge paths is applicable to the given member id
					try{
						if (!km.containsKnowledge(knowledgePath)){
							break;
						}
					}catch (KMException kme){
					}
					index++;
				}
				// if the member is effectively matching the set of parameters' paths against the identifier
				if (index == coordinatorParameters.size()){
					result = true;
				}
				if (session == null)
					localSession.end();
				else
					break;
			}
			return result;
		} catch (Exception e) {
			Log.e("", e);
			return null;
		}
	}
	
	protected Object[] getParameterMethodValues(List<Parameter> in,
			List<Parameter> inOut, List<Parameter> out, List<SelectorParameter> selectors,
			ISession session, String coordinator, List<List<String>> groups) throws KMException{
		final List<Parameter> parametersIn = new ArrayList<Parameter>();
		// first add all in and inout parameters
		parametersIn.addAll(in);
		parametersIn.addAll(inOut);
		// result instantiation
		Object value;
		Integer size = parametersIn.size()
				+ ((out != null) ? out.size() : 0) ;
		// compute the size of the selectors parameters
		if (selectors != null){
			for (int i = 0; i < selectors.size(); i++){
				SelectorParameter sp = selectors.get(i);
				// if the sp is explicitly defined in the function
				if (sp.index > -1)
					size++;
				// in any case, increase the size by the number of parameters
				size += selectors.get(i).groupIn.size() + selectors.get(i).groupInOut.size()
						+ (selectors.get(i).groupOut != null ? selectors.get(i).groupOut.size() : 0);
			}
		}
		Object[] result = new Object[size];
		// start session and loop over the identified parametersIn
		ISession localSession;
		if (session == null) {
			localSession = km.createSession();
			localSession.begin();
		} else
			localSession = session;
		try {
			while (localSession.repeat()) {
				try{
					// the coordinator
					for (Parameter p : parametersIn) {
						// the change concerns the input array of candidates to the getParameterInstance
						value = getNodeParameterInstance(p, coordinator, coordinator, km, localSession);
						//Log.i(p.kPath.toString());
						result[p.index] = value;
					}
					// the groups
					for (int i = 0; i < selectors.size(); i++){
						SelectorParameter sp = selectors.get(i);
						List<String> group = groups.get(i);
						parametersIn.clear();
						parametersIn.addAll(sp.groupIn);
						parametersIn.addAll(sp.groupInOut);
						// for each input parameter of the selector
						for (Parameter p : parametersIn) {
							// the change concerns the input array of candidates to the getParameterInstance
							value = getMemberGroupParameterInstance(p, group, km, localSession);
							//Log.i(p.kPath.toString());
							result[p.index] = value;
						}
					}
						
				}catch (KMException kme){
					
				}
				if (session == null)
					localSession.end();
				else
					break;
			}
			// out parameters
			final List<Parameter> parametersOut = out;
			for (Parameter p : parametersOut)
				result[p.index] = getParameterInstance(p.type);
			// selectors
			for (int i = 0; i < selectors.size(); i++){
				SelectorParameter sp = selectors.get(i);
				// if the selector is explicitly defined in the method definition (not the case for the knowledge)
				if (sp.index > -1)
					result[sp.index] = getSelectorParameterInstance(sp, groups.get(i).size());
			}
			// return the result
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
						putNodeParameterInstance(p, parameterValues, coordinator, member, km, localSession);
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
	
	protected void putParameterMethodValues(Object[] parameterValues,
			List<Parameter> inOut, List<Parameter> out, List<SelectorParameter> selectors, ISession session,
			String coordinator, List<List<String>> groups) {
		if (parameterValues != null) {
			final List<Parameter> parameters = new ArrayList<Parameter>();
			parameters.addAll(inOut);
			parameters.addAll(out);
			ISession localSession;
			if (session == null) {
				localSession = km.createSession();
				localSession.begin();
			} else
				localSession = session;
			try {
				while (localSession.repeat()) {
					// the coordinator
					for (Parameter p : parameters) {
						putNodeParameterInstance(p, parameterValues, coordinator, coordinator, km, localSession);
					}
					// the groups
					for (int i = 0; i < selectors.size(); i++){
						SelectorParameter sp = selectors.get(i);
						List<String> group = groups.get(i);
						parameters.clear();
						parameters.addAll(sp.groupInOut);
						parameters.addAll(sp.groupOut);
						// for each input parameter of the selector
						for (Parameter p : parameters) {
							putMemberGroupParameterInstance(p, parameterValues, group, km, localSession);
						}
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
	
	/**
	 * put the modified node knowledge into the knowledge repository using the alterKnowledge method of the knowledge manager
	 * @param p 
	 * 			the parameter instance
	 * @param parameterValues
	 * 			the values to be altered
	 * @param coordinator
	 * @param member
	 * @param km
	 * @param session
	 * @throws KMException
	 * @throws Exception
	 */
	private void putNodeParameterInstance(Parameter p, Object[] parameterValues, String coordinator,
			String member, KnowledgeManager km, ISession session) throws KMException, Exception {
		
		Object parameterValue = parameterValues[p.index];
		km.alterKnowledge(
				p.kPath.getEvaluatedPath(km, coordinator,
						member, session),
				p.type.isOutWrapper() ? ((OutWrapper) parameterValue).value
						: parameterValue, session);
	}
	
	/**
	 * 
	 * @param p
	 * @param parameterValues
	 * @param groupMembers
	 * @param km
	 * @param session
	 * @throws KMException
	 * @throws Exception
	 */
	private void putMemberGroupParameterInstance(Parameter p, Object[] parameterValues, List<String> groupMembers, KnowledgeManager km, ISession session) throws KMException, Exception {
		Object parameterValue = parameterValues[p.index];
		Boolean isOutWrapper = p.type.isOutWrapper();
		// the treatment of the parameter can be different regarding the type of the output object (OutWrapper...)
		Object iteratedValue = (isOutWrapper ? ((OutWrapper<?>) parameterValue).value : parameterValue);
		// retrieve the knowledge for each path into an array
		for (int i = 0; i < groupMembers.size(); i++){
			// select the right object value regarding the type of the output object (OutWrapper...)
			Object value = null;
			// list of objects which must be equally distributed over the group members
			if (TypeUtils.isList(iteratedValue.getClass())){
				value = ((List<?>) iteratedValue).get(i);
				// otherwise the object is not contained in any structure and is meant to be processed as a single iteration
			}else{
				//value = iteratedValue;
				throw new Exception("The type parameter associated to the path " + p.kPath.getNaiveEvaluatedPath() + " must be contained in a List");
			}
			// alter the knowledge of coordinator-candidate pairs with the given value (from the list or as a primitive)
			// the coordinator will then collect all the related candidate knowledge in lists
			km.alterKnowledge(
				p.kPath.getEvaluatedPath(km, groupMembers.get(i), session), 
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
	
	private Object getNodeParameterInstance(Parameter p, String coordinator,
			String member, KnowledgeManager km, ISession session)
			throws KMException, Exception {
		OutWrapper ow = null;
		Object objectReturn = null;
		Object objectValue = null;
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
		// retrieve the knowledge for each path into an array
		objectValue = km.getKnowledge(p.kPath.getEvaluatedPath(km, coordinator, member, session), td, session);
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
	
	private Object getMemberGroupParameterInstance(Parameter p,
			List<String> groupMembers, KnowledgeManager km, ISession session)
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
		Boolean isList = td.isList();
		// the type description unit is the type to be retrieved for one iteration on the loop
		// can differ regarding the input type (for instance in an OutWrapper<List<T>>)
		// the treatment for candidates is iterative
		if (isList){
			tdUnit = p.type.getParametricTypeAt(0);
		}else if (td.isMap()){
			// TODO: check out this case from the input data structure
			throw new Exception("No behavior handled for a Map, this part has to be developed");
		}else{
			tdUnit = td;
		}
		Object[] groupKnowledge = new Object[groupMembers.size()];
		// retrieve the knowledge for each path into an array
		for (int i = 0; i < groupMembers.size(); i++){
			groupKnowledge[i] = km.getKnowledge(p.kPath.getEvaluatedPath(km, groupMembers.get(i), session), tdUnit, session);
		}
		// different treatment for the result regarding the knowledge type
		if (td.isList()){
			objectValue = Arrays.asList(groupKnowledge);
		}else {
			throw new Exception("getParameterInstance : Type for this parameter is not supported yet");
		}/*else{
			objectValue = candidatesKnowledge[0];
		}*/
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
	
	/**
	 * Initializes the selector group selection field from a given input size.
	 * @param p
	 * @param groupSize
	 * 			the size of the selection list 
	 * 			typically retrieved from the number of potential members fitting the parameters
	 * @return the group selection field of the selector parameter
	 * @throws KMException
	 * @throws Exception
	 */
	private Object getSelectorParameterInstance(SelectorParameter p, Integer groupSize) throws KMException, Exception{
		if (!p.type.isList() && !p.type.getParametricTypeAt(0).getClass().equals(Boolean.class)){
			throw new Exception("The selector should be a list of booleans.");
		}
		//OutWrapper ow = (OutWrapper) p.type.newInstance();
		// set all booleans to true for initialization
		Boolean[] selectorList = new Boolean[groupSize];
		Arrays.fill(selectorList, true);
		// put the group selection of the selector in the out wrapper
		p.groupSelection = Arrays.asList(selectorList);
		return p.groupSelection;
	}
	
	/**
	 * Defines the member groups according to the selectors and the identified parameters using the getGroupMembers
	 * into lists of ids per group.
	 * @param selectors
	 * @param members
	 * @param session
	 * @return
	 * @throws KMException
	 */
	protected List<List<String>> getMemberGroups(List<SelectorParameter> selectors, Object[] members, ISession session) throws KMException {
		// get all member groups by identifier
		List<List<String>> groups = new ArrayList<List<String>>();
		for (SelectorParameter selector : selectors){
			List<String> group = getGroupMembers(selector, members, session);
			groups.add(group);
		}
		return groups;
	}
	
	/**
	 * retrieves all the ids of the nodes respecting the selector-identified parameters using duck-typing
	 * @param selector
	 * @param members
	 * @param session
	 * @return
	 * @throws KMException
	 */
	private List<String> getGroupMembers(SelectorParameter selector, Object[] members, ISession session) throws KMException{
		List<String> groupMembers = new ArrayList<String> ();
		List<Parameter> parameters = new ArrayList<Parameter>();
		// insert all parameters related to the given selector (grouping the ids under a groupId)
		parameters.addAll(selector.groupIn);
		parameters.addAll(selector.groupInOut);
		parameters.addAll(selector.groupOut);
		// start session and loop over the identified parametersIn
		ISession localSession;
		if (session == null) {
			localSession = km.createSession();
			localSession.begin();
		} else
			localSession = session;
		try {
			while (localSession.repeat()) {
				for (Object member : members){
					// pack all paths from the id perspective into an array
					Integer index = 0;
					while (index < parameters.size()){
						// this part deals with the duck-typing by checking the presence of the knowledge in the knowledge repository via the knowledge manager
						String knowledgePath = parameters.get(index).kPath.getEvaluatedPath(km, (String)member, localSession);
						try{
							if (!km.containsKnowledge(knowledgePath)){
								break;
							}
						}catch (KMException kme){
						}
						index++;
					}
					// if the member is effectively matching the set of parameters' paths against the identifier
					if (index == parameters.size()){
						groupMembers.add((String)member);
					}
				}
				if (session == null)
					localSession.end();
				else
					break;
			}
			return groupMembers;
		} catch (Exception e) {
			Log.e("", e);
			return null;
		}
	}
	
	/**
	 * from the member id groups, take only the member ids which have been preselected by the user
	 * via the membership function.
	 * @param selectors
	 * @param members
	 * @param memberGroups
	 * @return
	 * @throws KMException
	 */
	protected List<List<String>> getSelectedMemberGroups(List<SelectorParameter> selectors, Object[] members, List<List<String>> memberGroups) throws KMException {
		// get all member groups by identifier
		List<List<String>> selectedGroups = new ArrayList<List<String>>();
		for (int i = 0; i < selectors.size(); i++){
			SelectorParameter selector = selectors.get(i);
			List<String> group = new ArrayList<String> ();
			List<Boolean> groupSelection = selector.groupSelection;
			// for each member of the group 
			for (int j = 0; j < memberGroups.get(i).size(); j++){
				// check if selection from the groupSelection array
				if (groupSelection.get(j)){
					// then retain the id in the selected groups
					group.add(memberGroups.get(i).get(j));
				}
			}
			selectedGroups.add(group);
		}
		return selectedGroups;
	}

	public abstract void invoke(String triggererId, ETriggerType recipientMode);
}
