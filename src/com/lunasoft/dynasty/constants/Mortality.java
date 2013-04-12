package com.lunasoft.dynasty.constants;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;

/**
 * Stores base mortality rates for various causes of death, organized by age. A
 * variety of factors can adjust base mortality.
 */
public class Mortality {

	public final ImmutableRangeMap<Integer, Double> BASE_ACCIDENT_MORTALITY
			= new ImmutableRangeMap.Builder<Integer, Double>()
			.put(Range.<Integer>closedOpen(0, 1), 0.1)
			.put(Range.<Integer>closedOpen(1, 5), 0.05)
			.put(Range.<Integer>closedOpen(5, 15), 0.02)
			.put(Range.<Integer>closedOpen(15, 45), 0.01)
			.put(Range.<Integer>closedOpen(45, 65), 0.01)
			.put(Range.<Integer>atLeast(65), 0.02)
			.build();

	public final ImmutableRangeMap<Integer, Double> BASE_INFECTIOUS_DISEASE_MORTALITY
			= new ImmutableRangeMap.Builder<Integer, Double>()
			.put(Range.<Integer>closedOpen(0, 1), 0.1)
			.put(Range.<Integer>closedOpen(1, 5), 0.05)
			.put(Range.<Integer>closedOpen(5, 15), 0.05)
			.put(Range.<Integer>closedOpen(15, 45), 0.05)
			.put(Range.<Integer>closedOpen(45, 65), 0.05)
			.put(Range.<Integer>atLeast(65), 0.05)
			.build();
}
