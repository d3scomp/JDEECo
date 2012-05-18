package cz.cuni.mff.d3s.deeco.demo.convoy;

import java.rmi.RMISecurityManager;
import java.util.Arrays;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;
import cz.cuni.mff.d3s.deeco.knowledge.TSUtils;
import cz.cuni.mff.d3s.deeco.knowledge.TransactionUtils;
import cz.cuni.mff.d3s.deeco.knowledge.Tuple;

public class Test {

	/**
	 * @param args
	 */

	static class A {
		public String a;
	}

	static class B extends A {
		public String b;
	}

	public static void main(String[] args) {
		System.out.println("Printing:");
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		try {
			// TSKnowledgeRepository km = new TSKnowledgeRepository();
			// RootKnowledge initialKnowledge =
			// RobotFollowerComponent.getInitialKnowledge();
			// km.writeKnowledge(initialKnowledge);
			// km.registerRootKnowledge(initialKnowledge.id);
			JavaSpace05 space = TSUtils.getSpace();
			Transaction tx = TransactionUtils.createTransaction();
			MatchSet result = space.contents(Arrays
					.asList(new Object[] { TSUtils.createTemplate(null) }), tx,
					Lease.FOREVER, Long.MAX_VALUE);
			Tuple t = (Tuple) result.next();
			System.out.println("Printing:");
			while (t != null) {
				System.out.println(t.key + ": " + t.value);
				t = (Tuple) result.next();
			}
			Class tPath = (Class) ((Tuple) space.readIfExists(
					TSUtils.createTemplate("2.path.typeDefinition"), tx,
					Lease.FOREVER)).value;
			System.out.println(tPath.equals(Path.class));
			tx.commit();
		} catch (Exception e) {
			System.out.println("sdfasdfasdf");
		}
	}

}
