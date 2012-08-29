package cz.cuni.mff.d3s.deeco.provider;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableProcessCreator;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.processor.ParsedObjectReader;

public class FileDEECoObjectProvider extends AbstractDEECoObjectProvider {

	private ParsedObjectReader por;
	
	private List<SchedulableProcessCreator> rawProcesses;
	
	
	public FileDEECoObjectProvider(KnowledgeManager km, String fileName) {
		super(km);
		
		por = new ParsedObjectReader(fileName);
		rawProcesses = new LinkedList<SchedulableProcessCreator>();
		knowledges = new LinkedList<ComponentKnowledge>();
		por.read(rawProcesses, knowledges);
	}
	
	public FileDEECoObjectProvider(KnowledgeManager km) {
		this(km, null);
	}

	@Override
	protected void processKnowledges() {}

	@Override
	protected void processProcesses() {
		if (processes == null) {
			processes = new LinkedList<SchedulableProcess>();
			for (SchedulableProcessCreator spw : rawProcesses) {
				processes.add(spw.extract(km));
			}
		}
	}

}
