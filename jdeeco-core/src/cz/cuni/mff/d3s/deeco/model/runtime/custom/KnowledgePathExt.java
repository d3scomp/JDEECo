/**
 * 
 */
package cz.cuni.mff.d3s.deeco.model.runtime.custom;

import org.eclipse.emf.ecore.util.EcoreUtil;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.KnowledgePathImpl;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class KnowledgePathExt extends KnowledgePathImpl {

	/**
	 * 
	 */
	public KnowledgePathExt() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object that) {
		if (that instanceof KnowledgePath) {
			return EcoreUtil.equals(this, (KnowledgePath)that);
		}
		
		return false;
	}
}
