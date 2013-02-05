package com.lunasoft.experimental.model;

import com.google.common.base.Objects;

class PopulationOnlyState implements CellState {
	private final double population;
	private static final double POPULATION_LIMIT = 1000.0;

	public PopulationOnlyState(double population) {
		this.population = population;
	}

	public double getPopulation() {
		return population;
	}

	public double getPopulationLimit() {
		return POPULATION_LIMIT;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("population", population)
				.toString();
	}
}