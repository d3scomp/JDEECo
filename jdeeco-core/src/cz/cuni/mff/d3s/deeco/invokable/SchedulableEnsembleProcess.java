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

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.ETriggerType;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;

/**
 * Class representing schedulable ensemble process, which is used by the system
 * to perform either triggered or periodic ensembling.
 * 
 * @author Michal Kit
 * 
 */
public class SchedulableEnsembleProcess extends SchedulableProcess {

	private static final long serialVersionUID = -726573275082252987L;

	public final ParameterizedMethod mapper;
	public final Membership membership;

	/**
	 * Returns <code>SchedulableEnsembleProcess</code> instance for specified
	 * membership function (in <code>membership</code>), mapping function (in
	 * <code>mapper</code>), scheduling type (in <code>scheduling</code>) and
	 * knowledge manager (<code>km</code>).
	 * 
	 * @param scheduling
	 *            describes the type of the schedulability for the ensemble
	 * @param membership
	 *            method used to evaluate the ensemble condition
	 * @param mapper
	 *            method used to perform data transfer in case of positive
	 *            membership condition evaluation
	 * @param km
	 *            instance of the knowledge manager that is used for parameter
	 *            retrieval
	 */
	public SchedulableEnsembleProcess(KnowledgeManager km, ProcessSchedule scheduling, Membership membership,
			ParameterizedMethod mapper, ClassLoader contextClassLoader) {
		super(km, scheduling, contextClassLoader);
		
		this.membership = membership;
		this.mapper = mapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess#invoke()
	 */
	@Override
	public void invoke(String triggererId, ETriggerType recipientMode) {
		// System.out.println("Ensembling starts");
		try {
			Object[] ids = (Object[]) km
					.getKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID);
			if (recipientMode == null)
				periodicInvocation(ids);
			else
				try {
					singleInvocation(triggererId, recipientMode, ids);
				} catch (KMException kme) {
				}
			// System.out.println("Ensembling ends");
		} catch (KMException kme) {
			return;
		}
	}

	private void periodicInvocation(Object[] rootIds) throws KMException {
		for (Object oid : rootIds) {
			singleInvocation((String) oid, ETriggerType.COORDINATOR, rootIds);
		}
	}

	// to rewrite in order of different ETriggerType
	private void singleInvocation(String outerId, ETriggerType recipientMode,
			Object[] rootIds) throws KMException {
		ISession session = null;
		try {
			String cId = null, mId = null;
			if (recipientMode.equals(ETriggerType.COORDINATOR)) {
				cId = outerId;
			} else {
				mId = outerId;
			}
			mloop: for (Object iid : rootIds) {
				if (recipientMode.equals(ETriggerType.COORDINATOR)) {
					mId = (String) iid;
				} else {
					cId = (String) iid;
				}
				session = km.createSession();
				session.begin();
				while (session.repeat()) {
					try {
						ParametersPair[] parametersMembership = getParameterMethodValues(
								membership.getIn(), membership.getInOut(),
								membership.getOut(), session,
								(String) cId, (String) mId);
						if (evaluateMembership(parametersMembership)) {
							ParametersPair[] parametersMapper = getParameterMethodValues(mapper.in,
									mapper.inOut, mapper.out, session,
									(String) cId, (String) mId);
							evaluateMapper(parametersMapper);
							putParameterMethodValues(parametersMapper, mapper.inOut,
									mapper.out, session, (String) cId,
									(String) mId);
						}
					} catch (KMNotExistentException kmnee) {
						session.cancel();
						continue mloop;
					}
					session.end();
				}
			}
		} catch (KMException kme) {
			if (session != null)
				session.cancel();
			throw kme;
		}
	}

	private boolean evaluateMembership(ParametersPair[] params) {
		try {
			Object[] parameterValues = ParametersPair.extractValues(params);
			return membership.membership(parameterValues);
		} catch (Exception e) {
			System.out.println("Ensemble membership exception! - "
					+ e.getMessage());
			return false;
		}
	}

	private void evaluateMapper(ParametersPair[] params) {
		try {
			Object[] parameterValues = ParametersPair.extractValues(params);
			mapper.invoke(parameterValues);
		} catch (Exception e) {
			System.out.println("Ensemble evaluation exception! - "
					+ e.getMessage());
		}
	}
	
	public Method getMapperMethod() {
		if (mapper == null)
			return null;
		return mapper.method;
	}
	
	public Method getMembershipMethod() {
		if (membership == null || membership.method == null)
			return null;
		return membership.method.method;
	}

}
