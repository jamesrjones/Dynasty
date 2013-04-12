package com.lunasoft.dynasty.common;

import java.util.Random;

public class Randoms {

	// TODO: make this an injectable RNG?
	private static final Random RANDOM = new Random();

	private Randoms() {
		// prevent instantiation
	}

	/** Returns a Gaussian variate with the given std deviation and mean. */
	public static double nextGaussian(double stdev, double mean) {
		return stdev * RANDOM.nextGaussian() + mean;
	}
}
