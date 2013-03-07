package cz.cuni.mff.d3s.deeco.knowledge.local;

import java.util.HashMap;
import java.util.List;

/**
 * Interface for separating atomic proposition API from the internals of knowledge repository implementation.
 * 
 * @author Jaroslav Keznikl
 *
 */
public interface KnowledgeJPF {
	
	
	public Object getSingle(String knowledgeId);
	
	public Object [] get(String knowledgeId);

}
