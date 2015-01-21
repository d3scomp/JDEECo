package cz.cuni.mff.d3s.deeco.integrity;

import cz.cuni.mff.d3s.deeco.annotations.RatingsProcess;

/**
 * Possible states of knowledge data. Components can use {@link RatingsProcess} and {@link RatingsHolder} to assign
 * any of these values to a knowledge path.
 * @author Ondřej Štumpf
 *
 */
public enum PathRating {
	OK,
	OUT_OF_RANGE,
	UNUSUAL
}
