package cz.cuni.mff.d3s.deeco.knowledge.jini;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cz.cuni.mff.d3s.deeco.exceptions.KnowledgeRepositoryException;
import cz.cuni.mff.d3s.deeco.exceptions.UnavailableEntryException;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.KPBuilder;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;

public class ChangeNotifier {
	private KnowledgeRepository kr;
	private List<String> toNotify;
	private boolean notifying;

	public ChangeNotifier(KnowledgeRepository kr) {
		this.kr = kr;
		toNotify = new ArrayList<String>();
		notifying = false;
	}

	public void knowledgeWritten(String knowledgePath) {
		if (!notifying) {
			String[] dPath = KPBuilder.decomposePath(knowledgePath);
			if (dPath.length > 0) {
				String current = null;
				int i = 0;
				do {
					current = KPBuilder.appendToRoot(current, dPath[i]);
					if (!toNotify.contains(current))
						toNotify.add(current);
					i++;
				} while (i < dPath.length);
			}
		}
	}

	public void notifyAboutChanges(TransactionalSession session) {
		notifying = true;
		Object value;
		String newVersion = UUID.randomUUID().toString(), listenPath;
		try {
			for (String path : toNotify) {
				listenPath = KPBuilder.prependToRoot(path, ConstantKeys.LISTEN_ID);
				try {
					value = kr.take(listenPath, session);
					value = newVersion;
					kr.put(listenPath, value, session);
				} catch (UnavailableEntryException uee) {
				}
			}
		} catch (KnowledgeRepositoryException kre) {
			System.out.println("Knowledge Repository communication exception!");
		}
		notifying = false;
	}
}
