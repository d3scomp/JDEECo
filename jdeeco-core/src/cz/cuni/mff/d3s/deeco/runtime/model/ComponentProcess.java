package cz.cuni.mff.d3s.deeco.runtime.model;

import java.lang.reflect.Method;
import java.util.List;

public class ComponentProcess extends Invocable {
	private Schedule schedule;

	public ComponentProcess(List<Parameter> parameters, Method method,
			Schedule schedule, LockingMode lockingMode) {
		super(parameters, method, lockingMode);
		this.schedule = schedule;
	}

	public ComponentProcess(Method method, Schedule schedule,
			LockingMode lockingMode) {
		super(method, lockingMode);
		this.schedule = schedule;
	}

	public Schedule getSchedule() {
		return schedule;
	}

}
