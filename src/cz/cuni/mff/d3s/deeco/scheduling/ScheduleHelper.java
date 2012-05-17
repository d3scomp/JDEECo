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

import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoTriggeredScheduling;

public class ScheduleHelper {
	public static ProcessSchedule getSchedule(Annotation scheduleAnnotation) {
		if (scheduleAnnotation == null) {
			return new ProcessPeriodicSchedule();
		} else if (scheduleAnnotation instanceof DEECoPeriodicScheduling) {
			return new ProcessPeriodicSchedule(
					((DEECoPeriodicScheduling) scheduleAnnotation).value());
		} else if (scheduleAnnotation instanceof DEECoTriggeredScheduling) {
			return new ProcessTriggeredSchedule();
		} else
			return null;
	}
}
