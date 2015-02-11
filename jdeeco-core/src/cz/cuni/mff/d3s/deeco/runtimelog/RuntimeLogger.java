package cz.cuni.mff.d3s.deeco.runtimelog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.CustomStepTask;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * {@link RuntimeLogger} provides the functionality for the logging of
 * <a href="http://d3s.mff.cuni.cz/projects/components_and_services/deeco/">DEECo</a>
 * runtime events. The log records are stored into the following files:
 * <ul>
 * 	<li>{@link RuntimeLogger#DATA_FILE}</li>
 * 	<li>{@link RuntimeLogger#DATA_INDEX_FILE}</li>
 * 	<li>{@link RuntimeLogger#BACK_LOG_OFFSET_TABLE_FILE}</li>
 * </ul>
 * <p> The log destination can be overridden by corresponding parameters in the
 * {@link RuntimeLogger#init(CurrentTimeProvider, Scheduler, Writer, Writer, Writer)}
 * method. </p>
 * <p> The {@link RuntimeLogger#DATA_FILE} serves as the storage of logged runtime
 * events. There are the following types of records: <em>event</em> and <em>snapshot</em>.
 * <em>Event</em> can be self containing information (e.g. networking event) or a knowledge
 * change difference. They are logged via the {@link RuntimeLogger#log(RuntimeLogRecord)}
 * method. <em>Snapshot</em> records are all the data of a components knowledge. These
 * records are handled by the {@link RuntimeLogger} itself by periodically calling
 * the {@link SnapshotProvider#getSnapshot()} method on the registered {@link SnapshotProvider}
 * objects. The format of this file is a <a href="http://en.wikipedia.org/wiki/XML">XML</a> fragment.
 * Each line in this file is a <a href="http://en.wikipedia.org/wiki/XML">XML</a> element in which
 * the values are stored in its attributes.</p>
 * <p> The {@link RuntimeLogger#DATA_INDEX_FILE} stores the index of the {@link RuntimeLogger#DATA_FILE}.
 * This file is supposed to be small enough to fit into a memory and to be quickly searchable.
 * A record is stored into this file each time a <em>snapshot</em> is taken.
 * The format of this file is a <a href="http://en.wikipedia.org/wiki/XML">XML</a> fragment.
 * Each line in this file is a <a href="http://en.wikipedia.org/wiki/XML">XML</a> element in which
 * the values are stored in its attributes. There is an <em>time</em> and <em>offset</em> (number of bytes)
 * referring to the <em>runtime data file</em> stored in each element.</p>
 * <p> The {@link RuntimeLogger#BACK_LOG_OFFSET_TABLE_FILE} stores the maximum time offsets
 * for individual <em>events</em>/<em>snapshots</em>. These offsets denotes how much into the
 * past it needs to be looked for a given time, to be able to compute the whole information
 * for the system about the <em>event</em>/<em>snapshot</em>. The maximum of the values in this
 * file is the minimum time offset where to start the computation of the system information
 * to be able to deliver the consistent system state for the specified time. This file contains
 * individual provided time offsets. Each line holds one single number.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class RuntimeLogger {
	/**
	 * This {@link Writer} is used to write into the file for the runtime events being logged.
	 */
	private BufferedWriter dataWriter;
	/**
	 * This {@link Writer} is used to write into the file
	 * where the index of the log records is stored. 
	 */
	private BufferedWriter indexWriter;
	/**
	 * This {@link Writer} serves for writing the time offsets for backward jumps
	 * in the main log file.
	 * @see RuntimeLogger 
	 */
	private BufferedWriter backLogWriter;

	/**
	 * The size of the data (number of bytes) so far being written
	 * into the {@link RuntimeLogger#dataWriter}. 
	 */
	private long currentDataOffset;
	/**
	 * There are {@link SnapshotProvider}s registered on the {@link RuntimeLogger}
	 * being held in this collection before the {@link RuntimeLogger#init} method is called.
	 * With each {@link SnapshotProvider} there is associated period, specifying,
	 * how often the {@link SnapshotProvider} should be called.
	 * After the {@link RuntimeLogger#init} method is called the stored
	 * {@link SnapshotProvider}s are processed and this collection is cleared.
	 */
	private final Map<SnapshotProvider, Long> snapshotProviders;
	/**
	 * There are the time offsets registered on the {@link RuntimeLogger}
	 * being held in this collection before the {@link RuntimeLogger#init} method is called.
	 * After the {@link RuntimeLogger#init} method is called the stored time offsets
	 * are written using the {@link RuntimeLogger#backLogWriter} and this collection
	 * is cleared.
	 */
	private final List<Long> backLogOffsets;
	/**
	 * The {@link CurrentTimeProvider} that is used to add time value into each record
	 * being written using the {@link RuntimeLogger#dataWriter} or {@link RuntimeLogger#indexWriter}. 
	 */
	private CurrentTimeProvider timeProvider;
	/**
	 * The {@link Scheduler} that is used to plan the {@link Task}s
	 * that are responsible for periodic invocation of the registered {@link SnapshotProvider}s.
	 */
	private Scheduler scheduler;

	/**
	 * The singleton instance of the {@link RuntimeLogger}. This singleton holds the
	 * {@link Writer}s that access the log files. Multiple access to these files is not
	 * desirable.
	 */
	private static RuntimeLogger instance = new RuntimeLogger();

	/**
	 * Specifies the default file where the logging records of the runtime events are written.
	 * This file destination can be overridden using the
	 * {@link RuntimeLogger#init(CurrentTimeProvider, Scheduler, Writer, Writer, Writer)}
	 * method.
	 */
	private static final File DATA_FILE = new File("logs/runtimeData.xml");
	/**
	 * Specifies the default file where the index of the runtime log is stored.
	 * This file destination can be overridden using the
	 * {@link RuntimeLogger#init(CurrentTimeProvider, Scheduler, Writer, Writer, Writer)}
	 * method.
	 */
	private static final File DATA_INDEX_FILE = new File("logs/dataIndex.xml");
	/**
	 * Specifies the default file where the back log time offsets are written.
	 * This file destination can be overridden using the
	 * {@link RuntimeLogger#init(CurrentTimeProvider, Scheduler, Writer, Writer, Writer)}
	 * method.
	 */
	private static final File BACK_LOG_OFFSET_TABLE_FILE = new File("logs/backLogOffsetTable.xml");
	
	/**
	 * Specifies whether the log files are supposed to be overwritten (false) or appended (true)
	 * when a new run of <a href="http://d3s.mff.cuni.cz/projects/components_and_services/deeco/">DEECo</a>
	 * is issued.
	 */
	private static final boolean APPEND_TO_OLD_LOG = false;
	/**
	 * The encoding used to write into each log file. the number of bytes written for each character
	 * depends on the selected encoding.
	 */
	private static final String CHARSET_NAME = "UTF-8";

	/**
	 * The name of the <a href="http://en.wikipedia.org/wiki/XML">XML</a> element
	 * used for storing the events being logged.
	 */
	private static final String EVENT_RECORD_NAME = "event";
	/**
	 * The name of the <a href="http://en.wikipedia.org/wiki/XML">XML</a> element
	 * used for storing the snapshots.
	 */
	private static final String SNAPSHOT_RECORD_NAME = "snapshot";
	/**
	 * The name of the <a href="http://en.wikipedia.org/wiki/XML">XML</a> element attribute
	 * that is used for the ID of the component that is logging the event.
	 */
	private static final String RECORD_ID = "componentID";
	/**
	 * The name of the <a href="http://en.wikipedia.org/wiki/XML">XML</a> element attribute
	 * that is used for the type of the event that is being logged.
	 */
	private static final String RECORD_TYPE = "eventType";
	/**
	 * The name of the <a href="http://en.wikipedia.org/wiki/XML">XML</a> element attribute
	 * that is used for the time of the event being logged.
	 */
	private static final String RECORD_TIME = "time";
	/**
	 * The name of the <a href="http://en.wikipedia.org/wiki/XML">XML</a> element attribute
	 * that is used for the byte offset when an index record is being written.
	 */
	private static final String RECORD_OFFSET = "offset";

	/**
	 * Private constructor of the {@link RuntimeLogger} singleton instance.
	 * The {@link RuntimeLogger#snapshotProviders} and {@link RuntimeLogger#backLogOffsets}
	 * are being initialized in this constructor. 
	 */
	private RuntimeLogger() {
		snapshotProviders = new HashMap<SnapshotProvider, Long>();
		backLogOffsets = new ArrayList<Long>();
	}

	/**
	 * Provides an {@link Writer} for the given <em>file</em>. If there arise an {@link Exception}
	 * during the creating of the {@link Writer} it is being logged and propagated outwards
	 * from this method.
	 * @param file specifies the file to be used for the {@link Writer}.
	 * @return The {@link Writer} for the given <em>file</em>.
	 * @throws IOException Thrown is the {@link Writer} cannot be opened for the given <em>file</em>.
	 */
	private static Writer openStream(File file) throws IOException {
		try {
			return new OutputStreamWriter(new FileOutputStream(file,
					APPEND_TO_OLD_LOG), CHARSET_NAME);
		} catch (IOException e) {
			Log.e("Simulation logging not enabled. Failed to open the log file "
					+ DATA_FILE.getAbsolutePath(), e);
			throw e;
		}
	}

	/**
	 * When the {@link CurrentTimeProvider} and the {@link Scheduler} are ready it is necessary
	 * to initialize the {@link RuntimeLogger} before any call to the {@link RuntimeLogger#log} and
	 * {@link RuntimeLogger#logSnapshot(RuntimeLogRecord)} method. In this method
	 * there are the log files being opened. Also there are created
	 * {@link Task}s for the registered {@link SnapshotProviders}.
	 * The registered <em>back log offsets</em> are written into the {@link RuntimeLogger#backLogWriter}.
	 * @param currentTimeProvider Provides the current time of the
	 * <a href="http://d3s.mff.cuni.cz/projects/components_and_services/deeco/">DEECo</a> runtime.
	 * @param scheduler Serves for planning the {@link Task}s for
	 * registered {@link SnapshotProvider}s.
	 * @throws IOException Thrown if any of the log files cannot be opened or written into.
	 * @throws IllegalArgumentException Thrown if any of the input arguments is null. 
	 */
	public static void init(CurrentTimeProvider currentTimeProvider,
			Scheduler scheduler) throws IOException {
		Writer dataOut = openStream(DATA_FILE);
		Writer indexOut = openStream(DATA_INDEX_FILE);
		Writer backLogOut = openStream(BACK_LOG_OFFSET_TABLE_FILE);

		init(currentTimeProvider, scheduler, dataOut, indexOut, backLogOut);
	}

	/**
	 * When the {@link CurrentTimeProvider} and the {@link Scheduler} are ready it is necessary
	 * to initialize the {@link RuntimeLogger} before any call to the {@link RuntimeLogger#log} and
	 * {@link RuntimeLogger#logSnapshot(RuntimeLogRecord)} method. In this method there are the log
	 * {@link Writer}s being opened. Also there are created {@link Task}s
	 * for the registered {@link SnapshotProviders}. The registered <em>back log offsets</em>
	 * are written into the {@link RuntimeLogger#backLogWriter}.
	 * @param currentTimeProvider Provides the current time of the
	 * <a href="http://d3s.mff.cuni.cz/projects/components_and_services/deeco/">DEECo</a> runtime.
	 * @param scheduler Serves for planning the {@link Task}s for
	 * @param dataOut specifies the {@link Writer} for the <em>log data</em>.
	 * @param indexOut specifies the {@link Writer} for the <em>index data</em>.
	 * @param backLogOut specifies the {@link Writer} for the <em>time offsets</em>.
	 * @throws IOException Thrown if any of the log files cannot be opened or written into.
	 * @throws IllegalArgumentException Thrown if any of the input arguments is null.
	 */
	public static void init(CurrentTimeProvider currentTimeProvider,
			Scheduler scheduler, Writer dataOut, Writer indexOut,
			Writer backLogOut) throws IOException {
		if (currentTimeProvider == null)
			throw new IllegalArgumentException(String.format(
					"The argument \"%s\" is null.", "currentTimeProvider"));
		if (scheduler == null)
			throw new IllegalArgumentException(String.format(
					"The argument \"%s\" is null.", "scheduler"));
		if (dataOut == null)
			throw new IllegalArgumentException(String.format(
					"The argument \"%s\" is null.", "dataOut"));
		if (indexOut == null)
			throw new IllegalArgumentException(String.format(
					"The argument \"%s\" is null.", "indexOut"));
		if (backLogOut == null)
			throw new IllegalArgumentException(String.format(
					"The argument \"%s\" is null.", "backLogOut"));

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

		for (Long time : instance.backLogOffsets) {
			instance.backLogWriter.write(String.format("%d\n", time));
		}
		instance.backLogWriter.flush();
		instance.backLogOffsets.clear();

	}

	/**
	 * Provides the singleton instance of the {@link RuntimeWriter}.
	 * @return The singleton instance of the {@link RuntimeWriter}.
	 */
	public static RuntimeLogger getInstance() {
		return instance;
	}

	/**
	 * Writes the data from the given <em>record</em> to the {@link RuntimeLogger#dataWriter}.
	 * The written record has a format of an <a href="http://en.wikipedia.org/wiki/XML">XML</a>
	 * element. The values from the given <em>record</em> are stored as attributes of the
	 * written <a href="http://en.wikipedia.org/wiki/XML">XML</a> element.
	 * <p> The {@link Writer} is not being flushed when this method is called. </p>
	 * @param record contains the data that will be logged.
	 * @throws IOException Thrown if there is a problem writing into the {@link Writer}s
	 * for the logging.
	 * @throws IllegalStateException Thrown if the {@link RuntimeLogger#init} method hasn't
	 * been called before this method is being called.
	 * @throws IllegalArgumentException Thrown if the given argument is null.
	 */
	public static void log(RuntimeLogRecord record) throws IOException,
			IllegalStateException {
		log(record, false);
	}

	/**
	 * Writes the data from the given <em>snapshotData</em> argument to the {@link RuntimeLogger#dataWriter}.
	 * The written record has a format of an <a href="http://en.wikipedia.org/wiki/XML">XML</a>
	 * element. The values from the given <em>snapshotData</em> argument are stored as attributes of the
	 * written <a href="http://en.wikipedia.org/wiki/XML">XML</a> element. Also an index record
	 * pointing to this snapshot record is written using the {@link RuntimeLogger#indexWriter}.
	 * <p> The {@link Writer}s are being flushed when this method is called. </p>
	 * @param snapshotData contains the data that will be logged.
	 * @throws IOException Thrown if there is a problem writing into the {@link Writer}s
	 * for the logging.
	 * @throws IllegalStateException Thrown if the {@link RuntimeLogger#init} method hasn't
	 * been called before this method is being called.
	 * @throws IllegalArgumentException Thrown if the given argument is null.
	 */
	public static void logSnapshot(RuntimeLogRecord snapshotData)
			throws IOException, IllegalStateException {
		log(snapshotData, true);
	}

	/**
	 * Writes the data from the given <em>record</em> to the {@link RuntimeLogger#dataWriter}.
	 * The written record has a format of an <a href="http://en.wikipedia.org/wiki/XML">XML</a>
	 * element. The values from the given <em>record</em> argument are stored as attributes of the
	 * written <a href="http://en.wikipedia.org/wiki/XML">XML</a> element. If the <em>snapshot</em>
	 * argument is true also an index record pointing to the current snapshot record is written
	 * using the {@link RuntimeLogger#indexWriter}.
	 * <p> The {@link Writer}s are being flushed when the <em>snapshot</em> argument is true. </p> 
	 * @param record contains the data that will be logged.
	 * @param snapshot indicates whether the <em>record</em> holds a snapshot (true) or an event (false).
	 * @throws IOException Thrown if there is a problem writing into the {@link Writer}s
	 * for the logging.
	 * @throws IllegalStateException Thrown if the {@link RuntimeLogger#init} method hasn't
	 * been called before this method is being called.
	 * @throws IllegalArgumentException Thrown if the given argument is null.
	 */
	private static void log(RuntimeLogRecord record, boolean snapshot) // TODO: check whether the record fields are not null
			throws IOException, IllegalStateException {
		if (instance.timeProvider == null)
			throw new IllegalStateException(
					"The SimLogger class not initialized.");
		if (record == null)
			throw new IllegalArgumentException(String.format(
					"The argument \"%s\" is null.", "record"));

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
					+ DATA_FILE.getAbsolutePath() + " or "
					+ DATA_INDEX_FILE.getAbsolutePath(), e);
			throw e;
		}
	}

	/**
	 * Creates a {@link Task} for the given <em>snapshotProvider</em>
	 * This {@link Task} will periodically record the snapshots
	 * using the {@link SnapshotProvider#getSnapshot()} method.
	 * <p> This method is save to be called before the {@link RuntimeLogger#init} method is. </p>
	 * @param snapshotProvider is the {@link SnapshotProvider} that will be registered. 
	 * @param period specifies the period for the repetitive invocation of the given <em>snapshotProvider</em>.
	 * @throws IOException Thrown if the {@link RuntimeLogger#backLogWriter} is unable to be
	 * written into.
	 * @throws IllegalArgumentException Thrown if the <em>snapshotProvider</em> argument is null
	 * or the <em>time</em> is less or equal to 0.
	 */
	public static void registerSnapshotProvider(
			SnapshotProvider snapshotProvider, long period) throws IOException {
		if (snapshotProvider == null)
			throw new IllegalArgumentException(String.format(
					"The argument \"%s\" is null.", "snapshotProvider"));
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

	/**
	 * Writes the given <em>time</em> into the {@link RuntimeLogger#backLogWriter}.
	 * <p> This method is save to be called before the {@link RuntimeLogger#init} method is. </p>
	 * @param time specifies the time offset to be remembered as a <em>back log offset</em>.
	 * @throws IOException Thrown if the {@link RuntimeLogger#backLogWriter} is unable to be
	 * written into.
	 * @throws IllegalArgumentException Thrown if the <em>time</em> is less or equal to 0.
	 */
	public static void registerBackLogOffset(long time) throws IOException {
		if (time <= 0)
			throw new IllegalArgumentException(String.format(
					"The argument \"%s\" has to be greater than 0.", "time"));
		if (instance.scheduler == null) // If not initialized, store the snapshot provider for later
		{
			instance.backLogOffsets.add(time);
		} else // If initialized register task for the snapshot provider
		{
			instance.backLogWriter.write(String.format("%d\n", time));
			instance.backLogWriter.flush();
		}
	}

	/**
	 * Flushes the following {@link Writer}s:
	 * <ul>
	 * 	<li>{@link RuntimeLogger#DATA_FILE}</li>
	 * 	<li>{@link RuntimeLogger#DATA_INDEX_FILE}</li>
	 * 	<li>{@link RuntimeLogger#BACK_LOG_OFFSET_TABLE_FILE}</li>
	 * </ul>
	 * The {@link Writer}s has to be opened in order the be flushed. See the
	 * {@link RuntimeLogger#init} method.
	 * @throws IOException Thrown if any of the {@link Writer}s cannot be flushed.
	 * @throws IllegalStateException Thrown if the {@link RuntimeLogger#init} method hasn't
	 * been called before this method is being called.
	 */
	public static void flush() throws IOException {
		if (instance.timeProvider == null)
			throw new IllegalStateException(
					"The SimLogger class not initialized.");

		if (instance.dataWriter != null) {
			instance.dataWriter.flush();
		}
		if (instance.indexWriter != null) {
			instance.indexWriter.flush();
		}
		if (instance.backLogWriter != null) {
			instance.backLogWriter.flush();
		}
	}

	/**
	 * Closes the following {@link Writer}s:
	 * <ul>
	 * 	<li>{@link RuntimeLogger#DATA_FILE}</li>
	 * 	<li>{@link RuntimeLogger#DATA_INDEX_FILE}</li>
	 * 	<li>{@link RuntimeLogger#BACK_LOG_OFFSET_TABLE_FILE}</li>
	 * </ul>
	 * The {@link Writer}s has to be opened in order the be closed. See the
	 * {@link RuntimeLogger#init} method.
	 * @throws IOException Thrown if any of the {@link Writer}s cannot be closed.
	 * @throws IllegalStateException Thrown if the {@link RuntimeLogger#init} method hasn't
	 * been called before this method is being called.
	 */
	public static void close() throws IOException {
		
		if (instance.dataWriter != null) {
			instance.dataWriter.close();
		}
		if (instance.indexWriter != null) {
			instance.indexWriter.close();
		}
		if (instance.backLogWriter != null) {
			instance.backLogWriter.close();
		}
	}

	/**
	 * This method is opposite to the {@link RuntimeLogger#init} method. It doesn't
	 * supplies the functionality for {@link RuntimeLogger#close} method, that needs
	 * to be called separately before this method is called. The purpose of this method
	 * is to uninit the {@link RuntimeLogger} singleton instance. The visibility specifier
	 * is not defined on purpose - it defines package visibility, therefore this method
	 * is visible in tests and that is the only place where it should be used.
	 */
	static void unload() 
	{
		instance.timeProvider = null;
		instance.scheduler = null;
		instance.currentDataOffset = 0;
		instance.snapshotProviders.clear();
		instance.backLogOffsets.clear();
	}

}