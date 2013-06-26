package cz.cuni.mff.d3s.deeco.knowledge;

import java.lang.reflect.Field;

import cz.cuni.mff.d3s.deeco.exceptions.KMCastException;

/**
 * Accessor used to access Map type of the knowledge.
 * 
 * @author Michal
 * 
 */
public class KnowledgeAccessor implements IObjectAccessor {

	private Class<?> clazz;
	private Knowledge target;

	public KnowledgeAccessor(Knowledge target) {
		this.target = target;
		this.clazz = target.getClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.IObjectAccessor#getValue(java.lang.String
	 * )
	 */
	@Override
	public Object getValue(String propertyName) throws KMCastException {
		if (target == null || propertyName == null)
			throw new KMCastException("Wrong parameterTypes: " + propertyName);
		try {
			Field f = clazz.getField(propertyName);
			return f.get(target);
		} catch (Exception e) {
			throw new KMCastException("Field access exception: " + propertyName);
		}
	}

}
