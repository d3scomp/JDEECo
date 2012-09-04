package cz.cuni.mff.d3s.deeco.provider;

import java.util.Arrays;
import java.util.LinkedList;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.processor.ClassFinder;
import cz.cuni.mff.d3s.deeco.processor.ClassProcessor;
import cz.cuni.mff.d3s.deeco.processor.ProcessedHolder;
import cz.cuni.mff.d3s.deeco.processor.UnwrappedProcessedHolder;


public class FileDEECoObjectProvider extends AbstractDEECoObjectProvider {
	
	private final String fileName;
	private final ClassLoader parentClassLoader;
	
	public FileDEECoObjectProvider(String fileName) {
		this.fileName = fileName;
		parentClassLoader = null;
	}
	
	public FileDEECoObjectProvider(String fileName, ClassLoader parentClassLoader) {
		this.fileName = fileName;
		this.parentClassLoader = parentClassLoader;
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
