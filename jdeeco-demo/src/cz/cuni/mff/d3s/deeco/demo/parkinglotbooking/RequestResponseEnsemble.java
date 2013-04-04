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
package cz.cuni.mff.d3s.deeco.demo.parkinglotbooking;

import java.util.Map;
import java.util.UUID;

import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

/**
 * base request-response ensemble class.
 * 
 * @author Jaroslav Keznikl
 *
 */


public class RequestResponseEnsemble extends Ensemble {

	public final static long serialVersionUID = 1L;
	
	// must be public, static and extend Knowledge
	public static class RequesterInterface extends Knowledge {
		
		public final static long serialVersionUID = 1L;
		
		public Request request;
		public Response response;
	}

	public static class ResponderInterface extends Knowledge {
		
		public final static long serialVersionUID = 1L;
		
		public Map<UUID, Request> incomingRequests;
		public Map<UUID, Response> processedResponses;
	}

	@KnowledgeExchange
	@PeriodicScheduling(2000)
	public static void map(
			@In("member.request") Request request, 
			@Out("member.response") OutWrapper<Response> response,
			@Out("coord.incomingRequests[member.request.requestId]") OutWrapper<Request> incomingRequest, 
			@In("coord.processedResponses[member.request.requestId]") Response processedResponse) {
		incomingRequest.value = request;
		if (processedResponse != null)
			response.value = processedResponse;
	}

	
}
