package cz.cuni.mff.d3s.deeco.executor;

import cz.cuni.mff.d3s.deeco.scheduling.Job;

public interface JobExecutionListener {
	void jobExecutionFinished(Job job);
	void jobExecutionStarted(Job job);
	void jobExecutionException(Job job, Throwable t);
}
