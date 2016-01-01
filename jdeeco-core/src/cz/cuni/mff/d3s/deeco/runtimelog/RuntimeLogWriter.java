package cz.cuni.mff.d3s.deeco.runtimelog;

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
	 * This {@link Writer} is used to write into the files for the runtime logs.
	 */
	private RuntimeLogWriters writers;

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
	 * The counter of references pointing to the instance of {@link RuntimeLogWriter}
	 */
	private int referenceCounter;

	/**
	 * Provides the default instance of {@link RuntimeLogWriter}.
	 * 
	 * @return The default instance of {@link RuntimeLogWriter}.
	 * @throws IOException
	 *             Thrown if there is a problem opening the writers.
	 */
	public static synchronized RuntimeLogWriter getDefaultWriter() throws IOException {
		if (INSTANCE == null) {
			INSTANCE = new RuntimeLogWriter(new RuntimeLogWriters());
		} else {
			INSTANCE.referenceCounter++;
		}
		return INSTANCE;
	}

	/**
	 * Create a new instance of {@link RuntimeLogWriter} with given writers.
	 * 
	 * @param writers
	 *            The writer for the runtime data.
	 * @throws IOException  Thrown if there is a problem using the writers.
	 * @throws IllegalArgumentException Thrown if the writers argument is null.
	 */
	public RuntimeLogWriter(RuntimeLogWriters writers) throws IOException {
		if (writers == null)
			throw new IllegalArgumentException(String.format("The argument \"%s\" is null.", "writers"));

		this.writers = writers;

		currentDataOffset = 0;
		lastIndexTime = 0;
		
		referenceCounter = 1;
		
		writeStartElement();
	}
	
	private void writeStartElement() throws IOException {
		writeData(String.format("<%s>\n", ROOT_ELEMENT_NAME));
	}
	
	private void writeEndElement() throws IOException {
		writeData(String.format("</%s>\n", ROOT_ELEMENT_NAME));
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
			writers.dataWriter.write(entry);
			currentDataOffset += entry.getBytes(RuntimeLogWriters.CHARSET_NAME).length;
		} catch (IOException e) {
			Log.e("Failed to write to the log file " + writers.logPath + RuntimeLogWriters.DEFAULT_DATA_FILE_PATH, e);
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
		writers.dataWriter.flush();
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
			writers.indexWriter.write(entry);
			lastIndexTime = currentTime;
		} catch (IOException e) {
			Log.e("Failed to write to the log file " + writers.logPath + RuntimeLogWriters.DEFAULT_INDEX_FILE_PATH, e);
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
		writers.indexWriter.flush();
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
			writers.snapshotPeriodWriter.write(entry);
		} catch (IOException e) {
			Log.e("Failed to write to the log file " + writers.logPath + RuntimeLogWriters.DEFAULT_PERIOD_FILE_PATH, e);
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
		writers.snapshotPeriodWriter.flush();
	}

	/**
	 * Close the data, index and snapshot period writers.
	 * 
	 * @throws IOException
	 *             Thrown if either the data, index or snapshot period writer
	 *             cannot be closed.
	 */
	public synchronized void closeWriters() throws IOException {
		referenceCounter--;
		if(referenceCounter > 0){
			return;
		}
		writeEndElement();
		writers.close();
	}
}
