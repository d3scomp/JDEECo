package cz.cuni.mff.d3s.deeco.runtimelog;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.CustomStepTask;
import cz.cuni.mff.d3s.deeco.task.Task;
import cz.cuni.mff.d3s.deeco.task.TimerTaskListener;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;

/**
 * {@link RuntimeLogger} provides the functionality for the logging of
 * <a href="http://d3s.mff.cuni.cz/projects/components_and_services/deeco/">DEECo</a>
 * runtime events. The log records are by default stored into the following files:
 * <ul>
 * 	<li>{@link RuntimeLogWriter#DATA_FILE}</li>
 * 	<li>{@link RuntimeLogWriter#DATA_INDEX_FILE}</li>
 * 	<li>{@link RuntimeLogWriter#SNAPSHOT_PERIOD_FILE}</li>
 * </ul>
 * <p> The log destination can be overridden by corresponding parameters in the
 * {@link RuntimeLogger#init(CurrentTimeProvider, Scheduler, Writer, Writer, Writer)}
 * method. </p>
 * <p> The {@link RuntimeLogWriter#DATA_FILE} serves as the storage of logged runtime
 * events. There are the following types of records: <em>event</em> and <em>snapshot</em>.
 * <em>Event</em> can be self containing information (e.g. networking event) or a knowledge
 * change difference. They are logged via the {@link RuntimeLogger#log(RuntimeLogRecord)}
 * method. <em>Snapshot</em> records are all the data of a components knowledge. These
 * records are handled by the {@link RuntimeLogger} itself by periodically calling
 * the {@link SnapshotProvider#getSnapshot()} method on the registered {@link SnapshotProvider}
 * objects. The format of this file is a <a href="http://en.wikipedia.org/wiki/XML">XML</a> fragment.
 * Each line in this file is a <a href="http://en.wikipedia.org/wiki/XML">XML</a> element in which
 * the values are stored in its attributes.</p>
 * <p> The {@link RuntimeLogWriter#DATA_INDEX_FILE} stores the index of the {@link RuntimeLogWriter#DATA_FILE}.
 * This file is supposed to be small enough to fit into a memory and to be quickly searchable.
 * A record is stored into this file each time a <em>snapshot</em> is taken.
 * The format of this file is a <a href="http://en.wikipedia.org/wiki/XML">XML</a> fragment.
 * Each line in this file is a <a href="http://en.wikipedia.org/wiki/XML">XML</a> element in which
 * the values are stored in its attributes. There is an <em>time</em> and <em>offset</em> (number of bytes)
 * referring to the <em>runtime data file</em> stored in each element.</p>
 * <p> The {@link RuntimeLogWriter#SNAPSHOT_PERIOD_FILE} stores the maximum time periods
 * for individual <em>events</em>/<em>snapshots</em>. These periods denotes how much into the
 * past it needs to be looked for a given time, to be able to compute the whole information
 * for the system about the <em>event</em>/<em>snapshot</em>. The maximum of the values in this
 * file is the minimum time offset where to start the computation of the system information
 * to be able to deliver the consistent system state for the specified time. This file contains
 * individual provided time offsets. Each line holds one single number and type of the corresponding record.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class RuntimeLogger {
	/**
	 * The set of the record types that was registered either via the {@link RuntimeLogger#registerSnapshotPeriod(long, Class)}
	 * or via the {@link RuntimeLogger#registerSnapshotProvider(SnapshotProvider, long)} method.
	 * When a record of a type that is stored in this variable is being logged, the
	 * {@link RuntimeLogWriter#DATA_INDEX_FILE} is being updated accordingly and both the
	 * {@link RuntimeLogWriter#DATA_FILE} and {@link RuntimeLogWriter#DATA_INDEX_FILE} are flushed.
	 */
	private final Set<Class<? extends RuntimeLogRecord>> snapshotTypes;
	/**
	 * The {@link CurrentTimeProvider} that is used to add time value into each record
	 * being written using the {@link RuntimeLogWriter#dataWriter} or {@link RuntimeLogWriter#indexWriter}. 
	 */
	private final CurrentTimeProvider timeProvider;
	/**
	 * The {@link Scheduler} that is used to plan the {@link Task}s
	 * that are responsible for periodic invocation of the registered {@link SnapshotProvider}s.
	 */
	private final Scheduler scheduler;

	/**
	 * The runtime log writer used by the instance of {@link RuntimeLogger}.
	 */
	private final RuntimeLogWriter writer;
	
	/**
	 * The name of the <a href="http://en.wikipedia.org/wiki/XML">XML</a> element
	 * used for storing the events being logged.
	 */
	private static final String EVENT_RECORD_NAME = "event";
	/**
	 * The name of the <a href="http://en.wikipedia.org/wiki/XML">XML</a> element
	 * that holds a single value from a collection.
	 */
	private static final String COLLECION_VALUE_ENCLOSURE = "value";
	/**
	 * The name of the <a href="http://en.wikipedia.org/wiki/XML">XML</a> element attribute
	 * that is used for the ID of the component that is logging the event.
	 */
	private static final String RECORD_ID = "id";
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
	 * The minimum time difference between records in the {@link RuntimeLogWriter#DATA_INDEX_FILE}.
	 * This value limits how often the {@link RuntimeLogWriter#DATA_INDEX_FILE} is being updated.
	 */
	private static final long INDEX_MIN_PERIOD = 10; // milliseconds

	/**
	 * The constructor of {@link RuntimeLogger} instance.
	  * @param currentTimeProvider Provides the current time of the
	 * <a href="http://d3s.mff.cuni.cz/projects/components_and_services/deeco/">DEECo</a> runtime.
	 * @param scheduler Serves for planning the {@link Task}s for
	 * @param writers specifies the {@link RuntimeLogWriters} for the <em>runtime data</em>.
	 * @throws IOException Thrown if any of the log files cannot be opened or written into.
	 * @throws IllegalArgumentException Thrown if any of the input arguments is null.
	 */
	public RuntimeLogger(CurrentTimeProvider currentTimeProvider,
			Scheduler scheduler, RuntimeLogWriters writers) throws IOException {
		if (currentTimeProvider == null)
			throw new IllegalArgumentException(String.format(
					"The argument \"%s\" is null.", "currentTimeProvider"));
		if (scheduler == null)
			throw new IllegalArgumentException(String.format(
					"The argument \"%s\" is null.", "scheduler"));
		if (writers == null)
			throw new IllegalArgumentException(String.format(
					"The argument \"%s\" is null.", "writers"));

		snapshotTypes = new HashSet<Class<? extends RuntimeLogRecord>>();

		timeProvider = currentTimeProvider;
		this.scheduler = scheduler;
		
		writer = new RuntimeLogWriter(writers);
	}
	
	/**
	 * The constructor of {@link RuntimeLogger} instance.
	 * @param currentTimeProvider Provides the current time of the
	 * <a href="http://d3s.mff.cuni.cz/projects/components_and_services/deeco/">DEECo</a> runtime.
	 * @param scheduler Serves for planning the {@link Task}s for
	 * registered {@link SnapshotProvider}s.
	 * @throws IOException Thrown if any of the log files cannot be opened or written into.
	 * @throws IllegalArgumentException Thrown if any of the input arguments is null. 
	 */
	public RuntimeLogger(CurrentTimeProvider currentTimeProvider,
			Scheduler scheduler) throws IOException {
		if (currentTimeProvider == null)
			throw new IllegalArgumentException(String.format(
					"The argument \"%s\" is null.", "currentTimeProvider"));
		if (scheduler == null)
			throw new IllegalArgumentException(String.format(
					"The argument \"%s\" is null.", "scheduler"));

		snapshotTypes = new HashSet<Class<? extends RuntimeLogRecord>>();

		timeProvider = currentTimeProvider;
		this.scheduler = scheduler;
		
		writer = RuntimeLogWriter.getDefaultWriter();
	}

	/**
	 * Writes the data from the given <em>record</em> to the {@link RuntimeLogWriter#dataWriter}.
	 * The written record has a format of an <a href="http://en.wikipedia.org/wiki/XML">XML</a>
	 * element. The values from the given <em>record</em> argument are stored hierarchically under
	 * the written <a href="http://en.wikipedia.org/wiki/XML">XML</a> element. If the <em>record</em>
	 * type is registered as a snapshot, also an index record pointing to the current record is written
	 * using the {@link RuntimeLogWriter#indexWriter}.
	 * <p> The {@link Writer}s are flushed when the <em>index</em> file is being written into. </p> 
	 * @param record contains the data that will be logged.
	 * @throws IOException Thrown if there is a problem writing into the {@link Writer}s
	 * for the logging.
	 * @throws IllegalStateException Thrown if the {@link RuntimeLogger#init} method hasn't
	 * been called before this method is being called.
	 * @throws IllegalArgumentException Thrown if the given argument is null. Or if the <em>record</em>
	 * returns null when the {@link RuntimeLogRecord#getId()} or the {@link RuntimeLogRecord#getValues()}
	 * method is called.
	 */
	public void log(RuntimeLogRecord record)
			throws IOException, IllegalStateException {
		if (record == null)
			throw new IllegalArgumentException(String.format(
					"The argument \"%s\" is null.", "record"));
		if(record.getId() == null) throw new IllegalArgumentException(String.format(
				"The %s method invoked on the %s argument returns null.", "getID()", "record"));
		if(record.getValues() == null) throw new IllegalArgumentException(String.format(
				"The %s method invoked on the %s argument returns null.", "getValues()", "record"));
		
		
		StringBuilder recordBuilder = new StringBuilder();
		long dataOffset = writer.getCurrentDataOffset();
		long currentTime = timeProvider.getCurrentMilliseconds(); 

		// Open the record tag
		recordBuilder.append("<").append(EVENT_RECORD_NAME).append(" ")
				.append(RECORD_TIME).append("=\"")
				.append(currentTime).append("\" ")
				.append(RECORD_ID).append("=\"")
				.append(record.getId()).append("\" ")
				.append(RECORD_TYPE).append("=\"")
				.append(record.getClass().getCanonicalName()).append("\" >\n");

		// Write the knowledge
		Map<String, Object> values = record.getValues();
		for (String valueKey : values.keySet()) {
			recordBuilder.append("\t<")
				.append(valueKey)
				.append(">");
			writeKnowledge(recordBuilder, values.get(valueKey));
			recordBuilder.append("</")
				.append(valueKey)
				.append(">\n");
		}

		// Close the record tag
		recordBuilder.append("</").append(EVENT_RECORD_NAME).append(">\n");

		String compiledRecord = recordBuilder.toString();
		writer.writeData(compiledRecord);

		// If a snapshot is being logged and there passed enough time since the last index was written
		if (snapshotTypes.contains(record.getClass())
				&& currentTime - writer.getLastIndexTime() >= INDEX_MIN_PERIOD) {
			logIndex(currentTime, dataOffset);
		}
	}
	
	/**
	 * Write the <em>currentTime</em> and the <em>dataOffset</em> into the {@link RuntimeLogWriter#indexWriter}
	 * and flush both the {@link RuntimeLogWriter#dataWriter} and the {@link RuntimeLogWriter#indexWriter}.
	 * <p> The <em>index</em> file has a format of <a href="http://en.wikipedia.org/wiki/XML">XML</a>. 
	 * Each <a href="http://en.wikipedia.org/wiki/XML">XML</a> element contains <em>currentTime</em>
	 * and <em>dataOffset</em> in its attributes. </p>
	 * @param currentTime is the time to be logged into the {@link RuntimeLogWriter#indexWriter}.
	 * @param dataOffset is the byte offset in the <em>data</em> file corresponding to the <em>currentTime</em>. 
	 * @throws IOException Thrown if there is a problem writing into the {@link RuntimeLogWriter#indexWriter}
	 * or flushing the {@link RuntimeLogWriter#indexWriter} or the {@link RuntimeLogWriter#dataWriter}.
	 */
	private void logIndex(long currentTime, long dataOffset) throws IOException
	{
		StringBuilder indexBuilder = new StringBuilder();

		indexBuilder.append("<").append(EVENT_RECORD_NAME)
				.append(" ").append(RECORD_TIME).append("=\"")
				.append(currentTime)
				.append("\" ").append(RECORD_OFFSET).append("=\"")
				.append(dataOffset).append("\" />\n");

		writer.flushData();
		writer.writeIndex(indexBuilder.toString(), currentTime);
		writer.flushIndex();
	}
	
	/**
	 * Structure and transform the given <em>knowledge</em> into a <a href="http://en.wikipedia.org/wiki/XML">XML</a>
	 * representation. The <a href="http://en.wikipedia.org/wiki/XML">XML</a> knowledge representation is appended
	 * into the given <em>builder</em>. The internal pieces of the <em>knowledge</em> are allowed to be one of the
	 * following form:
	 * <ul>
	 * <li>{@link Iterable} - A collection of <em>knowledge</em> (e.g. {@link List}).</li>
	 * <li>{@link Map} - A collection of pairs <em>name</em> - <em>knowledge</em>.</li>
	 * <li>Value - A <em>knowledge</em> value that is directly transformed into a string.</li>
	 * </ul>
	 * @param builder is the target where the <a href="http://en.wikipedia.org/wiki/XML">XML</a> representation
	 * of <em>knowledge</em> is appended.
	 * @param knowledge contains the data that will be transformed.
	 * @throws IllegalArgumentException Thrown if either the <em>builder</em> or the <em>knowledge</em>
	 * argument is null.
	 */
	@SuppressWarnings("unchecked")
	private void writeKnowledge(StringBuilder builder, Object knowledge)
	{
		if(builder == null) throw new IllegalArgumentException(String.format(
				"The argument \"%s\" is null.", "builder"));
		if(knowledge == null) throw new IllegalArgumentException(String.format(
				"The argument \"%s\" is null.", "knowledge"));
		
		if(knowledge instanceof Iterable)
		{
			Iterable<Object> collection = (Iterable<Object>) knowledge;
			for(Object item : collection)
			{
				builder.append("<").append(COLLECION_VALUE_ENCLOSURE).append(">");
				writeKnowledge(builder, item);
				builder.append("</").append(COLLECION_VALUE_ENCLOSURE).append(">");
			}
		}
		else if(knowledge instanceof Map)
		{
			Map<String, Object> map = (Map<String, Object>) knowledge;
			for(String valueKey : map.keySet())
			{
				builder.append("<")
					.append(valueKey)
					.append(">");
				writeKnowledge(builder, map.get(valueKey));
				builder.append("</")
					.append(valueKey)
					.append(">");
			}
		}
		else
		{
			builder.append(knowledge.toString());
		}
	}

	/**
	 * Creates a {@link Task} for the given <em>snapshotProvider</em>
	 * This {@link Task} will periodically record the snapshots
	 * using the {@link SnapshotProvider#getSnapshot()} method.
	 * The <em>snapshotType</em> provided by the given <em>snapshotProvider</em>
	 * is registered in the {@link RuntimeLogWriter#snapshotTypes} and the
	 * {@link RuntimeLogger#log(RuntimeLogRecord)} method handles such records as snapshots.
	 * @param snapshotProvider is the {@link SnapshotProvider} that will be registered. 
	 * @param period specifies the period for the repetitive invocation of the given <em>snapshotProvider</em>.
	 * @throws IOException Thrown if the {@link RuntimeLogWriter#snapshotPeriodWriter} is unable to be
	 * written into.
	 * @throws IllegalArgumentException Thrown if one of the following occurs:
	 * <ul>
	 * <li>The <em>snapshotProvider</em> argument is null.</li>
	 * <li>The <em>period</em> is less or equal to 0.</li>
	 * <li>The <em>snapshotProvider</em> argument returns null when its {@link SnapshotProvider#getRecordClass()}
	 * method is called.</li>
	 * </ul>
	 */
	public void registerSnapshotProvider(
			SnapshotProvider snapshotProvider, long period) throws IOException {
		if (snapshotProvider == null)
			throw new IllegalArgumentException(String.format(
					"The argument \"%s\" is null.", "snapshotProvider"));
		if(snapshotProvider.getRecordClass() == null) throw new IllegalArgumentException(
				String.format("The %s method invoked on the %s argument returns null.", "getRecordClass()", "snapshotProvider"));
		
		// Register task for the snapshot provider
		RuntimeLogTimerTaskListener slttListener = new RuntimeLogTimerTaskListener(snapshotProvider, period);
		scheduler.addTask(new CustomStepTask(scheduler,	slttListener, period));
		
		registerSnapshotPeriod(period, snapshotProvider.getRecordClass());
	}

	/**
	 * Writes the given <em>time</em> into the {@link RuntimeLogWriter#snapshotPeriodWriter}.
	 * The given <em>snapshotType</em> is registered in the {@link RuntimeLogger#snapshotTypes}
	 * and the {@link RuntimeLogger#log(RuntimeLogRecord)} method handles such records as snapshots.
	 * @param period specifies the time offset to be remembered as a <em>back log offset</em>.
	 * @throws IOException Thrown if the {@link RuntimeLogWriter#snapshotPeriodWriter} is unable to be
	 * written into.
	 * @throws IllegalArgumentException Thrown if the <em>time</em> is less or equal to 0
	 * or the <em>snapshotType</em> argument is null.
	 */
	public void registerSnapshotPeriod(long period, Class<? extends RuntimeLogRecord> snapshotType) throws IOException {
		if (period <= 0) throw new IllegalArgumentException(String.format(
					"The argument \"%s\" has to be greater than 0.", "time"));
		if(snapshotType == null) throw new IllegalArgumentException(String.format(
				"The argument \"%s\" is null.", "snapshotType")); 
		// Register task for the snapshot provider
		writer.writeSnapshotPeriod(String.format("%d %s\n", period, snapshotType.getCanonicalName()));
		writer.flushSnapshotPeriod();
		
		snapshotTypes.add(snapshotType);
	}

	/**
	 * Flushes the following {@link Writer}s:
	 * <ul>
	 * 	<li>{@link RuntimeLogWriter#DATA_FILE}</li>
	 * 	<li>{@link RuntimeLogWriter#DATA_INDEX_FILE}</li>
	 * 	<li>{@link RuntimeLogWriter#SNAPSHOT_PERIOD_FILE}</li>
	 * </ul>
	 * @throws IOException Thrown if any of the {@link Writer}s cannot be flushed.
	 */
	public void flush() throws IOException {
		if (writer != null) {
			writer.flushData();
			writer.flushIndex();
			writer.flushSnapshotPeriod();
		}
	}

	/**
	 * Closes the following {@link Writer}s:
	 * <ul>
	 * 	<li>{@link RuntimeLogWriter#DATA_FILE}</li>
	 * 	<li>{@link RuntimeLogWriter#DATA_INDEX_FILE}</li>
	 * 	<li>{@link RuntimeLogWriter#SNAPSHOT_PERIOD_FILE}</li>
	 * </ul>
	 * @throws IOException Thrown if any of the {@link Writer}s cannot be closed.
	 */
	public void close() throws IOException {
		if (writer != null) {
			writer.closeWriters();
		}
	}

	/**
	 * The {@link TimerTaskListener} for the {@link SnapshotProvider}s registered
	 * on {@link RuntimeLogger}. The {@link RuntimeLogTimerTaskListener} invokes
	 * the {@link RuntimeLogger#logSnapshot(RuntimeLogRecord)} method with the
	 * snapshot provided by a {@link SnapshotProvider}, when being fired
	 * by the {@link Scheduler}. After processing the snapshot the
	 * {@link Task} corresponding to the
	 * {@link TimerTaskListener} is being planned again for the new period cycle.
	 * 
	 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
	 */
	private class RuntimeLogTimerTaskListener implements TimerTaskListener 
	{
		/**
		 * The {@link SnapshotProvider} for which the {@link TimerTaskListener} instance
		 * is being fired by the {@link Scheduler}.
		 */
		private SnapshotProvider snapshotProvider;

		/**
		 * The period of the {@link Task} that handles the {@link TimerTaskListener} instance.
		 */
		private long period;
		
		/**
		 * Constructs an instance of the {@link RuntimeLogTimerTaskListener} for the
		 * given <em>snapshotProvider</em> with the given <em>period</em>.
		 * @param snapshotProvider is the {@link SnapshotProvider} that will be periodically asked for a snapshot.
		 * @param period specifies the period in which will be the <em>snapshotProvider</em> invoked.
		 */
		public RuntimeLogTimerTaskListener(SnapshotProvider snapshotProvider, long period)
		{
			this.snapshotProvider = snapshotProvider;
			this.period = period;
		}
		
		/**
		 * Logs the snapshot from the {@link RuntimeLogTimerTaskListener#snapshotProvider}
		 * using the {@link RuntimeLogger}. After the snapshot is processed the {@link Task}
		 * is planned for the next period.
		 */
		@Override
		public void at(long time, Object triger)
		{
			try
			{
				RuntimeLogRecord snapshot = snapshotProvider.getSnapshot();
				log(snapshot);
			}
			catch(IOException e)
			{
				// If the runtime logging fails interrupt the simulation
				throw new RuntimeException(e);
			}
			
			CustomStepTask task = (CustomStepTask) triger;
			task.scheduleNextExecutionAfter(period);

		}
	}
}