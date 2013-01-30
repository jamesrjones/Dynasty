package com.lunasoft.experimental.model;

import com.google.common.base.Objects;

class SimpleState implements CellState {
	private final double population;
	private final double food;

	public SimpleState(double population, double food) {
		this.population = population;
		this.food = food;
	}

	public double getPopulation() {
		return population;
	}

	public double getFood() {
		return food;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("population", population)
				.add("food", food)
				.toString();
	}
}