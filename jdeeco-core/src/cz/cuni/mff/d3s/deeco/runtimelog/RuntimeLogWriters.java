package cz.cuni.mff.d3s.deeco.runtimelog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import cz.cuni.mff.d3s.deeco.logging.Log;

/**
 * This class wraps the {@link Writer}s used by the {@link RuntimeLogger}.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class RuntimeLogWriters {

	/**
	 * The default directory where the logging files will be created. 
	 */
	static final String DEFAULT_LOG_PATH = "logs/runtime";

	/**
	 * Specifies the default name of the file where the logging records of the
	 * runtime events are written.
	 */
	static final String DEFAULT_DATA_FILE_PATH = "/runtimeData.xml";

	/**
	 * Specifies the default name of the file where the index of the runtime log
	 * is stored.
	 */
	static final String DEFAULT_INDEX_FILE_PATH = "/dataIndex.xml";

	/**
	 * Specifies the default name of the file where the back log time offsets
	 * are written.
	 */
	static final String DEFAULT_PERIOD_FILE_PATH = "/snapshotPeriodTable.xml";

	/**
	 * The encoding used to write into each log file. the number of bytes
	 * written for each character depends on the selected encoding.
	 */
	static final String CHARSET_NAME = "UTF-8";

	/**
	 * The default directory where the log files are placed.
	 */
	final String logPath;
	
	/**
	 * This {@link Writer} is used to write into the file for the runtime events
	 * being logged.
	 * <p>
	 * Internal to the {@link cz.cuni.mff.d3s.deeco.runtimelog} package.
	 * </p>
	 */
	final BufferedWriter dataWriter;
	/**
	 * This {@link Writer} is used to write into the file where the index of the
	 * log records is stored.
	 * <p>
	 * Internal to the {@link cz.cuni.mff.d3s.deeco.runtimelog} package.
	 * </p>
	 */
	final BufferedWriter indexWriter;
	/**
	 * This {@link Writer} serves for writing the time offsets for backward
	 * jumps in the main log file.
	 * <p>
	 * Internal to the {@link cz.cuni.mff.d3s.deeco.runtimelog} package.
	 * </p>
	 * 
	 * @see RuntimeLogger
	 */
	final BufferedWriter snapshotPeriodWriter;

	/**
	 * Create a new instance of {@link RuntimeLogWriters} with default path and writers.
	 *
	 * @throws IOException
	 *             Thrown if there is a problem using the writers.
	 */
	public RuntimeLogWriters() throws IOException {
		this.logPath = DEFAULT_LOG_PATH;
		createLogDir();
		dataWriter = new BufferedWriter(openStream(new File(logPath + DEFAULT_DATA_FILE_PATH)));
		indexWriter = new BufferedWriter(new BufferedWriter(openStream(new File(logPath + DEFAULT_INDEX_FILE_PATH))));
		snapshotPeriodWriter = new BufferedWriter(openStream(new File(logPath + DEFAULT_PERIOD_FILE_PATH)));
	}
	
	/**
	 * Create a new instance of {@link RuntimeLogWriters} with given writers.
	 * Given writers are wrapped to {@link BufferedWriter} instances.
	 *
	 * @param logPath
	 *        The directory where the logging files will be created.
	 * @throws IOException
	 *             Thrown if there is a problem using the writers.
	 * @throws IllegalArgumentException
	 *             Thrown if any of the given argument is null.
	 */
	public RuntimeLogWriters(String logPath) throws IOException {
		if (logPath == null)
			throw new IllegalArgumentException(String.format("The argument \"%s\" is null.", "logPath"));

		this.logPath = logPath;
		createLogDir();
		dataWriter = new BufferedWriter(openStream(new File(logPath + DEFAULT_DATA_FILE_PATH)));
		indexWriter = new BufferedWriter(new BufferedWriter(openStream(new File(logPath + DEFAULT_INDEX_FILE_PATH))));
		snapshotPeriodWriter = new BufferedWriter(openStream(new File(logPath + DEFAULT_PERIOD_FILE_PATH)));
	}
	
	/**
	 * Create a new instance of {@link RuntimeLogWriters} with given writers.
	 * Given writers are wrapped to {@link BufferedWriter} instances.
	 *
	 * @param logPath
	 *        The directory where the logging files will be created.
	 * @param dataOut
	 *            The writer for data.
	 * @param indexOut
	 *            The writer for index.
	 * @param periodOut
	 *            The writer for snapshot periods.
	 * @throws IOException
	 *             Thrown if there is a problem using the writers.
	 * @throws IllegalArgumentException
	 *             Thrown if any of the given argument is null.
	 */
	public RuntimeLogWriters(String logPath, Writer dataOut, Writer indexOut, Writer periodOut) throws IOException {
		if (logPath == null)
			throw new IllegalArgumentException(String.format("The argument \"%s\" is null.", "logPath"));
		if (dataOut == null)
			throw new IllegalArgumentException(String.format("The argument \"%s\" is null.", "dataOut"));
		if (indexOut == null)
			throw new IllegalArgumentException(String.format("The argument \"%s\" is null.", "indexOut"));
		if (periodOut == null)
			throw new IllegalArgumentException(String.format("The argument \"%s\" is null.", "periodOut"));

		this.logPath = logPath;
		createLogDir();
		dataWriter = new BufferedWriter(dataOut);
		indexWriter = new BufferedWriter(indexOut);
		snapshotPeriodWriter = new BufferedWriter(periodOut);
	}
	
	/**
	 * Create a new instance of {@link RuntimeLogWriters} with given writers.
	 * Given writers are wrapped to {@link BufferedWriter} instances.
	 * 
	 * @param dataOut
	 *            The writer for data.
	 * @param indexOut
	 *            The writer for index.
	 * @param periodOut
	 *            The writer for snapshot periods.
	 * @throws IOException
	 *             Thrown if there is a problem using the writers.
	 * @throws IllegalArgumentException Thrown if any of the given argument is null.
	 */
	public RuntimeLogWriters(Writer dataOut, Writer indexOut, Writer periodOut) throws IOException {
		if (dataOut == null)
			throw new IllegalArgumentException(String.format("The argument \"%s\" is null.", "dataOut"));
		if (indexOut == null)
			throw new IllegalArgumentException(String.format("The argument \"%s\" is null.", "indexOut"));
		if (periodOut == null)
			throw new IllegalArgumentException(String.format("The argument \"%s\" is null.", "periodOut"));

		logPath = DEFAULT_LOG_PATH;
		createLogDir();
		dataWriter = new BufferedWriter(dataOut);
		indexWriter = new BufferedWriter(indexOut);
		snapshotPeriodWriter = new BufferedWriter(periodOut);
	}

	/**
	 * Close the {@link #dataWriter}, {@link #indexWriter} and the
	 * {@link #snapshotPeriodWriter}.
	 * <p>
	 * Internal to the {@link cz.cuni.mff.d3s.deeco.runtimelog} package.
	 * </p>
	 */
	void close() throws IOException {
		dataWriter.close();
		indexWriter.close();
		snapshotPeriodWriter.close();
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
	
	// Check whether the directory for log files exists and create it if needed
	private void createLogDir() {	
		File logDirectory = new File(logPath);
		if (!logDirectory.exists() || !logDirectory.isDirectory()) {
			logDirectory.mkdirs();
		}
	}

}
