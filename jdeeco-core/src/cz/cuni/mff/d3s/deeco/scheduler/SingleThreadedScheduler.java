/*
 * parts taken from java.util.Timer
 *  
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package cz.cuni.mff.d3s.deeco.scheduler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;
import cz.cuni.mff.d3s.deeco.task.TaskTriggerListener;

/**
 * Implementation of the Scheduler as SingleThreadedScheduler 
 * <p>
 * Current class implements yet another type of a scheduler - SingleThreadedScheduler.
 * This scheduler has a single execution thread and a special task queue which
 * is shared with the scheduler. Also it distinguishes between periodic and non-periodic
 * events and keeps a separate list for periodic tasks.<br>
 * This file contains not only the implementations of SingleThreadedScheduler but also 
 * all helper classes listed below:
 * <ul>
 * 	<li>{@link cz.cuni.mff.d3s.deeco.scheduler.SchedulerThread SchedulerThread}</li>
 * 	<li>{@link cz.cuni.mff.d3s.deeco.scheduler.TaskQueue TaskQueue}</li>
 * 	<li>{@link cz.cuni.mff.d3s.deeco.scheduler.SchedulerEvent SchedulerEvent}</li>
 * 	<li>{@link cz.cuni.mff.d3s.deeco.scheduler.InvokeAndWaitTask InvokeAndWaitTask}</li>
 * </ul>
 * 
 * @author 	Andranik Muradyan 	<muradian@d3s.mff.cuni.cz>
 * @author 	Jaroslav Keznikl 	<keznikl@d3s.mff.cuni.cz>
 *
 */
public class SingleThreadedScheduler implements Scheduler {
	/**
	 * Map capturing the SchedulerTaskWrappers for periodic Tasks. These are
	 * mostly important for postponing the Tasks' execution when the previous
	 * execution took longer than the period.
	 */
	Map<Task, SchedulerEvent> periodicEvents = new HashMap<>();
	
	Set<Task> allTasks = new HashSet<>();
	
	 /**
     * The scheduler task queue.  This data structure is shared with the scheduler
     * thread.  The scheduler produces tasks, via its various schedule calls,
     * and the scheduler thread consumes, executing scheduler tasks as appropriate,
     * and removing them from the queue when they're obsolete.
     */
    TaskQueue queue;

    /**
     * The scheduler thread.
     */
    SchedulerThread thread;
    
    public SingleThreadedScheduler(){
    	 queue = new TaskQueue();
    	 thread = new SchedulerThread(queue);
    }
        
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
     * Or it's the first time it is invoked
     */
	@Override
	public void executionCompleted(Task task) {
		if (task instanceof InvokeAndWaitTask) {
			synchronized (task) {
				task.notify();	
			}			
			return;
		}
		
		synchronized (queue) {
			SchedulerEvent event = periodicEvents.get(task);
			// continue only for periodic tasks
			if (event == null)
				return;
			
				// if the periodic task execution took more than it remained till the next period 
				if (event.nextExecutionTime < System.currentTimeMillis()) {				
					queue.rescheduleTask(event, System.currentTimeMillis() + event.period);
				}
			
		}		
	}

	@Override
	public void executionFailed(Task task, Exception e) {
		Log.e(e.getMessage());
		
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

	@Override
	public void stop() {
		 synchronized(queue) {
             thread.tasksMayBeExecuted = false;
         }
	}
	
	/**
	 * Adds the task to the scheduler
	 * <p>
	 * This method adds the task to the scheduler task list. However except just 
	 * adding the task to allTasks first it wraps them with {@link cz.cuni.mff.d3s.deeco.scheduler.SchedulerEvent SchedulerEvent}
	 * class. In both(periodic and non-periodic) cases the task is being scheduled for immediate execution
	 * but non-periodic tasks are scheduled with zero period which makes them execute just once. Periodic 
	 * tasks on the other hand with some non-zero period and added not only to allTasks but also to 
	 * {@link cz.cuni.mff.d3s.deeco.scheduler.SingleThreadedScheduler#periodicEvents periodicEvents}.
	 *  
	 * @param task the task to be added 
	 * @throws IllegalStateException 	if scheduler thread already terminated.
	 * @throws IllegalArgumentException if a null task is passed as an argument.
	 */
	@Override
	public void addTask(Task task) {		
		if (task == null)
			throw new IllegalArgumentException("The task cannot be null");
		
		synchronized (allTasks) {		
			if (allTasks.contains(task))
				return;
			
			synchronized (queue) {
				if (!thread.newTasksMayBeScheduled)
					throw new IllegalStateException(
							"Scheduler already terminated.");
				
				if (task.getPeriodicTrigger() != null) {
					SchedulerEvent event = new SchedulerEvent(task, task.getPeriodicTrigger());
					scheduleNow(event, task.getPeriodicTrigger().getPeriod());
					periodicEvents.put(task, event);
				}
			}
			task.setTriggerListener(new TaskTriggerListener() {	
				@Override
				public void triggered(Task task, Trigger trigger) {
					synchronized (queue) {
						if (!thread.newTasksMayBeScheduled || !thread.tasksMayBeExecuted)
							return;
						
						boolean isScheduled;
						synchronized (allTasks) {
							isScheduled = allTasks.contains(task);
						}
						if (isScheduled) {
							scheduleNow(new SchedulerEvent(task, trigger), 0);
						}
					}
				}
			});
			
			allTasks.add(task);
		}
	}
	
	/**
	 * Note that this method has to be explicitly protected by queue's monitor!
	 */
	void scheduleNow(SchedulerEvent event, long period) {			
			event.nextExecutionTime = System.currentTimeMillis(); // start immediately
			event.period = period;
			event.state = SchedulerEvent.SCHEDULED;

			queue.add(event);
			if (queue.getMin() == event)
				queue.notify();		
	}


	@Override
	public void removeTask(Task task) {
		synchronized (allTasks) {		
			if (!allTasks.contains(task))
				return;
			
			task.unsetTriggerListener();
			synchronized(queue) {
				 // cancel all the periodic/triggered schedules of the task
				 queue.cancelAll(task);			
				 periodicEvents.remove(task);				 
			}				 
			allTasks.remove(task);
		}
	}


	@Override
	public void setExecutor(Executor executor) {
		synchronized (thread.executorLock) {
			thread.executor = executor;
		}		
	}
	

	/**
	 * This method instantiates a special kind of task of type 
	 * {@link cz.cuni.mff.d3s.deeco.scheduler.InvokeAndWaitTask InvokeAndWaitTask}
	 * attaching a runnable and a scheduler, in whose context the runnable will run, to it.
	 * After executing the task(which will invoke the runnable) and adding it to the list of the tasks
	 * it waits, until awaken by someone's notification sharing a monitor with that task. 
	 * 
	 * @param 	doRun the runnable instance
	 * @throws 	InterruptedException when the invocation is interrupted
	 */
	public void invokeAndWait(Runnable doRun) throws InterruptedException {
		InvokeAndWaitTask task = new InvokeAndWaitTask(this, doRun);
		synchronized (task) {
			addTask(task);
			task.wait();
		}
	}
}


/**
 * This "helper class" implements the scheduler's task execution thread, which
 * waits for tasks on the scheduler queue, executes them when they fire,
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
    boolean tasksMayBeExecuted = false;

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

    SchedulerThread(TaskQueue queue, ExecutorService execSer) {
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
	 * Wait on the queue till the nextExecutionTime of the event, or till
	 * the event is removed from the top of the queue (either by some new event
	 * that has to be executed sooner, or by removing the current event).
	 * 
	 *
     * @param event
     * @return
  	 * @throws InterruptedException
	 *             if the thread gets interrupted during waiting
	 */
    private boolean waitTaskFired(SchedulerEvent event) throws InterruptedException {
    	while (true) {
            long currentTime = System.currentTimeMillis();
            long executionTime = event.nextExecutionTime;
            boolean taskFired = (executionTime<=currentTime);

            if (!taskFired) { // Task hasn't yet fired; wait 
                queue.wait(executionTime - currentTime);
				
				// restart the main loop, since the current event might have been
				// deleted, or some other event that has to be
				// scheduled earlier might have been added, or all
				// tasks might have been deleted
                // or the event was cancelled
                if ((queue.getMin() != event) || (event.state == SchedulerEvent.CANCELLED))
                	return false;                 
                
				// check taskFired again, i.e., that we were
				// waken up by waiting long enough (and not by
				// some other notify, e.g., when adding a task)	                         
            } else {
            	// we have waited long enough
            	return true;                        	
            }
        }    	
    }
    
    /**
     * The main scheduler loop.  (See class comment.)
     */
    private void mainLoop() {

        while (true) {        	
            try {
                SchedulerEvent event;
                boolean canExecute;
                Task task;
                Trigger trigger;
                synchronized(queue) {
                    // Wait for queue to become non-empty
                    while (queue.isEmpty() && newTasksMayBeScheduled)
                        queue.wait();
                    if (queue.isEmpty() && !newTasksMayBeScheduled)
                        break; // Queue is empty and will forever remain; die

                    // Queue nonempty; look at first event and do the right thing                   
                    event = queue.getMin();
                    
                
                    if (event.state == SchedulerEvent.CANCELLED) {
                        queue.removeMin();
                        continue;  // No action required, poll queue again
                    }                        
                                           
                    canExecute = waitTaskFired(event);
                    
                    if (!canExecute)
                    	continue; // The event cannot continue with execution, poll queue again
                    
                    if( event.state == SchedulerEvent.RUNNING )
                    	continue;
                    
                    if (event.period == 0) { // Non-repeating, remove
                        queue.removeMin();
                        event.state = SchedulerEvent.EXECUTED;
                    } else { // Repeating task, reschedule
                        queue.rescheduleMin(event.nextExecutionTime + event.period);
                    }              
                    
                    task = event.executable;
                    trigger = event.trigger;
                    
                    
                    // make sure the fire task can be executed
                    canExecute = canExecute && tasksMayBeExecuted;
                }                
               
                if (canExecute)  { // Task fired and can be executed; run it
                	synchronized (executorLock) {
                		executor.execute(task, trigger);
                	}                	
            	}
            } catch(InterruptedException e) {
            }
        }
    }
}

/**
 * This class represents a scheduler task queue: a priority queue of SchedulerTasks,
 * ordered on next ExecutionTime. Each Scheduler object has one of these, which it
 * shares with its SchedulerThread. Internally this class uses a heap, which
 * offers log(n) performance for the add, removeMin and rescheduleMin
 * operations, and constant time performance for the getMin operation.
 */
class TaskQueue {
    /**
     * Priority queue represented as a balanced binary heap: the two children
     * of queue[n] are queue[2*n] and queue[2*n+1].  The priority queue is
     * ordered on the nextExecutionTime field: The SchedulerEvent with the lowest
     * nextExecutionTime is in queue[1] (assuming the queue is nonempty).  For
     * each node n in the heap, and each descendant of n, d,
     * n.nextExecutionTime <= d.nextExecutionTime.
     */
    private SchedulerEvent[] queue = new SchedulerEvent[128];

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
    void add(SchedulerEvent task) {
    	if( task == null )
    		throw new NullPointerException();
    	
        int i = size;
    	if (i >= queue.length)
    	    grow(i + 1);
    	size = i + 1;
    	if (i == 0)
    	    queue[0] = task;
    	else
    		siftUp(i, task);
    }

    /**
     * Return the "head task" of the priority queue.  (The head task is an
     * task with the lowest nextExecutionTime.)
     */
    SchedulerEvent getMin() {
    	if (size != 0)
    		return queue[0];
    	else
    		return null;
    }
    
    private void grow(int minCapacity){
    	if (minCapacity < 0) // overflow
    	    throw new OutOfMemoryError();
    	
    	int oldCapacity = queue.length;
    	
    	// Double size if small; else grow by 50%
    	int newCapacity = ((oldCapacity < 64)?
    	                   ((oldCapacity + 1) * 2):
    	                   ((oldCapacity / 2) * 3));
    	
    	if (newCapacity < 0) // overflow
    	    newCapacity = Integer.MAX_VALUE;
    	
    	if (newCapacity < minCapacity)
    	    newCapacity = minCapacity;
    	
    	queue = Arrays.copyOf(queue, newCapacity);
    }

    /**
     * Return the ith task in the priority queue, where i ranges from 1 (the
     * head task, which is returned by getMin) to the number of tasks on the
     * queue, inclusive.
     */
    SchedulerEvent get(int i) {
        return queue[i];
    }

    /**
     * Remove the head task from the priority queue.
     */
    void removeMin() {
    	int s = --size;
    	if (s == 0) // removed last element
    	    queue[0] = null;
    	else {
    	    SchedulerEvent moved = queue[s];
    	    
    	    queue[s] = null;
    	    
    	    siftDown(0, moved);
    	    
    	    if (queue[0] == moved)
    	        siftUp(0, moved);
    	}
    }

    /**
	 * Cancels all the scheduler tasks holding the given executable task from
	 * queue. There can be many of them due to multiple triggers firing at a
	 * rapid succession. The cancelled tasks will be removed automatically by
	 * the SchedulerThreat. 
	 */
    void cancelAll(Task executable) {    	    	
        for (int i=0; i < size; ++i) {
        	if (queue[i].executable.equals(executable)) {
        		queue[i].state = SchedulerEvent.CANCELLED;
        	}        	
        }	      
    }

    /**
     * Sets the nextExecutionTime associated with the head task to the
     * specified value, and adjusts priority queue accordingly.
     */
    void rescheduleMin(long newTime) {
        queue[0].nextExecutionTime = newTime;
        siftDown(0, queue[0]);
    }
    
    /**
	 * Sets the nextExecutionTime associated with the given scheduler task to
	 * the specified value, and adjusts priority queue accordingly.
	 */
	void rescheduleTask(SchedulerEvent task, long newTime) {
    	int i = 0;    	
        for (;i <= size; ++i) {
        	if (queue[i].equals(task))
        		break;
        }
        // no more occurences found
        if (i > size)
        	return;
        
        assert queue[i].nextExecutionTime <= newTime;
        
        queue[i].nextExecutionTime = newTime;
        siftDown(i, task);
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
        for (int i=0; i<=size; i++)
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
    private void siftUp(int k, SchedulerEvent event) {
        Comparable<? super SchedulerEvent> key = (Comparable<? super SchedulerEvent>) event;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
        	SchedulerEvent e = queue[parent];
            if (key.compareTo((SchedulerEvent) e) >= 0)
                break;
            queue[k] = e;
            k = parent;
        }
        queue[k] = (SchedulerEvent) key;
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
    private void siftDown(int k, SchedulerEvent event) {
    	Comparable<? super SchedulerEvent> key = (Comparable<? super SchedulerEvent>)event;
    	int half = size >>> 1;        // loop while a non-leaf
    	while (k < half) {
    	    int child = (k << 1) + 1; // assume left child is least
    	    SchedulerEvent c = queue[child];
    	    int right = child + 1;
    	    if (right < size &&
    	        ((Comparable<? super SchedulerEvent>) c).compareTo((SchedulerEvent) queue[right]) > 0)
    	        c = queue[child = right];
    	    if (key.compareTo((SchedulerEvent) c) <= 0)
    	        break;
    	    queue[k] = c;
    	    k = child;
    	}
    	queue[k] = (SchedulerEvent)key;
    }

    /**
     * Establishes the heap invariant (described above) in the entire tree,
     * assuming nothing about the order of the elements prior to the call.
     */
    void heapify() {
	    for (int i = (size >>> 1) - 1; i >= 0; i--)
	    	siftDown(i, queue[i]);
	    }
}

/**
 * Ad-hoc tasks for one-time execution of a runnable within the context of the scheduler thread.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
class InvokeAndWaitTask extends Task {

	Runnable runnable;
	
	public InvokeAndWaitTask(Scheduler scheduler, Runnable runnable) {
		super(scheduler);
		this.runnable = runnable;
	}

	@Override
	public void invoke(Trigger trigger) throws TaskInvocationException {
		if (runnable != null) {
			runnable.run();
		}
	}

	@Override
	protected void registerTriggers() {				
	}

	@Override
	protected void unregisterTriggers() {		
	}

	@Override
	public PeriodicTrigger getPeriodicTrigger() {		
		return null;
	}
	
}