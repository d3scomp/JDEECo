package cz.cuni.mff.d3s.deeco.scheduling;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcessTrigger;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.performance.PeriodicEnsembleInfo;
import cz.cuni.mff.d3s.deeco.performance.PeriodicProcessInfo;
import cz.cuni.mff.d3s.deeco.performance.SchedulableProcessTimeStampsVisitor;

public class MultithreadedScheduler extends Scheduler {
	private Map<SchedulableProcess, ScheduledExecutorService> threads;
	private SchedulableProcessTimeStampsVisitor visitor=new SchedulableProcessTimeStampsVisitor();

	public MultithreadedScheduler() {
		super();
		threads = new HashMap<SchedulableProcess, ScheduledExecutorService>();
	}

	@Override
	public synchronized void start() {
		if (!running) {
			for (SchedulableProcess sp : periodicProcesses) {
				startPeriodicProcess(sp);
			}

			List<KnowledgeManager> kms = new LinkedList<KnowledgeManager>();
			for (SchedulableProcessTrigger tsp : triggeredProcesses) {
				tsp.registerListener();
				if (!kms.contains(tsp.getKnowledgeManager()))
					kms.add(tsp.getKnowledgeManager());
			}
			for (KnowledgeManager km : kms) {
				km.setListenersActive(true);
			}
			running = true;
		}
	}

	@Override
	public synchronized void stop() {
		if (running) {
			for (SchedulableProcess sp : periodicProcesses) {
				threads.get(sp).shutdown();
			}
			List<KnowledgeManager> kms = new LinkedList<KnowledgeManager>();
			for (SchedulableProcessTrigger tsp : triggeredProcesses) {
				if (!kms.contains(tsp.getKnowledgeManager()))
					kms.add(tsp.getKnowledgeManager());
			}
			for (KnowledgeManager km : kms) {
				km.setListenersActive(false);
			}
			running = false;
		}
	}

	@Override
	protected synchronized void startPeriodicProcess(SchedulableProcess process) {
//		////////////////////////////////////////////////////////////////
//		//		start time of the period 
//		////////////////////////////////////////////////////////////////
//		
//		if(process instanceof SchedulableComponentProcess){
//			((PeriodicProcessInfo)(process.pInfo)).R=(((ProcessPeriodicSchedule)(process.scheduling)).interval)*1000000000;
//			((PeriodicProcessInfo)(process.pInfo)).startPeriods=System.nanoTime();
//			System.out.println("process :  " + ((PeriodicProcessInfo)(process.pInfo)).startPeriods);
//		}else if(process instanceof SchedulableEnsembleProcess){
//			((PeriodicEnsembleInfo) (process.pInfo)).R=(((ProcessPeriodicSchedule)(process.scheduling)).interval)*1000000000;
//			((PeriodicEnsembleInfo)(process.pInfo)).startPeriodsCoord=System.nanoTime();
//			((PeriodicEnsembleInfo)(process.pInfo)).startPeriodsMem=System.nanoTime();
//			System.out.println("ensemble :  co " + ((PeriodicEnsembleInfo)(process.pInfo)).startPeriodsCoord+ "  mem "+((PeriodicEnsembleInfo)(process.pInfo)).startPeriodsMem);
//
//		}
//		///////////////////////////////////////////////////////////////
//		///////////////////////////////////////////////////////////////
		process.accept(visitor);
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
		ses.scheduleAtFixedRate(new PeriodicProcessThread(process), 0,
				((ProcessPeriodicSchedule) (process.scheduling)).interval,
				TimeUnit.MILLISECONDS);
		threads.put(process, ses);
	}

	class PeriodicProcessThread implements Runnable {

		private SchedulableProcess process;

		public PeriodicProcessThread(SchedulableProcess process) {
			this.process = process;
		}

		@Override
		public void run() {
			try {
				if (process.contextClassLoader != null)
					Thread.currentThread().setContextClassLoader(
							process.contextClassLoader);
				process.invoke();
			} catch (Exception e) {
				Log.e("Process scheduled exception",e);
			}
		}
	}


}
