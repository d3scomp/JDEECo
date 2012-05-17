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
package cz.cuni.mff.d3s.deeco.demo;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.TSKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

public class Launcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Class [] classes = {RobotLeaderComponent.class, RobotFollowerComponent.class};
		Class [] ensembles = {ConvoyEnsemble.class};
		KnowledgeManager km = new RepositoryKnowledgeManager(new TSKnowledgeRepository());
		Runtime runtime = new Runtime(classes, ensembles, km);
	}

}
