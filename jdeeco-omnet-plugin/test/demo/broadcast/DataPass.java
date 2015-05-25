package demo.broadcast;


import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;


@Ensemble
@PeriodicScheduling(period=100)
public class DataPass {
	@Membership
	public static boolean membership(
			@In("member.id") String memberId,
			@In("coord.id") String coordId) {
		
		return !memberId.equals(coordId);
	}

	@KnowledgeExchange
	public static void map(
			@Out("member.value") ParamHolder<Integer> value,
			@In("coord.source") Integer source) {
		value.value = source;
	}
}
