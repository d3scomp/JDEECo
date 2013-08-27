package cz.cuni.mff.d3s.deeco.knowledge.jini;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.export.Exporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.tcp.TcpServerEndpoint;
import net.jini.space.AvailabilityEvent;
import cz.cuni.mff.d3s.deeco.knowledge.IKnowledgeChangeListener;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryChangeNotifier;
import cz.cuni.mff.d3s.deeco.logging.Log;

public class TSRepositoryChangeNotifier implements RemoteEventListener,
		RepositoryChangeNotifier {

	private RemoteEventListener stub;
	private List<IKnowledgeChangeListener> toNotify;
	private String lastProcessed = null;
	private KnowledgeRepository kr;

	private TSRepositoryChangeNotifier() throws RemoteException {
		Exporter exporter = new BasicJeriExporter(
				TcpServerEndpoint.getInstance(0), new BasicILFactory(), false,
				true);
		stub = (RemoteEventListener) exporter.export(this);
		toNotify = new ArrayList<IKnowledgeChangeListener>();
	}

	public static TSRepositoryChangeNotifier getRemoteEventListener(
			KnowledgeRepository kr) {
		try {
			TSRepositoryChangeNotifier tsre = new TSRepositoryChangeNotifier();
			tsre.kr = kr;
			return tsre;
		} catch (Exception e) {
			return null;
		}
	}

	public RemoteEventListener getStub() {
		return stub;
	}

	@Override
	public void addKnowledgeChangeListener(IKnowledgeChangeListener listener) {
		if (!toNotify.contains(listener))
			toNotify.add(listener);
	}

	@Override
	public void notify(RemoteEvent re) throws UnknownEventException,
			RemoteException {
		if (kr.isTriggeringActive()) {
			try {
				AvailabilityEvent ae = (AvailabilityEvent) re;
				Tuple t = (Tuple) ae.getEntry();
				lastProcessed = kr.notify(t.key, lastProcessed, toNotify);
			} catch (UnusableEntryException e) {
				Log.e("TSRepositoryChangeNotifier", e);
			}
		}
	}

}
