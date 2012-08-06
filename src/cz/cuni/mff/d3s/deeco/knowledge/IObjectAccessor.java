package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.exceptions.KMCastException;

public interface IObjectAccessor {
	public Object getValue(String propertyName) throws KMCastException;
}
