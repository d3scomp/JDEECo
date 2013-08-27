package cz.cuni.mff.d3s.deeco.runtime.model;

public class Ensemble {
	private String id;
	private Condition membership;
	private Exchange knowledgeExchange;
	private Schedule schedule;
	
	public Ensemble(String id, Condition membership,
			Exchange knowledgeExchange, Schedule schedule) {
		super();
		this.id = id;
		this.membership = membership;
		this.knowledgeExchange = knowledgeExchange;
		this.schedule = schedule;
	}

	public String getId() {
		return id;
	}

	public Condition getMembership() {
		return membership;
	}

	public Exchange getKnowledgeExchange() {
		return knowledgeExchange;
	}

	public Schedule getSchedule() {
		return schedule;
	}
	
}
