package cz.cuni.mff.d3s.deeco.demo.cloud.scenarios;

import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;

public class Snapshot extends Knowledge {
	
	private static final long serialVersionUID = 1L;

	// TODO: the initial type was Instant (see 
	// http://svn.pst.ifi.lmu.de/svn/scp/tags/SCPi_v_1.0.0/eu.ascens_ist.cloud.knowledge/src/eu/ascens_ist/cloud/knowledge/model/Snapshot.java
	// shall it be adapted to it?
	public int timestamp;

	public byte[] bytes;

	public Snapshot() {
	}
	
	public Snapshot(int timestamp, byte[] bytes) {
		this.timestamp= timestamp;
		this.bytes= bytes;
	}
/*
	public byte[] getBytes() {
		return bytes;
	}

	public int getTimestamp() {
		return timestamp;
	}*/
}
