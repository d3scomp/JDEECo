package cz.cuni.mff.d3s.deeco;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DummyTest {

	Dummy dummy;
	
	@Before
	public void setUp() throws Exception {
		dummy = new Dummy();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertEquals("hello", dummy.hello());
	}

}
