package cz.cuni.mff.d3s.deeco.runtime.middleware;

public class MiddlewareLink {
	
	private String fromId;
	private String toId;
	private Object distance;
	
	public MiddlewareLink(String fromId, String toId) {
		super();
		this.fromId = fromId;
		this.toId = toId;
	}
	
	public MiddlewareLink(String fromId, String toId, Object distance) {
		super();
		this.fromId = fromId;
		this.toId = toId;
		this.distance = distance;
	}

	public String getFromId() {
		return fromId;
	}
	
	public String getToId() {
		return toId;
	}

	public Object getDistance() {
		return distance;
	}
	
	public void setDistance(Object distance) {
		this.distance = distance;
	}
	
	@Override
	public boolean equals(Object obj) {
		MiddlewareLink ml = (MiddlewareLink) obj;
		// TODO Auto-generated method stub
		return (ml.fromId.equals(fromId) && ml.toId.equals(toId)) || 
				(ml.fromId.equals(toId) && ml.toId.equals(fromId));
	}
}
