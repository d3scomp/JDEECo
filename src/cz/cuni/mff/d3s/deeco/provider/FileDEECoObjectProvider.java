package cz.cuni.mff.d3s.deeco.provider;

import java.util.Arrays;
import java.util.LinkedList;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableProcessCreator;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.processor.ClassFinder;
import cz.cuni.mff.d3s.deeco.processor.ClassProcessor;
import cz.cuni.mff.d3s.deeco.processor.ProcessedHolder;
import cz.cuni.mff.d3s.deeco.processor.UnwrappedProcessedHolder;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.processor.ParsedObjectReader;


public class FileDEECoObjectProvider extends AbstractDEECoObjectProvider {
	
	private final String fileName;
	private final ClassLoader parentClassLoader;
	private List<SchedulableProcessCreator> rawProcesses;
	
	public FileDEECoObjectProvider(KnowledgeManager km, String fileName) {
		super(km);
		
		this.fileName = fileName;
		parentClassLoader = null;
		por = new ParsedObjectReader(fileName);
		rawProcesses = new LinkedList<SchedulableProcessCreator>();
		knowledges = new LinkedList<ComponentKnowledge>();
		por.read(rawProcesses, knowledges);
	}
	
	public FileDEECoObjectProvider(String fileName, ClassLoader parentClassLoader) {
		this.fileName = fileName;
		this.parentClassLoader = parentClassLoader;
	public FileDEECoObjectProvider(KnowledgeManager km) {
		this(km, null);
	}

	@Override
	protected void processKnowledges() {
		processAll();
	}

	@Override
	protected void processProcesses() {
		processAll();
	}
	
	private void processAll() {
		if (fileName == null || fileName.length() == 0) {
			System.out.println("Wrong path");
			return;
		if (processes == null) {
			processes = new LinkedList<SchedulableProcess>();
			for (SchedulableProcessCreator spw : rawProcesses) {
				processes.add(spw.extract(km));
			}
		}
		processes = new LinkedList<SchedulableProcess>();
		knowledges = new LinkedList<ComponentKnowledge>();
		ClassFinder cf = new ClassFinder();
		cf.resolve(new String[]{fileName});
		ProcessedHolder ph = new UnwrappedProcessedHolder();
		ClassProcessor.processClassFiles(cf.getClasses(), cf.getDirURLs(), ph, parentClassLoader);
		processes.addAll(Arrays.asList(ph.getProcesses().toArray(new SchedulableProcess[]{})));
		knowledges.addAll(ph.getKnowledges());
	}

}
