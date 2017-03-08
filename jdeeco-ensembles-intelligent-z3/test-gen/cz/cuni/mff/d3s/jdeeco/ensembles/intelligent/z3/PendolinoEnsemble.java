package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.util.ArrayList;
import java.util.List;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;
import cz.cuni.mff.d3s.jdeeco.edl.functions.*;
import java.util.stream.*;

public abstract class PendolinoEnsemble implements EnsembleInstance {
	// Ensemble ID
	public final Integer trainId;
	
	public PendolinoEnsemble(Integer trainId) {
		this.trainId = trainId;
		rescuers = new ArrayList<>();
	}
	
	// Aliases
	
	// Ensemble roles		
	public Rescuer leader;
	public List<Rescuer> rescuers;
	public FireFighter firefighter;

}