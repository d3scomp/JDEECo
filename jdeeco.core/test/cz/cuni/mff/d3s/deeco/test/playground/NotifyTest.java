package cz.cuni.mff.d3s.deeco.test.playground;

import java.rmi.MarshalledObject;
import java.rmi.RMISecurityManager;
import java.util.Arrays;

import net.jini.core.lease.Lease;
import net.jini.space.JavaSpace05;
import cz.cuni.mff.d3s.deeco.knowledge.jini.TSUtils;
import cz.cuni.mff.d3s.deeco.knowledge.jini.Tuple;

public class NotifyTest {

	public static void main(String[] args) {
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		try {
			MyRemoteEventListener mrel = new MyRemoteEventListener();
			JavaSpace05 space = TSUtils.getSpace();
			space.write(TSUtils.createTuple("a.b", new Integer(333)), null,
					Lease.FOREVER);
			space.registerForAvailabilityEvent(Arrays.asList(TSUtils.createTemplate("a.b")), null, true,
					mrel.getStub(), Lease.FOREVER,
					new MarshalledObject<Integer>(new Integer(1)));
			Tuple t = (Tuple) space.take(TSUtils.createTemplate("a.b"), null, 0);
			System.out.println(t);
		} catch (Exception e) {
			System.out.println("error");
		}
	}

}
