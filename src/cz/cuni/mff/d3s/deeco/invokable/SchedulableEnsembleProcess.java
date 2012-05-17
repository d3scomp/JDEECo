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

import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMapper;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMembership;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.exceptions.KMAccessException;
import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.SessionException;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ScheduleHelper;

/**
 * Class representing schedulable ensemble process, which is used by the system
 * to perform either triggered or periodic ensembling.
 * 
 * @author Michal Kit
 * 
 */
public class SchedulableEnsembleProcess extends SchedulableProcess {

	private EnsembleParametrizedMethod mapper;
	private EnsembleParametrizedMethod membership;

	public SchedulableEnsembleProcess(ProcessSchedule scheduling,
			KnowledgeManager km) {
		super(scheduling, km);
	}

	public SchedulableEnsembleProcess(EnsembleParametrizedMethod membership,
			EnsembleParametrizedMethod mapper, ProcessSchedule scheduling,
			KnowledgeManager km) {
		this(scheduling, km);
		this.membership = membership;
		this.mapper = mapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess#invoke()
	 */
	@Override
	protected void invoke() {
		ISession session = km.createSession();
		ISession coordinatorSession = null, memberSession = null;
		try {
			Object[] ids = null;
			while (session.repeat()) {
				session.begin();
				ids = km.findAllProperties(
						ConstantKeys.ROOT_KNOWLEDGE_ID_FIELD, session);
				session.end();
			}
			if (!(session.repeat() || session.hasSucceeded())) {
				return;
			}
			Object[] membershipParams = getParameterList(membership), mapperParams = getParameterList(mapper);
			cloop: for (Object oid : ids) {
				coordinatorSession = km.createSession();
				while (coordinatorSession.repeat()) {
					coordinatorSession.begin();
					try {
						getParameterMethodValues(
								membership.coordinatorIn,
								membership.coordinatorInOut,
								membership.coordinatorOut, (String) oid, membershipParams,
								coordinatorSession);
						getParameterMethodValues(
								mapper.coordinatorIn, mapper.coordinatorInOut,
								mapper.coordinatorOut, (String) oid, mapperParams,
								coordinatorSession);
					} catch (KMException kme) {
						try {
							coordinatorSession.cancel();
						} catch (SessionException se) {
						}
						if (kme instanceof KMAccessException)
							throw (KMAccessException) kme;
						continue cloop;
					}
					mloop: for (Object iid : ids) {
						memberSession = km.createSession();
						while (memberSession.repeat()) {
							memberSession.begin();
							try {
								getParameterMethodValues(
										membership.memberIn,
										membership.memberInOut,
										membership.memberOut, (String) iid, membershipParams,
										memberSession);
								if (evaluateMembership(membershipParams)) {
									getParameterMethodValues(
											mapper.memberIn,
											mapper.memberInOut,
											mapper.memberOut, (String) iid, mapperParams,
											memberSession);
									evaluateMapper(mapperParams);
									putParameterMethodValues(mapperParams, mapper.coordinatorInOut, mapper.coordinatorOut, (String) oid, coordinatorSession);
									putParameterMethodValues(mapperParams, mapper.memberInOut, mapper.memberOut, (String) iid, memberSession);
								}
							} catch (KMException kme) {
								try {
									memberSession.cancel();
								} catch (SessionException se) {
								}
								if (kme instanceof KMAccessException)
									throw (KMAccessException) kme;
								continue mloop;
							}
							memberSession.end();
						}
					}
					coordinatorSession.end();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				if (coordinatorSession != null)
					coordinatorSession.cancel();
				if (memberSession != null)
					memberSession.cancel();
				session.cancel();
			} catch (SessionException se) {
			}
		}
	}

	private boolean evaluateMembership(Object[] params) {
		try {
			return (Boolean) membership.invoke(params);
		} catch (Exception e) {
			System.out.println("Ensemble membership exception! - "
					+ e.getMessage());
			return false;
		}
	}

	private void evaluateMapper(Object[] params) {
		try {
			mapper.invoke(params);
		} catch (Exception e) {
			System.out.println("Ensemble evaluation exception! - "
					+ e.getMessage());
		}
	}
	
	private Object [] getParameterList(EnsembleParametrizedMethod epm) {
		int size = 0;
		if (epm.coordinatorIn != null)
			size += epm.coordinatorIn.size();
		if (epm.coordinatorInOut != null)
			size += epm.coordinatorInOut.size();
		if (epm.coordinatorOut != null)
			size += epm.coordinatorOut.size();
		if (epm.memberIn != null)
			size += epm.memberIn.size();
		if (epm.memberInOut != null)
			size += epm.memberInOut.size();
		if (epm.memberOut != null)
			size += epm.memberOut.size();
		return new Object[size];
	}

	/**
	 * Static function used to extract {@link SchedulableEnsembleProcess}
	 * instance from the class definition
	 * 
	 * @param c
	 *            class to be parsed for extraction
	 * @param km
	 *            {@link KnowledgeManager} instance that is used for knowledge
	 *            repository communication
	 * @return list of {@link SchedulableEnsembleProcess} instances extracted
	 *         from the class definition
	 */
	public static SchedulableEnsembleProcess extractEnsembleProcesses(Class c,
			KnowledgeManager km) {
		SchedulableEnsembleProcess result = null;
		if (c != null) {
			result = new SchedulableEnsembleProcess(
					ScheduleHelper.getSchedule(AnnotationHelper.getAnnotation(
							DEECoPeriodicScheduling.class, c.getAnnotations())),
					km);
			Method method = AnnotationHelper.getAnnotatedMethod(c,
					DEECoEnsembleMembership.class);
			if (method != null)
				result.membership = EnsembleParametrizedMethod
						.extractParametrizedMethod(method);
			else
				return null;
			method = AnnotationHelper.getAnnotatedMethod(c,
					DEECoEnsembleMapper.class);
			if (method != null)
				result.mapper = EnsembleParametrizedMethod
						.extractParametrizedMethod(method);
			else
				return null;
			return result;
		}
		return result;
	}
}
