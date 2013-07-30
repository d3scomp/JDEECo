package cz.cuni.mff.d3s.deeco.invokable;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.scheduling.ETriggerType;

/**
 * To be used instead of the
 * {@link cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess
 * SchedulableEnsembleProcess} when keeping track of the knowledge fields in the
 * end of every execution is needed (e.g. in case of the
 * {@link cz.cuni.mff.d3s.deeco.scheduling.discrete.DiscreteScheduler
 * DiscreteScheduler}).
 * 
 * @author Ilias Gerostathopoulos
 * 
 */
public class SchedulableEnsembleProcessWrapper extends
		SchedulableProcessWrapper {

	private final SchedulableEnsembleProcess sep;

	public SchedulableEnsembleProcessWrapper(SchedulableProcess sp) {
		super(sp);
		this.sep = (SchedulableEnsembleProcess) sp;
	}

	public void invoke() {
		changedKnowledgePaths = new ArrayList<String>();
		SchedulableProcess.runtime.set(sep.km.getRuntime());
		Object[] rootIds = null;
		try {
			rootIds = (Object[]) sep.km
					.getKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID);
			for (Object oid : rootIds) {
				changedKnowledgePaths
						.addAll(singleInvocationWithOutputMonitoring(
								(String) oid, ETriggerType.COORDINATOR, rootIds));
			}
		} catch (KMException e) {
			e.printStackTrace();
		}
	}

	private List<String> singleInvocationWithOutputMonitoring(String outerId,
			ETriggerType recipientMode, Object[] rootIds) throws KMException {
		List<String> changedPaths = new ArrayList<String>();
		ISession session = null;
		try {
			String cId = null, mId = null;
			if (recipientMode.equals(ETriggerType.COORDINATOR)) {
				cId = outerId;
			} else {
				mId = outerId;
			}
			mloop: for (Object iid : rootIds) {
				if (recipientMode.equals(ETriggerType.COORDINATOR)) {
					mId = (String) iid;
				} else {
					cId = (String) iid;
				}
				session = sep.km.createSession();
				session.begin();
				while (session.repeat()) {
					try {
						Object[] parametersMembership = sep
								.getParameterMethodValues(
										sep.membership.getIn(),
										sep.membership.getInOut(),
										sep.membership.getOut(), session,
										(String) cId, (String) mId);
						if (sep.evaluateMembership(parametersMembership)) {
							Object[] parametersKnowledgeExchange = sep
									.getParameterMethodValues(
											sep.knowledgeExchange.in,
											sep.knowledgeExchange.inOut,
											sep.knowledgeExchange.out, session,
											(String) cId, (String) mId);
							for (Parameter p : sep.knowledgeExchange.inOut) {
								String changedPath = p.kPath.getEvaluatedPath(
										sep.km, cId, mId, session);
								if (!changedPaths.contains(changedPath))
									changedPaths.add(changedPath);
							}
							for (Parameter p : sep.knowledgeExchange.out) {
								String changedPath = p.kPath.getEvaluatedPath(
										sep.km, cId, mId, session);
								if (!changedPaths.contains(changedPath))
									changedPaths.add(changedPath);
							}
							sep.evaluateKnowledgeExchange(parametersKnowledgeExchange);
							sep.putParameterMethodValues(
									parametersKnowledgeExchange,
									sep.knowledgeExchange.inOut,
									sep.knowledgeExchange.out, session,
									(String) cId, (String) mId);
						}
					} catch (KMNotExistentException kmnee) {
						session.cancel();
						continue mloop;
					}
					session.end();
				}
			}
		} catch (KMException kme) {
			if (session != null)
				session.cancel();
			throw kme;
		}
		return changedPaths;
	}
}
