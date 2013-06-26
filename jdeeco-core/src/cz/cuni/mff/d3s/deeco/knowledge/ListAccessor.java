package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.KMCastException;

/**
 * Accessor used to access List type of the knowledge.
 * 
 * @author Michal
 * 
 */
public class ListAccessor implements IObjectAccessor {

	private List<?> target;

	public ListAccessor(List<?> target) {
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
		try {
			return target.get(Integer.parseInt(propertyName));
		} catch (Exception e) {
			throw new KMCastException("List access exception: " + propertyName);
		}
	}

}
