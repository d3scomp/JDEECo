package cz.cuni.mff.d3s.deeco.simlog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.CustomStepTask;

public class SimLogger
{	
	// These files probably configurable in config file
	private final File simDataFile = new File("logs/simData.xml");
	private final File dataIndexFile = new File("logs/dataIndex.xml");
	private final File backLogOffsetTableFile = new File("logs/backLogOffsetTable.xml");
	
	private BufferedWriter dataWriter;
	private BufferedWriter indexWriter;
	private BufferedWriter backLogWriter;
	
	private long currentDataOffset;
	private final Map<SnapshotProvider, Long> snapshotProviders;
	private CurrentTimeProvider timeProvider;
	private Scheduler scheduler;
	
	private static SimLogger instance = new SimLogger();
	
	private final boolean APPEND_TO_OLD_LOG = false;
	private static final String CHARSET_NAME = "UTF-8";
	
	private static final String EVENT_RECORD_NAME = "event";
	private static final String SNAPSHOT_RECORD_NAME = "snapshot";
	private static final String RECORD_ID = "event_id";
	private static final String RECORD_TYPE = "event_type";
	private static final String RECORD_TIME = "time";
	private static final String RECORD_OFFSET = "offset";
	
	private SimLogger()
	{
		snapshotProviders = new HashMap<SnapshotProvider, Long>();
		currentDataOffset = 0;
	}
	
	private BufferedWriter openFile(File file) throws IOException
	{
		try
		{
			return new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, APPEND_TO_OLD_LOG),
					CHARSET_NAME));
		}
		catch(IOException e)
		{
			Log.e("Simulation logging not enabled. Failed to open the log file " + simDataFile.getAbsolutePath(), e);
			throw e;
		}
	}
	
	public void init(CurrentTimeProvider currentTimeProvider, Scheduler scheduler) throws IOException
	{
		// Opening the files in init method to avoid IOException in the constructor
		dataWriter = openFile(simDataFile);
		indexWriter = openFile(dataIndexFile);
		backLogWriter = openFile(backLogOffsetTableFile);
		
		this.timeProvider = currentTimeProvider;
		this.scheduler = scheduler;
		
		for (SnapshotProvider sp: snapshotProviders.keySet())
		{
			long period = snapshotProviders.get(sp);
			SimLogTimerTaskListener slttListener = new SimLogTimerTaskListener(sp, period);
			scheduler.addTask(new CustomStepTask(scheduler, slttListener, period));
		}
		
		snapshotProviders.clear();
	}
	
	public static SimLogger getInstance()
	{
		return instance;
	}
	
	public static void log(SimLogRecord record) throws IOException, IllegalStateException
	{
		if(instance == null) throw new IllegalStateException("The SimLogger class not initialized.");
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("<")
				.append(EVENT_RECORD_NAME)
				.append(" ")
				.append(RECORD_ID)
				.append("=\"")
				.append(record.getID())
				.append("\" ")
				.append(RECORD_TYPE)
				.append("=\"")
				.append(record.getEventType())
				.append("\" ")
				.append(RECORD_TIME)
				.append("=\"")
				.append(instance.timeProvider.getCurrentMilliseconds())
				.append("\" ");
		
		Map<String, Object> values = record.getValues(); 
		for(String valueKey : values.keySet())
		{
			builder.append(valueKey)
					.append("=\"")
					.append(values.get(valueKey))
					.append("\" ");
		}
		
		builder.append("/>\n");
		
		try
		{
			String compiledRecord = builder.toString();
			instance.dataWriter.write(compiledRecord);
			instance.currentDataOffset += compiledRecord.getBytes(CHARSET_NAME).length;
		}
		catch(IOException e)
		{
			Log.e("Failed to write to the log file " + instance.simDataFile.getAbsolutePath(), e);
			throw e;
		}
	}
	
	public static void logSnapshot(Map<String, Object> snapshotData) throws IOException, IllegalStateException // TODO: Wouldn't it be better to merge this method with the log(...) method and distinguish the snapshot log in the LogRecord parameter?
	{
		if(instance == null) throw new IllegalStateException("The SimLogger class not initialized.");
		
		StringBuilder recordBuilder = new StringBuilder();
		StringBuilder indexBuilder = new StringBuilder();
		
		recordBuilder.append("<")
				.append(SNAPSHOT_RECORD_NAME)
				.append(" ");
		 
		for(String valueKey : snapshotData.keySet())
		{
			recordBuilder.append(valueKey)
					.append("=\"")
					.append(snapshotData.get(valueKey))
					.append("\" ");
		}
		
		recordBuilder.append("/>\n");
		
		indexBuilder.append("<")
					.append(SNAPSHOT_RECORD_NAME)
					.append(" ")
					.append(RECORD_TIME)
					.append("=\"")
					.append(instance.timeProvider.getCurrentMilliseconds())
					.append("\" ")
					.append(RECORD_OFFSET)
					.append("=\"")
					.append(instance.currentDataOffset)
					.append("\" />\n");
		
		try
		{
			String compiledRecord = recordBuilder.toString();
			instance.dataWriter.write(compiledRecord);
			instance.dataWriter.flush();
			instance.currentDataOffset += compiledRecord.getBytes(CHARSET_NAME).length;

			instance.indexWriter.write(indexBuilder.toString());
			instance.indexWriter.flush();
		}
		catch(IOException e)
		{
			Log.e("Failed to write to the log file " + instance.simDataFile.getAbsolutePath()
					+ " or " + instance.dataIndexFile.getAbsolutePath(), e);
			throw e;
		}
	}
	
	public void registerSnapshotProvider(SnapshotProvider snapshotProvider, long period) throws IOException
	{
		if(scheduler == null) // If not initialized, store the snapshot provider for later
		{
			snapshotProviders.put(snapshotProvider, period);
		}
		else // If initialized register task for the snapshot provider
		{
			SimLogTimerTaskListener slttListener = new SimLogTimerTaskListener(snapshotProvider, period);
			scheduler.addTask(new CustomStepTask(scheduler, slttListener, period));	
		}
		
		registerBackLogOffset(period);
	}
	
	public void registerBackLogOffset(long time) throws IOException
	{
		backLogWriter.write(String.format("%d\n", time)); // TODO: make sure that the back log file is being written into after the init method was called
	}
	
}