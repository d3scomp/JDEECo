package cz.cuni.mff.d3s.jdeeco.network.utils;

import java.util.Collection;
import java.util.TreeSet;

/**
 * Limited sorted set
 * 
 * Holds elements up to the limit. If the limit is reached the least(first) element is removed.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 * @param <E>
 */
public class LimitedSortedSet<E extends Comparable<E>> extends TreeSet<E> {
	private static final long serialVersionUID = 1L;
	private final int limit;

	/**
	 * Creates limited sorted
	 * 
	 * @param limit
	 *            Maximum number of items in set
	 */
	public LimitedSortedSet(int limit) {
		if (limit < 1) {
			throw new UnsupportedOperationException("Cannot create limited set with limit < 1");
		}

		this.limit = limit;
	}

	/**
	 * Adds element drops all elements that are over the limit
	 */
	@Override
	public boolean add(E e) {
		ensureSpace(1);
		return super.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		ensureSpace(c.size());
		return super.addAll(c);
	}

	/**
	 * Ensures free space in collection
	 * 
	 * Drops old values until the space is enough
	 * 
	 * @param space Required free space
	 */
	private void ensureSpace(int space) {
		if (limit - space < 0) {
			throw new UnsupportedOperationException(String.format(
					"Cannot make space for %d items when collection limit is %d", space, limit));
		}

		while (size() > limit - space) {
			remove(first());
		}
	}
}
