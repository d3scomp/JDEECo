package cz.cuni.mff.d3s.deeco.security;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class SecurityTagCollection extends ArrayList<List<KnowledgeSecurityTag>> {

	private static final long serialVersionUID = -246404371591456723L;

	public SecurityTagCollection mergeWith(SecurityTagCollection other) {
		SecurityTagCollection result = new SecurityTagCollection();
		
		if (this.isEmpty()) {
			result.addAll(other);
		} else {
			for (List<KnowledgeSecurityTag> parentList : this) {
				if (other.isEmpty()) {
					result.add(parentList);
				} else {
					for (List<KnowledgeSecurityTag> innerList : other) {
						List<KnowledgeSecurityTag> parentClone = new ArrayList<>();
						parentClone.addAll(parentList);
						parentClone.addAll(innerList);
						result.add(parentClone);
					}	 
				}
			}
		}
		
		return result; 	
	}
}
