package cz.cuni.mff.d3s.deeco.processor;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;

public class SchedulableComponentProcessWrapper extends
		SchedulableProcessWrapper {

	private MethodDescription process;

	public SchedulableComponentProcessWrapper(Class<?> classDefinition,
			SchedulableComponentProcess schedulableProcess,
			MethodDescription processDescirption) {
		super(classDefinition, schedulableProcess);
		this.process = processDescirption;
	}

	@Override
	public SchedulableProcess extract() {
		try {
			SchedulableComponentProcess scp = (SchedulableComponentProcess) schedulableProcess;
			scp.setProcessMethod(classDefinition.getMethod(process.methodName,
					process.parameterTypes));
			return scp;
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("Extracting method exception");
		}
		return null;
	}

	public static List<SchedulableComponentProcessWrapper> wrapComponentProcess(
			Class<?> c, List<SchedulableComponentProcess> scps) {
		List<SchedulableComponentProcessWrapper> result = new LinkedList<SchedulableComponentProcessWrapper>();
		for (SchedulableComponentProcess scp : scps) {
			result.add(new SchedulableComponentProcessWrapper(c, scp,
					new MethodDescription(scp.getProcessMethod())));
		}
		return result;
	}

}
