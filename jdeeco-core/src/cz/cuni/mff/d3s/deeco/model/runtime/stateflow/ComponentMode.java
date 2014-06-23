package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

public abstract class ComponentMode {
	
	public String name = null;
	public Boolean isActive = null;
	
	
	public abstract void entry();
	public abstract void during();
	public abstract void exit();
	
}
