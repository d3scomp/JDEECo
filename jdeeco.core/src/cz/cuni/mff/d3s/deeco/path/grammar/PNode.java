package cz.cuni.mff.d3s.deeco.path.grammar;

import java.io.Serializable;


public class PNode implements Serializable {
	
	private static final long serialVersionUID = -5958865049373843865L;
	
	public final Object value; //May be String or PNode or EEnsembleParty
	public final PNode next;
	
	
	public PNode(Object value, PNode next) {
		this.value = value;
		this.next = next;
	}
	
	
	
	@Override
	public String toString() {
		return "PNode [value=" + value + ", next=" + next + "]";
	}



	/**
	 * Traverses the list created by {@link PNode#next} until it reach end
	 *    (the {@code null}) and then makes a copy of them
	 *
	 * @param root {@link PNode#next} list where to add suffix.
	 * @param suffix Suffix to add.
	 * @return Gets new copy of PNode list with appended suffix.
	 */
	public static PNode appendSuffix(PNode root, PNode suffix) {
		if (root == null) {
			return null;
		}
		if (root.next == null) {
			// Reach and of the list
			return new PNode(root.value, suffix);
		};
		
		return new PNode(root.value, appendSuffix(root.next, suffix));
	}
	
}
