package cz.cuni.mff.d3s.deeco.modes;

/**
 * 
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
public abstract class DEECoMode {
	protected final String id;
	
	public DEECoMode(String id) {
		if(id == null){
			throw new IllegalArgumentException(String.format(
					"The %s argument is null.", "id"));
		}
		
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DEECoMode){
			DEECoMode other = (DEECoMode) obj;
			return this.id.equals(other.id);
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return id;
	}
}
