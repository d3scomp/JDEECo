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
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcess;
import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMIllegalArgumentException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ScheduleHelper;

/**
 * Class representing a component process.
 * 
 * @author Michal Kit
 * 
 */
public class SchedulableKnowledgeProcess extends SchedulableProcess {

	private ProcessParametrizedMethod process;

	public SchedulableKnowledgeProcess(ProcessParametrizedMethod process,
			ProcessSchedule scheduling, KnowledgeManager km) {
		super(scheduling, km);
		this.process = process;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess#invoke()
	 */
	@Override
	protected void invoke() {
		try {
			Object[] processParameters = getParameterMethodValues(process.in,
					process.inOut, process.out, process.root);
			process.invoke(processParameters);
			putParameterMethodValues(processParameters, process.inOut,
					process.out, process.root);
		} catch (KMException kme) {
			System.out.println(kme.getMessage());
		}
	}

	/**
	 * Static function used to extract {@link SchedulableKnowledgeProcess}
	 * instance from the class definition
	 * 
	 * @param c
	 *            class to be parsed for extraction
	 * @param root
	 *            component id for which process executes
	 * @param km
	 *            {@link KnowledgeManager} instance that is used for knowledge
	 *            repository communication
	 * @return list of {@link SchedulableKnowledgeProcess} instances extracted
	 *         from the class definition
	 */
	public static List<SchedulableKnowledgeProcess> extractKnowledgeProcesses(
			Class c, String root, KnowledgeManager km) {
		List<SchedulableKnowledgeProcess> result = null;
		if (c != null) {
			result = new ArrayList<SchedulableKnowledgeProcess>();
			List<Method> methods = AnnotationHelper.getAnnotatedMethods(c,
					DEECoProcess.class);
			ProcessParametrizedMethod currentMethod;
			if (methods != null && methods.size() > 0) {
				for (Method m : methods) {
					currentMethod = ProcessParametrizedMethod
							.extractParametrizedMethod(m, root);
					if (currentMethod != null) {
						result.add(new SchedulableKnowledgeProcess(
								currentMethod,
								ScheduleHelper.getSchedule(AnnotationHelper
										.getAnnotation(
												DEECoPeriodicScheduling.class,
												m.getAnnotations())), km));
					}
				}
			}
		}
		return result;
	}
}
