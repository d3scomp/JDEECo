package cz.cuni.mff.d3s.deeco.runtimelog;

import java.util.HashMap;

/**
 * 
 * @author Ilias Gerostathopoulos
 *
 */
public class EnsembleLogRecord extends RuntimeLogRecord
{
	/**
	 * Construct the {@link EnsembleLogRecord} instance.
	 */
	public EnsembleLogRecord() {
		super("EnsembleTask", new HashMap<String, Object>());
	}
	
	public void setEnsembleName(String ensembleName) {
		recordValues.put("ensembleName", ensembleName);
	} 
	
	
	public void setCoordinatorID(String coordinatorID) {
		recordValues.put("coordinatorID", coordinatorID);
	}
	
	public void setMemberID(String memberID) {
		recordValues.put("memberID", memberID);
	}
	
	public void setMembership(boolean membership) {
		recordValues.put("membership", membership);
	}

}