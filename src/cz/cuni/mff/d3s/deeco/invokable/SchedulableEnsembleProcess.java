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

import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMapper;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMembership;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.exceptions.KMAccessException;
import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.SessionException;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessPeriodicSchedule;
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

	private ParameterizedMethod mapper;
	private ParameterizedMethod membership;

	/**
	 * Returns <code>SchedulableEnsembleProcess</code> instance for specified
	 * scheduling (in <code>scheduling</code>) and knowledge manager (in
	 * <code>km</code>).
	 * 
	 * @param scheduling
	 *            describes the type of the schedulability for the ensemble
	 * @param km
	 *            instance of the knowledge manager that is used for parameter
	 *            retrieval
	 */
	public SchedulableEnsembleProcess(KnowledgeManager km) {
		super(km);
	}

	/**
	 * Returns <code>SchedulableEnsembleProcess</code> instance for specified
	 * membership function (in <code>membership</code>), mapping function (in
	 * <code>mapper</code>), scheduling type (in <code>scheduling</code>) and
	 * knowledge manager (<code>km</code>).
	 * 
	 * @param membership
	 *            method used to evaluate the ensemble condition
	 * @param mapper
	 *            method used to perform data transfer in case of positive
	 *            membership condition evaluation
	 * @param scheduling
	 *            describes the type of the schedulability for the ensemble
	 * @param km
	 *            instance of the knowledge manager that is used for parameter
	 *            retrieval
	 */
	public SchedulableEnsembleProcess(ParameterizedMethod membership,
			ParameterizedMethod mapper, KnowledgeManager km) {
		this(km);
		this.membership = membership;
		this.mapper = mapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess#invoke()
	 */
	@Override
	public void invoke() {
		try {
			ISession session = km.createSession();
			Object[] ids = null;
			session.begin();
			while (session.repeat()) {
				ids = (Object[]) km.getKnowledge(
						ConstantKeys.ROOT_KNOWLEDGE_ID_FIELD, null, session);
				session.end();
			}
			Object[] parameters;
			cloop: for (Object oid : ids) {
				mloop: for (Object iid : ids) {
					session = km.createSession();
					session.begin();
					while (session.repeat()) {
						try {
							parameters = getParameterMethodValues(
									membership.in, membership.inOut,
									membership.out, session, (String) oid,
									(String) iid);
							if (evaluateMembership(parameters)) {
								parameters = getParameterMethodValues(
										mapper.in, mapper.inOut, mapper.out,
										session, (String) oid, (String) iid);
								evaluateMapper(parameters);
								putParameterMethodValues(parameters,
										mapper.inOut, mapper.out, session,
										(String) oid, (String) iid);
							}
						} catch (KMAccessException kme) {
							try {
								session.cancel();
							} catch (SessionException se) {
							}
							continue mloop;
						}
						session.end();
					}
				}
			}
		} catch (Exception kme) {
			return;
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
			ProcessSchedule pSchedule = ScheduleHelper
					.getPeriodicSchedule(AnnotationHelper.getAnnotation(
							DEECoPeriodicScheduling.class, c.getAnnotations()));
			result = new SchedulableEnsembleProcess(km);
			Method method = AnnotationHelper.getAnnotatedMethod(c,
					DEECoEnsembleMembership.class);
			if (method != null) {
				result.membership = ParameterizedMethod
						.extractParametrizedMethod(method);
				if (pSchedule == null) {// not periodic
					pSchedule = ScheduleHelper.getTriggeredSchedule(
							method.getParameterAnnotations(),
							result.membership.in, result.membership.inOut);
					if (pSchedule == null)
						result.scheduling = new ProcessPeriodicSchedule();
					else
						result.scheduling = pSchedule;
				} else
					result.scheduling = pSchedule;
			} else
				return null;
			method = AnnotationHelper.getAnnotatedMethod(c,
					DEECoEnsembleMapper.class);
			if (method != null)
				result.mapper = ParameterizedMethod
						.extractParametrizedMethod(method);
			else
				return null;
			return result;
		}
		return result;
	}
}
