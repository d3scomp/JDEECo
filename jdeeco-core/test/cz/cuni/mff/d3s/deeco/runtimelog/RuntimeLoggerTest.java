package cz.cuni.mff.d3s.deeco.runtimelog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.w3c.dom.Element;

import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.Task;

public class RuntimeLoggerTest
{
	private CurrentTimeProvider timeProvider;
	private Scheduler scheduler;

	private final String CHARSET_NAME = "UTF-8";
	private final String EVENT_RECORD_NAME = "event";
	private final String SNAPSHOT_RECORD_NAME = "snapshot";
	private final String RECORD_ID = "componentID";
	private final String RECORD_TYPE = "eventType";
	private final String RECORD_TIME = "time";
	private final String RECORD_OFFSET = "offset";
	
	private final long TEST_TIME = 123456789;
	private final String EMPTY_STRING = ""; 
	
	private RuntimeLogRecord testRecord;

	private StringWriter dataOut;
	private StringWriter indexOut;
	private StringWriter backLogOut;
	
	@Before
	public void setUp()
	{
		scheduler = mock(Scheduler.class);
		timeProvider = mock(CurrentTimeProvider.class);
		Mockito.when(timeProvider.getCurrentMilliseconds()).thenAnswer(new Answer<Long>() {
		    @Override
		    public Long answer(InvocationOnMock invocation) throws Throwable 
		    {
		      return TEST_TIME;
		    }
		  });

		String recordType = "testType";
		String componentID = "testComponent";
		Map<String, Object> recordValues = new HashMap<String, Object>();
		recordValues.put("attr1", "val1");
		recordValues.put("attr2", "val2");
		recordValues.put("attr3", "val3");
		testRecord = new RuntimeLogRecord(recordType, componentID, recordValues);

		dataOut = new StringWriter();
		indexOut = new StringWriter();
		backLogOut = new StringWriter();
	}
	
	@After
	public void tearDown() 
	{
		try
		{
			RuntimeLogger.close();
		}
		catch(IOException e){}
		
		RuntimeLogger.unload();
	}
	
	@Test(expected=IllegalStateException.class)
	public void log_initSkipped_throwsException() throws Exception
	{
		RuntimeLogger.log(testRecord);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void init_nullArguments_throwsException() throws Exception
	{
		RuntimeLogger.init(null, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void init_nullTimeProvider_throwsException() throws Exception
	{
		RuntimeLogger.init(null, scheduler);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void init_nullScheduler_throwsException() throws Exception
	{
		RuntimeLogger.init(timeProvider, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void init_nullArgumentsExtended_throwsException() throws Exception
	{
		RuntimeLogger.init(null, null, null, null, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void init_nullTimeProviderExtended_throwsException() throws Exception
	{
		RuntimeLogger.init(null, scheduler, dataOut, indexOut, backLogOut);
	}

	@Test(expected=IllegalArgumentException.class)
	public void init_nullSchedulerExtended_throwsException() throws Exception
	{
		RuntimeLogger.init(timeProvider, null, dataOut, indexOut, backLogOut);
	}

	@Test(expected=IllegalArgumentException.class)
	public void init_nullDataOutExtended_throwsException() throws Exception
	{
		RuntimeLogger.init(timeProvider, scheduler, null, indexOut, backLogOut);
	}

	@Test(expected=IllegalArgumentException.class)
	public void init_nullIndexOutExtended_throwsException() throws Exception
	{
		RuntimeLogger.init(timeProvider, scheduler, dataOut, null, backLogOut);
	}

	@Test(expected=IllegalArgumentException.class)
	public void init_nullBackLogOutExtended_throwsException() throws Exception
	{
		RuntimeLogger.init(timeProvider, scheduler, dataOut, indexOut, null);
	}
	
	@Test
	public void getInstance_called_loggerReturned()
	{
		RuntimeLogger logger = RuntimeLogger.getInstance();
		assertNotNull(logger);
	}

	@Test(expected=IllegalArgumentException.class)
	public void log_nullArgument_throwsException() throws Exception
	{
		RuntimeLogger.init(timeProvider, scheduler, dataOut, indexOut, backLogOut);

		RuntimeLogger.log(null);
	}
	
	@Test
	public void log_fullRecord_recordWritten() throws Exception
	{
		RuntimeLogger.init(timeProvider, scheduler, dataOut, indexOut, backLogOut);

		// Write and flush the record
		RuntimeLogger.log(testRecord);
		RuntimeLogger.flush();
		
		// Load the record to a DOM element node
		// There has to be only one element written in order to be a valid XML
		Element node = DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.parse(new ByteArrayInputStream(dataOut.toString().getBytes()))
				.getDocumentElement();
		
		// Assert the written record has all its values correctly written
		assertEquals(EVENT_RECORD_NAME, node.getNodeName());
		assertEquals(testRecord.getEventType(), node.getAttribute(RECORD_TYPE));
		assertEquals(testRecord.getComponentID(), node.getAttribute(RECORD_ID));
		assertEquals(TEST_TIME, Long.parseLong(node.getAttribute(RECORD_TIME)));
		
		Map<String, Object> recordValues = testRecord.getValues();
		for(String valueKey : recordValues.keySet())
		{
			assertEquals(recordValues.get(valueKey), node.getAttribute(valueKey));
		}
		
		// Assert there are no extra attributes
		assertEquals(recordValues.size() + 3, node.getAttributes().getLength()); // + 3 stands for the following attributes: RECORD_TYPE, RECORD_ID, RECORD_TIME		
		
		// Assert no other writer was written into
		assertEquals(EMPTY_STRING, indexOut.toString());
		assertEquals(EMPTY_STRING, backLogOut.toString());
	}

	@Test(expected=IllegalArgumentException.class)
	public void logSnapshot_nullArgument_throwsException() throws Exception
	{
		RuntimeLogger.init(timeProvider, scheduler, dataOut, indexOut, backLogOut);
		
		RuntimeLogger.logSnapshot(null);	
	}
	
	@Test
	public void logSnapshot_fullRecord_recordWritten() throws Exception
	{
		RuntimeLogger.init(timeProvider, scheduler, dataOut, indexOut, backLogOut);
		
		RuntimeLogger.logSnapshot(testRecord); // Is supposed to flush itself

		// Load the record to a DOM element node
		// There has to be only one element written in order to be a valid XML
		Element node = DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.parse(new ByteArrayInputStream(dataOut.toString().getBytes()))
				.getDocumentElement();

		// Assert the written record has all its values correctly written
		assertEquals(SNAPSHOT_RECORD_NAME, node.getNodeName());
		assertEquals(testRecord.getEventType(), node.getAttribute(RECORD_TYPE));
		assertEquals(testRecord.getComponentID(), node.getAttribute(RECORD_ID));
		assertEquals(TEST_TIME, Long.parseLong(node.getAttribute(RECORD_TIME)));
		
		Map<String, Object> recordValues = testRecord.getValues();
		for(String valueKey : recordValues.keySet())
		{
			assertEquals(recordValues.get(valueKey), node.getAttribute(valueKey));
		}

		// Assert there are no extra attributes
		assertEquals(recordValues.size() + 3, node.getAttributes().getLength()); // + 3 stands for the following attributes: RECORD_TYPE, RECORD_ID, RECORD_TIME		

		// Load the record to a DOM element node
		// There has to be only one element written in order to be a valid XML
		node = DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.parse(new ByteArrayInputStream(indexOut.toString().getBytes()))
				.getDocumentElement();

		// Assert the written record has all its values correctly written
		assertEquals(SNAPSHOT_RECORD_NAME, node.getNodeName());
		assertEquals(TEST_TIME, Long.parseLong(node.getAttribute(RECORD_TIME)));
		assertEquals(0, Long.parseLong(node.getAttribute(RECORD_OFFSET)));

		// Assert no other writer was written into
		assertEquals(EMPTY_STRING, backLogOut.toString());
	}
	
	@Test
	public void logSnapshot_recordAndSnapshot_recordWritten() throws Exception
	{
		RuntimeLogger.init(timeProvider, scheduler, dataOut, indexOut, backLogOut);

		// Write and flush the record
		RuntimeLogger.log(testRecord);
		RuntimeLogger.flush();
		long dataOffset = dataOut.toString().getBytes(CHARSET_NAME).length;
		RuntimeLogger.logSnapshot(testRecord); // Is supposed to flush itself

		// Load the record to a DOM element node
		// There has to be only one element written in order to be a valid XML
		Element node = DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.parse(new ByteArrayInputStream(indexOut.toString().getBytes()))
				.getDocumentElement();

		// Assert the written record has all its values correctly written
		assertEquals(SNAPSHOT_RECORD_NAME, node.getNodeName());
		assertEquals(TEST_TIME, Long.parseLong(node.getAttribute(RECORD_TIME)));
		assertEquals(dataOffset, Long.parseLong(node.getAttribute(RECORD_OFFSET)));

		// Assert there are no extra attributes
		assertEquals(2, node.getAttributes().getLength()); // The 2 stands for the following attributes: RECORD_TIME, RECORD_OFFSET
		
		assertNotNull(dataOut.toString());
		// Assert no other writer was written into
		assertNotEquals(EMPTY_STRING, dataOut.toString());
		assertEquals(EMPTY_STRING, backLogOut.toString());
	}

	@Test(expected=IllegalArgumentException.class)
	public void registerSnapshotProvider_nullAttribute_throwsException() throws Exception
	{
		long testPeriod = 14;
		
		RuntimeLogger.init(timeProvider, scheduler, dataOut, indexOut, backLogOut);
		
		RuntimeLogger.registerSnapshotProvider(null, testPeriod);
	}

	@Test(expected=IllegalArgumentException.class)
	public void registerSnapshotProvider_negativePeriod_throwsException() throws Exception
	{
		SnapshotProvider snapshotProvider = mock(SnapshotProvider.class);
		long testPeriod = -5;
		
		RuntimeLogger.init(timeProvider, scheduler, dataOut, indexOut, backLogOut);
		
		RuntimeLogger.registerSnapshotProvider(snapshotProvider, testPeriod);
	}

	@Test(expected=IllegalArgumentException.class)
	public void registerSnapshotProvider_zeroPeriod_throwsException() throws Exception
	{
		SnapshotProvider snapshotProvider = mock(SnapshotProvider.class);
		long testPeriod = 0;
		
		RuntimeLogger.init(timeProvider, scheduler, dataOut, indexOut, backLogOut);
		
		RuntimeLogger.registerSnapshotProvider(snapshotProvider, testPeriod);
	}
	
	@Test
	public void registerSnapshotProvider_argumentsOK_providersRegistered() throws Exception
	{
		SnapshotProvider snapshotProvider1 = mock(SnapshotProvider.class);
		SnapshotProvider snapshotProvider2 = mock(SnapshotProvider.class);
		SnapshotProvider snapshotProvider3 = mock(SnapshotProvider.class);
		long snapshotPeriod1 = 1;
		long snapshotPeriod2 = 2;
		long snapshotPeriod3 = 3;
		
		// Register snapshot providers before init is called
		RuntimeLogger.registerSnapshotProvider(snapshotProvider1, snapshotPeriod1);
		RuntimeLogger.registerSnapshotProvider(snapshotProvider2, snapshotPeriod2);
		
		RuntimeLogger.init(timeProvider, scheduler, dataOut, indexOut, backLogOut);
		
		// Register snapshot provider after init was called
		RuntimeLogger.registerSnapshotProvider(snapshotProvider3, snapshotPeriod3);
		
		// Assert the tasks for registered snapshot providers were scheduled
		Mockito.verify(scheduler, Mockito.times(3)).addTask(Matchers.any(Task.class));

		// Assert back log time offsets were written
		StringBuilder answer = new StringBuilder();
			answer.append(snapshotPeriod1)
				.append("\n")
				.append(snapshotPeriod2)
				.append("\n")
				.append(snapshotPeriod3)
				.append("\n");
				
		assertEquals(answer.toString(), backLogOut.toString());
	}

	@Test(expected=IllegalArgumentException.class)
	public void registerBackLogOffset_negativePeriod_throwsException() throws Exception
	{
		long testPeriod = -5;
		
		RuntimeLogger.registerBackLogOffset(testPeriod);
	}

	@Test(expected=IllegalArgumentException.class)
	public void registerBackLogOffset_zeroPeriod_throwsException() throws Exception
	{
		long testPeriod = 0;
		
		RuntimeLogger.init(timeProvider, scheduler, dataOut, indexOut, backLogOut);
		
		RuntimeLogger.registerBackLogOffset(testPeriod);
	}
	
	@Test
	public void registerBackLogOffset_argumentsOK_offsetsRegistered() throws Exception
	{
		long period1 = 14;
		long period2 = 15;
		long period3 = 16;
		
		// Register back log offsets before init is called
		RuntimeLogger.registerBackLogOffset(period1);
		RuntimeLogger.registerBackLogOffset(period2);

		RuntimeLogger.init(timeProvider, scheduler, dataOut, indexOut, backLogOut);

		// Register back log offsets after init was called
		RuntimeLogger.registerBackLogOffset(period3);

		// Assert back log time offsets were written
		StringBuilder answer = new StringBuilder();
			answer.append(period1)
				.append("\n")
				.append(period2)
				.append("\n")
				.append(period3)
				.append("\n");
				
		assertEquals(answer.toString(), backLogOut.toString());
	}
}
