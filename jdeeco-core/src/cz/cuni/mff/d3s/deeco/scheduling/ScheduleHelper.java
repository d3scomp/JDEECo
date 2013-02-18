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
package cz.cuni.mff.d3s.deeco.scheduling;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoTrigger;
import cz.cuni.mff.d3s.deeco.invokable.AnnotationHelper;
import cz.cuni.mff.d3s.deeco.invokable.Parameter;

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
	public static ProcessSchedule getPeriodicSchedule(
			Annotation scheduleAnnotation) {
		if (scheduleAnnotation instanceof DEECoPeriodicScheduling) {
			return new ProcessPeriodicSchedule(
					((DEECoPeriodicScheduling) scheduleAnnotation).value());
		} else
			return null;
	}

	/**
	 * Retrieves triggered scheduling information (if any) from given function
	 * header constructs.
	 * 
	 * @param pAnnotations Parameter annotations from the function header.
	 * @param in List of input parameters from the function header.
	 * @param inOut List of in/out parameters from the function header.
	 * @return
	 */
	public static ProcessSchedule getTriggeredSchedule(
			Annotation[][] pAnnotations, List<Parameter> in,
			List<Parameter> inOut) {
		List<Integer> triggeredIndecies = AnnotationHelper
				.getAnnotationOuterIndecies(DEECoTrigger.class, pAnnotations);
		if (triggeredIndecies.size() == 0)
			return null;
		else {
			List<Parameter> resultParameters = new ArrayList<Parameter>();
			List<Parameter> jParams = new ArrayList<Parameter>(in);
			jParams.addAll(inOut);
			oLoop: for (Integer index : triggeredIndecies) {
				for (Parameter p : jParams) {
					if (index.equals(p.index)) {
						jParams.remove(p);
						resultParameters.add(p);
						continue oLoop;
					}
				}
			}
			return new ProcessTriggeredSchedule(resultParameters);
		}
	}
}
