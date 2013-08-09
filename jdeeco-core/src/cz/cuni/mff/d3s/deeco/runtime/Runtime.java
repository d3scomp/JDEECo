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
package cz.cuni.mff.d3s.deeco.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.Component;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.provider.ComponentInstance;
import cz.cuni.mff.d3s.deeco.provider.DEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.jmx.RuntimeMX;
import cz.cuni.mff.d3s.deeco.scheduling.IScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.SchedulerUtils;

/**
 * Class representing jDEECo runtime
 * 
 * @author Michal Kit
 * 
 */

public class Runtime implements IRuntime {

	protected IScheduler scheduler;
	protected KnowledgeManager km;

	private static List<Runtime> runtimes = new LinkedList<Runtime>();

	public Runtime(boolean useMXBeans) {
		if (useMXBeans) {
			RuntimeMX.registerMBeanForRuntime(this);
		}
		runtimes.add(this);
	}
	
	public Runtime() {
		this(false);
	}

	public Runtime(KnowledgeManager km) {
		this(km, false);
	}
	
	public Runtime(KnowledgeManager km, boolean useMXBeans) {
		this(useMXBeans);
		this.km = km;
        this.km.setRuntime(this);
	}

	public Runtime(IScheduler scheduler) {
		this(scheduler, false);
	}
	
	public Runtime(IScheduler scheduler, boolean useMXBeans) {
		this(useMXBeans);
		this.scheduler = scheduler;
	}

	public Runtime(KnowledgeManager km, IScheduler scheduler) {
		this(km, scheduler, false);
	}
	
	public Runtime(KnowledgeManager km, IScheduler scheduler, boolean useMXBeans) {
		this(useMXBeans);
		this.km = km;
        this.km.setRuntime(this);
		this.scheduler = scheduler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.runtime.IRuntime#setScheduler(java.lang.Object)
	 */
	@Override
	public void setScheduler(Object scheduler) {
		unsetScheduler(scheduler);
		if (scheduler instanceof IScheduler) {
			this.scheduler = (IScheduler) scheduler;
                }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.runtime.IRuntime#unsetScheduler(java.lang.Object)
	 */
	@Override
	public void unsetScheduler(Object scheduler) {
		if (this.scheduler != null) {
			this.scheduler.clearAll();
                        this.scheduler = null;
                }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.runtime.IRuntime#setKnowledgeManager(java.lang.
	 * Object)
	 */
	@Override
	public void setKnowledgeManager(Object km) {
		unsetKnowledgeManager(null);
		this.km = (KnowledgeManager) km;
                this.km.setRuntime(this);
	}

	@Override
	public void unsetKnowledgeManager(Object km) {
		stopRuntime();
		this.km = null;
                this.km.unsetRuntime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.runtime.IRuntime#registerComponentsAndEnsembles
	 * (cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider)
	 */
	@Override
	public void registerComponentsAndEnsembles(
			DEECoObjectProvider provider) {
		ClassLoader contextClassLoader = provider.getContextClassLoader();

		List<? extends SchedulableProcess> sp = provider
				.getEnsembles();
		setUpProcesses(sp, km, contextClassLoader);
		addSchedulableProcesses(sp);
		Component initialKnowledge = null;
		for (ComponentInstance ci : provider.getComponentInstances()) {
			try {
				initialKnowledge = provider.getInitialKnowledgeForComponentInstance(ci);
				initComponentKnowledge(initialKnowledge, km);
			} catch (Exception e) {
				Log.e(String.format(
						"Error when initializing knowledge of component %s",
						initialKnowledge.getClass()), e);
				continue;
			}
			List<? extends SchedulableProcess> componentProcesses = ci.getProcesses();
			// the component can have no processes
			if (componentProcesses != null) {
				setUpProcesses(componentProcesses, km, contextClassLoader);
				addSchedulableProcesses(componentProcesses);
			}
		}
	}

	/**
	 * Set-up the processes for runtime execution; i.e., assign them knowledge
	 * manager reference and classloader reference.
	 * 
	 * @param processes
	 *            processes to be set up
	 * @param km
	 *            knowledge manager
	 * @param contextClassLoader
	 *            classloader for the process
	 */
	protected void setUpProcesses(List<? extends SchedulableProcess> processes,
			KnowledgeManager km, ClassLoader contextClassLoader) {
		for (SchedulableProcess p : processes) {
			p.km = km;
			p.contextClassLoader = contextClassLoader;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.runtime.IEnsembleComponentInformer#getComponentsIds
	 * ()
	 */
	@Override
	public List<String> getComponentsIds() {
		List<String> result = new LinkedList<String>();
		try {
			Object[] ids = (Object[]) km
					.getKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID);
			for (Object id : ids)
				result.add((String) id);
		} catch (KMException e) {
			System.err.println("GOTCHA 1");
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.runtime.IRuntime#getComponentKnowledge(java.lang
	 * .String)
	 */
	@Override
	public Object getComponentKnowledge(String componentId) {
		try {
			return km.getKnowledge(componentId);
		} catch (Exception e) {
			System.err.println("GOTCHA 2");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.runtime.IRuntime#getComponentProcesses(java.lang
	 * .String)
	 */
	@Override
	public List<SchedulableComponentProcess> getComponentProcesses(
			String componentId) {
		if (scheduler != null)
			return SchedulerUtils.getComponentProcesses(componentId, scheduler);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.runtime.IRuntime#getEnsembleProcesses()
	 */
	@Override
	public List<SchedulableEnsembleProcess> getEnsembleProcesses() {
		if (scheduler != null)
			return SchedulerUtils.getEnsembleProcesses(scheduler);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.runtime.IRuntime#isRunning()
	 */
	@Override
	public synchronized boolean isRunning() {
		return scheduler.isRunning();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.runtime.IRuntime#startRuntime()
	 */
	@Override
	public synchronized void startRuntime() {
		scheduler.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.runtime.IRuntime#stopRuntime()
	 */
	@Override
	public synchronized void stopRuntime() {
		scheduler.clearAll();
	}

	/**
	 * Registers single schedulable process within the scheduler.
	 * 
	 * @param process
	 *            process that needs to be registered
	 */
	private synchronized void addSchedulableProcess(SchedulableProcess process) {
		if (process != null)
			scheduler.add(process);
	}

	/**
	 * Registers a collection of schedulable processes within the scheduler.
	 * 
	 * @param processes
	 */
	protected synchronized void addSchedulableProcesses(
			List<? extends SchedulableProcess> processes) {
		if (processes != null)
			for (SchedulableProcess sp : processes) {
				addSchedulableProcess(sp);
			}
	}

	/**
	 * Adds component knowledge into the knowledge manager.
	 * 
	 * @param initKnowledge
	 *            component knowledge that needs to be added to the knowledge
	 *            manager.
	 * @param km
	 *            reference to the knowledge manager.
	 * @throws Exception
	 *             in case the knowledge couldn't be initialized, the message
	 *             contains the reason.
	 */
	protected synchronized void initComponentKnowledge(Component initKnowledge,
			KnowledgeManager km) throws Exception {
		if ((initKnowledge == null) || (km == null))
			throw new NullPointerException();
		// check if the component is already in the knowledge manager
		try {
			Object[] currentIds = (Object[]) km
					.getKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID);
			if (Arrays.asList(currentIds).contains(initKnowledge.id))
				throw new Exception(String.format(
						"Knowledge of a component with id '%s' already exists",
						initKnowledge.id));
		} catch (KMNotExistentException kmnee) {
		}
		// a default constructor should exist in any knowledge structure
		for (Field f : initKnowledge.getClass().getFields()){
			if (Knowledge.class.isAssignableFrom(f.getType())){
				try{
					f.getType().getConstructor();
				}catch(Exception e){
					throw new Exception("The knowledge structure " + f.getType().getName() + " shall have a default constructor");
				}
			}	
		}

		km.putKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID, initKnowledge.id);
		km.alterKnowledge(initKnowledge.id, initKnowledge);
	}

	/**
	 * Returns the Runtime singleton object. Works if only one Runtime object
	 * has been created. Otherwise it is not supported.
	 * 
	 * @return The Runtime singleton object.
	 * @throws UnsupportedOperationException
	 *             Thrown when no Runtime or more Runtimes are instantiated.
	 */
	public static IRuntime getDefaultRuntime()
			throws UnsupportedOperationException {
		if (runtimes.size() != 1) {
			throw new UnsupportedOperationException();
		} else {
			return runtimes.get(0);
		}
	}
}
