/**
 * Contains common interfaces and classes providing an alternative way of constructing
 * ensembles and performing knowledge exchange that can work alongside the traditional API.
 * 
 * The relationship between the classes is straightforward - the {@link cz.cuni.mff.d3s.deeco.ensembles.EnsembleFactory} 
 * represents an entity responsible for creating objects implementing the 
 * {@link cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance} interface, representing the individual ensemble instances
 * that can be formed in the system. The usage is similar to deploying a traditional ensemble - calling
 * {@link cz.cuni.mff.d3s.deeco.runtime.DEECoContainer#deployEnsembleFactory}. Once the factory is deployed, 
 * the runtime takes care of creating the corresponding tasks and scheduling the instance creation as requested. 
 * After the instances are created, {@link cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance#performKnowledgeExchange} 
 * is invoked on all currently existing ensemble instances. 
 * 
 * @author Filip Krijt
 * @author Zbyněk Jiráček
 *
 */
package cz.cuni.mff.d3s.deeco.ensembles;