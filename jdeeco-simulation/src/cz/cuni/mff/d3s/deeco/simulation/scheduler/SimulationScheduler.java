package cz.cuni.mff.d3s.deeco.simulation.scheduler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.scheduler.SchedulerEvent;
import cz.cuni.mff.d3s.deeco.simulation.Host;
import cz.cuni.mff.d3s.deeco.simulation.SimulationTimeEventListener;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskTriggerListener;

/**
 * The {@link Scheduler} implementation designed for single threaded simulation.
 * This scheduler is suppose to be driven by the simulation through
 * {@link SimulationTimeEventListener} methods.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class SimulationScheduler implements Scheduler,
		SimulationTimeEventListener {

	private final Host host;
	private final SortedSet<SchedulerEvent> queue;
	private final Set<Task> allTasks;
	private final Map<Task, SchedulerEvent> periodicEvents;
	
	private long lastProcessStartTime = -1;
	private long lastProcessExecutionTime = 1;

	private Executor executor;

	public SimulationScheduler(Host host) {
		this.host = host;
		queue = new TreeSet<SchedulerEvent>();
		allTasks = new HashSet<>();
		periodicEvents = new HashMap<>();

		host.setSimulationTimeEventListener(this);
	}

	@Override
	public void start() {
		registerNextExecution();
	}

	@Override
	public void stop() {
		Log.i("The simulation scheduler is stopped together with the simulation.");
	}

	@Override
	public void addTask(Task task) {
		if (task == null)
			throw new IllegalArgumentException("The task cannot be null");
		if (allTasks.contains(task))
			return;
		if (task.getPeriodicTrigger() != null) {
			SchedulerEvent event = new SchedulerEvent(task,
					task.getPeriodicTrigger());
			scheduleNow(event, task.getPeriodicTrigger().getPeriod());
			periodicEvents.put(task, event);
		}
		task.setTriggerListener(new TaskTriggerListener() {
			@Override
			public void triggered(Task task, Trigger trigger) {

				boolean isScheduled;
				synchronized (allTasks) {
					isScheduled = allTasks.contains(task);
				}
				if (isScheduled) {
					measureExecutionTime();
					scheduleNow(new SchedulerEvent(task, trigger), 0);
				}
			}
		});
		
		allTasks.add(task);

	}

	@Override
	public void removeTask(Task task) {
		if (!allTasks.contains(task))
			return;
		task.unsetTriggerListener();
		List<SchedulerEvent> remove = new LinkedList<>();
		for (SchedulerEvent se : queue) {
			if (se.executable.equals(task)) {
				remove.add(se);
			}
		}
		queue.removeAll(remove);
		periodicEvents.remove(task);
		allTasks.remove(task);
	}

	@Override
	public void executionFailed(Task task, Exception e) {
		Log.e(e.getMessage());
		executionCompleted(task);
	}

	@Override
	public void executionCompleted(Task task) {
		measureExecutionTime();
		if (task.getPeriodicTrigger() != null && lastProcessExecutionTime > task.getPeriodicTrigger().getPeriod()) {
			Log.e("Task " + task.toString() + " has greater actual execution time than its period (" + task.getPeriodicTrigger().getPeriod() + ")");
		}
		registerNextExecution();
	}

	@Override
	public void setExecutor(Executor executor) {
		this.executor = executor;
		if (this.executor != null)
			this.executor.setExecutionListener(this);
	}

	@Override
	public void invokeAndWait(Runnable doRun) throws InterruptedException {
		Log.e("Simulation scheduler is being used! Invoking an external task is not allowed.");
	}

	@Override
	public void at(long time) {
		//System.out.println("Scheduler " +host.getId()+" at: "+time+" called with queue: " + Arrays.toString(queue.toArray()));
		SchedulerEvent event;
		while ((event = queue.first()) != null)  {
			// if notified too early
			if (event.nextExecutionTime > time)
				break;
			
			// The time is right to execute the next task			
			pop();
			if (event.period != 0) {
				event.nextExecutionTime = event.nextExecutionTime
						+ event.period;
				push(event);
			}
			if (executor != null) {
				lastProcessStartTime = System.currentTimeMillis();
				executor.execute(event.executable, event.trigger);
			} else {
				Log.e("The simulation scheduler is associated with no excecutor!");
			}		
			
		}
	}

	// ------Private methods--------

	private void scheduleNow(SchedulerEvent event, long period) {
		event.period = period;
		event.nextExecutionTime = host.getSimulationTime() + lastProcessExecutionTime;
		push(event);
	}

	private void registerNextExecution() {
		if (!queue.isEmpty()) {
			long nextExecutionTime = queue.first().nextExecutionTime;
			if (nextExecutionTime <= host.getSimulationTime()) {
				return; //nextExecutionTime = host.getSimulationTime() + 1;
			}
			host.callAt(nextExecutionTime);
			//System.out.println("Scheduler " + host.getId() + " registering callback at " + nextExecutionTime);
		}
	}

	private SchedulerEvent pop() {
		if (queue.isEmpty())
			return null;
		else {
			SchedulerEvent result = queue.first();
			queue.remove(result);
			return result;
		}
	}

	private void push(SchedulerEvent event) {
		Log.i("Adding: " + event);

		queue.add(event);
		Log.i("Queue: " + Arrays.toString(queue.toArray()));

		// TODO take into account different scheduling policies and WCET of
		// tasks. According to those the tasks need to be rescheduled.
	}
	
	private void measureExecutionTime() {
		lastProcessExecutionTime = Math.max(System.currentTimeMillis() - lastProcessStartTime, lastProcessExecutionTime);
	}
}
