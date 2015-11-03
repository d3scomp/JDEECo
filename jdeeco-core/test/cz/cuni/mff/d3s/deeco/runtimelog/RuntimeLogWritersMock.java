package cz.cuni.mff.d3s.deeco.runtimelog;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.Writer;

/**
 * This class is only for test purposes!
 * Use this class in all tests that use somehow the runtime logging.
 * That means that the test creates some DEECo Nodes.
 * 
 * This class is located here to be easily accessible in all the tests,
 * referencing DEECo core.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class RuntimeLogWritersMock extends RuntimeLogWriters {

	private static Writer out = mock(Writer.class);
	
	public RuntimeLogWritersMock() throws IOException {
		super(out, out, out);
	}
	
	@Override
	void close() throws IOException {
	}
}
