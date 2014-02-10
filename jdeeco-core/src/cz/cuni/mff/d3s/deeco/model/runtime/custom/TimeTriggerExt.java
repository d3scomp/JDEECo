package cz.cuni.mff.d3s.deeco.model.runtime.custom;


import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.TimeTriggerImpl;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class TimeTriggerExt extends TimeTriggerImpl {

	public TimeTriggerExt() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object that) {
		if (that != null && that instanceof TimeTrigger) {
			TimeTrigger t = (TimeTrigger)that;
			return (getPeriod() == t.getPeriod()) && (getOffset() == t.getOffset());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int)getPeriod() & (int)(getOffset());
	}
}
