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

import cz.cuni.mff.d3s.deeco.annotations.ELockingMode;
import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.performance.KnowledgeInfo;
import cz.cuni.mff.d3s.deeco.performance.PeriodicProcessInfo;
import cz.cuni.mff.d3s.deeco.performance.TimeStamp;
import cz.cuni.mff.d3s.deeco.scheduling.ETriggerType;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessPeriodicSchedule;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessSchedule;

/**
 * Class representing a component process.
 * 
 * @author Michal Kit
 * 
 */
public class SchedulableComponentProcess extends SchedulableProcess {

	private static final long serialVersionUID = -8702709624128892523L;

	public final ParameterizedMethod process;
	private final ELockingMode lockingMode;
	private final String componentId;

	private long release = 0;

	public SchedulableComponentProcess(KnowledgeManager km,
			ProcessSchedule scheduling, ParameterizedMethod process,
			ELockingMode lockingMode, String componentId,
			ClassLoader contextClassLoader) {
		super(km, scheduling, contextClassLoader);

		this.process = process;
		this.lockingMode = lockingMode;
		this.componentId = componentId;
		pInfo = new PeriodicProcessInfo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess#invoke()
	 */
	@Override
	public void invoke(String triggererId, ETriggerType recipientMode) {
		// LoggerFactory.getLogger().fine("Component process starts - " +
		// this.toString());
		try {
			if (lockingMode.equals(ELockingMode.STRONG)) {
				ISession session = km.createSession();
				session.begin();
				System.out.println(" session" + session);
				try {
					while (session.repeat()) {
						evaluateMethod(session);
						session.end();
					}

				} catch (KMException e) {
					Log.e("", e);
					session.cancel();
				}
			} else {
				try {
					evaluateMethod();
				} catch (KMException kme) {
					Log.e("SCP message error", kme);
					kme.printStackTrace();
				}
			}
		} catch (Exception e) {
			Log.e("", e);
		}
		// LoggerFactory.getLogger().fine("Component process ends - " +
		// this.toString());
	}

	private void evaluateMethod() throws KMException {
		evaluateMethod(null);
	}

	private void evaluateMethod(ISession session) throws KMException {
		time = new TimeStamp();
		release = System.nanoTime();
		///////////////////////////////////
		Object[] processParameters = getParameterMethodValues(process.in,
				process.inOut, process.out, session);
		time.start = System.nanoTime();
		process.invoke(processParameters);
		putParameterMethodValues(processParameters, process.inOut, process.out,
				session);
		time.finish = System.nanoTime();
		time.release = release;
		scheduling.timestampProcess(this, pInfo, time, process,km);

	}

	public Method getProcessMethod() {
		if (process == null)
			return null;
		return process.getMethod();
	}

	public String getComponentId() {
		return componentId;
	}

	
	public void timestampStart() {
		// TODO Auto-generated method stub
		PeriodicProcessInfo v = (PeriodicProcessInfo)(pInfo);
		ProcessPeriodicSchedule n = (ProcessPeriodicSchedule)(scheduling);
		v.R=(((ProcessPeriodicSchedule)(scheduling)).interval)*1000000000;
		v.startPeriods=System.nanoTime();
//		System.out.println("process :  " + ((PeriodicProcessInfo)(pInfo)).startPeriods);
//		System.out.println(km.getKnowledge(knowledgePath));
	}

	
}
