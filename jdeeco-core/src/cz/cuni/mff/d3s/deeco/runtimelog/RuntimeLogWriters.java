package cz.cuni.mff.d3s.deeco.runtimelog;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * This class wraps the {@link Writer}s used by the {@link RuntimeLogger}.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class RuntimeLogWriters {

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

}
