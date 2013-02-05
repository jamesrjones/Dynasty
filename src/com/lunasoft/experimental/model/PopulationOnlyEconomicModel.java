package com.lunasoft.experimental.model;

import java.util.Random;

public class PopulationOnlyEconomicModel extends SingleCellModel<PopulationOnlyState> {
	// In this model, each cell is independent, so we only model a single cell for
	// experimentation.
	// The change from time T to T+1 is given by:
	// P(T+1) = P(T) * g(P(T))

	private static final Random RANDOM = new Random();

	/** The ratio of pop to max pop below which population growth is considered "unconstrainted." */
	private static final double UNCONSTRAINED_GROWTH_MAX_RATIO = 0.7;

	/** Mean annual growth rate when unconstrained. */
	private static final double UNCONSTRAINED_MEAN_GROWTH_RATE = 0.02;

	/** Sigma of annual growth rate. (Taken so that p(N(MEAN, SIGMA) < -0.15) ~ 1/200.) */
	private static final double SIGMA_GROWTH_RATE = 0.055;

	@Override
	public void step() {
		double newPopulation = currentState.getPopulation() * Math.exp(growthRate(currentState));
		currentState = new PopulationOnlyState(newPopulation);
	}

	private double growthRate(PopulationOnlyState state) {
		double ratio = state.getPopulation() / state.getPopulationLimit();
		double variate = RANDOM.nextGaussian();
		if (ratio < UNCONSTRAINED_GROWTH_MAX_RATIO) {
			return transform(variate);
		}
		double constrainedMean = (1 - ratio) / (1 - UNCONSTRAINED_GROWTH_MAX_RATIO)
				* UNCONSTRAINED_MEAN_GROWTH_RATE;
		return transform(variate, constrainedMean);
	}

	private double transform(double variate) {
		return transform(variate, UNCONSTRAINED_MEAN_GROWTH_RATE);
	}

	private double transform(double variate, double mean) {
		return mean + SIGMA_GROWTH_RATE * variate;
	}
}
