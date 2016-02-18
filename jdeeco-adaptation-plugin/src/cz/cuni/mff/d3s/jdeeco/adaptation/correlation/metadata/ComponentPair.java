package cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metadata;

import java.io.Serializable;

/**
 * Holds a pair of components IDs.
 * Two {@link ComponentPair}s can be compared and they are equal whenever
 * they contain the same components IDs (the ordering of which doesn't matter).
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class ComponentPair implements Serializable {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 14064809845208208L;
	
	/**
	 * The ID of the first component.
	 */
	public final String component1Id;
	/**
	 * The ID of the second component.
	 */
	public final String component2Id;
	
	/**
	 * Creates a new instance of {@link ComponentPair} for the given components IDs.
	 * The ordering of the IDs doesn't matter.
	 * @param component1Id The ID of the first component.
	 * @param component2Id The ID of the second component.
	 */
	public ComponentPair(String component1Id, String component2Id){
		if(component1Id == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "component1Id"));
		if(component2Id == null) throw new IllegalArgumentException(
				String.format("The \"%s\" argument is null.", "component2Id"));
		
		this.component1Id = component1Id;
		this.component2Id = component2Id;
	}
	
	/**
	 * Two {@link ComponentPair}s are equal if they contain the same
	 * components IDs.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object otherPair){
		if(otherPair == null){
			return false;
		}
		if(!(otherPair instanceof ComponentPair)){
			return false;
		}
		
		ComponentPair other = (ComponentPair) otherPair;
		
		return (this.component1Id.equals(other.component1Id)
				&& this.component2Id.equals(other.component2Id))
				|| (this.component1Id.equals(other.component2Id)
						&& this.component2Id.equals(other.component1Id));
	}
	
	@Override
	public int hashCode() {
		String smaller, bigger;
		if(component1Id.hashCode() < component2Id.hashCode()){
			smaller = component1Id;
			bigger = component2Id;
		} else {
			smaller = component2Id;
			bigger = component1Id;
		}
		return String.format("%s;%s", smaller, bigger).hashCode();
	}
}
