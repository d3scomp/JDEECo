package cz.cuni.mff.d3s.deeco.processor;

import java.io.Serializable;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;

public abstract class  SchedulableProcessWrapper implements Serializable {

	protected Class<?> classDefinition;
	protected SchedulableProcess schedulableProcess;
	
	public SchedulableProcessWrapper(Class<?> classDefinition, SchedulableProcess schedulableProcess) {
		this.classDefinition = classDefinition;
		this.schedulableProcess = schedulableProcess;
	}
	
	abstract public SchedulableProcess extract();
}
