package cz.cuni.mff.d3s.deeco.manager;

import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

public class DEECoManagerService {

	private List<AbstractDEECoObjectProvider> providers;

	private Runtime rt;
	private KnowledgeManager km;
	
	private static DEECoManagerService instance;
	private ClassLoader thisBundleLoader;
	
	public static DEECoManagerService getInstance() {
		return instance;
	}
	
	protected void activate(ComponentContext context) {
		Bundle b = context.getBundleContext().getBundle();
		thisBundleLoader = b.adapt(BundleWiring.class).getClassLoader();
		System.out.println("JDEECo SDE Tool activated");
    }

	public DEECoManagerService() {
		providers = new LinkedList<AbstractDEECoObjectProvider>();
		instance = this;
	}

	public synchronized void addDEECoPrimitivesProvider(Object dpp) {
		if (dpp != null && dpp instanceof AbstractDEECoObjectProvider) {
			providers.add((AbstractDEECoObjectProvider) dpp);
			System.out.println("Provider added: " + dpp);
		}
	}
	
	public synchronized void removeDEECoPrimitivesProvider(Object dpp) {
		if (dpp != null && dpp instanceof AbstractDEECoObjectProvider) {
			if (providers.contains(dpp)) {
				providers.remove(dpp);
				System.out.println("Provider removed: " + dpp);
			}
		}
	}

	public synchronized void registerRuntime(Object rt) {
		unregisterRuntime(null);
		this.rt = (Runtime) rt;
		System.out.println("Runtime registered");
	}

	public synchronized void unregisterRuntime(Object rt) {
		if (this.rt != null) {
			this.rt.stopRuntime();
			this.rt = null;
			System.out.println("Runtime unregistered");
		}
	}
	
	public synchronized void registerKnowledgeManager(Object km) {
		unregisterKnowledgeManager(null);
		this.km = (KnowledgeManager) km;
		System.out.println("Knowledge manager registered");
	}
	
	public synchronized void unregisterKnowledgeManager(Object km) {
		if (this.km != null) {
			this.km = null;
			System.out.println("Knowledge manager unregistered");
		}
	}
	
	public synchronized KnowledgeManager getKnowledgeManager() {
		return km;
	}

	public List<ComponentKnowledge> getKnowledges() {
		List<ComponentKnowledge> result = new LinkedList<ComponentKnowledge>();
		for (AbstractDEECoObjectProvider adop : providers) {
			adop.setKnowledgeManager(km);
			result.addAll(adop.getKnowledges());
		}
		return result;
	}

	public List<SchedulableProcess> getSchedulableProcesses() {
		List<SchedulableProcess> result = new LinkedList<SchedulableProcess>();
		for (AbstractDEECoObjectProvider adop : providers) {
			adop.setKnowledgeManager(km);
			result.addAll(adop.getProcesses());
		}
		return result;
	}

	public Runtime getRuntime() {
		return rt;
	}

	public ClassLoader getThisBundleLoader() {
		return thisBundleLoader;
	}
}
