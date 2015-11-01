package cz.cuni.mff.d3s.deeco.runtimelog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;

/**
 * This class holds the runtime log writer for data, index and snapshot periods.
 * It can be instantiated with given output stream or a default instance with
 * predefined output streams can be obtained. Using the writers via this class
 * is thread safe.
 * <p>
 * This class is internal to the {@link cz.cuni.mff.d3s.deeco.runtimelog}
 * package.
 * </p>
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
class RuntimeLogWriter {

	/**
	 * The default directory where the log files are placed.
	 */
	private static final String LOG_DIRECTORY = "logs/runtime";
	/**
	 * Specifies the default file where the logging records of the runtime
	 * events are written. This file destination can be overridden using the
	 * {@link RuntimeLogger#init(CurrentTimeProvider, Scheduler, Writer, Writer, Writer)}
	 * method.
	 */
	private static final File DATA_FILE = new File(LOG_DIRECTORY + "/runtimeData.xml");
	/**
	 * Specifies the default file where the index of the runtime log is stored.
	 * This file destination can be overridden using the
	 * {@link RuntimeLogger#init(CurrentTimeProvider, Scheduler, Writer, Writer, Writer)}
	 * method.
	 */
	private static final File DATA_INDEX_FILE = new File(LOG_DIRECTORY + "/dataIndex.xml");
	/**
	 * Specifies the default file where the back log time offsets are written.
	 * This file destination can be overridden using the
	 * {@link RuntimeLogger#init(CurrentTimeProvider, Scheduler, Writer, Writer, Writer)}
	 * method.
	 */
	private static final File SNAPSHOT_PERIOD_FILE = new File(LOG_DIRECTORY + "/snapshotPeriodTable.xml");
	/**
	 * The encoding used to write into each log file. the number of bytes
	 * written for each character depends on the selected encoding.
	 */
	private static final String CHARSET_NAME = "UTF-8";

	/**
	 * This {@link Writer} is used to write into the file for the runtime events
	 * being logged.
	 */
	private BufferedWriter dataWriter;
	/**
	 * This {@link Writer} is used to write into the file where the index of the
	 * log records is stored.
	 */
	private BufferedWriter indexWriter;
	/**
	 * This {@link Writer} serves for writing the time offsets for backward
	 * jumps in the main log file.
	 * 
	 * @see RuntimeLogger
	 */
	private BufferedWriter snapshotPeriodWriter;

	/**
	 * The size of the data (number of bytes) so far being written into the
	 * {@link RuntimeLogger#dataWriter}.
	 */
	private long currentDataOffset;
	/**
	 * The time when the last record into the
	 * {@link RuntimeLogger#DATA_INDEX_FILE} was written.
	 */
	private long lastIndexTime;

	/**
	 * The name of the <a href="http://en.wikipedia.org/wiki/XML">XML</a> element
	 * used as the root in the data file.
	 */
	private static final String ROOT_ELEMENT_NAME = "events";

	/**
	 * The default instance of {@link RuntimeLogWriter}.
	 */
	private static RuntimeLogWriter INSTANCE = null;

	/**
	 * Provides the default instance of {@link RuntimeLogWriter}.
	 * 
	 * @return The default instance of {@link RuntimeLogWriter}.
	 * @throws IOException
	 *             Thrown if there is a problem opening the writers.
	 */
	public static synchronized RuntimeLogWriter getDefaultWriter() throws IOException {
		if (INSTANCE == null) {
			// Check whether the directory for log files exists and create it if
			// needed
			File logDirectory = new File(LOG_DIRECTORY);
			if (!logDirectory.exists() || !logDirectory.isDirectory()) {
				logDirectory.mkdirs();
			}

			Writer defaultDataWriter = openStream(DATA_FILE);
			Writer defaultIndexWriter = openStream(DATA_INDEX_FILE);
			Writer defaultSnapshotPeriodWriter = openStream(SNAPSHOT_PERIOD_FILE);

			INSTANCE = new RuntimeLogWriter(defaultDataWriter, defaultIndexWriter, defaultSnapshotPeriodWriter);
		}
		return INSTANCE;
	}

	/**
	 * Create a new instance of {@link RuntimeLogWriter} with given writers.
	 * 
	 * @param dataOut
	 *            The writer for data.
	 * @param indexOut
	 *            The writer for index.
	 * @param periodOut
	 *            The writer for snapshot periods.
	 * @throws IOException  Thrown if there is a problem using the writers.
	 */
	public RuntimeLogWriter(Writer dataOut, Writer indexOut, Writer periodOut) throws IOException {
		if (dataOut == null)
			throw new IllegalArgumentException(String.format("The argument \"%s\" is null.", "dataOut"));
		if (indexOut == null)
			throw new IllegalArgumentException(String.format("The argument \"%s\" is null.", "indexOut"));
		if (periodOut == null)
			throw new IllegalArgumentException(String.format("The argument \"%s\" is null.", "periodOut"));

		// Opening the files in init method to avoid IOException in the
		// constructor
		dataWriter = new BufferedWriter(dataOut);
		indexWriter = new BufferedWriter(indexOut);
		snapshotPeriodWriter = new BufferedWriter(periodOut);

		currentDataOffset = 0;
		lastIndexTime = 0;
		
		writeStartElement();
	}
	
	private void writeStartElement() throws IOException {
		dataWriter.write(String.format("<%s>\n", ROOT_ELEMENT_NAME));
	}
	
	private void writeEndElement() throws IOException {
		dataWriter.write(String.format("</%s>\n", ROOT_ELEMENT_NAME));
	}

	/**
	 * Provides an {@link Writer} for the given <em>file</em>. If there arise an
	 * {@link Exception} during the creating of the {@link Writer} it is being
	 * logged and propagated outwards from this method.
	 * 
	 * @param file
	 *            specifies the file to be used for the {@link Writer}.
	 * @return The {@link Writer} for the given <em>file</em>.
	 * @throws IOException
	 *             Thrown is the {@link Writer} cannot be opened for the given
	 *             <em>file</em>.
	 */
	private static Writer openStream(File file) throws IOException {
		try {
			return new OutputStreamWriter(new FileOutputStream(file), CHARSET_NAME);
		} catch (IOException e) {
			Log.e("Simulation logging not enabled. Failed to open the log file " + file.getAbsolutePath(), e);
			throw e;
		}
	}

	/**
	 * Provides the {@link #lastIndexTime} that was written into the index file.
	 * 
	 * @return The {@link #lastIndexTime} that was written into the index file.
	 */
	public long getLastIndexTime() {
		return lastIndexTime;
	}

	/**
	 * Provides the {@link #currentDataOffset} that indicates the data offset in
	 * the data file.
	 * 
	 * @return The {@link #currentDataOffset} that indicates the data offset in
	 *         the data file.
	 */
	public long getCurrentDataOffset() {
		return currentDataOffset;
	}

	/**
	 * Write data into the data file.
	 * 
	 * @param entry
	 *            The data to be written.
	 * @throws IOException
	 *             Thrown if the data cannot be written.
	 */
	public synchronized void writeData(String entry) throws IOException {
		try {
			dataWriter.write(entry);
			currentDataOffset += entry.getBytes(CHARSET_NAME).length;
		} catch (IOException e) {
			Log.e("Failed to write to the log file " + DATA_FILE.getAbsolutePath(), e);
			throw e;
		}
	}

	/**
	 * Flush the data file.
	 * 
	 * @throws IOException
	 *             Thrown if the data file cannot be flushed.
	 */
	public synchronized void flushData() throws IOException {
		dataWriter.flush();
	}

	/**
	 * Write index into the index file.
	 * 
	 * @param entry
	 *            The index to be written.
	 * @throws IOException
	 *             Thrown if the index cannot be written.
	 */
	public synchronized void writeIndex(String entry, long currentTime) throws IOException {
		try {
			indexWriter.write(entry);
			lastIndexTime = currentTime;
		} catch (IOException e) {
			Log.e("Failed to write to the log file " + DATA_INDEX_FILE.getAbsolutePath(), e);
			throw e;
		}
	}

	/**
	 * Flush the index file.
	 * 
	 * @throws IOException
	 *             Thrown if the index file cannot be flushed.
	 */
	public synchronized void flushIndex() throws IOException {
		indexWriter.flush();
	}

	/**
	 * Write snapshot period into the snapshot period file.
	 * 
	 * @param entry
	 *            The snapshot period to be written.
	 * @throws IOException
	 *             Thrown if the snapshot period cannot be written.
	 */
	public synchronized void writeSnapshotPeriod(String entry) throws IOException {
		try {
			snapshotPeriodWriter.write(entry);
		} catch (IOException e) {
			Log.e("Failed to write to the log file " + SNAPSHOT_PERIOD_FILE.getAbsolutePath(), e);
			throw e;
		}
	}

	/**
	 * Flush the snapshot period file.
	 * 
	 * @throws IOException
	 *             Thrown if the snapshot period file cannot be flushed.
	 */
	public synchronized void flushSnapshotPeriod() throws IOException {
		snapshotPeriodWriter.flush();
	}

	/**
	 * Close the data, index and snapshot period writers.
	 * 
	 * @throws IOException
	 *             Thrown if either the data, index or snapshot period writer
	 *             cannot be closed.
	 */
	public synchronized void closeWriters() throws IOException {
		if (dataWriter != null) {
			writeEndElement();
			dataWriter.close();
			dataWriter = null;
		}
		if (indexWriter != null) {
			indexWriter.close();
			indexWriter = null;
		}
		if (snapshotPeriodWriter != null) {
			snapshotPeriodWriter.close();
			snapshotPeriodWriter = null;
		}
	}

}
