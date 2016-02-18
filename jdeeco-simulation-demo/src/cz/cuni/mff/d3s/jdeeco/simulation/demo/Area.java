package cz.cuni.mff.d3s.jdeeco.simulation.demo;

public abstract class Area {
	private final String[] teams;
	private final String id;
	
	protected final double TOLERANCE = 0;

	
	public Area(String id, String[] teams) {
		this.teams = teams;
		this.id = id;
	}

	public String[] getTeams() {
		return teams;
	}	
	
	public String getId() {
		return id;
	}

	public abstract boolean isInArea(Position pos);
	
	public abstract double getCenterX();
	public abstract double getCenterY();
	
}
