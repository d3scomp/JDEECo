package cz.cuni.mff.d3s.deeco.runtime.model;

import java.lang.reflect.Method;
import java.util.List;

public class ComponentProcess extends Invocable {
	private final Schedule schedule;
	private final String id;

	public ComponentProcess(String id, List<Parameter> parameters, Method method,
			Schedule schedule, LockingMode lockingMode) {
		super(parameters, method, lockingMode);
		this.schedule = schedule;
		this.id = id;
	}

	public ComponentProcess(String id, Method method, Schedule schedule,
			LockingMode lockingMode) {
		super(method, lockingMode);
		this.schedule = schedule;
		this.id = id;
	}

	public Schedule getSchedule() {
		return schedule;
	}
	
	public String getId() {
		return id;
	}

}
