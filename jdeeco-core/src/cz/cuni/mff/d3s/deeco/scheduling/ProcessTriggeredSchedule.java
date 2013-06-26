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

import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.Parameter;
import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.performance.ProcessParametersDecomposer;
import cz.cuni.mff.d3s.deeco.performance.EnsembleKnowledgeDecomposer;
import cz.cuni.mff.d3s.deeco.performance.IPerformanceInfo;
import cz.cuni.mff.d3s.deeco.performance.TimeStamp;
import cz.cuni.mff.d3s.deeco.performance.TriggeredEnsembleInfo;
import cz.cuni.mff.d3s.deeco.performance.TriggeredProcessInfo;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

/**
 * Class representing triggered schedule.
 * 
 * @author Michal Kit
 * 
 */
public class ProcessTriggeredSchedule implements ProcessSchedule {

	private static final long serialVersionUID = 6761694624983409719L;
	
	public List<Parameter> parameters;

	public ProcessTriggeredSchedule(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	@Override
	public void timestampProcess(SchedulableComponentProcess scp, IPerformanceInfo pInfo, TimeStamp time, ParameterizedMethod process,KnowledgeManager km) {
		// TODO Auto-generated method stub
		TriggeredProcessInfo v = (TriggeredProcessInfo)pInfo;
		((TriggeredProcessInfo)pInfo).timeStamps.add(time);
		System.out.println("time stamp process comId:"+this+"  release :"+time.release+" start:"+time.start+"  finish:"+time.finish);
//		ProcessParametersDecomposer ckd=new ProcessParametersDecomposer();
//		ckd.inProcess(scp, pInfo, time, process, km);
//		ckd.outProcess(scp, pInfo, time, process, km);
//		ckd.inOutProcess(scp, pInfo, time, process, km);

	}	

	@Override
	public void timestampEnsemble(SchedulableEnsembleProcess sep, IPerformanceInfo pInfo, TimeStamp time, ParameterizedMethod knowledgeExchange, KnowledgeManager km, String coord, String mem) {
		// TODO Auto-generated method stub
		TriggeredEnsembleInfo v = (TriggeredEnsembleInfo)pInfo;
		v.timeStampsCoord.add(time);
		v.timeStampsMem.add(time);
//		EnsembleKnowledgeDecomposer ekd=new EnsembleKnowledgeDecomposer();
//		ekd.inEnsemble(sep, pInfo, time, knowledgeExchange, km, coord, mem);
//		ekd.outEnsemble(sep, pInfo, time, knowledgeExchange, km, coord, mem);
//		ekd.inOutEnsemble(sep, pInfo, time, knowledgeExchange, km, coord, mem);

	}



}
