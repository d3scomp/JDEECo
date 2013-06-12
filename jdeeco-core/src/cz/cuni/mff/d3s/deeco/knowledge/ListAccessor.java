package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.KMCastException;

public class ListAccessor implements IObjectAccessor {

	private List<?> target;

	public ListAccessor(List<?> target) {
		this.target = target;
	}

	@Override
	public Object getValue(String propertyName) throws KMCastException {
		try {
			return target.get(Integer.parseInt(propertyName));
		} catch (Exception e) {
			throw new KMCastException("List access exception: " + propertyName);
		}
	}

}
