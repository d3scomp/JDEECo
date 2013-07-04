package cz.cuni.mff.d3s.deeco.runtime.middleware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import cz.cuni.mff.d3s.deeco.knowledge.Component;
import cz.cuni.mff.d3s.deeco.runtime.middleware.network.NetworkComponent;

public class LinkedMiddlewareEntry {
	
	protected ServiceLevelAgreement sla;
	protected List<MiddlewareLink> distanceLinks;
	protected static LinkedMiddlewareEntry middlewareEntry;
	protected Comparator<MiddlewareLink> distanceComparator;
	
	public LinkedMiddlewareEntry() {
		distanceComparator = new MiddlewareLinkComparator();
		sla = new ServiceLevelAgreement();
	}
	
	public Object getDistance(String fromId, String toId) {
		return distanceLinks.get(distanceLinks.indexOf(new MiddlewareLink(fromId, toId))).getDistance();
	}
	
	public ServiceLevelAgreement getSla() {
		return sla;
	}
	
	public String[] distanceLinksToString(){
		String[] matStr = new String[distanceLinks.size()+2];
		matStr[0] = "[src/dest]-[dest/src]-[distance]";
		matStr[1] = " ";
		for (int i = 0; i < distanceLinks.size(); i++) {
			MiddlewareLink link = distanceLinks.get(i);
			matStr[i+2] = "[" + link.getFromId() + "]-[" + link.getToId() + "]-[" + link.getDistance() + "]";
		}
		return matStr;
	}
	
	public String[] distanceLinksToStringWithSLA(){
		ArrayList<String> matStr = new ArrayList<String>();
		matStr.add("[src/dest]-[dest/src]-[distance] from SLA");
		matStr.add(" ");
		for (int i = 0; i < distanceLinks.size(); i++) {
			MiddlewareLink link = distanceLinks.get(i);
			if ((Integer)link.getDistance() <= sla.maxLinkLatency)
				matStr.add("[" + link.getFromId() + "]-[" + link.getToId() + "]-[" + link.getDistance() + "]");
		}
		return matStr.toArray(new String[matStr.size()]);
	}
	
	/**
	 * getMiddlewareEntry returns a singleton of the RandomNetworkDistanceMiddlewareEntry instance.
	 * This returns a non-initialized instance if it has been called for the first time.
	 * Use updateDistanceMatrix with an input set of components for filling in the associated distance matrix
	 * @return middlewareEntry
	 * @author Julien Malvot
	 */
	public static LinkedMiddlewareEntry getMiddlewareEntrySingleton() {
		if (middlewareEntry == null){
			middlewareEntry = new LinkedMiddlewareEntry();
		}
		return middlewareEntry;
	}
	
	public void updateDistanceTopology(List<Component> components,
			Object maxValue) {
		Random rand = new Random();
		distanceLinks = new ArrayList<MiddlewareLink> (components.size());
		for (int i = 0; i < components.size(); i++) {
			// generate the list of distances from one component to the others
			for (int j = 0; j <= i; j++){
				// no loop on same index, symetric between source and destination
				if (i != j){
					Integer max = null;
					Component c1 = components.get(i);
					Component c2 = components.get(j);
					// different treatment for NetworkComponents
					if (NetworkComponent.class.isAssignableFrom(c1.getClass()) 
						&& NetworkComponent.class.isAssignableFrom(c2.getClass())
						// if the network id is not the same, then the latency is close to 150 ms
						// case of the first scenario
						&& !((NetworkComponent) c1).networkId.equals(((NetworkComponent)c2).networkId)){
						max = 150;
					}else{
						max = rand.nextInt((Integer)maxValue);
					}
					distanceLinks.add(new MiddlewareLink(components.get(i).id, components.get(j).id, max));
				}
			}
		}
		// sort the links by distance
		Collections.sort(distanceLinks, distanceComparator);
	}
	
	public List<String> getBestDestinationsFromSource(String srcId, int range) throws Exception {
		if (distanceComparator == null)
			throw new Exception("The distance comparator is not initialized to sort the distance links in the network topology !");
		return getDestinationIds(takeFirstDistanceLinks(getDistanceLinksFromSource(srcId), range), srcId);
	}
	
	/**
	 * applied when the sorting is needed for finding the best distance links
	 * @param srcId
	 * @return
	 */
	/*private List<MiddlewareLink> sortDistanceLinksFromSource(String srcId) {
		List<MiddlewareLink> sortedDistanceLinks = getDistanceLinksFromSource(srcId);
		// sort the list by distance
		Collections.sort(sortedDistanceLinks, distanceComparator);
		// return the list
		return sortedDistanceLinks; 
	}*/
	
	private List<MiddlewareLink> takeFirstDistanceLinks(List<MiddlewareLink> links, int range) {
		return links.subList(0,  range);
	}
	
	private List<String> getDestinationIds(List<MiddlewareLink> links, String srcId) {
		List<String> destIds = new ArrayList<String> ();
		for (MiddlewareLink ml : links) {
			// either the source is the fromId, so add the toId
			if (ml.getFromId().equals(srcId))
				destIds.add(ml.getToId());
			// or the source is the toId, so add the fromId
			else
				destIds.add(ml.getFromId());
		}
		return destIds;
	}
	
	private List<MiddlewareLink> getDistanceLinksFromSource(String srcId) {
		List<MiddlewareLink> distanceLinksFromSource = new ArrayList<MiddlewareLink>();
		for (MiddlewareLink ml : distanceLinks){
			if ((ml.getFromId().equals(srcId) || ml.getToId().equals(srcId)) && (Integer)ml.getDistance() <= sla.maxLinkLatency){
				distanceLinksFromSource.add(ml);
			}
		}
		return distanceLinksFromSource;
	}
}
