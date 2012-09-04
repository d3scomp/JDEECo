package cz.cuni.mff.d3s.deeco.processor;

public class UnwrappedProcessedHolder extends ProcessedHolder {

	@Override
	protected void extractComponentProcess(Class<?> clazz, String id) {
		processes.addAll(cp.extractComponentProcess(clazz, id));

	}

	@Override
	protected void extractEnsembleProcess(Class<?> clazz) {
		processes.add(ep.extractEnsembleProcess(clazz));
	}

}
