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
package cz.cuni.mff.d3s.deeco.concepts.parkinglotbooking;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

/**
 * Sample car-park booking ensemble class.
 * 
 * @author Jaroslav Keznikl
 *
 */

@Ensemble
@PeriodicScheduling(2000)
public class BookingEnsemble {
	
	public final static long serialVersionUID = 1L;

	@Membership
	@PeriodicScheduling(2000)
	public static boolean membership(
			@In("member.targetPosition") Position targetPosition,
			@In("coord.position") Position position) {
		return targetPosition.equals(position);
	}

	@KnowledgeExchange
	public static void map(
			@In("member.request") Request request, 
			@Out("member.response") ParamHolder<Response> response,
			@Out("coord.incomingRequests.[member.request.requestId]") ParamHolder<Request> incomingRequest, 
			@In("coord.processedResponses.[member.request.requestId]") Response processedResponse) {
		incomingRequest.value = request;
		if (processedResponse != null)
			response.value = processedResponse;
	}
}
