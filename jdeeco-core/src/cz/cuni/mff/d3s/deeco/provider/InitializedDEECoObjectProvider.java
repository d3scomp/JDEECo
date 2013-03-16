package cz.cuni.mff.d3s.deeco.provider;

import static cz.cuni.mff.d3s.deeco.processor.ComponentParser.extractComponentProcess;
import static cz.cuni.mff.d3s.deeco.processor.EnsembleParser.extractEnsembleProcess;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.knowledge.Component;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;

/**
 * Provider class delivering parsed components and ensembles retrieved from
 * component instances and ensemble classes respectively.
 * 
 * @author Michal Kit
 * 
 */
public class InitializedDEECoObjectProvider extends AbstractDEECoObjectProvider {

	private static final long serialVersionUID = -1454153558315293318L;

	protected final List<Class<?>> rawEnsembles;
	protected final List<Component> knowledges;

	public InitializedDEECoObjectProvider() {
		this(new LinkedList<Component>(), new LinkedList<Class<?>>());
	}

	public InitializedDEECoObjectProvider(List<Component> knowledges,
			List<Class<?>> ensembles) {
		this.rawEnsembles = ensembles;
		this.knowledges = knowledges;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider#processComponents
	 * ()
	 */
	@Override
	protected synchronized void processComponents() {
		components = new LinkedList<ParsedComponent>();
		for (Component c : knowledges) {
			if (c != null) {
				components.add(new ParsedComponent(extractComponentProcess(
						c.getClass(), c.id), c));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider#processEnsembles
	 * ()
	 */
	@Override
	protected synchronized void processEnsembles() {
		ensembles = new LinkedList<SchedulableEnsembleProcess>();
		if (rawEnsembles != null) {
			for (Class<?> c : rawEnsembles) {
				try {
					ensembles.add(extractEnsembleProcess(c));
				} catch (ParseException e) {
					Log.e(String.format("Parsing error in class '%s': %s",
							c.getName(), e.getMessage()), e);
				}
			}
		}
	}

}
