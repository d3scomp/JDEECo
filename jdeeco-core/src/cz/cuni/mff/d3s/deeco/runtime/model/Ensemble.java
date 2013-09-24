package cz.cuni.mff.d3s.deeco.runtime.model;

public class Ensemble {
	private String id;
	private BooleanCondition membership;
	private Exchange knowledgeExchange;
	private Schedule schedule;
	
	public Ensemble(String id, BooleanCondition membership,
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

	public BooleanCondition getMembership() {
		return membership;
	}

	public Exchange getKnowledgeExchange() {
		return knowledgeExchange;
	}

	public Schedule getSchedule() {
		return schedule;
	}
	
	public String getExchangeId() {
		return knowledgeExchange.getId();
	}
	
}
