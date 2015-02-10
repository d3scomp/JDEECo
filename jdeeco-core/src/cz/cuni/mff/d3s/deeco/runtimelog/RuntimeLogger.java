package cz.cuni.mff.d3s.deeco.runtimelog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.CustomStepTask;

public class RuntimeLogger {
	// These files probably configurable in config file
	private final File simDataFile = new File("logs/simData.xml");
	private final File dataIndexFile = new File("logs/dataIndex.xml");
	private final File backLogOffsetTableFile = new File(
			"logs/backLogOffsetTable.xml");

	private BufferedWriter dataWriter;
	private BufferedWriter indexWriter;
	private BufferedWriter backLogWriter;

	private long currentDataOffset;
	private final Map<SnapshotProvider, Long> snapshotProviders;
	private CurrentTimeProvider timeProvider;
	private Scheduler scheduler;

	private static RuntimeLogger instance = new RuntimeLogger();

	private final boolean APPEND_TO_OLD_LOG = false;
	private static final String CHARSET_NAME = "UTF-8";

	private static final String EVENT_RECORD_NAME = "event";
	private static final String SNAPSHOT_RECORD_NAME = "snapshot";
	private static final String RECORD_ID = "event_id";
	private static final String RECORD_TYPE = "event_type";
	private static final String RECORD_TIME = "time";
	private static final String RECORD_OFFSET = "offset";

	private RuntimeLogger() {
		snapshotProviders = new HashMap<SnapshotProvider, Long>();
	}

	private static OutputStreamWriter openStream(File file) throws IOException {
		try {
			return new OutputStreamWriter(new FileOutputStream(file,
					instance.APPEND_TO_OLD_LOG), CHARSET_NAME);
		} catch (IOException e) {
			Log.e("Simulation logging not enabled. Failed to open the log file "
					+ instance.simDataFile.getAbsolutePath(), e);
			throw e;
		}
	}

	public static void init(CurrentTimeProvider currentTimeProvider,
			Scheduler scheduler) throws IOException {
		OutputStreamWriter dataOut = openStream(instance.simDataFile);
		OutputStreamWriter indexOut = openStream(instance.dataIndexFile);
		OutputStreamWriter backLogOut = openStream(instance.backLogOffsetTableFile);

		init(currentTimeProvider, scheduler, dataOut, indexOut, backLogOut);
	}

	public static void init(CurrentTimeProvider currentTimeProvider,
			Scheduler scheduler, Writer dataOut, Writer indexOut,
			Writer backLogOut) throws IOException {
		// Opening the files in init method to avoid IOException in the constructor
		instance.dataWriter = new BufferedWriter(dataOut);
		instance.indexWriter = new BufferedWriter(indexOut);
		instance.backLogWriter = new BufferedWriter(backLogOut);

		instance.timeProvider = currentTimeProvider;
		instance.scheduler = scheduler;

		instance.currentDataOffset = 0;

		for (SnapshotProvider sp : instance.snapshotProviders.keySet()) {
			long period = instance.snapshotProviders.get(sp);
			RuntimeLogTimerTaskListener slttListener = new RuntimeLogTimerTaskListener(
					sp, period);
			scheduler.addTask(new CustomStepTask(scheduler, slttListener,
					period));
		}

		instance.snapshotProviders.clear();
	}

	public static RuntimeLogger getInstance() {
		return instance;
	}

	public static void log(RuntimeLogRecord record) throws IOException,
			IllegalStateException {
		log(record, false);
	}

	public static void logSnapshot(RuntimeLogRecord snapshotData)
			throws IOException, IllegalStateException {
		log(snapshotData, true);
	}

	private static void log(RuntimeLogRecord record, boolean snapshot)
			throws IOException, IllegalStateException {
		if (instance.timeProvider == null)
			throw new IllegalStateException(
					"The SimLogger class not initialized.");

		StringBuilder recordBuilder = new StringBuilder();
		String recordName = snapshot ? SNAPSHOT_RECORD_NAME : EVENT_RECORD_NAME;
		long currentDataOffset = instance.currentDataOffset;

		recordBuilder.append("<").append(recordName).append(" ")
				.append(RECORD_ID).append("=\"")
				.append(record.getComponentID()).append("\" ")
				.append(RECORD_TYPE).append("=\"")
				.append(record.getEventType()).append("\" ")
				.append(RECORD_TIME).append("=\"")
				.append(instance.timeProvider.getCurrentMilliseconds())
				.append("\" ");

		Map<String, Object> values = record.getValues();
		for (String valueKey : values.keySet()) {
			recordBuilder.append(valueKey).append("=\"")
					.append(values.get(valueKey)).append("\" ");
		}

		recordBuilder.append("/>\n");

		try {
			String compiledRecord = recordBuilder.toString();
			instance.dataWriter.write(compiledRecord);
			instance.currentDataOffset += compiledRecord.getBytes(CHARSET_NAME).length;

			if (snapshot) {
				StringBuilder indexBuilder = new StringBuilder();

				indexBuilder.append("<").append(SNAPSHOT_RECORD_NAME)
						.append(" ").append(RECORD_TIME).append("=\"")
						.append(instance.timeProvider.getCurrentMilliseconds())
						.append("\" ").append(RECORD_OFFSET).append("=\"")
						.append(currentDataOffset).append("\" />\n");

				instance.dataWriter.flush();
				instance.indexWriter.write(indexBuilder.toString());
				instance.indexWriter.flush();
			}
		} catch (IOException e) {
			Log.e("Failed to write to the log file "
					+ instance.simDataFile.getAbsolutePath() + " or "
					+ instance.dataIndexFile.getAbsolutePath(), e);
			throw e;
		}
	}

	public static void registerSnapshotProvider(SnapshotProvider snapshotProvider,
			long period) throws IOException {
		if (instance.scheduler == null) // If not initialized, store the snapshot provider for later
		{
			instance.snapshotProviders.put(snapshotProvider, period);
		} else // If initialized register task for the snapshot provider
		{
			RuntimeLogTimerTaskListener slttListener = new RuntimeLogTimerTaskListener(
					snapshotProvider, period);
			instance.scheduler.addTask(new CustomStepTask(instance.scheduler,
					slttListener, period));
		}

		registerBackLogOffset(period);
	}

	public static void registerBackLogOffset(long time) throws IOException {
		instance.backLogWriter.write(String.format("%d\n", time));
		// TODO: make sure that the back log file is being written into after the init method was called
	}

	public static void flush() throws IOException {
		if (instance.timeProvider == null)
			throw new IllegalStateException(
					"The SimLogger class not initialized.");

		instance.dataWriter.flush();
		instance.indexWriter.flush();
		instance.backLogWriter.flush();
	}

	public static void close() throws IOException {
		instance.dataWriter.close();
		instance.indexWriter.close();
		instance.backLogWriter.close();
	}
	
	static void unload() // Visible in tests, opposite to init. Doesn't replace close.
	{
		instance.timeProvider = null;
		instance.scheduler = null;
		instance.currentDataOffset = 0;
		instance.snapshotProviders.clear();
	}

}