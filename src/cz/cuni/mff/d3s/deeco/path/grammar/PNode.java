package cz.cuni.mff.d3s.deeco.path.grammar;

import java.io.Serializable;


public class PNode implements Serializable {
	public Object value = null; //May be String or PNode or EEnsembleParty
	public PNode next = null;
}
