package cz.cuni.mff.d3s.deeco.runtime.middleware;

import java.util.Comparator;

public class MiddlewareLinkComparator implements Comparator<MiddlewareLink> {

	@Override
	public int compare(MiddlewareLink ml1, MiddlewareLink ml2) {
		Object d1 = ml1.getDistance();
		Object d2 = ml2.getDistance();
		if (d1.getClass().equals(Integer.class) && d2.getClass().equals(Integer.class)){
			return Integer.compare((Integer)d1, (Integer)d2);
		}
		return 0;
	}
}
