package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.io.IOException;
import java.io.InputStreamReader;

import cz.cuni.mff.d3s.jdeeco.edl.EDLReader;
import cz.cuni.mff.d3s.jdeeco.edl.validation.EdlValidationException;
import cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3.Z3IntelligentEnsembleFactory;

public class pendolinoEdlFactory extends Z3IntelligentEnsembleFactory {

	public pendolinoEdlFactory() throws IOException, EdlValidationException {		
		super(new EDLReader().readDocument(new InputStreamReader(pendolinoEdlFactory.class.getResourceAsStream("/cz/cuni/mff/d3s/jdeeco/ensembles/intelligent/z3/pendolino.edl"))));		
	}
}