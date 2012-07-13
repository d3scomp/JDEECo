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
import cz.cuni.mff.d3s.deeco.annotations.DEECoStrongLocking;
import cz.cuni.mff.d3s.deeco.annotations.ELockingMode;
import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.SessionException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessPeriodicSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ScheduleHelper;

/**
 * Class representing a component process.
 * 
 * @author Michal Kit
 * 
 */
public class SchedulableKnowledgeProcess extends SchedulableProcess {

	private ParameterizedMethod process;
	private ELockingMode lockingMode;

	public SchedulableKnowledgeProcess(ParameterizedMethod process,
			ELockingMode lockingMode, KnowledgeManager km) {
		super(km);
		this.process = process;
		this.lockingMode = lockingMode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess#invoke()
	 */
	@Override
	public void invoke() {
		//System.out.println("Component process starts - " + this.toString());
		try {
			if (lockingMode.equals(ELockingMode.STRONG)) {
				ISession session = km.createSession();
				session.begin();
				try {
					while (session.repeat()) {
						evaluateMethod(session);
						session.end();
					}
				} catch (KMException e) {
					System.out.println(e.getMessage());
					session.cancel();
				}
			} else {
				try {
					evaluateMethod();
				} catch (KMException kme) {
					System.out.println("SKP message - " + kme.getMessage());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		//System.out.println("Component process ends - " + this.toString());
	}

	private void evaluateMethod() throws KMException {
		evaluateMethod(null);
	}

	private void evaluateMethod(ISession session) throws KMException {
		Object[] processParameters = getParameterMethodValues(process.in,
				process.inOut, process.out, session);
		process.invoke(processParameters);
		putParameterMethodValues(processParameters, process.inOut, process.out,
				session);
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
			ParameterizedMethod currentMethod;
			ProcessSchedule ps;
			SchedulableKnowledgeProcess skp;
			ELockingMode lm;
			if (methods != null && methods.size() > 0) {
				for (Method m : methods) {
					currentMethod = ParameterizedMethod
							.extractParametrizedMethod(m, root);
					if (currentMethod != null) {
						ps = ScheduleHelper
								.getPeriodicSchedule(AnnotationHelper
										.getAnnotation(
												DEECoPeriodicScheduling.class,
												m.getAnnotations()));
						if (ps == null) {// not periodic
							ps = ScheduleHelper.getTriggeredSchedule(
									m.getParameterAnnotations(),
									currentMethod.in, currentMethod.inOut);
							if (ps == null)
								ps = new ProcessPeriodicSchedule();
						}
						if (AnnotationHelper.getAnnotation(
								DEECoStrongLocking.class, m.getAnnotations()) == null)
							lm = (ps instanceof ProcessPeriodicSchedule) ? ELockingMode.WEAK
									: ELockingMode.STRONG;
						else
							lm = ELockingMode.STRONG;
						skp = new SchedulableKnowledgeProcess(currentMethod,
								lm, km);
						skp.scheduling = ps;
						result.add(skp);
					}
				}
			}
		}
		return result;
	}
}
