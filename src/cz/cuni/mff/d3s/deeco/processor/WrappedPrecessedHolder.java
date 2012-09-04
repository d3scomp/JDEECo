package cz.cuni.mff.d3s.deeco.processor;

public class WrappedPrecessedHolder extends ProcessedHolder {

	@Override
	protected void extractComponentProcess(Class<?> clazz, String id) {
		processes.addAll(SchedulableComponentProcessWrapper.wrapComponentProcess(
				clazz, cp.extractComponentProcess(clazz, id)));
	}

	@Override
	protected void extractEnsembleProcess(Class<?> clazz) {
		processes.add(SchedulableEnsembleProcessWrapper.wrapEnsembleProcess(clazz,
				ep.extractEnsembleProcess(clazz)));
	}

}
