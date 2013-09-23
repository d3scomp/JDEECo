package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.IKnowledgeChangeListener;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.path.grammar.PathGrammar;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.runtime.model.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.runtime.model.Trigger;
import cz.cuni.mff.d3s.deeco.runtime.model.TriggeredSchedule;

public abstract class TriggeredJobProducer implements IKnowledgeChangeListener {

	private Scheduler scheduler;
	private TriggeredSchedule schedule;
	protected Runtime runtime;
	protected final MonitorProvider monitorProvider;

	public TriggeredJobProducer(TriggeredSchedule schedule,
			Scheduler scheduler, Runtime runtime, MonitorProvider monitorProvider) {
		this.scheduler = scheduler;
		this.schedule = schedule;
		this.runtime = runtime;
		this.monitorProvider = monitorProvider;
	}

	protected void scheduleJob(Job job) {
		scheduler.schedule(job);
	}

	@Override
	public List<String> getKnowledgePaths() {
		List<Trigger> triggers = schedule.getTriggers();
		List<String> result = new LinkedList<>();
		for (Trigger trigger : triggers)
			result.add(((KnowledgeChangeTrigger) trigger).getKnowledgePath()
					.getEvaluatedPath(runtime.getKnowledgeManager(),
							PathGrammar.COORD, PathGrammar.MEMBER, null, null));
		return result;
	}

	@Override
	public boolean equals(Object o) {
		return o != null && o instanceof TriggeredJobProducer
				&& ((TriggeredJobProducer) o).schedule.equals(schedule);
	}
}
