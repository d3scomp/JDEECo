package cz.cuni.mff.d3s.jdeeco.adaptation;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;

/**
 * A class providing reflective capabilities to monitors.
 * TODO use EnsembleController instead of EnsembleDefinition when ready
 */
public class MonitorContext {

	/** Thread singleton. */
	private static ThreadLocal<MonitorContext> context = new ThreadLocal<>();

	/**
	 * Returns currently monitored component.
	 * @return currently monitored component
	 */
	static public ComponentInstance getMonitoredComponent() {
		final MonitorContext c = context.get();
		if (c != null) {
			return c.monitoredComponent;
		} else {
			return null;
		}
	}

	/**
	 * Sets currently monitored component.
	 * @param monitoredComponent new monitored component
	 */
	static void setMonitoredComponent(final ComponentInstance monitoredComponent) {
		MonitorContext c = context.get();
		if (c == null) {
			c = new MonitorContext();
			context.set(c);
		}
		c.monitoredComponent = monitoredComponent;
	}

	/**
	 * Returns currently monitored ensemble.
	 * @return currently monitored ensemble
	 */
	static public EnsembleDefinition getMonitoredEnsemble() {
		final MonitorContext c = context.get();
		if (c != null) {
			return c.moniteredEnsemble;
		} else {
			return null;
		}
	}

	/**
	 * Sets currently monitored ensemble.
	 * @param monitoredEnsemble new monitored ensemble
	 */
	static void setMonitoredEnsemble(final EnsembleDefinition monitoredEnseble) {
		MonitorContext c = context.get();
		if (c == null) {
			c = new MonitorContext();
			context.set(c);
		}
		c.moniteredEnsemble= monitoredEnseble;
	}

	/** Monitored component. */
	private ComponentInstance monitoredComponent;

	/** Monitored ensemble. */
	private EnsembleDefinition moniteredEnsemble;
}
