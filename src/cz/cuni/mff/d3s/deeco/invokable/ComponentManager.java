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
package cz.cuni.mff.d3s.deeco.invokable;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import cz.cuni.mff.d3s.deeco.annotations.DEECoInitialize;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/**
 * Class providing component managing functionalities.
 * 
 * @author Michal Kit
 * 
 */
public class ComponentManager extends
		InvokableManager<SchedulableComponentProcess> {

	public ComponentManager(KnowledgeManager km, Scheduler scheduler) {
		super(km, scheduler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.invokable.InvokableManager#addInvokable(java.lang
	 * .Class)
	 */
	@Override
	public void addInvokable(Class invokableDefinition) {
		if (invokableDefinition != null) {
			ComponentKnowledge ik = getInitialKnowledge(invokableDefinition);
			if (ik != null) {
				if (writeRootKnowledge(ik)) {
					List<SchedulableComponentProcess> invokables = SchedulableComponentProcess
							.extractKnowledgeProcesses(invokableDefinition,
									ik.id, km);
					processes.addAll(invokables);
					scheduler.register(invokables);
				}
			}
		}
	}

	private ComponentKnowledge getInitialKnowledge(Class invokableDefinition) {
		ComponentKnowledge rk = null;
		try {
			Method init = getInitMethod(invokableDefinition);
			if (init != null) {
				rk = (ComponentKnowledge) init.invoke(null, new Object[] {});
				if (rk != null) {
					if (rk.id == null || rk.id.equals(""))
						rk.id = UUID.randomUUID().toString();
				}
			}
		} catch (Exception e) {
		}
		return rk;
	}
	
	/**
	 * Retrieves init method from the <code>ComponentKnowledge</code> class.
	 * 
	 * @param c
	 *            class to be parsed
	 * @return init method or null in case no matching found
	 */
	private Method getInitMethod(Class c) {
		List<Method> result = AnnotationHelper.getAnnotatedMethods(c,
				DEECoInitialize.class);
		if (result.size() == 1) {
			return result.get(0);
		}
		return null;
	}

	private boolean writeRootKnowledge(ComponentKnowledge rootKnowledge) {
		if (rootKnowledge != null) {
			try {
				km.putKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID,
						rootKnowledge.id, null, null, false);
				km.putKnowledge(rootKnowledge.id, rootKnowledge,
						rootKnowledge.getClass());
				return true;
			} catch (Exception e) {
				System.out.println("Error when writing root knowledge: "
						+ e.getMessage());
			}
		}
		return false;
	}
}
