/**
 * Intelligent ensembles package contains necessary support to construct ensembles based on logical solving,
 * using scripts written in MiniZinc language. It contains the 
 * {@link cz.cuni.mff.d3s.deeco.ensembles.intelligent.MiniZincIntelligentEnsembleFactory} class which implements
 * the {@link cz.cuni.mff.d3s.deeco.ensembles.EnsembleFactory} interface.
 * 
 * The ensemble construction is conducted in the following steps:
 * 
 * 1) A MiniZinc script is written and added into the project.
 * 
 * 2) Ensemble instance classes are designed and implemented (inherit from
 * {@link cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance}).
 * 
 * 3) An ensemble factory is created, derived from {@link cz.cuni.mff.d3s.deeco.ensembles.intelligent.MiniZincIntelligentEnsembleFactory}.
 * Methods for transforming the knowledge to the script input and for instantiating ensembles based on the script
 * result must be implemented there.
 * 
 * 4) The ensemble factory is registered at DEECo nodes (same as for any other ensemble factory).
 * 
 * @author Filip Krijt
 * @author Zbyněk Jiráček
 *
 */
package cz.cuni.mff.d3s.deeco.ensembles.intelligent;