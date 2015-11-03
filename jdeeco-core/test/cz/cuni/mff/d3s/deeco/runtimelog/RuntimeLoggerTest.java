package cz.cuni.mff.d3s.deeco.runtimelog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;

public class RuntimeLoggerTest
{
	private CurrentTimeProvider timeProvider;
	private Scheduler scheduler;

	private final String CHARSET_NAME = "UTF-8";
	private final String EVENTS_ROOT_NAME = "events";
	private final String EVENT_RECORD_NAME = "event";
	private final String VALUE_RECORD_NAME = "value";
	private final String RECORD_ID = "id";
	private final String RECORD_TYPE = "eventType";
	private final String RECORD_TIME = "time";
	private final String RECORD_OFFSET = "offset";
	
	private final long TEST_TIME = 123456789;
	private final String EMPTY_STRING = ""; 
	
	private RuntimeLogRecord testRecord;

	private StringWriter dataOut;
	private StringWriter indexOut;
	private StringWriter periodOut;
	
	private RuntimeLogWriters writers;
	
	@Before
	public void setUp() throws IOException
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
		
		testRecord = mock(RuntimeLogRecord.class);
		Mockito.when(testRecord.getId()).thenAnswer(new Answer<String>() {
			@Override
		    public String answer(InvocationOnMock invocation) throws Throwable 
		    {
				return "testId";
		    }
		});
		Mockito.when(testRecord.getValues()).thenAnswer(new Answer<Map<String, Object>>() {
			@Override
		    public Map<String, Object> answer(InvocationOnMock invocation) throws Throwable 
		    {
				Map<String, Object> recordValues = new HashMap<String, Object>();
				recordValues.put("attr1", "val1");
				recordValues.put("attr2", "val2");
				ArrayList<String> collection = new ArrayList<String>();
				collection.add("colVal1");
				collection.add("colVal2");
				recordValues.put("coll", collection);
				
				return recordValues;
		    }
		});
		
		dataOut = new StringWriter();
		indexOut = new StringWriter();
		periodOut = new StringWriter();
		
		writers = new RuntimeLogWriters(dataOut, indexOut, periodOut);
	}
	
	@After
	public void tearDown() 
	{
		try
		{
			writers.close();
		}
		catch(IOException e){}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void contructor_nullArguments_throwsException() throws Exception
	{
		@SuppressWarnings("unused")
		RuntimeLogger runtimeLogger = new RuntimeLogger(null, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void construstor_nullTimeProvider_throwsException() throws Exception
	{
		@SuppressWarnings("unused")
		RuntimeLogger runtimeLogger = new RuntimeLogger(null, scheduler);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void construstor_nullScheduler_throwsException() throws Exception
	{
		@SuppressWarnings("unused")
		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void construstor_nullArgumentsExtended_throwsException() throws Exception
	{
		@SuppressWarnings("unused")
		RuntimeLogger runtimeLogger = new RuntimeLogger(null, null, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void construstor_nullTimeProviderExtended_throwsException() throws Exception
	{
		@SuppressWarnings("unused")
		RuntimeLogger runtimeLogger = new RuntimeLogger(null, scheduler, writers);
	}

	@Test(expected=IllegalArgumentException.class)
	public void construstor_nullSchedulerExtended_throwsException() throws Exception
	{
		@SuppressWarnings("unused")
		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, null, writers);
	}

	@Test(expected=IllegalArgumentException.class)
	public void construstor_nullWritersExtended_throwsException() throws Exception
	{
		@SuppressWarnings("unused")
		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, scheduler, null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void log_nullArgument_throwsException() throws Exception
	{
		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, scheduler, writers);

		runtimeLogger.log(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void log_recordNullId_throwsException() throws Exception
	{
		RuntimeLogRecord record = mock(RuntimeLogRecord.class);
		Mockito.when(record.getId()).thenAnswer(new Answer<String>() {
			@Override
		    public String answer(InvocationOnMock invocation) throws Throwable 
		    {	
				return null;
		    }
		});
		Mockito.when(record.getValues()).thenAnswer(new Answer<Map<String, Object>>() {
			@Override
		    public Map<String, Object> answer(InvocationOnMock invocation) throws Throwable 
		    {	
				return new HashMap<String, Object>();
		    }
		});
		
		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, scheduler, writers);

		runtimeLogger.log(record);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void log_recordNullValues_throwsException() throws Exception
	{
		RuntimeLogRecord record = mock(RuntimeLogRecord.class);
		Mockito.when(record.getId()).thenAnswer(new Answer<String>() {
			@Override
		    public String answer(InvocationOnMock invocation) throws Throwable 
		    {	
				return "";
		    }
		});
		Mockito.when(record.getValues()).thenAnswer(new Answer<Map<String, Object>>() {
			@Override
		    public Map<String, Object> answer(InvocationOnMock invocation) throws Throwable 
		    {	
				return null;
		    }
		});
		
		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, scheduler, writers);

		runtimeLogger.log(record);
	}
	
	@Test
	public void log_fullRecord_recordWritten() throws Exception
	{
		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, scheduler, writers);

		// Write and flush the record
		runtimeLogger.log(testRecord);
		runtimeLogger.close();
		
		// Load the record to a DOM element node
		// There has to be only one element written in order to be a valid XML
		Element node = DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.parse(new ByteArrayInputStream(dataOut.toString().getBytes()))
				.getDocumentElement();
		
		// The root node should be <events>
		assertEquals(EVENTS_ROOT_NAME, node.getNodeName());
		// Assert the written record has all its values correctly written
		// Get the <event> child node
		List<Element> childs = getChildElements(node);
		assertEquals(1, childs.size());
		node = childs.get(0);
		assertEquals(EVENT_RECORD_NAME, node.getNodeName());
		assertEquals(testRecord.getClass().getCanonicalName(), node.getAttribute(RECORD_TYPE));
		assertEquals(testRecord.getId(), node.getAttribute(RECORD_ID));
		assertEquals(TEST_TIME, Long.parseLong(node.getAttribute(RECORD_TIME)));
		
		compareKnowledgeValues(testRecord.getValues(), node);
		
		// Assert there are no extra attributes
		assertEquals(3, node.getAttributes().getLength()); // 3 stands for the following attributes: RECORD_TYPE, RECORD_ID, RECORD_TIME		
		
		// Assert no other writer was written into
		assertEquals(EMPTY_STRING, indexOut.toString());
		assertEquals(EMPTY_STRING, periodOut.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void registerSnapshotProvider_nullAttribute_throwsException() throws Exception
	{
		long testPeriod = 14;

		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, scheduler, writers);
		
		runtimeLogger.registerSnapshotProvider(null, testPeriod);
	}

	@Test(expected=IllegalArgumentException.class)
	public void registerSnapshotProvider_negativePeriod_throwsException() throws Exception
	{
		SnapshotProvider snapshotProvider = mock(SnapshotProvider.class);
		Mockito.when(snapshotProvider.getSnapshot()).thenAnswer(new Answer<RuntimeLogRecord>() {
		    @Override
		    public RuntimeLogRecord answer(InvocationOnMock invocation) throws Throwable 
		    {
		      return testRecord;
		    }
		  });
		Mockito.when(snapshotProvider.getRecordClass()).thenAnswer(new Answer<Class<? extends RuntimeLogRecord>>(){
			@Override
			public Class<? extends RuntimeLogRecord> answer(InvocationOnMock invocation) throws Throwable
			{
				return testRecord.getClass();
			}
		});
		long testPeriod = -5;

		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, scheduler, writers);
		
		runtimeLogger.registerSnapshotProvider(snapshotProvider, testPeriod);
	}

	@Test(expected=IllegalArgumentException.class)
	public void registerSnapshotProvider_zeroPeriod_throwsException() throws Exception
	{
		SnapshotProvider snapshotProvider = mock(SnapshotProvider.class);
		Mockito.when(snapshotProvider.getSnapshot()).thenAnswer(new Answer<RuntimeLogRecord>() {
		    @Override
		    public RuntimeLogRecord answer(InvocationOnMock invocation) throws Throwable 
		    {
		      return testRecord;
		    }
		  });
		Mockito.when(snapshotProvider.getRecordClass()).thenAnswer(new Answer<Class<? extends RuntimeLogRecord>>(){
			@Override
			public Class<? extends RuntimeLogRecord> answer(InvocationOnMock invocation) throws Throwable
			{
				return testRecord.getClass();
			}
		});
		long testPeriod = 0;

		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, scheduler, writers);
		
		runtimeLogger.registerSnapshotProvider(snapshotProvider, testPeriod);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void registerSnapshotProvider_nullRecordType_throwsException() throws Exception
	{
		SnapshotProvider snapshotProvider = mock(SnapshotProvider.class);
		Mockito.when(snapshotProvider.getSnapshot()).thenAnswer(new Answer<RuntimeLogRecord>() {
		    @Override
		    public RuntimeLogRecord answer(InvocationOnMock invocation) throws Throwable 
		    {
		      return testRecord;
		    }
		  });
		Mockito.when(snapshotProvider.getRecordClass()).thenAnswer(new Answer<Class<? extends RuntimeLogRecord>>(){
			@Override
			public Class<? extends RuntimeLogRecord> answer(InvocationOnMock invocation) throws Throwable
			{
				return null;
			}
		});
		long testPeriod = 0;

		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, scheduler, writers);
		
		runtimeLogger.registerSnapshotProvider(snapshotProvider, testPeriod);
	}
	
	@Test
	public void registerSnapshotProvider_argumentsOK_providersRegistered() throws Exception
	{
		RuntimeLogRecord logRecord1 = new TestLogRecord1("record1", new HashMap<String, Object>());
		SnapshotProvider snapshotProvider1 = mock(SnapshotProvider.class);
		Mockito.when(snapshotProvider1.getSnapshot()).thenAnswer(new Answer<RuntimeLogRecord>() {
		    @Override
		    public RuntimeLogRecord answer(InvocationOnMock invocation) throws Throwable 
		    {
		      return logRecord1;
		    }
		  });
		Mockito.when(snapshotProvider1.getRecordClass()).thenAnswer(new Answer<Class<? extends RuntimeLogRecord>>(){
			@Override
			public Class<? extends RuntimeLogRecord> answer(InvocationOnMock invocation) throws Throwable
			{
				return logRecord1.getClass();
			}
		});

		RuntimeLogRecord logRecord2 = new TestLogRecord2("record2", new HashMap<String, Object>());
		SnapshotProvider snapshotProvider2 = mock(SnapshotProvider.class);
		Mockito.when(snapshotProvider2.getSnapshot()).thenAnswer(new Answer<RuntimeLogRecord>() {
		    @Override
		    public RuntimeLogRecord answer(InvocationOnMock invocation) throws Throwable 
		    {
		      return logRecord2;
		    }
		  });
		Mockito.when(snapshotProvider2.getRecordClass()).thenAnswer(new Answer<Class<? extends RuntimeLogRecord>>(){
			@Override
			public Class<? extends RuntimeLogRecord> answer(InvocationOnMock invocation) throws Throwable
			{
				return logRecord2.getClass();
			}
		});
		
		RuntimeLogRecord logRecord3 = new TestLogRecord3("record3", new HashMap<String, Object>());
		SnapshotProvider snapshotProvider3 = mock(SnapshotProvider.class);
		Mockito.when(snapshotProvider3.getSnapshot()).thenAnswer(new Answer<RuntimeLogRecord>() {
		    @Override
		    public RuntimeLogRecord answer(InvocationOnMock invocation) throws Throwable 
		    {
		      return logRecord3;
		    }
		  });
		Mockito.when(snapshotProvider3.getRecordClass()).thenAnswer(new Answer<Class<? extends RuntimeLogRecord>>(){
			@Override
			public Class<? extends RuntimeLogRecord> answer(InvocationOnMock invocation) throws Throwable
			{
				return logRecord3.getClass();
			}
		});
		
		long snapshotPeriod1 = 1;
		long snapshotPeriod2 = 2;
		long snapshotPeriod3 = 3;
		
		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, scheduler, writers);
		runtimeLogger.registerSnapshotProvider(snapshotProvider1, snapshotPeriod1);
		runtimeLogger.registerSnapshotProvider(snapshotProvider2, snapshotPeriod2);
		runtimeLogger.registerSnapshotProvider(snapshotProvider3, snapshotPeriod3);
		
		// Assert the tasks for registered snapshot providers were scheduled
		Mockito.verify(scheduler, Mockito.times(3)).addTask(Matchers.any(Task.class));

		// Assert back log time offsets were written
		StringBuilder answer = new StringBuilder();
			answer.append(snapshotPeriod1).append(" ").append(logRecord1.getClass().getCanonicalName()).append("\n")
				.append(snapshotPeriod2).append(" ").append(logRecord2.getClass().getCanonicalName()).append("\n")
				.append(snapshotPeriod3).append(" ").append(logRecord3.getClass().getCanonicalName()).append("\n");
				
		assertEquals(answer.toString(), periodOut.toString());
	}

	@Test(expected=IllegalArgumentException.class)
	public void registerSnapshotPeriod_negativePeriod_throwsException() throws Exception
	{
		long testPeriod = -5;

		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, scheduler, writers);
		runtimeLogger.registerSnapshotPeriod(testPeriod, testRecord.getClass());
	}

	@Test(expected=IllegalArgumentException.class)
	public void registerSnapshotPeriod_zeroPeriod_throwsException() throws Exception
	{
		long testPeriod = 0;

		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, scheduler, writers);
		
		runtimeLogger.registerSnapshotPeriod(testPeriod, testRecord.getClass());
	}

	@Test(expected=IllegalArgumentException.class)
	public void registerSnapshotPeriod_nullRecordType_throwsException() throws Exception
	{
		long testPeriod = 14;

		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, scheduler, writers);
		
		runtimeLogger.registerSnapshotPeriod(testPeriod, null);
	}
	
	@Test
	public void registerSnapshotPeriod_argumentsOK_offsetsRegistered() throws Exception
	{
		long period1 = 14;
		RuntimeLogRecord logRecord1 = new TestLogRecord1("record1", new HashMap<String, Object>());
		
		long period2 = 15;
		RuntimeLogRecord logRecord2 = new TestLogRecord2("record2", new HashMap<String, Object>());
		
		long period3 = 16;
		RuntimeLogRecord logRecord3 = new TestLogRecord3("record3", new HashMap<String, Object>());
		
		// Register snapshot periods before init is called
		RuntimeLogger runtimeLogger = new RuntimeLogger(timeProvider, scheduler, writers);
		runtimeLogger.registerSnapshotPeriod(period1, logRecord1.getClass());
		runtimeLogger.registerSnapshotPeriod(period2, logRecord2.getClass());
		
		// Register snapshot period after init was called
		runtimeLogger.registerSnapshotPeriod(period3, logRecord3.getClass());

		// Assert back log time offsets were written
		StringBuilder answer = new StringBuilder();
			answer.append(period1).append(" ").append(logRecord1.getClass().getCanonicalName()).append("\n")
				.append(period2).append(" ").append(logRecord2.getClass().getCanonicalName()).append("\n")
				.append(period3).append(" ").append(logRecord3.getClass().getCanonicalName()).append("\n");
				
		assertEquals(answer.toString(), periodOut.toString());
	}

	@Test
	public void log_registeredSnapshotProvider_logWritten() throws Exception
	{
		long time1 = 10;
		long time2 = 15;
		long time3 = 20;
		
		CurrentTimeProvider testTimeProvider = mock(CurrentTimeProvider.class);
		Mockito.when(testTimeProvider.getCurrentMilliseconds()).thenAnswer(new Answer<Long>(){
			private int callTimes = 0;
			@Override
			public Long answer(InvocationOnMock invocation) throws Throwable {
				switch(callTimes)
				{
				case 0:
					callTimes++;
					return time1;
				case 1:
					callTimes++;
					return time2;
				default:
					callTimes = 0;
					return time3;
				}
			}});
		
		long period1 = 14;
		RuntimeLogRecord logRecord1 = new TestLogRecord1("record1", new HashMap<String, Object>());
		logRecord1.getValues().put("v1", 1);
		logRecord1.getValues().put("v2", "two");
		SnapshotProvider snapshotProvider1 = mock(SnapshotProvider.class);
		Mockito.when(snapshotProvider1.getSnapshot()).thenAnswer(new Answer<RuntimeLogRecord>() {
		    @Override
		    public RuntimeLogRecord answer(InvocationOnMock invocation) throws Throwable 
		    {
		      return logRecord1;
		    }
		  });
		Mockito.when(snapshotProvider1.getRecordClass()).thenAnswer(new Answer<Class<? extends RuntimeLogRecord>>(){
			@Override
			public Class<? extends RuntimeLogRecord> answer(InvocationOnMock invocation) throws Throwable
			{
				return logRecord1.getClass();
			}
		});
		
		long period2 = 15;
		RuntimeLogRecord logRecord2 = new TestLogRecord2("record2", new HashMap<String, Object>());
		logRecord2.getValues().put("v3", 3);
		logRecord2.getValues().put("v4", "four");
		SnapshotProvider snapshotProvider2 = mock(SnapshotProvider.class);
		Mockito.when(snapshotProvider2.getSnapshot()).thenAnswer(new Answer<RuntimeLogRecord>() {
		    @Override
		    public RuntimeLogRecord answer(InvocationOnMock invocation) throws Throwable 
		    {
		      return logRecord2;
		    }
		  });
		Mockito.when(snapshotProvider2.getRecordClass()).thenAnswer(new Answer<Class<? extends RuntimeLogRecord>>(){
			@Override
			public Class<? extends RuntimeLogRecord> answer(InvocationOnMock invocation) throws Throwable
			{
				return logRecord2.getClass();
			}
		});

		long period3 = 16;
		RuntimeLogRecord logRecord3 = new TestLogRecord3("record3", new HashMap<String, Object>());
		logRecord3.getValues().put("v5", 5);
		logRecord3.getValues().put("v6", "six");
		SnapshotProvider snapshotProvider3 = mock(SnapshotProvider.class);
		Mockito.when(snapshotProvider3.getSnapshot()).thenAnswer(new Answer<RuntimeLogRecord>() {
		    @Override
		    public RuntimeLogRecord answer(InvocationOnMock invocation) throws Throwable 
		    {
		      return logRecord3;
		    }
		  });
		Mockito.when(snapshotProvider3.getRecordClass()).thenAnswer(new Answer<Class<? extends RuntimeLogRecord>>(){
			@Override
			public Class<? extends RuntimeLogRecord> answer(InvocationOnMock invocation) throws Throwable
			{
				return logRecord3.getClass();
			}
		});
		
		// Register snapshot period before init is called
		RuntimeLogger runtimeLogger = new RuntimeLogger(testTimeProvider, scheduler, writers);
		runtimeLogger.registerSnapshotProvider(snapshotProvider1, period1);

		// Register snapshot period after init was called

		runtimeLogger.registerSnapshotProvider(snapshotProvider2, period2);
		runtimeLogger.registerSnapshotProvider(snapshotProvider3, period3);

		// Log record1
		runtimeLogger.log(logRecord1); // Is supposed to flush and write index

		// Assert log1 written properly - index also written
		String lastWrittenData = initLogElement();
		String lastWrittenIndex = EMPTY_STRING;
		String writtenData = dataOut.toString();
		String writtenIndex = indexOut.toString();
		compareLogs(logRecord1, time1, writtenData, lastWrittenData, writtenIndex, lastWrittenIndex, true);

		lastWrittenData = writtenData;
		lastWrittenIndex = writtenIndex;
		
		// Log record2
		runtimeLogger.log(logRecord2); // Is not supposed to flush and not to write index
		runtimeLogger.flush();

		// Assert log2 written properly - index not written
		writtenData = dataOut.toString();
		writtenIndex = indexOut.toString();
		compareLogs(logRecord2, time2, writtenData, lastWrittenData, writtenIndex, lastWrittenIndex, false);

		lastWrittenData = writtenData;
		lastWrittenIndex = writtenIndex;
		
		// Log record3
		runtimeLogger.log(logRecord3); // Is supposed to flush and write index

		// Assert log3 written properly - index also written
		writtenData = dataOut.toString();
		writtenIndex = indexOut.toString();
		compareLogs(logRecord3, time3, writtenData, lastWrittenData, writtenIndex, lastWrittenIndex, true);
	}
	
	@Test
	public void log_registeredSnapshotPeriod_logWritten() throws Exception
	{
		long time1 = 10;
		long time2 = 15;
		long time3 = 20;
		
		CurrentTimeProvider testTimeProvider = mock(CurrentTimeProvider.class);
		Mockito.when(testTimeProvider.getCurrentMilliseconds()).thenAnswer(new Answer<Long>(){
			private int callTimes = 0;
			@Override
			public Long answer(InvocationOnMock invocation) throws Throwable {
				switch(callTimes)
				{
				case 0:
					callTimes++;
					return time1;
				case 1:
					callTimes++;
					return time2;
				default:
					callTimes = 0;
					return time3;
				}
			}});
		
		long period1 = 14;
		RuntimeLogRecord logRecord1 = new TestLogRecord1("record1", new HashMap<String, Object>());
		logRecord1.getValues().put("v1", 1);
		logRecord1.getValues().put("v2", "two");
		
		long period2 = 15;
		RuntimeLogRecord logRecord2 = new TestLogRecord2("record2", new HashMap<String, Object>());
		logRecord2.getValues().put("v3", 3);
		logRecord2.getValues().put("v4", "four");

		long period3 = 16;
		RuntimeLogRecord logRecord3 = new TestLogRecord3("record3", new HashMap<String, Object>());
		logRecord3.getValues().put("v5", 5);
		logRecord3.getValues().put("v6", "six");
		
		// Register snapshot period before init is called
		RuntimeLogger runtimeLogger = new RuntimeLogger(testTimeProvider, scheduler, writers);
		runtimeLogger.registerSnapshotPeriod(period1, logRecord1.getClass());

		// Register snapshot period after init was called
		runtimeLogger.registerSnapshotPeriod(period2, logRecord2.getClass());
		runtimeLogger.registerSnapshotPeriod(period3, logRecord3.getClass());

		// Log record1
		runtimeLogger.log(logRecord1); // Is supposed to flush and write index

		// Assert log1 written properly - index also written
		String lastWrittenData = initLogElement();
		String lastWrittenIndex = EMPTY_STRING;
		String writtenData = dataOut.toString();
		String writtenIndex = indexOut.toString();
		compareLogs(logRecord1, time1, writtenData, lastWrittenData, writtenIndex, lastWrittenIndex, true);

		lastWrittenData = writtenData;
		lastWrittenIndex = writtenIndex;
		
		// Log record2
		runtimeLogger.log(logRecord2); // Is not supposed to flush and not to write index
		runtimeLogger.flush();

		// Assert log2 written properly - index not written
		writtenData = dataOut.toString();
		writtenIndex = indexOut.toString();
		compareLogs(logRecord2, time2, writtenData, lastWrittenData, writtenIndex, lastWrittenIndex, false);

		lastWrittenData = writtenData;
		lastWrittenIndex = writtenIndex;
		
		// Log record3
		runtimeLogger.log(logRecord3); // Is supposed to flush and write index

		// Assert log3 written properly - index also written
		writtenData = dataOut.toString();
		writtenIndex = indexOut.toString();
		compareLogs(logRecord3, time3, writtenData, lastWrittenData, writtenIndex, lastWrittenIndex, true);
	}
	
	/**
	 * Compare the last content of the <em>writtenData</em> and <em>writtenIndex</em> to expected values
	 * defined by <em>record</em>, <em>time</em> and <em>indexWritten</em> arguments.
	 * @param record is the last {@link RuntimeLogRecord} written.
	 * @param time is the time of the last {@link RuntimeLogRecord} being logged. 
	 * @param writtenData contains all the written data.
	 * @param lastWrittenData contains all the data written before the last {@link RuntimeLogRecord} was logged.
	 * @param writtenIndex contains all the written index data.
	 * @param lastWrittenIndex contains all the index data written before the last {@link RuntimeLogRecord} was logged.
	 * @param indexWritten indicates whether the index file was supposed to be appended when the last {@link RuntimeLogRecord} was logged. 
	 * @throws Exception Thrown if there is a problem building DOM model from the last data/index record.
	 */
	private void compareLogs(RuntimeLogRecord record, long time,
			String writtenData, String lastWrittenData,
			String writtenIndex, String lastWrittenIndex,
			boolean indexWritten) throws Exception
	{
		String lastWrittenDataRecord = writtenData.substring(lastWrittenData.length(), writtenData.length());
		String lastWrittenDataIndex = writtenIndex.substring(lastWrittenIndex.length(), writtenIndex.length());
		
		// Load the written record to a DOM element node
		Element node = DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.parse(new ByteArrayInputStream(lastWrittenDataRecord.getBytes()))
				.getDocumentElement();
		
		// Assert the written record has all its values correctly written
		assertEquals(EVENT_RECORD_NAME, node.getNodeName());
		assertEquals(record.getClass().getCanonicalName(), node.getAttribute(RECORD_TYPE));
		assertEquals(record.getId(), node.getAttribute(RECORD_ID));
		assertEquals(time, Long.parseLong(node.getAttribute(RECORD_TIME)));
		
		compareKnowledgeValues(record.getValues(), node);
		
		// Assert there are no extra attributes
		assertEquals(3, node.getAttributes().getLength()); // 3 stands for the following attributes: RECORD_TYPE, RECORD_ID, RECORD_TIME		
		
		if(indexWritten)
		{
			// Load the index to a DOM element node
			node = DocumentBuilderFactory
					.newInstance()
					.newDocumentBuilder()
					.parse(new ByteArrayInputStream(lastWrittenDataIndex.getBytes()))
					.getDocumentElement();
			
			// Assert index writer was written into
			assertEquals(EVENT_RECORD_NAME, node.getNodeName());
			assertEquals(time, Long.parseLong(node.getAttribute(RECORD_TIME)));
			assertEquals(lastWrittenData.getBytes(CHARSET_NAME).length, Integer.parseInt(node.getAttribute(RECORD_OFFSET)));
		}
		else
		{
			assertEquals(EMPTY_STRING, lastWrittenDataIndex);
		}
	}
	
	/**
	 * Compares the given <em>knowledge</em> and the given DOM <em>node</em> whether
	 * they contain the same information.
	 * @param knowledge is the knowledge to be checked.
	 * @param node is the XML representation of the knowledge to be checked.
	 * @return True if the given <em>node</em> is not empty. False otherwise.
	 */
	@SuppressWarnings("unchecked")
	private boolean compareKnowledgeValues(Object knowledge, Node node)
	{
		// Skip empty nodes that DOM adds for some reason
		if(node.getNodeType() == Node.TEXT_NODE
				&& node.getTextContent().trim().equals(EMPTY_STRING))
			return false;
		if(knowledge instanceof Iterable)
		{
			assertEquals(node.getNodeType(), Node.ELEMENT_NODE);
			Iterable<Object> collection = (Iterable<Object>) knowledge;
			int index = 0;
			for(Object item : collection)
			{
				boolean valuesCompared = false;
				Node child;
				do
				{
					assertTrue(index < node.getChildNodes().getLength());
					child = node.getChildNodes().item(index);
					valuesCompared = compareKnowledgeValues(item, child);
					index++;
				}
				while(!valuesCompared);
				assertNotNull(child);
				assertEquals(VALUE_RECORD_NAME, child.getNodeName());
				
			}
		}
		else if(knowledge instanceof Map)
		{
			assertEquals(node.getNodeType(), Node.ELEMENT_NODE);
			Map<String, Object> map = (Map<String, Object>) knowledge;
			int index = 0;
			for(String valueKey : map.keySet())
			{
				boolean valuesCompared = false;
				Node child;
				do
				{
					assertTrue(index < node.getChildNodes().getLength());
					child = node.getChildNodes().item(index);
					valuesCompared = compareKnowledgeValues(map.get(valueKey), child);
					index++;
				}
				while(!valuesCompared);
				assertNotNull(child);
				assertEquals(valueKey, child.getNodeName());
			}
		}
		else
		{
			assertEquals(knowledge.toString(), node.getTextContent());
		}
		
		return true;
	}
	
	private String initLogElement(){
		return String.format("<%s>\n", EVENTS_ROOT_NAME);
	}
	
	private List<Element> getChildElements(Node n){
		NodeList ch = n.getChildNodes();
		List<Element> e = new ArrayList<>();
		for(int i = 0; i < ch.getLength(); i++){
			if(ch.item(i).getNodeType() == Node.ELEMENT_NODE){
				e.add((Element) ch.item(i));
			}
		}
		return e;
	}
	
	// The code below is only for debugging purposes
/*	private void printDOM(Node node, int indent)
	{
		if(node.getNodeType() == Node.TEXT_NODE
				&& node.getTextContent().trim().equals(EMPTY_STRING))
			return;
		
		indent(indent);
		System.out.println(String.format("NodeName: %s", node.getNodeName()));
		indent(indent);
		System.out.println(String.format("NodeType: %s", node.getNodeType()));
//		indent(indent);
//		System.out.println(String.format("BaseURI: %s", node.getBaseURI()));
//		indent(indent);
//		System.out.println(String.format("LocalName: %s", node.getLocalName()));
//		indent(indent);
//		System.out.println(String.format("NodeValue: %s", node.getNodeValue()));
//		indent(indent);
//		System.out.println(String.format("Prefix: %s", node.getPrefix()));
//		indent(indent);
//		System.out.println(String.format("NamespaceURI: %s", node.getNamespaceURI()));
		indent(indent);
		System.out.println(String.format("TextContent: %s", node.getTextContent()));

		org.w3c.dom.NamedNodeMap attrs = node.getAttributes();
		if(attrs != null)
		{
			indent(indent);
			System.out.println(String.format("Attrs: %d", attrs.getLength()));
			for(int i = 0; i < attrs.getLength(); i++)
			{
				printDOM(attrs.item(i), indent+1);
			}
		}
		
		org.w3c.dom.NodeList childs = node.getChildNodes();
		if(childs != null)
		{
			indent(indent);
			System.out.println(String.format("Childs: %d", childs.getLength()));
			for(int i = 0; i < childs.getLength(); i++)
			{
				printDOM(childs.item(i), indent+1);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void printKnowledge(Object knowledge, int indent)
	{
		if(knowledge instanceof Iterable)
		{
			indent(indent);
			System.out.println("Collection:");
			Iterable<Object> collection = (Iterable<Object>) knowledge;
			for(Object item : collection)
			{
				printKnowledge(item, indent+1);
			}
		}
		else if(knowledge instanceof Map)
		{
			indent(indent);
			System.out.println("MAP:");
			Map<String, Object> map = (Map<String, Object>) knowledge;
			for(String valueKey : map.keySet())
			{
				printKnowledge(map.get(valueKey), indent+1);
			}
		}
		else
		{
			indent(indent);
			System.out.println(knowledge);
		}
	}
	
	private void printDOMTypes()
	{
		System.out.println(String.format("ATTRIBUTE_NODE: %d", Node.ATTRIBUTE_NODE));
		System.out.println(String.format("CDATA_SECTION_NODE: %d", Node.CDATA_SECTION_NODE));
		System.out.println(String.format("COMMENT_NODE: %d", Node.COMMENT_NODE));
		System.out.println(String.format("DOCUMENT_FRAGMENT_NODE: %d", Node.DOCUMENT_FRAGMENT_NODE));
		System.out.println(String.format("DOCUMENT_NODE: %d", Node.DOCUMENT_NODE));
		System.out.println(String.format("DOCUMENT_POSITION_CONTAINED_BY: %d", Node.DOCUMENT_POSITION_CONTAINED_BY));
		System.out.println(String.format("DOCUMENT_POSITION_CONTAINS: %d", Node.DOCUMENT_POSITION_CONTAINS));
		System.out.println(String.format("DOCUMENT_POSITION_DISCONNECTED: %d", Node.DOCUMENT_POSITION_DISCONNECTED));
		System.out.println(String.format("DOCUMENT_POSITION_FOLLOWING: %d", Node.DOCUMENT_POSITION_FOLLOWING));
		System.out.println(String.format("DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC: %d", Node.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC));
		System.out.println(String.format("DOCUMENT_POSITION_PRECEDING: %d", Node.DOCUMENT_POSITION_PRECEDING));
		System.out.println(String.format("DOCUMENT_TYPE_NODE: %d", Node.DOCUMENT_TYPE_NODE));
		System.out.println(String.format("ELEMENT_NODE: %d", Node.ELEMENT_NODE));
		System.out.println(String.format("ENTITY_NODE: %d", Node.ENTITY_NODE));
		System.out.println(String.format("ENTITY_REFERENCE_NODE: %d", Node.ENTITY_REFERENCE_NODE));
		System.out.println(String.format("NOTATION_NODE: %d", Node.NOTATION_NODE));
		System.out.println(String.format("PROCESSING_INSTRUCTION_NODE: %d", Node.PROCESSING_INSTRUCTION_NODE));
		System.out.println(String.format("TEXT_NODE: %d", Node.TEXT_NODE));
	}
	
	private void indent(int indent)
	{
		for(int i = 0; i < indent; i++)
		{
			System.out.print("  ");
		}
	}*/
	
	private class TestLogRecord1 extends RuntimeLogRecord
	{
		public TestLogRecord1(String id, Map<String, Object> values)
		{
			super(id, values);
		}
	}
	private class TestLogRecord2 extends RuntimeLogRecord
	{
		public TestLogRecord2(String id, Map<String, Object> values)
		{
			super(id, values);
		}
	}
	private class TestLogRecord3 extends RuntimeLogRecord
	{
		public TestLogRecord3(String id, Map<String, Object> values)
		{
			super(id, values);
		}
	}
	
}
