package cz.cuni.mff.d3s.deeco.model;

public abstract class KnowledgeType {
	protected Class<?> clazz;
	
	protected KnowledgeType(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
	
	public Object newInstance() throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}
}
