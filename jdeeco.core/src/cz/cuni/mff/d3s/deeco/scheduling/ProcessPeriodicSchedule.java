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

import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.performance.ComponentKnowledgeDecomoser;
import cz.cuni.mff.d3s.deeco.performance.EnsembleKnowledgeDecomposer;
import cz.cuni.mff.d3s.deeco.performance.IPerformanceInfo;
import cz.cuni.mff.d3s.deeco.performance.PeriodicEnsembleInfo;
import cz.cuni.mff.d3s.deeco.performance.PeriodicProcessInfo;
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
	public void timestampProcess(SchedulableComponentProcess scp, IPerformanceInfo pInfo, TimeStamp time, ParameterizedMethod process, KnowledgeManager km) {
		PeriodicProcessInfo v = (PeriodicProcessInfo)pInfo;
		((PeriodicProcessInfo)pInfo).runningPeriods.add(time);
		ComponentKnowledgeDecomoser ckd=new ComponentKnowledgeDecomoser();
		ckd.inProcess(scp, pInfo, time, process, km);
		ckd.outProcess(scp, pInfo, time, process, km);
		ckd.inOutProcess(scp, pInfo, time, process, km);

	}

	@Override
	public void timestampEnsemble(SchedulableEnsembleProcess sep, IPerformanceInfo pInfo, TimeStamp time, ParameterizedMethod knowledgeExchange, KnowledgeManager km, String coord, String mem) {
		PeriodicEnsembleInfo v = (PeriodicEnsembleInfo)pInfo;
		v.runningPeriodsCoord.add(time);
		v.runningPeriodsMem.add(time);
		EnsembleKnowledgeDecomposer ekd=new EnsembleKnowledgeDecomposer();
		ekd.inEnsemble(sep, pInfo, time, knowledgeExchange, km, coord, mem);
		ekd.outEnsemble(sep, pInfo, time, knowledgeExchange, km, coord, mem);
		ekd.inOutEnsemble(sep, pInfo, time, knowledgeExchange, km, coord, mem);
	}


}

