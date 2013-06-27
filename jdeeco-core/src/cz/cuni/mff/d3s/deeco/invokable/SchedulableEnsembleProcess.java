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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.runtime.AbstractMiddlewareEntry;
import cz.cuni.mff.d3s.deeco.runtime.RandomNetworkDistanceMiddlewareEntry;
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
		}
	}

	private void periodicInvocation(Object[] rootIds) throws KMException {
		for (Object oid : rootIds) {
			singleInvocation((String) oid, ETriggerType.COORDINATOR, rootIds);
		}
	}

	// TODO : to rewrite in order of different ETriggerType
	private void singleInvocation(String outerId, ETriggerType recipientMode,
			Object[] rootIds) throws KMException {
		ISession session = null;
		try {
			/* for a boolean membership, we test on coordinator-member pairs
			 * the validity of the association
			 */
			String cId = null;
			if (membership.getClass().isAssignableFrom(BooleanMembership.class)){
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
							if (evaluateBooleanMembership(parametersMembership)) {
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
			/* in case of an integer membership, we focus on the group of ids
			 * and find a subset based on the metric set by the user
			 */
			}else if (membership.getClass().isAssignableFrom(CandidateMembership.class)){
				/* the coordinator must be the only triggerer for the candidate membership
				 */
				if (recipientMode.equals(ETriggerType.COORDINATOR)) {
					cId = outerId;
					// retrieve the metric and size from the Membership annotation
					Membership aMembership = membership.method.getMethod().getAnnotation(Membership.class);
					int size = aMembership.candidateRange();
					// session creation and start
					session = km.createSession();
					session.begin();
					while (session.repeat()) {
						try {
							// the middleware entry provides us all the possible node-to-node distances from its distance matrix
							RandomNetworkDistanceMiddlewareEntry middlewareEntry = RandomNetworkDistanceMiddlewareEntry.getMiddlewareEntry();
							// get the sorted node distance pairs using the middleware entry (= distance provider)
							// by definition a node is either a member or a candidate !
							List<IntegerNodeDistancePair> sortedNodeDistancePairs = getSortedNodeDistancePairs(cId, rootIds, middlewareEntry, session);
							Object[] candidateIds = pickCandidateIds(sortedNodeDistancePairs, rootIds, size);
							// inject the parameters into a local object array
							Object[] parametersMembership = getParameterMethodValues(
									membership.getIn(), membership.getInOut(),
									membership.getOut(), session, (String) cId,
									(String[]) candidateIds);
							String candidateId = evaluateCandidateMembership(parametersMembership);
							// we handle the obtained candidateId as if it were a member id in the condition case
							// TODO: what if the candidate id is empty ?
							if (candidateId != null) { // && !candidateId.isEmpty()) {
								Object[] parametersKnowledgeExchange = getParameterMethodValues(
										knowledgeExchange.in,
										knowledgeExchange.inOut,
										knowledgeExchange.out, session,
										(String) cId, (String) candidateId);
								evaluateKnowledgeExchange(parametersKnowledgeExchange);
								putParameterMethodValues(
										parametersKnowledgeExchange,
										knowledgeExchange.inOut,
										knowledgeExchange.out, session,
										(String) cId, (String) candidateId);
							}
						} catch (KMNotExistentException kmnee) {
							session.cancel();
						}
						session.end();
					}
				}else{
					// ignore member/candidate triggering
				}
			}
		} catch (KMException kme) {
			if (session != null)
				session.cancel();
			throw kme;
		}
	}
	
	/**
	 * 
	 * @author Julien Malvot
	 *
	 */
	private class IntegerNodeDistancePair implements Comparable<IntegerNodeDistancePair> {
		private Integer distance;
		private Integer cIdx;
		
		public IntegerNodeDistancePair(Integer cIdx, Integer distance) {
			this.cIdx = cIdx;
			this.distance = distance;
		}

		public Integer getcIdx() {
			return cIdx;
		}
		
		@Override
		public int compareTo(IntegerNodeDistancePair integerNodeDistancePair) {
			// TODO: how about the case distance = otherdistance ? shall the node be substituted by the new one ?
			return (distance <= integerNodeDistancePair.distance ? -1 : 1);
		}
	};
	
	private List<IntegerNodeDistancePair> getSortedNodeDistancePairs(String cId, Object[] rootIds, AbstractMiddlewareEntry<Integer> middlewareEntry, ISession session) {
		List<IntegerNodeDistancePair> sortedNodeDistancePairs = new ArrayList<IntegerNodeDistancePair> (rootIds.length);
		// add all rootIds with their metric value
		int cIdIndex = Arrays.binarySearch(rootIds, (Object)cId);
		for (int rIdIndex = 0; rIdIndex < rootIds.length; rIdIndex++){
			if (rIdIndex != cIdIndex){
				sortedNodeDistancePairs.add(new IntegerNodeDistancePair(rIdIndex, middlewareEntry.getDistance(cIdIndex, rIdIndex)));
			}else{
				sortedNodeDistancePairs.add(new IntegerNodeDistancePair(cIdIndex, Integer.MAX_VALUE));
			}
		}
		// sort the list
		Collections.sort(sortedNodeDistancePairs);
		return sortedNodeDistancePairs;
	}
	
	private Object[] pickCandidateIds(List<IntegerNodeDistancePair> sortedNodeDistancePairs, Object[] rootIds, int size) {
		String[] cdIds = new String[size];
		// we only retrieve the first candidates with an index lower than the size
		for (int i = 0; i < size; i++)
			cdIds[i] = (String) rootIds[sortedNodeDistancePairs.get(i).getcIdx()];
		return cdIds;
	}

	private Boolean evaluateBooleanMembership(Object[] params) {
		try {
			return (Boolean) membership.membership(params);
		} catch (Exception e) {
			Log.e("Ensemble exception while boolean membership evaluation", e);
			return Boolean.FALSE;
		}
	}
	
	private String evaluateCandidateMembership(Object[] params) {
		try {
			return (String) membership.membership(params);
		} catch (Exception e) {
			Log.e("Ensemble exception while candidate membership evaluation", e);
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
