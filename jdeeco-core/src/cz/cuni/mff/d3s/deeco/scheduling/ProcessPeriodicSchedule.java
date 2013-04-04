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

import cz.cuni.mff.d3s.deeco.performance.IPerformanceInfo;
import cz.cuni.mff.d3s.deeco.performance.SchedulableProcessTimeStampsWithActionsVisitor;
import cz.cuni.mff.d3s.deeco.performance.TimeStamp;

/**
 * Class representing periodic scheduling.
 * 
 * @author Michal Kit
 * 
 */
public class ProcessPeriodicSchedule implements ProcessSchedule {

	private static final long serialVersionUID = -2588113311351580211L;
	/**
	 * Period interval in ms.
	 */
	public long interval;

	public ProcessPeriodicSchedule() {
		this.interval = 1000;
	}

	public ProcessPeriodicSchedule(long interval) {
		this.interval = interval;
	}

	@Override
	public void acceptProcess(
			SchedulableProcessTimeStampsWithActionsVisitor sv,
			IPerformanceInfo pInfo, TimeStamp time) {
		// TODO Auto-generated method stub
		sv.visitProcess(this, pInfo, time);
	}

	@Override
	public void acceptEnsemble(
			SchedulableProcessTimeStampsWithActionsVisitor sv,
			IPerformanceInfo pInfo, TimeStamp time) {
		// TODO Auto-generated method stub
		sv.visitEnsemble(this, pInfo, time);
	}

	
	


}
