/*******************************************************************************
 * Copyright 2013 Charles University in Prague
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
package cz.cuni.mff.d3s.deeco.demo.cloud;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.InstanceRuntimeMetadataProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.RealTimeScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * 
 * @author Petr Hnetynka
 */
public class LocalLauncherDynamicCloudNoJPF {

	public static void main(String[] args) {
		Scheduler scheduler = new RealTimeScheduler();
		KnowledgeManager km = new RepositoryKnowledgeManager(
				new LocalKnowledgeRepository(scheduler));
		InstanceRuntimeMetadataProvider provider = new InstanceRuntimeMetadataProvider();
		provider.fromComponentInstance(new NodeA());
		provider.fromComponentInstance(new NodeC());
		provider.fromEnsembleDefinition(MigrationEnsemble.class);
		Runtime rt = new Runtime(scheduler, km, true);
		rt.deployRuntimeMetadata(provider.getRuntimeMetadata());
		rt.run();
	}
}
