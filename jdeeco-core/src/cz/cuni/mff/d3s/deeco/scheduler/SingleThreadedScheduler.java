package cz.cuni.mff.d3s.deeco.scheduler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.executor.TaskExecutionException;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.runtime.DEECoNode;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskTriggerListener;
import cz.cuni.mff.d3s.deeco.timer.Timer;

/**
 * TODO
 */
public class SingleThreadedScheduler implements Scheduler {
	private final DEECoNode node;
	private Executor executor;
	private Timer timer;

	private final EventQueue queue;
	private final Set<Task> allTasks;
	private final Map<Task, SchedulerEvent> timeTriggeredEvents;
	private final Set<Trigger> knowledgeChangeTriggers;

	public SingleThreadedScheduler(Executor executor, Timer timer, DEECoNode node) throws NoExecutorAvailableException {
		if (executor == null) {
			throw new NoExecutorAvailableException();
		}
		this.executor = executor;
		this.timer = timer;
		this.node = node;

		queue = new EventQueue();
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
	public Timer getTimer() {
		return timer;
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
			long executionTime = timer.getCurrentMilliseconds() + task.getTimeTrigger().getOffset();
			event.nextExecutionTime = executionTime;
			event.nextPeriodStart = executionTime;
			queue.add(event);

			timer.notifyAt(event.nextExecutionTime, this, node);

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
					long executionTime = timer.getCurrentMilliseconds();
					event.nextExecutionTime = executionTime;
					event.nextPeriodStart = executionTime;
					
					// add event to queue
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
		
		// Remove scheduler events for the task
		queue.removeAllTaskEvents(task);
		
		timeTriggeredEvents.remove(task);
		allTasks.remove(task);
	}

	@Override
	public void executionCompleted(Task task, Trigger trigger) {
		knowledgeChangeTriggers.remove(trigger);

		if (!queue.isEmpty()) {
			long nextExecutionTime = queue.first().nextExecutionTime;
			// FIXME we need the '=' in the next line if we don't give a random offset
			if (nextExecutionTime >= timer.getCurrentMilliseconds()) {
				timer.notifyAt(nextExecutionTime, this, node);
			}
		}
	}

	@Override
	public void executionFailed(Task task, Trigger trigger, Exception e) {
		throw new TaskExecutionException(task, trigger, e);
	}

	@Override
	public void at(long time) {
		while ((!queue.isEmpty()) && (queue.first().nextExecutionTime <= time)) {
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
}

class EventQueue {
	private final TreeSet<SchedulerEvent> queue;
	private final Map<Task, Set<SchedulerEvent>> queueEvents;
	
	public EventQueue() {
		queue = new TreeSet<>();
		queueEvents = new HashMap<>();
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	public SchedulerEvent pollFirst() {
		return queue.pollFirst();
	}
	
	public SchedulerEvent first() {
		return queue.first();
	}
	
	/**
	 * Adds event to queue
	 * 
	 * @param event Event to add
	 * @param task Task owning the event
	 */
	public void add(final SchedulerEvent event) {
		Task task = event.executable;
		queue.add(event);
		Set<SchedulerEvent> events = queueEvents.get(task);
		if(events == null) {
			events = new HashSet<>();
			queueEvents.put(task, events);
		}
		events.add(event);
	}
	
	public void removeAllTaskEvents(final Task task) {
		Set<SchedulerEvent> eventsToBeRemoved = queueEvents.get(task);
		if(eventsToBeRemoved != null) {
			queue.removeAll(queueEvents.get(task));
		}
		queueEvents.remove(task);
	}
}