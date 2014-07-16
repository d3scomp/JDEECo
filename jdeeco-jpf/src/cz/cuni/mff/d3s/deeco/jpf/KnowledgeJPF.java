package cz.cuni.mff.d3s.deeco.jpf;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

import java.util.Collection;

/**
 * Interface for separating atomic proposition API from the internals of knowledge repository implementation.
 * 
 * @author Jaroslav Keznikl
 *
 */
public interface KnowledgeJPF 
{
    /**
     * Retrieves knowledge value for the knowledge path indicated by the given string.
     * The string is prefixed by the target component's id in the following way: 'mycomponent.parent.child.grandchild'.
     *
     * @param knowledgePath
     *         the knowledge path to return
     * @return object containing value for the specified knowledge path
     * @throws cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException
     *             when there is no value for the given knowledge path
     */
    public Object get(String knowledgePath)
            throws KnowledgeNotFoundException;
}
