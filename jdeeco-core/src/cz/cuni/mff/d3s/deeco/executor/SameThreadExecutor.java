package cz.cuni.mff.d3s.deeco.executor;


import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Transition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ProcessConditionParser;
import cz.cuni.mff.d3s.deeco.task.ProcessTask;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * Executor that reuses the thread context in which the {@link #execute(Task)} was called. 
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class SameThreadExecutor implements Executor {

	protected ExecutionListener listener = null;
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * The call is blocking, executes the task in the thread, which the method was called in.
	 * </p>
	 */
	@Override
	public void execute(Task task, Trigger trigger) {
		if (task != null) {
			try {
				
			   	if(task instanceof ProcessTask){
			   		
					ProcessTask newTask = (ProcessTask)task;
					ComponentProcess process = newTask.getComponentProcess();
					ComponentInstance componentInstance = process.getComponentInstance();
					KnowledgeManager km = process.getComponentInstance().getKnowledgeManager();

					boolean hasStates = !process.getTransitions().isEmpty();
					if(hasStates){
						ProcessConditionParser.initStates(componentInstance);
						Transition transition = ProcessConditionParser.returnTransitions(componentInstance,km, process);
						if(transition != null){
//							System.out.println(".....................................................................reset........"+transition);
							ProcessConditionParser.resetTransitions(componentInstance,process,transition);
						}
						
						if(process.isIsRunning()){
							task.invoke(trigger);
						}
					}else
						task.invoke(trigger);
					
			   	}else
			   		task.invoke(trigger);
									
			   	
			} catch (Exception e) {				
				if (listener != null) {
					Log.w("Task.invoke() failed", e);
					listener.executionFailed(task, e);
				}
				return;
			}			
			
			if (listener != null) {
				listener.executionCompleted(task);
			}
		}
	}

	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.executor.Executor#setExecutionListener(cz.cuni.mff.d3s.deeco.executor.ExecutionListener)
	 */
	@Override
	public void setExecutionListener(ExecutionListener listener) {
		this.listener = listener;  
	}

}
