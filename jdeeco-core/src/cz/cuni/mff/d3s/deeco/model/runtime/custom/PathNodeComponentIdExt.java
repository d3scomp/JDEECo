/**
 */
package cz.cuni.mff.d3s.deeco.model.runtime.custom;


import cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeComponentIdImpl;

/**
 */
public class PathNodeComponentIdExt extends PathNodeComponentIdImpl {
	
	public PathNodeComponentIdExt() {
		super();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object that) {
		return  that != null && that instanceof PathNodeComponentIdExt;
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
	
} 
