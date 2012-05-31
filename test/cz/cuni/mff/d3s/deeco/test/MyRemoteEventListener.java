package cz.cuni.mff.d3s.deeco.test;

import java.rmi.RemoteException;

import cz.cuni.mff.d3s.deeco.knowledge.jini.Tuple;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.export.Exporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.tcp.TcpServerEndpoint;
import net.jini.space.AvailabilityEvent;

public class MyRemoteEventListener implements RemoteEventListener {

	private RemoteEventListener theStub;

	public MyRemoteEventListener() throws RemoteException {
		Exporter myDefaultExporter = new BasicJeriExporter(
				TcpServerEndpoint.getInstance(0), new BasicILFactory(), false,
				true);

		theStub = (RemoteEventListener) myDefaultExporter.export(this);
	}

	RemoteEventListener getStub() {
		return theStub;
	}

	@Override
	public void notify(RemoteEvent re) throws UnknownEventException,
			RemoteException {
		try {
			AvailabilityEvent ae = (AvailabilityEvent) re;
			System.out.println(ae.getRegistrationObject().get());
			System.out.println(ae.getSequenceNumber());
			Tuple t = (Tuple) ae.getEntry();
			System.out.println(re);
		} catch (Exception uee) {
			System.out.println("uee error");
		}
	}

}
