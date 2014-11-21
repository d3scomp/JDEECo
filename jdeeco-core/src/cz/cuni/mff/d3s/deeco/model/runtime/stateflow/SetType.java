package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ExecutionType;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

public class SetType {

	public String name = "";
	public List<SetType> children = new ArrayList<SetType>();
	public List<KnowledgePath> KPs = new ArrayList<KnowledgePath>();
	public ExecutionType exeType = ExecutionType.EXECLUSIVE;
	public boolean isActive = false;
}
