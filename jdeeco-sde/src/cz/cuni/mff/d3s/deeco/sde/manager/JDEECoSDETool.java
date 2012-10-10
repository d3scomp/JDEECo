package cz.cuni.mff.d3s.deeco.sde.manager;

import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.provider.FileDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.sde.packager.JDEECoOSGiBundleNameGenerator;
import cz.cuni.mff.d3s.deeco.sde.packager.wizardpackager.JDEECoOSGiWizardPackager;

public class JDEECoSDETool implements IJDEECoSDETool {

	@Override
	public synchronized String start() {

		String result = "Runtime started";
		DEECoManagerService service = DEECoManagerService.getInstance();
		service.openConsole();
		Runtime rt = service.getRuntime();
		if (rt != null) {
			for (AbstractDEECoObjectProvider dop : service.getProviders())
				rt.addDefinitions(dop);
			rt.startRuntime();
		} else
			result = "There is no Runtime";
		return result;
	}

	@Override
	public synchronized String stop() {
		DEECoManagerService service = DEECoManagerService.getInstance();
		Runtime rt = service.getRuntime();
		rt.stopRuntime();
		String result = "Runtime stopped";
		return result;
	}

	@Override
	public String listProcesses() {
		DEECoManagerService service = DEECoManagerService.getInstance();
		List<AbstractDEECoObjectProvider> providers = service.getProviders();
		String result = "Process and Ensembles:\n";
		for (AbstractDEECoObjectProvider dop : providers)
			for (SchedulableProcess sp : dop.getProcesses(null))
				result += sp + "\n";
		return result;
	}

	@Override
	public String listKnowledges() {
		DEECoManagerService service = DEECoManagerService.getInstance();
		List<AbstractDEECoObjectProvider> providers = service.getProviders();
		String result = "Knowledges:\n";
		for (AbstractDEECoObjectProvider dop : providers)
			for (ComponentKnowledge ck : dop.getKnowledges(null))
				result += ck + "\n";
		return result;
	}

	@Override
	public String addDefinitions(String path) {
		DEECoManagerService service = DEECoManagerService.getInstance();
		FileDEECoObjectProvider fdop = new FileDEECoObjectProvider(path,
				service.getThisBundleLoader());
		service.addDEECoPrimitivesProvider(fdop);
		return "Definitions added";
	}

	@Override
	public String getRuntimeInfo() {
		DEECoManagerService service = DEECoManagerService.getInstance();
		Runtime rt = service.getRuntime();
		String result;
		if (rt == null)
			result = "No runtime available.";
		else
			result = rt.toString();
		return result;
	}

//	@Override
//	public String packageToOSGiBundle(List<File> input, String target) {
//		openConsole();
//		JDEECoOSGiSimplePackager packager = new JDEECoOSGiSimplePackager();
//		packager.pack(input, target, JDEECoOSGiBundleNameGenerator.generateBundleName());
//		return "Packaging complete";
//	}

	@Override
	public String packageToOSGiBundle() {
		openConsole();
		JDEECoOSGiWizardPackager packager = new JDEECoOSGiWizardPackager();
		packager.pack(JDEECoOSGiBundleNameGenerator.generateBundleName());
		return "Packaging complete";
	}
	
	private void openConsole() {
		DEECoManagerService service = DEECoManagerService.getInstance();
		service.openConsole();
	}

}
