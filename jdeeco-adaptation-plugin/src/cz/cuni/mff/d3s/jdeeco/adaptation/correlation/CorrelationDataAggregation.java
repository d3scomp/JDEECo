/*******************************************************************************
 * Copyright 2015 Charles University in Prague
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *******************************************************************************/
package cz.cuni.mff.d3s.jdeeco.adaptation.correlation;

import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.metaadaptation.correlation.CorrelationMetadataWrapper;

/**
 * Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
@Ensemble
@PeriodicScheduling(period = 1000)
public class CorrelationDataAggregation {

	@Membership
	public static boolean membership(@In("coord.id") String coordId,
			@In("coord.knowledgeOfAllComponents")
				Map<String, Map<String, CorrelationMetadataWrapper<? extends Object>>> knowledgefAllComponents,
			@In("member.*") Object... fields) {
		
		return true;
	}

	@KnowledgeExchange
	public static void map(@In("member.id") String memberId,
			@InOut("coord.knowledgeOfAllComponents") 
				ParamHolder<Map<String, Map<String, CorrelationMetadataWrapper<? extends Object>>>> knowledgeOfAllComponents,
			@In("member.*") Object... fields)
				throws KnowledgeNotFoundException {
		System.out.println("@KnowledgeExchange " + memberId);
		
		Map<String, CorrelationMetadataWrapper<?>> memberKnowledge = 
				knowledgeOfAllComponents.value.get(memberId);
		if (memberKnowledge == null) {
			memberKnowledge = new HashMap<>();
			knowledgeOfAllComponents.value.put(memberId, memberKnowledge);
		}

		for (Object o : fields) {
			// ignore fields that are not specified as CorrelationMetadataWrapper instances
			if (o instanceof CorrelationMetadataWrapper) {
				CorrelationMetadataWrapper<?> field = (CorrelationMetadataWrapper<?>) o;
				memberKnowledge.put(field.getName(), field);
			}
		}
	}
}