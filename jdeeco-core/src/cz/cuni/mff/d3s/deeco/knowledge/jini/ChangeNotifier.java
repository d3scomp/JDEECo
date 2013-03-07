package cz.cuni.mff.d3s.deeco.knowledge.jini;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionAccessError;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionUnavailableEntry;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgePathHelper;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;
import cz.cuni.mff.d3s.deeco.path.grammar.EEnsembleParty;

public class ChangeNotifier {
	private KnowledgeRepository kr;
	private List<String> toNotify;
	private List<String> roots;
	private boolean notifying;

	public ChangeNotifier(KnowledgeRepository kr) {
		this.kr = kr;
		toNotify = new ArrayList<String>();
		roots = new ArrayList<String>();
		notifying = false;
	}

	public void knowledgeWritten(String knowledgePath) {
		if (!notifying) {
			String[] dPath = KnowledgePathHelper.decomposePath(knowledgePath);
			if (dPath.length > 0) {
				String current = null;
				int i = 0;
				do {
					current = KnowledgePathHelper.appendToRoot(current, dPath[i]);
					if (!toNotify.contains(current))
						toNotify.add(current);
					i++;
				} while (i < dPath.length);
				if (dPath.length > 0 && !roots.contains(dPath[0]))
					roots.add(dPath[0]);
			}
		}
	}

	public String getOwner() {
		return (roots.size() == 1) ? roots.get(0) : ConstantKeys.UNDEFINED;
	}

	public void notifyAboutChanges(TransactionalSession session) {
		notifying = true;
		String listenPath;
		String newVersion = KnowledgePathHelper.appendToRoot(
				UUID.randomUUID().toString(), getOwner());
		try {
			for (String path : toNotify) {
				listenPath = KnowledgePathHelper.prependToRoot(path,
						ConstantKeys.LISTEN_ID);
				updateVersion(listenPath, session, newVersion);
				listenPath = KnowledgePathHelper.prependToRoot(
						KnowledgePathHelper.replaceHead(path,
								EEnsembleParty.COORDINATOR.toString()),
						ConstantKeys.LISTEN_ID);
				updateVersion(listenPath, session, newVersion);
				listenPath = KnowledgePathHelper.prependToRoot(
						KnowledgePathHelper.replaceHead(path,
								EEnsembleParty.MEMBER.toString()),
						ConstantKeys.LISTEN_ID);
				updateVersion(listenPath, session, newVersion);
			}
		} catch (KRExceptionAccessError kre) {
			System.out.println("Knowledge Repository communication exception!");
		}
		notifying = false;
	}

	private void updateVersion(String path, TransactionalSession session,
			String newVersion) throws KRExceptionAccessError {
		try {
			Object value = kr.take(path, session);
			value = newVersion;
			kr.put(path, value, session);
			System.out.println("Updating: " + path);
		} catch (KRExceptionUnavailableEntry uee) {
			//System.out.println("Updating error: " + path);
		}
	}
}
