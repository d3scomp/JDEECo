package cz.cuni.mff.d3s.deeco.demo.intelligent;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;

public class IntelligentEnsemble implements EnsembleInstance {

	public int trainId;
	
	public IntelligentEnsemble(int trainId) {
		this.trainId = trainId;
		members = new ArrayList<>();
	}
	
	public List<Rescuer> members;

	@Override
	public void performKnowledgeExchange() {
		for (Rescuer member : members) {
			member.trainId = trainId;
		}
	}

}
