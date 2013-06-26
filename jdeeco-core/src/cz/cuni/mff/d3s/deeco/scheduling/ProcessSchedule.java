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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.performance.IPerformanceInfo;
import cz.cuni.mff.d3s.deeco.performance.KnowledgeInfo;
import cz.cuni.mff.d3s.deeco.performance.TimeStamp;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

/**
 * Interface serving as an ancestor for different ways of scheduling.
 * 
 * @author Michal Kit
 * 
 */
public interface ProcessSchedule extends Serializable {
	
	public void timestampProcess(SchedulableComponentProcess scp, IPerformanceInfo pInfo, TimeStamp time, ParameterizedMethod process, KnowledgeManager km);
	public void timestampEnsemble(SchedulableEnsembleProcess sep, IPerformanceInfo pInfo, TimeStamp time, ParameterizedMethod knowledgeExchange, KnowledgeManager km, String coord, String mem);

}
