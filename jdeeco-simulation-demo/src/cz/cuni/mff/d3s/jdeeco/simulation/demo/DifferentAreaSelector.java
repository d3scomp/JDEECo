package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.DeecoProperties;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.network.DirectRecipientSelector;
import cz.cuni.mff.d3s.deeco.network.KnowledgeData;

class DifferentAreaSelector implements DirectRecipientSelector {

	private Set<String> ethernetEnabled = null;
	private AreaNetworkRegistry networkRegistry = null;
	private boolean returnAll = false;
	
	public void initialize(Set<String> ethernetEnabled, AreaNetworkRegistry networkRegistry) {
		this.ethernetEnabled = ethernetEnabled;
		this.networkRegistry = networkRegistry;
		
		returnAll = Boolean.getBoolean(DeecoProperties.DISABLE_BOUNDARY_CONDITIONS);
	}
	
	@Override
	public Collection<String> getRecipients(KnowledgeData data,
			ReadOnlyKnowledgeManager sender) {
		// when 'returnAll' or there is just one area, return everyone
		if (returnAll || (networkRegistry.getAreas().size() <= 1)) {
			return new ArrayList<>(ethernetEnabled);
		}
		List<String> recipients = new ArrayList<>();
		KnowledgePath kpTeam = KnowledgePathBuilder.buildSimplePath("teamId");
		String ownerTeam = (String) data.getKnowledge().getValue(kpTeam);
		if (ownerTeam != null) {
			//Find all areas of my team
			List<Area> areas = networkRegistry.getTeamSites(ownerTeam);				
			
			Collection<PositionAwareComponent> candidateComponents = new ArrayList<>();
			// handle teams deployed outside specifically
			if (areas.isEmpty()) {
				candidateComponents.addAll(networkRegistry.getComponentsOutside());
			} else {
				// get all components in those areas
				for (Area a: areas) {
					candidateComponents.addAll(networkRegistry.getComponentsInArea(a));
				}
			}
			// return all candidate components that are not the sender and are "ethernet-enabled"
			for (PositionAwareComponent c: candidateComponents) {
				recipients.add(c.id);
			}
			
			Log.d("Possible direct recipients for " + ownerTeam + " at " + sender.getId() + " are " + Arrays.deepToString(recipients.toArray()));
		}
		return recipients;
	}
	
}