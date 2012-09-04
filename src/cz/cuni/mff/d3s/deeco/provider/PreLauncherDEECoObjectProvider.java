package cz.cuni.mff.d3s.deeco.provider;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableProcessCreator;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.processor.ParsedObjectReader;

public class PreLauncherDEECoObjectProvider extends AbstractDEECoObjectProvider {

	private ParsedObjectReader por;
	
	private List<SchedulableProcessCreator> rawProcesses;
	
	
	public PreLauncherDEECoObjectProvider(String fileName) {
		por = new ParsedObjectReader(fileName);
		rawProcesses = new LinkedList<SchedulableProcessCreator>();
		knowledges = new LinkedList<ComponentKnowledge>();
		por.read(rawProcesses, knowledges);
	}
	
	public PreLauncherDEECoObjectProvider() {
		this(null);
	}

	@Override
	protected void processKnowledges() {}

	@Override
	protected void processProcesses() {
		if (processes == null) {
			processes = new LinkedList<SchedulableProcess>();
			for (SchedulableProcessCreator spc : rawProcesses) {
				processes.add(spc.extract(km));
			}
		}
	}

}
