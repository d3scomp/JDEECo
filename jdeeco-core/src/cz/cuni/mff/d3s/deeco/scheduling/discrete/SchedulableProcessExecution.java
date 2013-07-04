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
package cz.cuni.mff.d3s.deeco.scheduling.discrete;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcessWrapper;

/**
 * Holds a process to be invoked (<code>SchedulableProcess</code>) together with
 * its invocation time (<code>execTime</code>) and a randomly generated seed to
 * distinguish between processes to be executed at the same time. <br>
 * To be used in discrete event simulations.
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class SchedulableProcessExecution implements
		Comparable<SchedulableProcessExecution> {

	private SchedulableProcessWrapper processWrapper;
	private long execTime; // in usec
	private int seed; // should be randomly generated

	public SchedulableProcessExecution(SchedulableProcessWrapper process,
			long execTime, int seed) {
		this.processWrapper = process;
		this.execTime = execTime;
		this.seed = seed;
	}

	public SchedulableProcessWrapper getProcessWrapper() {
		return processWrapper;
	}

	public void setProcess(SchedulableProcessWrapper process) {
		this.processWrapper = process;
	}

	public long getExecTime() {
		return execTime;
	}

	public void setExecTime(long execTime) {
		this.execTime = execTime;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	@Override
	public int compareTo(SchedulableProcessExecution p) {
		return (int) ((this.execTime + this.seed) - (p.execTime + p.seed));
	}
}
