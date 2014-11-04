package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;

/**
 * A class providing reflective capabilities to a component process. 
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class EnsembleContext {
	private static ThreadLocal<EnsembleContext> context = new ThreadLocal<>();

	private CurrentTimeProvider currentTimeProvider;
	
	private EnsembleContext(CurrentTimeProvider currentTimeProvider) {
		this.currentTimeProvider = currentTimeProvider;
	}
	
	static void addContext(CurrentTimeProvider currentTimeProvider) {
		context.set(new EnsembleContext(currentTimeProvider));		
	}	
	
	public static CurrentTimeProvider getTimeProvider() {
		if (context.get() != null)
			return context.get().currentTimeProvider;
		else 
			return null;
	}
}
