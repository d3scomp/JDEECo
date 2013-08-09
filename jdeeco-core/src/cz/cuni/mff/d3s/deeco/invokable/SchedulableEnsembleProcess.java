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

import cz.cuni.mff.d3s.deeco.exceptions.EnsembleProcessInvocationException;
import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.invokable.memberships.AbstractMembershipMethod;
import cz.cuni.mff.d3s.deeco.invokable.memberships.MemberMembershipMethod;
import cz.cuni.mff.d3s.deeco.invokable.memberships.MembersMembershipMethod;
import cz.cuni.mff.d3s.deeco.invokable.parameters.Parameter;
import cz.cuni.mff.d3s.deeco.invokable.parameters.SelectorParameter;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
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

	public final ParameterizedMethod knowledgeExchange;
	public final AbstractMembershipMethod membership;

	/**
	 * Returns <code>SchedulableEnsembleProcess</code> instance for specified
	 * membership function (in <code>membership</code>), mapping function (in
	 * <code>knowledgeExchange</code>), scheduling type (in
	 * <code>scheduling</code>) and knowledge manager (<code>km</code>).
	 * 
	 * @param scheduling
	 *            describes the type of the schedulability for the ensemble
	 * @param membership
	 *            method used to evaluate the ensemble condition
	 * @param knowledgeExchange
	 *            method used to perform data transfer in case of positive
	 *            membership condition evaluation
	 * @param km
	 *            instance of the knowledge manager that is used for parameter
	 *            retrieval
	 */
	public SchedulableEnsembleProcess(KnowledgeManager km,
			ProcessSchedule scheduling, AbstractMembershipMethod membership,
			ParameterizedMethod knowledgeExchange,
			ClassLoader contextClassLoader) {
		super(km, scheduling, contextClassLoader);

		this.membership = membership;
		this.knowledgeExchange = knowledgeExchange;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess#invoke()
	 */
	@Override
	public void invoke(String triggererId, ETriggerType recipientMode) {
		// LoggerFactory.getLogger().fine("Ensembling starts");

		SchedulableProcess.runtime.set(km.getRuntime());

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
			// LoggerFactory.getLogger().fine("Ensembling ends");
		} catch (KMException kme) {
			return;
		} catch (EnsembleProcessInvocationException e) {
			return;
		}
	}

	private void periodicInvocation(Object[] rootIds) throws KMException, EnsembleProcessInvocationException {
		for (Object oid : rootIds) {
			singleInvocation((String)oid, ETriggerType.COORDINATOR, rootIds);
		}
	}

	// TODO : to rewrite in order of different ETriggerType
	private void singleInvocation(String outerId, ETriggerType recipientMode,
			Object[] rootIds) throws KMException, EnsembleProcessInvocationException {
		ISession session = null;
		try {
			// these two types (the return type of the membership and the type of membership) 
			// define the case we are running !
			Class<?> membershipType = membership.getClass();
			Class<?> membershipReturnType = membership.method.getMethod().getReturnType();
			String cId = null;
			// for a member-based membership method, we test on coordinator-member pairs the validity of the association
			if (MemberMembershipMethod.class.equals(membershipType) &&
					(Boolean.class.equals(membershipReturnType) || boolean.class.equals(membershipReturnType))){
				String mId = null;
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
							Object[] parametersMembership = getParameterMethodValues(
									membership.getIn(), membership.getInOut(),
									membership.getOut(), session, cId, mId);
							// if the membership returns a positive test
							if ((Boolean)evaluateMembership(parametersMembership)) {
								Object[] parametersKnowledgeExchange = getParameterMethodValues(
										knowledgeExchange.in, knowledgeExchange.inOut,
										knowledgeExchange.out, session, cId, mId);
								evaluateKnowledgeExchange(parametersKnowledgeExchange);
								putParameterMethodValues(parametersKnowledgeExchange,
										knowledgeExchange.inOut, knowledgeExchange.out, session,
										cId, mId);
							}
						} catch (KMNotExistentException kmnee) {
							session.cancel();
							continue mloop;
						}
						session.end();
					} 
				}
			// one coordinator and multi-member groups 
			}else if (MembersMembershipMethod.class.equals(membershipType) &&
					Boolean.class.isAssignableFrom(membershipReturnType)){
				// the coordinator is the only trigger type
				if (recipientMode.equals(ETriggerType.COORDINATOR)) {
					cId = outerId;
					session = km.createSession();
					session.begin();
					while (session.repeat()) {
						try {
							MembersMembershipMethod mms = (MembersMembershipMethod) membership;
							// if the coordinator is acceptable for the membership by prior duck-typing
							if (isMembershipCoordinator(mms.method.in, mms.method.inOut, session, cId)){
								// retrieved the different member groups by ids from the selectors and rootIds within the session
								List<List<String>> memberGroups = getMemberGroups(mms.method.selectors, rootIds, session);
								// retrieve the method parameters into a local object array
								Object[] parametersMembership = getParameterMethodValues(mms.method.in, mms.method.inOut, mms.method.out, mms.method.selectors, 
										session, cId, memberGroups);
								// evaluate the membership
								if ((Boolean) evaluateMembership(parametersMembership)) {
									// get only the selected members from the user-modified selectors
									memberGroups = getSelectedMemberGroups(mms.method.selectors, rootIds, memberGroups);
									// knowledge exchange
									ParameterizedSelectorMethod psm = (ParameterizedSelectorMethod) knowledgeExchange;
									// populate first the selectors of the knowledge exchange which (compared to the membership) are implicit
									//populateKnowledgeExchangeSelectors(psm.selectors)
									Object[] parametersKnowledgeExchange = getParameterMethodValues(
											psm.in, psm.inOut, psm.out, psm.selectors, session, cId, memberGroups);
									evaluateKnowledgeExchange(parametersKnowledgeExchange);
									// put the method values from the knowledge exchange into the knowledge repository
									putParameterMethodValues(parametersKnowledgeExchange,
											psm.inOut, psm.out, psm.selectors, session,
											cId, memberGroups);
								}
							}
						} catch (KMNotExistentException kmnee) {
						}
						// if there is an unexistency (or not) from the node, we do not cancel AND end the session
						// but we just abort the execution for this node
						session.end();
					}
					//}
				}else{
					throw new EnsembleProcessInvocationException("The return type of the membership function is not supported");
				}
			}
		} catch (KMException kme) {
			if (session != null)
				session.cancel();
			throw kme;
		}
	}

	// for the membership call
	protected Boolean evaluateMembership(Object[] params) {
		try {
			return membership.membership(params);
		} catch (Exception e) {
			Log.e("Ensemble exception while generic membership evaluation", e);
			return null;
		}
	}

	protected void evaluateKnowledgeExchange(Object[] params) {
		try {
			knowledgeExchange.invoke(params);
		} catch (Exception e) {
			Log.e("Ensemble exception while knowledge exchange", e);
		}
	}

	public Method getKnowledgeExchangeMethod() {
		if (knowledgeExchange == null)
			return null;
		return knowledgeExchange.getMethod();
	}

	public Method getMembershipMethod() {
		if (membership == null || membership.method == null)
			return null;
		return membership.method.getMethod();
	}

}
