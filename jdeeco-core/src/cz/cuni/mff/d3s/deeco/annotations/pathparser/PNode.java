package cz.cuni.mff.d3s.deeco.annotations.pathparser;

import java.io.Serializable;

public class PNode implements Serializable {

	private static final long serialVersionUID = -5958865049373843865L;

	public final Object value; // May be String or PNode
	public final PNode next;

	public PNode(Object value, PNode next) {
		this.value = value;
		this.next = next;
	}
	
	public String toString(){
		return "PNode [value="+value+", next="+next+"]";
	}
	
}
