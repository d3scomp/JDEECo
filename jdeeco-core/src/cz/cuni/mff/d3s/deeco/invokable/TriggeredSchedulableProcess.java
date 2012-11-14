package cz.cuni.mff.d3s.deeco.invokable;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.path.grammar.EEnsembleParty;
import cz.cuni.mff.d3s.deeco.scheduling.ETriggerType;
import cz.cuni.mff.d3s.deeco.scheduling.IKnowledgeChangeListener;
import cz.cuni.mff.d3s.deeco.scheduling.ProcessTriggeredSchedule;

public class TriggeredSchedulableProcess implements IKnowledgeChangeListener {

	public final SchedulableProcess sp;

	public TriggeredSchedulableProcess(SchedulableProcess sp) {
		this.sp = sp;
	}

	@Override
	public void knowledgeChanged(final String triggerer,
			final ETriggerType recMode) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				if (sp.contextClassLoader != null)
					Thread.currentThread().setContextClassLoader(
							sp.contextClassLoader);
				sp.invoke(triggerer, recMode);
			}
		});
		t.run();
	}

	public boolean equals(Object o) {
		return o != null && o instanceof TriggeredSchedulableProcess
				&& this.sp.equals(((TriggeredSchedulableProcess) o).sp);
	}

	@Override
	public List<String> getKnowledgePaths() {
		if (isEnsembleTriggered()) {
			return getEvaluatedKnowledgePaths(
					EEnsembleParty.COORDINATOR.toString(),
					EEnsembleParty.MEMBER.toString());
		} else {
			return getEvaluatedKnowledgePaths();
		}
	}

	public KnowledgeManager getKnowledgeManager() {
		return sp.km;
	}

	public void registerListener() {
		getKnowledgeManager().registerListener(this);
	}

	public void unregisterListener() {
		getKnowledgeManager().unregisterListener(this);
	}

	private List<String> getEvaluatedKnowledgePaths() {
		return getEvaluatedKnowledgePaths(null, null);
	}

	private List<String> getEvaluatedKnowledgePaths(String coord, String member) {
		List<Parameter> triggeredParams = ((ProcessTriggeredSchedule) sp.scheduling).parameters;
		List<String> result = new ArrayList<String>();
		ISession session;
		for (Parameter p : triggeredParams) {
			session = sp.km.createSession();
			session.begin();
			while (session.repeat()) {
				result.add(p.kPath.getEvaluatedPath(sp.km, coord, member,
						session));
				session.end();
			}
		}
		return result;
	}

	private boolean isEnsembleTriggered() {
		return sp instanceof SchedulableEnsembleProcess;
	}
}
