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
import cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.provider.ClassDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Petr Hnetynka
 */
public class LocalLauncherDynamicCloudNoJPF {
        public static void main(String[] args) {
		List<Class<?>> components = Arrays.asList(new Class<?>[] { NodeA.class, NodeC.class });
		List<Class<?>> ensembles = Arrays.asList(new Class<?>[] { MigrationEnsemble.class });
		KnowledgeManager km = new RepositoryKnowledgeManager(new LocalKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler();
		AbstractDEECoObjectProvider dop = new ClassDEECoObjectProvider(components, ensembles);
		cz.cuni.mff.d3s.deeco.runtime.Runtime rt = new cz.cuni.mff.d3s.deeco.runtime.Runtime(km, scheduler, true);
		rt.registerComponentsAndEnsembles(dop);

		rt.startRuntime();
	}
}
