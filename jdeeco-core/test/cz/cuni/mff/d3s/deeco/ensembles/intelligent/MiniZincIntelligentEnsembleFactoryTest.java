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
import static org.mockito.Mockito.refEq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.management.openmbean.InvalidOpenTypeException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.ensembles.EnsembleFormationException;
import cz.cuni.mff.d3s.deeco.ensembles.EnsembleInstance;
import cz.cuni.mff.d3s.deeco.knowledge.container.KnowledgeContainer;

public class MiniZincIntelligentEnsembleFactoryTest {

	class MiniZincIntelligentEnsembleFactoryMock extends MiniZincIntelligentEnsembleFactory {

		public ScriptInputVariableRegistry inputVars;
		public Collection<EnsembleInstance> resultCollection;
		
		public MiniZincIntelligentEnsembleFactoryMock(MznScriptRunner scriptRunner, ScriptInputVariableRegistry inputVars,
				Collection<EnsembleInstance> resultCollection) {
			super(scriptRunner);
			this.inputVars = inputVars;
			this.resultCollection = resultCollection;
		}

		@Override
		public int getSchedulingOffset() {
			throw new InvalidOpenTypeException("The method getSchedulingOffset should not have been called.");
		}

		@Override
		public int getSchedulingPeriod() {
			throw new InvalidOpenTypeException("The method getSchedulingOffset should not have been called.");
		}

		@Override
		protected ScriptInputVariableRegistry parseInput(KnowledgeContainer knowledgeContainer) throws EnsembleFormationException {
			return inputVars;
		}

		@Override
		protected Collection<EnsembleInstance> createInstancesFromOutput(ScriptOutputVariableRegistry scriptOutput)
				throws EnsembleFormationException {
			return resultCollection;
		}		
	}
	
	@Test
	public void createInstancesTest() throws EnsembleFormationException, ScriptExecutionException {
		KnowledgeContainer container = Mockito.mock(KnowledgeContainer.class);
		ScriptInputVariableRegistry inputVars = Mockito.mock(ScriptInputVariableRegistry.class);
		Map<String, String> output = new HashMap<String, String>();
		Collection<EnsembleInstance> resultCollection = new ArrayList<EnsembleInstance>();
		// TODO correct
		MznScriptRunner runnerMock = Mockito.mock(MznScriptRunner.class);
		Mockito.doReturn(output).when(runnerMock).runScript(any());
		
		MiniZincIntelligentEnsembleFactory target = Mockito.spy(
				new MiniZincIntelligentEnsembleFactoryMock(runnerMock, inputVars, resultCollection));
		Collection<EnsembleInstance> returnValue = target.createInstances(container);
		
		Mockito.verify(target, times(1)).parseInput(refEq(container));
		Mockito.verify(target, times(1)).createInstancesFromOutput(any());
		Mockito.verify(target, times(1)).createInstances(any());
		Mockito.verify(runnerMock, times(1)).runScript(refEq(inputVars));
		Mockito.verifyNoMoreInteractions(target, runnerMock);
		Assert.assertSame(resultCollection, returnValue);
	}
}
