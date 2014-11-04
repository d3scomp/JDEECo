package cz.cuni.mff.d3s.deeco.network;

import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;

public abstract class AbstractHost implements HostKnowledgeDataHandler, CurrentTimeProvider {

	protected final String id;
	protected final CurrentTimeProvider timeProvider;
	
	protected AbstractHost(String id, CurrentTimeProvider timeProvider) {
		this.id = id;
		this.timeProvider = timeProvider;
	}
	
	public String getHostId() {
		return id;
	}
	
	public long getCurrentMilliseconds() {
		return timeProvider.getCurrentMilliseconds();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractHost other = (AbstractHost) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
