package cz.cuni.mff.d3s.jdeeco.network.utils;

/**
 * Interface for objects sorted in time
 * 
 * Used to implement caches that drop oldest items
 * 
 * @see LimitedSet
 *
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 * 
 */
public interface TimeSorted<T> extends Comparable<T> {

}
