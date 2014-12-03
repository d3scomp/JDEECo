/**
 * 
 */
package cz.cuni.mff.d3s.deeco.network.connector;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PartitionedBy;
import cz.cuni.mff.d3s.deeco.annotations.TriggerOnChange;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

/**
 * 
 * @author Ondrej Kováč <info@vajanko.me>
 */
@Ensemble
//@PeriodicScheduling(period = 2000)
@PartitionedBy("connector_group")
public class ConnectorEnsemble {
	
	@Membership
	public static boolean membership(
			@In("member.id") String mId, 
			@In("coord.id") String cId,
			@In("member.CONNECTOR_TAG") Integer mTag,
			@In("coord.CONNECTOR_TAG") Integer cTag) {
		return mTag != null && cTag != null && !mId.equals(cId);
	}
	
	@KnowledgeExchange
	public static void exchange(
			@In("coord.id") String cId, 
			@In("member.id") String mId,
			@TriggerOnChange @InOut("member.outputEntries") ParamHolder<Collection<DicEntry>> mOutput,
			@InOut("coord.inputEntries") ParamHolder<Collection<DicEntry>> cInput,
			@In("coord.range") Set<Object> mRange
			) {
		
		//cInput.value.clear();
		
		Iterator<DicEntry> it = mOutput.value.iterator();
		while (it.hasNext()) {
			DicEntry entry = it.next();
			//if (mRange.contains(entry.getKey())) {
				if (!cInput.value.contains(entry))
					cInput.value.add(entry);
				//it.remove();
			//}
		}
	}
}
