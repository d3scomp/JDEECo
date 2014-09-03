package cz.cuni.mff.d3s.deeco.network;

import java.util.Collection;

public interface DirectGossipStrategy {
	public Collection<String> filterRecipients(Collection<String> recipients);
}
