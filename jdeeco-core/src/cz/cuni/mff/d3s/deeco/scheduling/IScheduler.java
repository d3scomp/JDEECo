/*******************************************************************************
 * Copyright 2012-2013 Charles University in Prague
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

import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.TriggeredSchedulableProcess;

public interface IScheduler {
	
	public boolean isRunning();

	public void add(List<? extends SchedulableProcess> processes);

	public void add(SchedulableProcess process);

	public void remove(List<SchedulableProcess> processes);

	public void remove(SchedulableProcess process);
	
	public List<TriggeredSchedulableProcess> getTriggeredProcesses();
	
	public List<SchedulableProcess> getPeriodicProcesses();
	
	public long getTime();
	
	public void clearAll();

	public void start();

	public void stop();
}
