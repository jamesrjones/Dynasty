package com.lunasoft.experimental.model;

import java.util.Random;

public class SimpleEconomicModel extends SingleCellModel<SimpleState> {
	// In this model, each cell is independent, so we only model a single cell for
	// experimentation.
	// The change from time T to T+1 is given by:
	// P(T+1) = P(T) * m(F(T), P(T))
	// F(T+1) = max(F(T) - P(T) + g(P(T)), 0)

	private static final Random RANDOM = new Random();

	@Override
	public void step() {
		double newFood = currentState.getFood() - currentState.getPopulation()
				+ foodProduction(currentState);
		double maxFood = 1.2 * currentState.getPopulation();
		if (newFood < 0.0) {
			newFood = 0.0;
		} else if (newFood > maxFood) {
			newFood = maxFood;
		}
		double newPopulation = currentState.getPopulation() * growthRate(currentState, newFood);
		currentState = new SimpleState(newPopulation, newFood);
	}

	private double growthRate(SimpleState state, double food) {
		double foodRatio = food / state.getPopulation();
		return 0.9 + 0.11 * Math.min(foodRatio, 2.0);
	}

	private double foodProduction(SimpleState state) {
		final double alpha = 2.7;
		final double beta = 0.001;
		final double sigma = 0.2;
		double p = state.getPopulation();
		double rand = Math.exp(sigma * RANDOM.nextGaussian());
		return rand * p * alpha * Math.exp(-beta * p);
	}
}
