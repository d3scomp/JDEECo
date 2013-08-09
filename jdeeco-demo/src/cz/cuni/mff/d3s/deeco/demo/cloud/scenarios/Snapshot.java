package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios;

import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;

/**
 * The knowledge snapshot is a capture of the node knowledge into bytes
 * at a given time. This is operated in a component process which
 * periodically saves its state into the snapshot.
 * 
 * The code is taken from {@link Snapshot.java http://svn.pst.ifi.lmu.de/svn/scp/tags/SCPi_v_1.0.0/eu.ascens_ist.cloud.knowledge/src/eu/ascens_ist/cloud/knowledge/model/Snapshot.java}
 * 
 * @author Julien Malvot
 *
 */
// TODO: the initial type was Instant (see 
// http://svn.pst.ifi.lmu.de/svn/scp/tags/SCPi_v_1.0.0/eu.ascens_ist.cloud.knowledge/src/eu/ascens_ist/cloud/knowledge/model/Snapshot.java
// shall it be adapted to it?
public class Snapshot extends Knowledge {
	
	private static final long serialVersionUID = 1L;

	/** timestamp describing the time when the snapshot has been captured */
	public int timestamp;
	/** data of the snapshot in bytes provided from the user with a ByteArrayOutputStream */
	public byte[] bytes;

	public Snapshot() {
	}
	
	/**
	 * 
	 * @param timestamp
	 * @param bytes
	 */
	public Snapshot(int timestamp, byte[] bytes) {
		this.timestamp= timestamp;
		this.bytes= bytes;
	}
}
