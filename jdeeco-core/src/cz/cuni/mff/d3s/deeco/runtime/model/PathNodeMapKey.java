package cz.cuni.mff.d3s.deeco.runtime.model;

public class PathNodeMapKey extends PathNode {
	private KnowledgePath keyPath;

	public PathNodeMapKey(KnowledgePath keyPath) {
		super();
		this.keyPath = keyPath;
	}

	public KnowledgePath getKeyPath() {
		return keyPath;
	}

}
