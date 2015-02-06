package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;

public interface DEECoPluginContainer {
	public <T extends DEECoPlugin> T getPluginInstance(Class<T> clazz) ;	
	public RuntimeFramework getRuntime();
	public void deploy(Object... objs) throws AnnotationProcessorException;
}
