package cz.cuni.mff.d3s.deeco.processor;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;

public class SchedulableEnsembleProcessWrapper extends
		SchedulableProcessWrapper {

	private MethodDescription membership;
	private MethodDescription mapper;

	public SchedulableEnsembleProcessWrapper(Class<?> classDefinition,
			SchedulableEnsembleProcess schedulableProcess,
			MethodDescription membership, MethodDescription mapper) {
		super(classDefinition, schedulableProcess);
		this.membership = membership;
		this.mapper = mapper;
	}

	@Override
	public SchedulableProcess extract() {
		try {
			SchedulableEnsembleProcess sep = (SchedulableEnsembleProcess) schedulableProcess;
			sep.setMapperMethod(classDefinition.getMethod(mapper.methodName,
					mapper.parameterTypes));
			sep.setMembershipMethod(classDefinition.getMethod(
					membership.methodName, membership.parameterTypes));
			return sep;
		} catch (Exception e) {
			System.out.println("Extracting method exception");
		}
		return null;
	}

	public static SchedulableEnsembleProcessWrapper wrapEnsembleProcess(
			Class<?> c, SchedulableEnsembleProcess sep) {
		return new SchedulableEnsembleProcessWrapper(c, sep,
				new MethodDescription(sep.getMembershipMethod()),
				new MethodDescription(sep.getMapperMethod()));
	}

}
