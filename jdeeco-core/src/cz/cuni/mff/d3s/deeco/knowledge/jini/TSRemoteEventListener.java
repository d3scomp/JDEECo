package cz.cuni.mff.d3s.deeco.knowledge.jini;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.export.Exporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.tcp.TcpServerEndpoint;
import net.jini.space.AvailabilityEvent;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgePathHelper;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;
import cz.cuni.mff.d3s.deeco.logging.LoggerFactory;
import cz.cuni.mff.d3s.deeco.scheduling.ETriggerType;
import cz.cuni.mff.d3s.deeco.scheduling.IKnowledgeChangeListener;

public class TSRemoteEventListener implements RemoteEventListener {

	private RemoteEventListener stub;
	private List<IKnowledgeChangeListener> toNotify;
	private String lastProcessed = null;
	private KnowledgeRepository kr;

	private TSRemoteEventListener() throws RemoteException {
		Exporter exporter = new BasicJeriExporter(
				TcpServerEndpoint.getInstance(0), new BasicILFactory(), false,
				true);
		stub = (RemoteEventListener) exporter.export(this);
		toNotify = new ArrayList<IKnowledgeChangeListener>();
	}

	public static TSRemoteEventListener getRemoteEventListener(
			KnowledgeRepository kr) {
		try {
			TSRemoteEventListener tsre = new TSRemoteEventListener();
			tsre.kr = kr;
			return tsre;
		} catch (Exception e) {
			return null;
		}

	}

	public RemoteEventListener getStub() {
		return stub;
	}

	public void addKCListener(IKnowledgeChangeListener listener) {
		if (!toNotify.contains(listener))
			toNotify.add(listener);
	}

	@Override
	public void notify(RemoteEvent re) throws UnknownEventException,
			RemoteException {
		if (kr.isListenersActive()) {
			TransactionalSession ts = null;
			try {
				AvailabilityEvent ae = (AvailabilityEvent) re;
				Tuple t = (Tuple) ae.getEntry();
				String stringVersionAndOwner, version, owner;
				String[] versionOwner;
				Object[] tObjects;
				ExecutorService es;
				ts = (TransactionalSession) kr.createSession();
				ts.begin();
				while (ts.repeat()) {
					tObjects = kr.get(t.key, ts);
					stringVersionAndOwner = (String) tObjects[0];
					versionOwner = extractVersionOwner(stringVersionAndOwner);
					// LoggerFactory.getLogger().fine("Triggered: " + t.key + " " +
					// stringVersionAndOwner);
					if (versionOwner != null) {
						version = versionOwner[0];
						owner = versionOwner[1];
						if (!version.equals(lastProcessed)) {
							es = Executors.newFixedThreadPool(toNotify.size());
							es.invokeAll(getThreadCollection(owner,
									getTriggerRecipient(t.key)));
							es.awaitTermination(
									TransactionalSession.DEFAULT_TRANSACTION_TIMEOUT,
									TimeUnit.MILLISECONDS);
							lastProcessed = version;
						}
					}
					ts.end();
				}
			} catch (Exception e) {
				if (ts != null)
					ts.cancel();
				LoggerFactory.getLogger().severe("Notification exception",e);
			}
		}
	}

	private List<Callable<Object>> getThreadCollection(String triggerer,
			ETriggerType triggerRecipient) {
		List<Callable<Object>> result = new ArrayList<Callable<Object>>();
		for (IKnowledgeChangeListener ikcl : toNotify) {
			result.add(Executors.callable(new TriggeredThread(ikcl, triggerer,
					triggerRecipient)));
		}
		return result;
	}

	private String[] extractVersionOwner(String string) {
		String[] dString = KnowledgePathHelper.decomposePath(string);
		if (dString.length == 2) { // correct format
			return dString;
		}
		return null;
	}

	private ETriggerType getTriggerRecipient(String listenKey) {
		String[] dString = KnowledgePathHelper.decomposePath(listenKey);
		if (dString.length > 2) { // correct format
			return ETriggerType.fromString(dString[1]);
		}
		return null;
	}

	class TriggeredThread implements Runnable {

		private IKnowledgeChangeListener ikcl;
		private String triggererId;
		private ETriggerType triggerRecipient;

		public TriggeredThread(IKnowledgeChangeListener ikcl,
				String triggererId, ETriggerType triggerRecipient) {
			this.ikcl = ikcl;
			this.triggererId = triggererId;
			this.triggerRecipient = triggerRecipient;
		}

		@Override
		public void run() {
			ikcl.knowledgeChanged(triggererId, triggerRecipient);
		}

	}

}
