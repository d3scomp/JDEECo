package cz.cuni.mff.d3s.deeco.sde.manager;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;

import cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.IRuntime;
import cz.cuni.mff.d3s.deeco.sde.console.ConsolePrinter;
import cz.cuni.mff.d3s.deeco.sde.utils.AbstractProvidersHolder;

public class JDEECoToolService {

	private AbstractProvidersHolder providersHolder;

	private IRuntime rt;
	private ConsolePrinter cp;

	private static JDEECoToolService instance;
	private ClassLoader thisBundleLoader;

	public synchronized static JDEECoToolService getInstance() {
		if (instance == null)
			instance = new JDEECoToolService();
		return instance;
	}
	
	public JDEECoToolService() {
		synchronized (JDEECoToolService.class) {
			if (instance == null)
				instance = this;
			providersHolder = new AbstractProvidersHolder();
		}
	}

	protected void activate(ComponentContext context) {
		Bundle b = context.getBundleContext().getBundle();
		thisBundleLoader = b.adapt(BundleWiring.class).getClassLoader();
		System.out.println("JDEECo SDE Tool activated");
	}

	public synchronized void addDEECoPrimitivesProvider(Object dpp) {
		if (dpp != null && dpp instanceof AbstractDEECoObjectProvider) {
			AbstractDEECoObjectProvider adop = (AbstractDEECoObjectProvider) dpp;
			providersHolder.add(adop);
			System.out.println("Provider added: " + adop);
		}
	}

	public synchronized void removeDEECoPrimitivesProvider(Object dpp) {
		if (dpp != null && dpp instanceof AbstractDEECoObjectProvider) {
			AbstractDEECoObjectProvider adop = (AbstractDEECoObjectProvider) dpp;
			if (providersHolder.contains(adop)) {
				providersHolder.remove(adop);
				System.out.println("Provider removed: " + adop);
			}
		}
	}

	public synchronized void registerRuntime(Object rt) {
		unregisterRuntime(null);
		this.rt = (IRuntime) rt;
		System.out.println("Runtime registered");
	}

	public synchronized void unregisterRuntime(Object rt) {
		if (this.rt != null) {
			this.rt.stopRuntime();
			this.rt = null;
			System.out.println("Runtime unregistered");
		}
	}

	public AbstractProvidersHolder getProvidersHolder() {
		return providersHolder;
	}

	public IRuntime getRuntime() {
		return rt;
	}

	public ClassLoader getThisBundleLoader() {
		return thisBundleLoader;
	}

	public void openConsole() {
		if (cp == null)
			cp = new ConsolePrinter();
		System.setErr(cp.openConsoleWindow());
		System.setOut(cp.openConsoleWindow());
	}
}
