package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.matches;
import static org.mockito.Mockito.refEq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.ensembles.intelligent.ScriptInputVariableRegistry.Entry;
import cz.cuni.mff.d3s.deeco.test.common.CollectionMatcher;

public class MznScriptRunnerTest {

	private String mzn2fznPath;
	private String fznSolverPath;

	@Before
	public void getPropertiesTest() throws FileNotFoundException, IOException {
		Properties props = MznScriptRunner.getProperties();
		assertTrue(props.containsKey(MznScriptRunner.MZN2FZN_PATH_PROPERTY));
		mzn2fznPath = props.getProperty(MznScriptRunner.MZN2FZN_PATH_PROPERTY);
		assertTrue(props.containsKey(MznScriptRunner.FZN_SOLVER_PATH_PROPERTY));
		fznSolverPath = props.getProperty(MznScriptRunner.FZN_SOLVER_PATH_PROPERTY);
		assertTrue(props.containsKey(MznScriptRunner.TEMP_FOLDER_PROPERTY));
	}
	
	@Test
	public void runScriptNoInputOutputTest() throws IOException, ScriptExecutionException {
		ExternalAppRunner appRunnerMock = Mockito.mock(ExternalAppRunner.class);
		Mockito.doReturn(new String[] {" "}).when(appRunnerMock).run(any(), any()); // TODO error (maybe because of the String... notation?)
		ScriptInputVariableRegistry inputVarsMock = Mockito.mock(ScriptInputVariableRegistry.class);
		Mockito.doReturn(new ArrayList<Entry>()).when(inputVarsMock).getInputVariables();
		
		MznScriptRunner target = new MznScriptRunner("path/file", appRunnerMock, "temp/file");
		Map<String, String> output = target.runScript(inputVarsMock);
		
		assertEquals(0, output.size());
		
		Mockito.verify(inputVarsMock, atLeast(1)).getInputVariables();
		Mockito.verify(appRunnerMock, times(1)).run(eq(mzn2fznPath), argThat(new CollectionMatcher<String>("\"path/file\"", "-o temp/file")));
		Mockito.verify(appRunnerMock, times(1)).run(eq(fznSolverPath), argThat(new CollectionMatcher<String>("temp/file")));
		Mockito.verifyNoMoreInteractions(appRunnerMock, inputVarsMock);
	}
	
	
	@Test
	public void runScriptTest() throws IOException, ScriptExecutionException {
		String[] testOutput = new String[] {"xx = line1", "yy = line2"};
		List<Entry> testInput = new ArrayList<>(Arrays.asList(new Entry("varA", "val1"), new Entry("varB", "val2")));
		
		ExternalAppRunner appRunnerMock = Mockito.mock(ExternalAppRunner.class);
		Mockito.doReturn(new String[0]).when(appRunnerMock).run(eq(mzn2fznPath), any());
		Mockito.doReturn(testOutput).when(appRunnerMock).run(eq(fznSolverPath), any());
		ScriptInputVariableRegistry inputVarsMock = Mockito.mock(ScriptInputVariableRegistry.class);
		Mockito.doReturn(testInput).when(inputVarsMock).getInputVariables();
		
		MznScriptRunner target = new MznScriptRunner("path/to/file", appRunnerMock, "temp/fzn/file");
		Map<String, String> output = target.runScript(inputVarsMock);
		
		assertEquals(2, output.size());
		assertEquals("line1", output.get("xx"));
		assertEquals("line2", output.get("yy"));
		
		Mockito.verify(inputVarsMock, atLeast(1)).getInputVariables();
		Mockito.verify(appRunnerMock, times(1)).run(eq(mzn2fznPath), argThat(new CollectionMatcher<String>(
				"\"path/to/file\"", "-o temp/fzn/file", "-D varA=val1", "-D varB=val2")));
		Mockito.verify(appRunnerMock, times(1)).run(eq(fznSolverPath), argThat(new CollectionMatcher<String>("temp/fzn/file")));
		Mockito.verifyNoMoreInteractions(appRunnerMock, inputVarsMock);
	}

}
