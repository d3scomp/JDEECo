package cz.cuni.mff.d3s.deeco.simulation.matsim;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;

public class MATSimOutput {
	public Id currentLinkId;
	public Coord estPosition;
	
	public MATSimOutput(Id currentLinkId, Coord estPostion) {
		this.currentLinkId = currentLinkId;
		this.estPosition = estPostion;
	}
	
	
}
