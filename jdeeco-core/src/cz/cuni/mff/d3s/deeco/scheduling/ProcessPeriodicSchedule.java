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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.performance.ComponentKnowledgeDecomposer;
import cz.cuni.mff.d3s.deeco.performance.KnowledgeInfo;
import cz.cuni.mff.d3s.deeco.performance.ProcessParametersDecomposer;
import cz.cuni.mff.d3s.deeco.performance.EnsembleKnowledgeDecomposer;
import cz.cuni.mff.d3s.deeco.performance.IPerformanceInfo;
import cz.cuni.mff.d3s.deeco.performance.PeriodicEnsembleInfo;
import cz.cuni.mff.d3s.deeco.performance.PeriodicProcessInfo;
import cz.cuni.mff.d3s.deeco.performance.TimeStamp;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

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
		ProcessParametersDecomposer ppd=new ProcessParametersDecomposer(km,scp,pInfo);
		ppd.inProcess(time, process);
		ppd.outProcess(time, process);
		ppd.inOutProcess(time, process);
	}

	@Override
	public void timestampEnsemble(SchedulableEnsembleProcess sep, IPerformanceInfo pInfo, TimeStamp time, ParameterizedMethod knowledgeExchange, KnowledgeManager km, String coord, String mem) {
		PeriodicEnsembleInfo v = (PeriodicEnsembleInfo)pInfo;
		v.runningPeriodsCoord.add(time);
		v.runningPeriodsMem.add(time);
		EnsembleKnowledgeDecomposer ekd=new EnsembleKnowledgeDecomposer(km,sep,pInfo);
		ekd.inEnsemble(time, knowledgeExchange, coord, mem);
		ekd.outEnsemble(time, knowledgeExchange, coord, mem);
		ekd.inOutEnsemble(time, knowledgeExchange, coord, mem);
		
	}


}

