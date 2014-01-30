/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.custom;


import cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeMapKeyImpl;

/**
 */
public class PathNodeMapKeyExt extends PathNodeMapKeyImpl {
	
	public PathNodeMapKeyExt() {
		super();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object that) {
		if (that != null && that instanceof PathNodeMapKeyExt) {
			return ((PathNodeMapKeyExt) that).getKeyPath().equals(this.getKeyPath());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
	
} 
