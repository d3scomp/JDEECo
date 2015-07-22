/**
 * Knowledge container and knowledge wrappers provide better interface to component knowledge, than
 * knowledge manager does. 
 * 
 * A knowledge wrapper is represented by the
 * {@link cz.cuni.mff.d3s.deeco.knowledge.container.TrackingKnowledgeWrapper} class.
 * It wraps a {@link cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager} instance and provides methods
 * for acquiring and modifying the knowledge in an object-oriented way. There is also a read-only
 * version for {@link cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager} called
 * {@link cz.cuni.mff.d3s.deeco.knowledge.container.ReadOnlyKnowledgeWrapper}.
 * 
 * When dealing with multiple components at once, knowledge container comes useful. A knowledge
 * container is an instance of the {@link cz.cuni.mff.d3s.deeco.knowledge.container.TrackingKnowledgeContainer}
 * class.
 * 
 * Knowledge wrappers and knowledge container use roles for accessing the knowledge. If a DEECo
 * component implements a particular role R (represented by a class R), one can acquire an instance of R
 * from the knowledge of the component. One can also change the acquired instance and than commit
 * the changes back into the component knowledge.
 * 
 * A role class is defined using the {@link cz.cuni.mff.d3s.deeco.annotations.Role}
 * annotation. A DEECo component implements the role using the
 * {@link cz.cuni.mff.d3s.deeco.annotations.PlaysRole} annotation. For more information
 * about roles, see the respective documentation.
 * 
 * @author Zbyněk Jiráček
 * 
 * @see cz.cuni.mff.d3s.deeco.knowledge.container.TrackingKnowledgeWrapper
 * @see cz.cuni.mff.d3s.deeco.knowledge.container.ReadOnlyKnowledgeWrapper
 * @see cz.cuni.mff.d3s.deeco.knowledge.container.TrackingKnowledgeContainer
 * @see cz.cuni.mff.d3s.deeco.annotations.Role
 * @see cz.cuni.mff.d3s.deeco.annotations.PlaysRole
 *
 */
package cz.cuni.mff.d3s.deeco.knowledge.container;