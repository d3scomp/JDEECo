package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.exceptions.KMCastException;

/**
 * Accessor used to access Map type of the knowledge.
 * 
 * @author Michal
 * 
 */
public class MapAccessor implements IObjectAccessor {

	private Map<String, ?> target;

	public MapAccessor(Map<String, ?> target) {
		this.target = target;
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
		if (target.containsKey(propertyName))
			return target.get(propertyName);
		else
			throw new KMCastException("Map access exception: " + propertyName);
	}

}
