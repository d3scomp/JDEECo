package cz.cuni.mff.d3s.deeco.simlog;

import java.util.Map;


public interface SnapshotProvider
{
	public Map<String, Object> getSnapshot();
}
