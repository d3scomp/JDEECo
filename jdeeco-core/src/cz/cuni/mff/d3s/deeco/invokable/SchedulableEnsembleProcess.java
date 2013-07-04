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
import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.exceptions.EnsembleProcessInvocationException;
import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.invokable.types.IdListType;
import cz.cuni.mff.d3s.deeco.invokable.types.IdType;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.middleware.LinkedMiddlewareEntry;
import cz.cuni.mff.d3s.deeco.runtime.middleware.MiddlewareLink;
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
	public final MembershipMethod membership;

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
			ProcessSchedule scheduling, MembershipMethod membership,
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
			singleInvocation((String) oid, ETriggerType.COORDINATOR, rootIds);
		}
	}

	// TODO : to rewrite in order of different ETriggerType
	private void singleInvocation(String outerId, ETriggerType recipientMode,
			Object[] rootIds) throws KMException, EnsembleProcessInvocationException {
		ISession session = null;
		try {
			Class<?> membershipReturnType = membership.method.getMethod().getReturnType();
			/* for a boolean membership, we test on coordinator-member pairs
			 * the validity of the association
			 */
			String cId = null;
			if (membershipReturnType.equals(Boolean.class) || membershipReturnType.equals(boolean.class)){
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
									membership.getOut(), session, (String) cId,
									(String) mId);
							// if the membership returns a positive test
							if ((Boolean)evaluateMembership(parametersMembership)) {
								// in case of outputs in the parameters of the membership (flags ...)
								putParameterMethodValues(parametersMembership,
										membership.getInOut(),
										membership.getOut(), session,
										(String) cId, (String) mId);
								// handling the knowledge exchange (get-evaluate-put)
								Object[] parametersKnowledgeExchange = getParameterMethodValues(
										knowledgeExchange.in,
										knowledgeExchange.inOut,
										knowledgeExchange.out, session,
										(String) cId, (String) mId);
								evaluateKnowledgeExchange(parametersKnowledgeExchange);
								putParameterMethodValues(
										parametersKnowledgeExchange,
										knowledgeExchange.inOut,
										knowledgeExchange.out, session,
										(String) cId, (String) mId);
							}
						} catch (KMNotExistentException kmnee) {
							session.cancel();
							continue mloop;
						}
						session.end();
					}
				}
			/* we have different cases according to the object return type
			 * and find a subset based on the distances provided by the middleware
			 */
			}else if (membershipReturnType.equals(IdType.class) || membershipReturnType.equals(IdListType.class)){
				// the coordinator must be the only triggerer for the candidate membership
				// as the candidates are selected in its processing
				if (recipientMode.equals(ETriggerType.COORDINATOR)) {
					cId = outerId;
					// retrieve the range (from the Membership annotation) of the "neighborhood" (candidates) to be selected 
					Membership aMembership = membership.method.getMethod().getAnnotation(Membership.class);
					int range = aMembership.candidateRange();
					// session creation and start
					session = km.createSession();
					session.begin();
					while (session.repeat()) {
						try {
							// the middleware entry provides us all the possible node-to-node distances from its distance matrix
							LinkedMiddlewareEntry middlewareEntry = LinkedMiddlewareEntry.getMiddlewareEntrySingleton();
							// get the sorted node distance pairs using the middleware entry (= distance provider)
							// by definition a node is either a member or a candidate !
							String[] candidateIds = null;
							List<String> candidateIdsList = null;
							try {
								candidateIdsList = (middlewareEntry.getBestDestinationsFromSource(cId, range));
							} catch (Exception e) {
								throw new EnsembleProcessInvocationException(e.getMessage());
							}
							if (candidateIdsList != null && !candidateIdsList.isEmpty()){
								candidateIds = candidateIdsList.toArray(new String[candidateIdsList.size()]);
							
								// inject the parameters into a local object array
								Object[] parametersMembership = getParameterMethodValues(
										membership.getIn(), membership.getInOut(),
										membership.getOut(), session, (String) cId,
										(String[]) candidateIds);
								// evaluate the membership which returns the best candidate(s)
								Object returnObject = evaluateMembership(parametersMembership);
								// the candidateId must be defined by the membership, otherwise no best candidate exists
								if (returnObject != null) {
									// if the returned object is a candidate id
									if (membershipReturnType.equals(IdType.class)) {
										String bestCandidateId = ((IdType) returnObject).id;
										// in case of outputs in the parameters of the membership (flags ...)
										putParameterMethodValues(parametersMembership,
												membership.getInOut(),
												membership.getOut(), session,
												(String) cId, (String) bestCandidateId);
										// handle the knowledge exchange
										Object[] parametersKnowledgeExchange = getParameterMethodValues(
												knowledgeExchange.in,
												knowledgeExchange.inOut,
												knowledgeExchange.out, session,
												(String) cId, bestCandidateId);
										evaluateKnowledgeExchange(parametersKnowledgeExchange);
										putParameterMethodValues(
												parametersKnowledgeExchange,
												knowledgeExchange.inOut,
												knowledgeExchange.out, session,
												(String) cId, bestCandidateId);
									// if the returned object is a list of candidate ids
									// the knowledge is exchanged between the coordinator and all candidates
									}else if (membershipReturnType.equals(IdListType.class)){
										List<String> bestCandidateIdsList = ((IdListType) returnObject).idList;
										String[] bestCandidateIds = bestCandidateIdsList.toArray(new String[bestCandidateIdsList.size()]);
										// in case of outputs in the parameters of the membership (flags ...)
										putParameterMethodValues(parametersMembership,
												membership.getInOut(),
												membership.getOut(), session,
												(String) cId, bestCandidateIds);
										// handle the knowledge exchange
										Object[] parametersKnowledgeExchange = getParameterMethodValues(
												knowledgeExchange.in,
												knowledgeExchange.inOut,
												knowledgeExchange.out, session,
												(String) cId, bestCandidateIds);
										evaluateKnowledgeExchange(parametersKnowledgeExchange);
										putParameterMethodValues(
												parametersKnowledgeExchange,
												knowledgeExchange.inOut,
												knowledgeExchange.out, session,
												(String) cId, bestCandidateIds);
									}else{
										throw new EnsembleProcessInvocationException("The return type of the membership function is not supported");
									} 						
								}
							}
						} catch (KMNotExistentException kmnee) {
							session.cancel();
						}
						session.end();
					}
				}else{
					// ignore member/candidate triggering
				}
			}else {
				throw new EnsembleProcessInvocationException("The return type of the membership function is not supported");
			}
		} catch (KMException kme) {
			if (session != null)
				session.cancel();
			throw kme;
		} catch (EnsembleProcessInvocationException e) {
			if (session != null)
				session.cancel();
			throw e;
		}
	}

	// for the membership call
	private Object evaluateMembership(Object[] params) {
		try {
			return membership.membership(params);
		} catch (Exception e) {
			Log.e("Ensemble exception while generic membership evaluation", e);
			return null;
		}
	}

	private void evaluateKnowledgeExchange(Object[] params) {
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
