package cz.cuni.mff.d3s.deeco.scheduler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.scheduler.notifier.SchedulerNotifier;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskTriggerListener;

/**
 * TODO
 */
public class SingleThreadedScheduler implements Scheduler {

	private Executor executor;
	private SchedulerNotifier schedulerNotifier;
	
	private final TreeSet<SchedulerEvent> queue;
	private final Set<Task> allTasks;
	private final Map<Task, SchedulerEvent> timeTriggeredEvents;
	private final Set<Trigger> knowledgeChangeTriggers;

    public SingleThreadedScheduler(Executor executor, SchedulerNotifier schedulerNotifier) throws NoExecutorAvailableException{
		if (executor == null) {
			throw new NoExecutorAvailableException();
		}
    	this.executor = executor;
    	this.schedulerNotifier = schedulerNotifier;
    	
		queue = new TreeSet<>();
		allTasks = new HashSet<>();
		timeTriggeredEvents = new HashMap<>();
		knowledgeChangeTriggers = new HashSet<>();
		
    }

	@Override
	public void setExecutor(Executor executor) {
		this.executor = executor;
		if (this.executor != null) {
			this.executor.setExecutionListener(this);
		}
	}
	
	@Override
	public void setSchedulerNotifier(SchedulerNotifier schedulerNotifier) {
		this.schedulerNotifier = schedulerNotifier;
	}
	
	@Override
	public void addTask(Task task) {
		if (task == null) {
			throw new IllegalArgumentException("The task to be added to the scheduler cannot be null");
		}
		if (allTasks.contains(task)) {
			Log.w("Attempting to re-schedule a task.");
			return;
		}
		
		if (task.getTimeTrigger() != null) {
			SchedulerEvent event = new SchedulerEvent(task, task.getTimeTrigger());
			long executionTime = schedulerNotifier.getCurrentMilliseconds() + task.getTimeTrigger().getOffset();
			event.nextExecutionTime = executionTime;
			event.nextPeriodStart = executionTime;
			queue.add(event);
			
			schedulerNotifier.notifyAt(event.nextExecutionTime, this);
						
			timeTriggeredEvents.put(task, event);
		}
		
		task.setTriggerListener(new TaskTriggerListener() {
			@Override
			public void triggered(Task task, Trigger trigger) {

				if (allTasks.contains(task)) {
					// if the trigger has been already scheduled (i.e., there have been many consecutive 
					// invocations of that trigger in a row), then skip this event
					if (knowledgeChangeTriggers.contains(trigger)) {
						Log.w("Attempting to re-schedule knowledge change triggered task.");
						return;
					}
					knowledgeChangeTriggers.add(trigger);
					// schedule immediately, regardless the actual runtime of the process that triggered this trigger
					SchedulerEvent event = new SchedulerEvent(task, trigger);
					long executionTime = schedulerNotifier.getCurrentMilliseconds();
					event.nextExecutionTime = executionTime;
					event.nextPeriodStart = executionTime;
					queue.add(event);
				}
			}
		});

		allTasks.add(task);
	}
	
	@Override
	public void removeTask(Task task) {
		if (!allTasks.contains(task)) {
			Log.w("Attempting to remove a non-scheduled task from the scheduler");
			return;
		}
		task.unsetTriggerListener();
		List<SchedulerEvent> eventsToBeRemoved = new LinkedList<>();
		for (SchedulerEvent se : queue) {
			if (se.executable.equals(task)) {
				eventsToBeRemoved.add(se);
			}
		}
		queue.removeAll(eventsToBeRemoved);
		timeTriggeredEvents.remove(task);
		allTasks.remove(task);
	}
	
	@Override
	public void executionCompleted(Task task, Trigger trigger) {
		knowledgeChangeTriggers.remove(trigger);
		
		if (!queue.isEmpty()) {
			long nextExecutionTime = queue.first().nextExecutionTime;
			// FIXME we need the '=' in the next line if we don't give a random offset
			if (nextExecutionTime >= schedulerNotifier.getCurrentMilliseconds()) {
				schedulerNotifier.notifyAt(nextExecutionTime, this);
			}
		}
	}

	@Override
	public void executionFailed(Task task, Trigger trigger, Exception e) {
		Log.e(e.getMessage());
		executionCompleted(task, trigger);
	}

	@Override
	public void at(long time) {
		while ((!queue.isEmpty()) && (queue.first().nextExecutionTime<=time)) {
			SchedulerEvent event = queue.pollFirst(); 

			if (event.periodic) {
				// schedule for the next period add a random offset within the period (up to 75% of the period)
				long period = event.executable.getTimeTrigger().getPeriod();
				event.nextPeriodStart += period;
				event.nextExecutionTime = event.nextPeriodStart;
				queue.add(event);
			}
			
			executor.execute(event.executable, event.trigger);
		}
	}
	
	public SchedulerNotifier getSchedulerNotifier() {
		return schedulerNotifier;
	}
     
}