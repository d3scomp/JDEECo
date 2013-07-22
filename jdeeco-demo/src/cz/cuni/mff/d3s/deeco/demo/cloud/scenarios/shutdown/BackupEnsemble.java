package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.shutdown;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.demo.cloud.scenarios.Snapshot;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

/** backup ensemble for all the application components to be backed-up by the backup scp components */
public class BackupEnsemble extends Ensemble {

	private static final long serialVersionUID = 1L;
	
	@Membership
	public static Boolean membership(
			// ScpComponent backup coordinator
			@In("coord.id") String buId, 
			@In("coord.buAppIds") List<String> buAppIds,
			
			// AppComponent member to be backed up
			@In("member.id") String mId, 
			@In("member.buScpId") String mBuId,
			@In("member.isDeployed") Boolean mIsDeployed
			) {
		if (mIsDeployed && mBuId != null && buAppIds.size() > 0 && buId.equals(mBuId) && buAppIds.contains(mId)){
			return true;
		}
		return false;
	}

	
	@KnowledgeExchange
	@PeriodicScheduling(1000)
	public static void map(
			// ScpComponent backup coordinator
			@In("coord.id") String buId, 
			@In("coord.buAppIds") List<String> buAppIds,
			@InOut("coord.buSnapshots") OutWrapper<List<Snapshot>> buSnapshots,
			
			// AppComponent member to be backed up
			@In("member.id") String mId, 
			@In("member.snapshot") Snapshot snapshot) {
		// if entering the knowledge exchange for the first time
		// then add the snapshot to the backup snapshots array
		if (snapshot != null){
			Snapshot currentSnapshot = null;
			if (buSnapshots.value.size() < buAppIds.size()){
				buSnapshots.value.add(snapshot);
			}else{
				currentSnapshot = buSnapshots.value.get(buAppIds.indexOf(mId));
				if (currentSnapshot.timestamp < snapshot.timestamp)
					buSnapshots.value.set(buAppIds.indexOf(mId), snapshot);
			}
			if (currentSnapshot == null || currentSnapshot.timestamp < snapshot.timestamp)
				System.out.println(buId + ", snapshot from " + mId + ", timestamp=" + snapshot.timestamp);
			else System.out.println(buId + ", keep the snapshot from " + mId); 
		}
	}
}
