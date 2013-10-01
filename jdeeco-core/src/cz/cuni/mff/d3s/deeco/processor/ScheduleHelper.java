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
package cz.cuni.mff.d3s.deeco.processor;

import static cz.cuni.mff.d3s.deeco.processor.AnnotationHelper.getAnnotationIndecies;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;
import cz.cuni.mff.d3s.deeco.runtime.model.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.runtime.model.Parameter;
import cz.cuni.mff.d3s.deeco.runtime.model.Schedule;
import cz.cuni.mff.d3s.deeco.runtime.model.Trigger;

/**
 * Helper class for used for scheduling information retrieval.
 * 
 * @author Michal Kit
 * 
 */
public class ScheduleHelper {
	/**
	 * Retrieves periodic scheduling information (if any) from the given
	 * annotation.
	 * 
	 * @param scheduleAnnotation
	 *            An annotation to be checked.
	 * @return Schedule information or null in case of not matching annotation
	 *         class.
	 */
	public static long getPeriod(Annotation scheduleAnnotation) {
		if (scheduleAnnotation instanceof PeriodicScheduling) {
			return ((PeriodicScheduling) scheduleAnnotation).value();
		} else
			return Schedule.NO_PERIOD;
	}

	/**
	 * Retrieves triggered scheduling information (if any) from given function
	 * header constructs.
	 * 
	 * @param pAnnotations
	 *            Parameter annotations from the function header.
	 * @param in
	 *            List of input parameters from the function header.
	 * @param inOut
	 *            List of in/out parameters from the function header.
	 * @return
	 */
	public static List<Trigger> getTriggers(Annotation[][] pAnnotations,
			List<Parameter> parameters) {
		List<Integer> triggeredIndecies = getAnnotationIndecies(
				TriggerOnChange.class, pAnnotations);
		List<Trigger> triggers = new LinkedList<>();
		if (triggeredIndecies.size() != 0) {
			for (Integer index : triggeredIndecies)
				triggers.add(new KnowledgeChangeTrigger(parameters.get(index)
						.getKnowledgePath()));

		}
		return triggers;
	}
}
