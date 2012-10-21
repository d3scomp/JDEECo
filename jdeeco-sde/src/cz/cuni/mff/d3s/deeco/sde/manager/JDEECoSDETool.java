package cz.cuni.mff.d3s.deeco.sde.manager;

import java.io.File;
import java.util.List;

import cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.provider.FileDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.IEnsembleComponentInformer;
import cz.cuni.mff.d3s.deeco.runtime.IRuntime;
import cz.cuni.mff.d3s.deeco.sde.packager.JDEECoOSGiBundleNameGenerator;
import cz.cuni.mff.d3s.deeco.sde.packager.wizardpackager.JDEECoOSGiWizardPackager;
import cz.cuni.mff.d3s.deeco.sde.utils.AbstractProvidersHolder;
import cz.cuni.mff.d3s.deeco.sde.utils.Printer;

public class JDEECoSDETool implements IJDEECoSDETool {

	@Override
	public synchronized String start() {

		String result = "Runtime started";
		JDEECoToolService service = JDEECoToolService.getInstance();
		service.openConsole();
		IRuntime rt = service.getRuntime();
		if (rt != null) {
			AbstractProvidersHolder aph = service.getProvidersHolder();
			for (AbstractDEECoObjectProvider dop : aph.getProviders())
				rt.registerComponentsAndEnsembles(dop);
			rt.startRuntime();
		} else
			result = "There is no Runtime";
		return result;
	}

	@Override
	public synchronized String stop() {
		JDEECoToolService service = JDEECoToolService.getInstance();
		IRuntime rt = service.getRuntime();
		rt.stopRuntime();
		String result = "Runtime stopped";
		return result;
	}

	@Override
	public String addDefinitions(String path) {
		JDEECoToolService service = JDEECoToolService.getInstance();
		FileDEECoObjectProvider fdop = new FileDEECoObjectProvider(path,
				service.getThisBundleLoader());
		service.addDEECoPrimitivesProvider(fdop);
		return "Definitions added";
	}

	@Override
	public String getRuntimeInfo() {
		JDEECoToolService service = JDEECoToolService.getInstance();
		IRuntime rt = service.getRuntime();
		String result;
		if (rt == null)
			result = "No runtime available.";
		else
			result = rt.toString();
		return result;
	}

	// @Override
	// public String packageToOSGiBundle(List<File> input, String target) {
	// openConsole();
	// JDEECoOSGiSimplePackager packager = new JDEECoOSGiSimplePackager();
	// packager.pack(input, target,
	// JDEECoOSGiBundleNameGenerator.generateBundleName());
	// return "Packaging complete";
	// }

	@Override
	public String packageToOSGiBundle() {
		openConsole();
		JDEECoOSGiWizardPackager packager = new JDEECoOSGiWizardPackager();
		packager.pack(JDEECoOSGiBundleNameGenerator.generateBundleName());
		return "Packaging complete";
	}

	private void openConsole() {
		JDEECoToolService service = JDEECoToolService.getInstance();
		service.openConsole();
	}

	@Override
	public String getComponentInfo(String componentId) {
		JDEECoToolService service = JDEECoToolService.getInstance();
		IEnsembleComponentInformer infoProvider = service.getRuntime();
		Object ck = infoProvider.getComponentKnowledge(componentId);
		if (ck != null) {
			return "Registered:\n\n"
					+ Printer.printComponentInfo(
							infoProvider.getComponentProcesses(componentId),
							infoProvider.getComponentKnowledge(componentId));
		}
		infoProvider = service.getProvidersHolder();
		ck = infoProvider.getComponentKnowledge(componentId);
		if (ck != null) {
			return "Unregegistered:\n\n"
					+ Printer.printComponentInfo(
							infoProvider.getComponentProcesses(componentId),
							infoProvider.getComponentKnowledge(componentId));
		}
		return "Component " + componentId + " not found.";
	}

	@Override
	public String listAllKnowledge() {
		JDEECoToolService service = JDEECoToolService.getInstance();
		String result = "Runtime Knowledge:\n\n";
		IEnsembleComponentInformer infoProvider = service.getRuntime();
		List<String> rComponentsIds = infoProvider.getComponentsIds();
		result += Printer.printKnowledgesInfo(infoProvider, rComponentsIds);
		infoProvider = service.getProvidersHolder();
		result += "\n\nUnregistered Knowledge:\n\n";
		List<String> pComponentsIds = infoProvider.getComponentsIds();
		if (pComponentsIds != null) {
			pComponentsIds.removeAll(rComponentsIds);
			result += Printer.printKnowledgesInfo(infoProvider, pComponentsIds);
		}
		return result;
	}

	@Override
	public String listAllComponents() {
		JDEECoToolService service = JDEECoToolService.getInstance();
		String result = "Runtime Components:\n\n";
		IEnsembleComponentInformer infoProvider = service.getRuntime();
		List<String> rComponentsIds = infoProvider.getComponentsIds();
		result += Printer.printComponentsInfo(infoProvider, rComponentsIds);
		infoProvider = service.getProvidersHolder();
		result += "\n\nUnregistered Components:\n\n";
		List<String> pComponentsIds = infoProvider.getComponentsIds();
		if (pComponentsIds != null) {
			pComponentsIds.removeAll(rComponentsIds);
			result += Printer.printComponentsInfo(infoProvider, pComponentsIds);
		}
		return result;
	}

	@Override
	public String listAllEnsembles() {
		JDEECoToolService service = JDEECoToolService.getInstance();
		String result = "Runtime Ensembles:\n\n";
		IEnsembleComponentInformer infoProvider = service.getRuntime();
		result += Printer.printEnsembles(infoProvider.getEnsembleProcesses());
		result += "\n\nUnregistered Ensembles:\n\n";
		infoProvider = service.getProvidersHolder();
		result += Printer.printEnsembles(infoProvider.getEnsembleProcesses());
		return result;
	}

	@Override
	public String validateInJPF(List<File> jars) {
		// TODO Auto-generated method stub
		return "Not implemented yet.";
	}
}
