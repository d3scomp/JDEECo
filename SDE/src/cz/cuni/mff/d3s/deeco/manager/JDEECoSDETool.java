package cz.cuni.mff.d3s.deeco.manager;

import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.provider.FileDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;

public class JDEECoSDETool implements IJDEECoSDETool {

	@Override
	public synchronized String start() {

		String result = "Runtime started";
		DEECoManagerService service = DEECoManagerService.getInstance();
		Runtime rt = service.getRuntime();
		if (rt != null) {
			rt.addKnowledges(service.getKnowledges(), service.getKnowledgeManager());
			rt.addSchedulablePorcesses(service.getSchedulableProcesses());
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
		List<SchedulableProcess> processes = service.getSchedulableProcesses();
		String result = "Processes: " + processes.size() + "\n";
		for (SchedulableProcess sp : processes)
			result += sp + "\n";
		return result;
	}

	@Override
	public String listKnowledges() {
		DEECoManagerService service = DEECoManagerService.getInstance();
		List<ComponentKnowledge> components = service.getKnowledges();
		String result = "Component Knowledges: " + components.size() + "\n";
		for (ComponentKnowledge ck : components)
			result += ck.getClass().toString() + "\n";
		return result;
	}

	@Override
	public void addDefinitions(String path) {
		DEECoManagerService service = DEECoManagerService.getInstance();
		FileDEECoObjectProvider fdop = new FileDEECoObjectProvider(path,
				service.getThisBundleLoader());
		service.addDEECoPrimitivesProvider(fdop);
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

}
