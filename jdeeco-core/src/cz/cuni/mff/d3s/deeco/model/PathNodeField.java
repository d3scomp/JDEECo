package cz.cuni.mff.d3s.deeco.model;

public class PathNodeField extends PathNode {
	private String name;

	public PathNodeField(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
