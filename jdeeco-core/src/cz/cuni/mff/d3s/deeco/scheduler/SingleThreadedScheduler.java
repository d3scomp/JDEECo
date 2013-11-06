package cz.cuni.mff.d3s.deeco.scheduler;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskTriggerListener;

public class SingleThreadedScheduler implements Scheduler {
	/**
	 * Map capturing the SchedulerTaskWrappers for periodic Tasks. These are
	 * mostly important for postponing the Tasks' execution when the previous
	 * execution took longer than the period.
	 */
	Map<Task, SchedulerTaskWrapper> periodicTasks;
	
	 /**
     * The scheduler task queue.  This data structure is shared with the scheduler
     * thread.  The scheduler produces tasks, via its various schedule calls,
     * and the scheduler thread consumes, executing scheduler tasks as appropriate,
     * and removing them from the queue when they're obsolete.
     */
    private TaskQueue queue = new TaskQueue();

    /**
     * The scheduler thread.
     */
    private SchedulerThread thread = new SchedulerThread(queue);

    /**
     * This object causes the scheduler's task execution thread to exit
     * gracefully when there are no live references to the Scheduler object and no
     * tasks in the scheduler queue.  It is used in preference to a finalizer on
     * Scheduler as such a finalizer would be susceptible to a subclass's
     * finalizer forgetting to call it.
     */
    private Object threadReaper = new Object() {
        protected void finalize() throws Throwable {
            synchronized(queue) {
                thread.newTasksMayBeScheduled = false;
                queue.notify(); // In case queue is empty.
            }
        }
    };

    /**
     * If the completed task is periodic and the completion time is greater than the intended start of the next period,
     * then the next period is moved. 
     */
	@Override
	public void executionCompleted(Task task) {

		synchronized (queue) {
			SchedulerTaskWrapper sTask = periodicTasks.get(task);
			// continue only for periodic tasks
			if (sTask == null)
				return;
			
			synchronized (sTask.lock) {
				// if the periodic task execution took more than it remained till the next period 
				if (sTask.nextExecutionTime < System.currentTimeMillis()) {				
					queue.rescheduleTask(sTask, System.currentTimeMillis() + sTask.period);
				}
			}
		}
		
	}

	@Override
	public void executionFailed(Task task, Exception e) {
		executionCompleted(task);		
	}

	@Override
	public void start() {
		
		if (!thread.isAlive())
			thread.start();	
		
		 synchronized(queue) {
             thread.tasksMayBeExecuted = true;
             queue.notify(); // In case queue is empty.
         }
	}

	/**
	 * Temporarily stop the scheduler.
	 */
	@Override
	public void stop() {
		 synchronized(queue) {
             thread.tasksMayBeExecuted = false;
         }
	}
	
	/**
	 * @throws IllegalStateException if scheduler thread already terminated.
	 * @throws IllegalArgumentException of a null task is passed as an argument.
	 */
	@Override
	public void addTask(Task task) {
		if (task == null)
			throw new IllegalArgumentException("The task cannot be null");
		synchronized (queue) {
			if (!thread.newTasksMayBeScheduled)
				throw new IllegalStateException(
						"Scheduler already terminated.");
			
			if (task.getSchedulingPeriod() > 0) {
				SchedulerTaskWrapper sTask = new SchedulerTaskWrapper(task);
				scheduleNow(sTask, task.getSchedulingPeriod());
				periodicTasks.put(task, sTask);
			}
		}
		task.setTriggerListener(new TaskTriggerListener() {			
			@Override
			public void triggered(Task task) {
				synchronized (queue) {
					if (!thread.newTasksMayBeScheduled || !thread.tasksMayBeExecuted)
						return;

					scheduleNow(new SchedulerTaskWrapper(task), 0);
				}
			}
		});
	}
	
	/**
	 * Note that this method has to be explicitly protected by queue's monitor!
	 */
	private void scheduleNow(SchedulerTaskWrapper sTask, long period) {			
			sTask.nextExecutionTime = System.currentTimeMillis(); // start immediately
			sTask.period = period;
			sTask.state = SchedulerTaskWrapper.SCHEDULED;

			queue.add(sTask);
			if (queue.getMin() == sTask)
				queue.notify();		
	}

	/**
	 * 
	 */
	@Override
	public void removeTask(Task task) {
		task.unsetTriggerListener();
		 synchronized(queue) {
			 // cancel all the periodic/triggered schedules of the task
			 queue.cancelAll(task);			
			 periodicTasks.remove(task);
		 }				 
	}

	@Override
	public void setExecutor(Executor executor) {
		synchronized (thread.executorLock) {
			thread.executor = executor;
		}		
	}
}


/**
 * This "helper class" implements the scheduler's task execution thread, which
 * waits for tasks on the scheduler queue, executions them when they fire,
 * reschedules repeating tasks, and removes cancelled tasks and spent
 * non-repeating tasks from the queue.
 */
class SchedulerThread extends Thread {	
    /**
     * This flag is set to false by the reaper to inform us that there
     * are no more live references to our Scheduler object.  Once this flag
     * is true and there are no more tasks in our queue, there is no
     * work left for us to do, so we terminate gracefully.  Note that
     * this field is protected by queue's monitor!
     */
    boolean newTasksMayBeScheduled = true;
    
    
    /**
	 * This flag is set to false by scheduler to inform us that the scheduler is
	 * temporarily stopped and all the scheduled tasks have to bee ignored. Note
	 * that this field is protected by queue's monitor!
	 */
    boolean tasksMayBeExecuted = true;

    /**
     * Our Scheduler's queue.  We store this reference in preference to
     * a reference to the Scheduler so the reference graph remains acyclic.
     * Otherwise, the Scheduler would never be garbage-collected and this
     * thread would never go away.
     */
    private TaskQueue queue;
    
    
    Object executorLock = new Object();
    
    Executor executor;

    SchedulerThread(TaskQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            mainLoop();
        } finally {
            // Someone killed this Thread, behave as if Scheduler cancelled
            synchronized(queue) {
                newTasksMayBeScheduled = false;
                queue.clear();  // Eliminate obsolete references
            }
        }
    }

    /**
     * The main scheduler loop.  (See class comment.)
     */
    private void mainLoop() {
        while (true) {
            try {
                SchedulerTaskWrapper task;
                boolean taskFired;
                synchronized(queue) {
                    // Wait for queue to become non-empty
                    while (queue.isEmpty() && newTasksMayBeScheduled)
                        queue.wait();
                    if (queue.isEmpty() && !newTasksMayBeScheduled)
                        break; // Queue is empty and will forever remain; die

                    // Queue nonempty; look at first evt and do the right thing
                    long currentTime, executionTime;
                    task = queue.getMin();
                    
                    synchronized(task.lock) {
                        if (task.state == SchedulerTaskWrapper.CANCELLED) {
                            queue.removeMin();
                            continue;  // No action required, poll queue again
                        }                        
                        
                        currentTime = System.currentTimeMillis();
                        executionTime = task.nextExecutionTime;
                        taskFired = (executionTime<=currentTime);

                        if (!taskFired) { // Task hasn't yet fired; wait 
                            queue.wait(executionTime - currentTime);
							
							// restart, since the current task might have been
							// deleted, or some other task that has to be
							// scheduled earlier might have been added, or all
							// tasks might have been deleted
                            if (queue.getMin() != task)
                            	continue;                          
                        }
                                             
                        assert taskFired;                        
                        
                        if (task.period == 0) { // Non-repeating, remove
                            queue.removeMin();
                            task.state = SchedulerTaskWrapper.EXECUTED;
                        } else { // Repeating task, reschedule
                            queue.rescheduleMin(executionTime + task.period);
                        }                        
                    }
                   
                    
                    // make sure the fire task can be executed
                    taskFired = taskFired && tasksMayBeExecuted;
                }
                
               
                if (taskFired)  { // Task fired; run it
                	synchronized (executorLock) {
                		executor.execute(task.executable);
                	}
            	}
            } catch(InterruptedException e) {
            }
        }
    }
}

/**
 * This class represents a scheduler task queue: a priority queue of SchedulerTasks,
 * ordered on nextExecutionTime.  Each Scheduler object has one of these, which it
 * shares with its SchedulerThread.  Internally this class uses a heap, which
 * offers log(n) performance for the add, removeMin and rescheduleMin
 * operations, and constant time performance for the getMin operation.
 */
class TaskQueue {
    /**
     * Priority queue represented as a balanced binary heap: the two children
     * of queue[n] are queue[2*n] and queue[2*n+1].  The priority queue is
     * ordered on the nextExecutionTime field: The SchedulerTaskWrapper with the lowest
     * nextExecutionTime is in queue[1] (assuming the queue is nonempty).  For
     * each node n in the heap, and each descendant of n, d,
     * n.nextExecutionTime <= d.nextExecutionTime.
     */
    private SchedulerTaskWrapper[] queue = new SchedulerTaskWrapper[128];

    /**
     * The number of tasks in the priority queue.  (The tasks are stored in
     * queue[1] up to queue[size]).
     */
    private int size = 0;

    /**
     * Returns the number of tasks currently on the queue.
     */
    int size() {
        return size;
    }


	/**
     * Adds a new task to the priority queue.
     */
    void add(SchedulerTaskWrapper task) {
        // Grow backing store if necessary
        if (size + 1 == queue.length)
            queue = Arrays.copyOf(queue, 2*queue.length);

        queue[++size] = task;
        fixUp(size);
    }

    /**
     * Return the "head task" of the priority queue.  (The head task is an
     * task with the lowest nextExecutionTime.)
     */
    SchedulerTaskWrapper getMin() {
    	if (size > 0)
    		return queue[1];
    	else
    		return null;
    }

    /**
     * Return the ith task in the priority queue, where i ranges from 1 (the
     * head task, which is returned by getMin) to the number of tasks on the
     * queue, inclusive.
     */
    SchedulerTaskWrapper get(int i) {
        return queue[i];
    }

    /**
     * Remove the head task from the priority queue.
     */
    void removeMin() {
        queue[1] = queue[size];
        queue[size--] = null;  // Drop extra reference to prevent memory leak
        fixDown(1);
    }

    /**
	 * Cancels all the scheduler tasks holding the given executable task from
	 * queue. There can be many of them due to multiple triggers firing at a
	 * rapid succession. The cancelled tasks will be removed automatically by
	 * the SchedulerThreat. 
	 */
    void cancelAll(Task executable) {    	    	
        for (int i=1; i <= size; ++i) {
        	synchronized(queue[i].lock) {
	        	if (queue[i].executable.equals(executable)) {
	        		queue[i].state = SchedulerTaskWrapper.CANCELLED;
	        	}
        	}
        }	      
    }

    /**
     * Sets the nextExecutionTime associated with the head task to the
     * specified value, and adjusts priority queue accordingly.
     */
    void rescheduleMin(long newTime) {
        queue[1].nextExecutionTime = newTime;
        fixDown(1);
    }
    
    /**
	 * Sets the nextExecutionTime associated with the given scheduler task to
	 * the specified value, and adjusts priority queue accordingly.
	 */
	public void rescheduleTask(SchedulerTaskWrapper task, long newTime) {
    	int i = 1;    	
        for (;i <= size; ++i) {
        	if (queue[i].equals(task))
        		break;
        }
        // no more occurences found
        if (i > size)
        	return;
        
        assert queue[i].nextExecutionTime <= newTime;
        
        queue[i].nextExecutionTime = newTime;
        fixDown(i);
	}

    /**
     * Returns true if the priority queue contains no elements.
     */
    boolean isEmpty() {
        return size==0;
    }

    /**
     * Removes all elements from the priority queue.
     */
    void clear() {
        // Null out task references to prevent memory leak
        for (int i=1; i<=size; i++)
            queue[i] = null;

        size = 0;
    }

    /**
     * Establishes the heap invariant (described above) assuming the heap
     * satisfies the invariant except possibly for the leaf-node indexed by k
     * (which may have a nextExecutionTime less than its parent's).
     *
     * This method functions by "promoting" queue[k] up the hierarchy
     * (by swapping it with its parent) repeatedly until queue[k]'s
     * nextExecutionTime is greater than or equal to that of its parent.
     */
    private void fixUp(int k) {
        while (k > 1) {
            int j = k >> 1;
            if (queue[j].nextExecutionTime <= queue[k].nextExecutionTime)
                break;
            SchedulerTaskWrapper tmp = queue[j];  queue[j] = queue[k]; queue[k] = tmp;
            k = j;
        }
    }

    /**
     * Establishes the heap invariant (described above) in the subtree
     * rooted at k, which is assumed to satisfy the heap invariant except
     * possibly for node k itself (which may have a nextExecutionTime greater
     * than its children's).
     *
     * This method functions by "demoting" queue[k] down the hierarchy
     * (by swapping it with its smaller child) repeatedly until queue[k]'s
     * nextExecutionTime is less than or equal to those of its children.
     */
    private void fixDown(int k) {
        int j;
        while ((j = k << 1) <= size && j > 0) {
            if (j < size &&
                queue[j].nextExecutionTime > queue[j+1].nextExecutionTime)
                j++; // j indexes smallest kid
            if (queue[k].nextExecutionTime <= queue[j].nextExecutionTime)
                break;
            SchedulerTaskWrapper tmp = queue[j];  queue[j] = queue[k]; queue[k] = tmp;
            k = j;
        }
    }

    /**
     * Establishes the heap invariant (described above) in the entire tree,
     * assuming nothing about the order of the elements prior to the call.
     */
    void heapify() {
        for (int i = size/2; i >= 1; i--)
            fixDown(i);
    }
}


class SchedulerTaskWrapper  {
    /**
     * This object is used to control access to the SchedulerTaskWrapper internals.
     */
	final Object lock = new Object();

    /**
     * The state of this task, chosen from the constants below.
     */
    int state = VIRGIN;

    /**
     * This task has not yet been scheduled.
     */
    static final int VIRGIN = 0;

    /**
     * This task is scheduled for execution.  If it is a non-repeating task,
     * it has not yet been executed.
     */
    static final int SCHEDULED   = 1;

    /**
     * This non-repeating task has already executed (or is currently
     * executing) and has not been cancelled.
     */
    static final int EXECUTED    = 2;

    /**
     * This task has been cancelled (with a call to SchedulerTaskWrapper.cancel).
     */
    static final int CANCELLED   = 3;

    /**
     * Next execution time for this task in the format returned by
     * System.currentTimeMillis, assuming this task is scheduled for execution.
     * For repeating tasks, this field is updated prior to each task execution.
     */
    long nextExecutionTime;

    /**
     * Period in milliseconds for repeating tasks.  A positive value indicates
     * fixed-rate execution.  A value of 0 indicates a non-repeating task.
     */
    long period = 0;
    
    /**
     * The actual task to be executed.
     */
    Task executable;
    

    /**
     * Creates a new scheduler task.
     */
    protected SchedulerTaskWrapper(Task task) {
    	this.executable = task;
    }

   

}


