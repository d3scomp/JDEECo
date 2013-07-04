package cz.cuni.mff.d3s.deeco.invokable;

import java.util.ArrayList;

import cz.cuni.mff.d3s.deeco.knowledge.ISession;

/**
 * To be used instead of the
 * {@link cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess
 * SchedulableComponentProcess} when keeping track of the knowledge fields in
 * the end of every execution is needed (e.g. in case of the
 * {@link cz.cuni.mff.d3s.deeco.scheduling.discrete.DiscreteScheduler
 * DiscreteScheduler}).
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class SchedulableComponentProcessWrapper extends
		SchedulableProcessWrapper {

	private final SchedulableComponentProcess scp;
	
	public SchedulableComponentProcessWrapper(SchedulableProcess sp) {
		super(sp);
		this.scp = (SchedulableComponentProcess) sp;
	}

	public void invoke() {
		scp.invoke(null, null);
		findChangedKnowledgePaths();
	}

	public void findChangedKnowledgePaths() {
		changedKnowledgePaths = new ArrayList<String>();
		for (Parameter par : scp.process.inOut) {
			changedKnowledgePaths.add(getKnowledgePath(scp, par));
		}
		for (Parameter par : scp.process.out) {
			changedKnowledgePaths.add(getKnowledgePath(scp, par));
		}
	}

	public String getKnowledgePath(SchedulableComponentProcess scp,
			Parameter par) {
		String path = null;
		String coord = null;
		String member = null;
		ISession session = scp.km.createSession();
		session.begin();
		while (session.repeat()) {
			path = par.kPath.getEvaluatedPath(scp.km, coord, member, session);
			session.end();
		}
		return path;
	}
}
