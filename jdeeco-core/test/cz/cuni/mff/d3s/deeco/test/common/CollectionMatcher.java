package cz.cuni.mff.d3s.deeco.test.common;

import java.util.Arrays;
import java.util.Collection;

import org.mockito.ArgumentMatcher;

public class CollectionMatcher<T> extends ArgumentMatcher<Collection<T>> {

	protected Collection<T> expected;
	
	@SafeVarargs
	public CollectionMatcher(T... expected) {
		this(Arrays.asList(expected));
	}
	
	public CollectionMatcher(Collection<T> expected) {
		this.expected = expected;
	}
	
	@Override
	public boolean matches(Object argument) {
		@SuppressWarnings("unchecked")
		Collection<T> collection = (Collection<T>) argument;
		return expected.size() == collection.size() && expected.containsAll(collection);
	}
	
}