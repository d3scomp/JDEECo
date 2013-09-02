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
package cz.cuni.mff.d3s.deeco.runtime.jmx;

import static cz.cuni.mff.d3s.deeco.processor.ComponentParser.extractInitialKnowledge;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import cz.cuni.mff.d3s.deeco.definitions.ComponentDefinition;
import cz.cuni.mff.d3s.deeco.provider.InstanceRuntimeMetadataProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

/**
 * Runtime MXBean implementation.
 * 
 * @see RuntimeMXBean
 * @author Petr Hnetynka
 */
public class RuntimeMX implements RuntimeMXBean {
	private final Runtime rt;

	private RuntimeMX(Runtime rt) {
		this.rt = rt;
	}

	@Override
	public void registerNewComponent(String className) {
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("Component's class not found.");
		}
		if (!ComponentDefinition.class.isAssignableFrom(clazz)) {
			throw new RuntimeException("The \"" + className
					+ "\" does not represent a DEECo component");
		}
		InstanceRuntimeMetadataProvider provider = new InstanceRuntimeMetadataProvider();
		provider.fromComponentInstance(extractInitialKnowledge(clazz));
		rt.deployRuntimeMetadata(provider.getRuntimeMetadata());
	}

	/**
	 * Registers the runtime as a MXBean.
	 * 
	 * @param rt
	 *            runtime
	 */
	public static void registerMBeanForRuntime(Runtime rt) {
		try {
			RuntimeMX rmx = new RuntimeMX(rt);
			ObjectName on = new ObjectName(
					"cz.cuni.mff.d3s.deeco:type=Runtime,name=" + rt.hashCode());
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			mbs.registerMBean(rmx, on);
		} catch (MalformedObjectNameException ex) {
			// This should not happen
			throw new RuntimeException(
					"Runtime MX bean could ne be registered", ex);
		} catch (InstanceAlreadyExistsException ex) {
			throw new RuntimeException("Runtime MX bean already registered", ex);
		} catch (MBeanRegistrationException ex) {
			throw new RuntimeException(
					"Runtime MX bean could ne be registered", ex);
		} catch (NotCompliantMBeanException ex) {
			// this should not happen
			throw new RuntimeException(
					"Runtime MX bean could ne be registered", ex);
		}

	}

	@Override
	public boolean isRunning() {
		return rt.isRunning();
	}

	@Override
	public void startRuntime() {
		rt.run();
	}

	@Override
	public void stopRuntime() {
		rt.shutdown();
	}

}
