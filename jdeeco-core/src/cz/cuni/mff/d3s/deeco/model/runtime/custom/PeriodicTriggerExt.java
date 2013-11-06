package cz.cuni.mff.d3s.deeco.model.runtime.custom;

import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.PeriodicTriggerImpl;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class PeriodicTriggerExt extends PeriodicTriggerImpl {

	public PeriodicTriggerExt() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object that) {
		if (that != null && that instanceof PeriodicTrigger) {
			return getPeriod() == ((PeriodicTrigger)that).getPeriod();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int)getPeriod();
	}
}
