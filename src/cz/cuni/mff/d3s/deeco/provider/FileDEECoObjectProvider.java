package cz.cuni.mff.d3s.deeco.provider;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.processor.ParsedObjectReader;
import cz.cuni.mff.d3s.deeco.processor.SchedulableProcessWrapper;

public class FileDEECoObjectProvider extends AbstractDEECoObjectProvider {

	private ParsedObjectReader por;
	
	private List<SchedulableProcessWrapper> rawProcesses;
	
	
	public FileDEECoObjectProvider(String fileName) {
		por = new ParsedObjectReader(fileName);
		rawProcesses = new LinkedList<SchedulableProcessWrapper>();
		knowledges = new LinkedList<ComponentKnowledge>();
		por.read(rawProcesses, knowledges);
	}
	
	public FileDEECoObjectProvider() {
		this(null);
	}

	@Override
	protected void processKnowledges() {}

	@Override
	protected void processProcesses() {
		if (processes == null) {
			processes = new LinkedList<SchedulableProcess>();
			for (SchedulableProcessWrapper spw : rawProcesses) {
				processes.add(spw.extract());
			}
		}
	}

}
