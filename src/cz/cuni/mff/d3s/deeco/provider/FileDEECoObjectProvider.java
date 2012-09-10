package cz.cuni.mff.d3s.deeco.provider;

import java.util.LinkedList;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.processor.ClassFinder;
import cz.cuni.mff.d3s.deeco.processor.ClassProcessor;

public class FileDEECoObjectProvider extends AbstractDEECoObjectProvider {

	private final String fileName;
	private final ClassLoader parentClassLoader;

	public FileDEECoObjectProvider(String fileName) {
		this(fileName, null);
	}

	public FileDEECoObjectProvider(String fileName,
			ClassLoader parentClassLoader) {
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
		cf.resolve(new String[] { fileName });
		AbstractDEECoObjectProvider dop = ClassProcessor.processClasses(
				cf.getClasses(), cf.getDirURLs(), parentClassLoader);
		processes.addAll(dop.getProcesses(null));
		knowledges.addAll(dop.getKnowledges(null));
	}

}
