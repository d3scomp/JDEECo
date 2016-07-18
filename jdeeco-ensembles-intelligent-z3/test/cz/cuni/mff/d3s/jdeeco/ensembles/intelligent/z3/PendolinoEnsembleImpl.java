package cz.cuni.mff.d3s.jdeeco.ensembles.intelligent.z3;

public final class PendolinoEnsembleImpl extends PendolinoEnsemble {
	
	public PendolinoEnsembleImpl(int trainId) {
		super(trainId);
	}
	
	// Knowledge exchange
	
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
		
		if (firefighter != null) {
			firefighter.trainId = trainId;
		} 
	}
}