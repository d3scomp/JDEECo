package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;

public class IntelligentEnsemble implements EnsembleInstance {

	public int trainId;
	
	public IntelligentEnsemble(int trainId) {
		this.trainId = trainId;
		rescuers = new ArrayList<>();
	}
	
	public Rescuer leader;
	
	public List<Rescuer> rescuers;

	@Override
	public void performKnowledgeExchange() {
		if (leader != null) {
			leader.trainId = trainId;
			leader.isLeader = true;
		} else {
			System.out.println("Ensemble " + trainId + " has no leader!!");
		}		
		
		for (Rescuer member : rescuers) {
			member.trainId = trainId;
		}
	}

}
