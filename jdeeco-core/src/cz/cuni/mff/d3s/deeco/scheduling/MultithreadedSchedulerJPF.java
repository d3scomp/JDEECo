package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.TriggeredSchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;

/**
 * Special scheduler for testing via JPF.
 * 
 * Each task has a separate thread and is executed in the endless cycle.
 * Timing is ignored (due to GC - see comments in code)
 * 
 * @author alf
 *
 */
public class MultithreadedSchedulerJPF extends Scheduler {

  private Set<Thread> threads;
  
  public MultithreadedSchedulerJPF() {
    super();
    this.threads = new HashSet<Thread>();
    
    
    // JPF Optimization -> earlier class loading a clinit call (in the single threaded part)
    // Shrinks state space
    @SuppressWarnings("unused")
    ETriggerType e = ETriggerType.COORDINATOR;  
  }

  @Override
  public synchronized void start() {
    if (!running) {
      for (SchedulableProcess sp : periodicProcesses) {
        startPeriodicProcess(sp,
            ((ProcessPeriodicSchedule) sp.scheduling).interval);
      }
      List<KnowledgeManager> kms = new LinkedList<KnowledgeManager>();
      for (TriggeredSchedulableProcess tsp : triggeredProcesses) {
        if (true) {
          assert(false); // ALF: Unsupported now
        }
        tsp.registerListener();
        if (!kms.contains(tsp.getKnowledgeManager()))
			kms.add(tsp.getKnowledgeManager());
      }
      for (KnowledgeManager km : kms) {
			km.switchListening(true);
      }
      running = true;
    }
  }

  @Override
  public synchronized void stop() {
    if (running) {
      for (Thread t : threads) {
        t.interrupt();
      }
      List<KnowledgeManager> kms = new LinkedList<KnowledgeManager>();
		for (TriggeredSchedulableProcess tsp : triggeredProcesses) {
			if (!kms.contains(tsp.getKnowledgeManager()))
				kms.add(tsp.getKnowledgeManager());
		}
		for (KnowledgeManager km : kms) {
			km.switchListening(false);
		}
		running = false;
    }
  }

  private void startPeriodicProcess(SchedulableProcess process, long period) {
    // Note Period is intentionally ignored - GC
    // Because of the stop the world Garbage collector, which can postpone all threads and then any thread can execute its own action
    Thread t = new Thread(new PeriodicProcessRunner(process));
    threads.add(t);
    t.start();
  }

  class PeriodicProcessRunner implements Runnable {

    final private SchedulableProcess process;

    public PeriodicProcessRunner(SchedulableProcess process) {
      this.process = process;
    }

    @Override
    public void run() {
      while (!Thread.interrupted())
      {
        try {
          process.invoke();
        } catch (Exception e) {
          System.out.println("Process scheduled exception!");
        }
        
        
        // JPF Optimization - it is recommended to break transition here
        // modeling GC
        Thread.yield();
      }
    }
  }

}
