package com.lunasoft.experimental.model;

import java.util.Arrays;

import com.google.common.base.Objects;

class DemographicState implements CellState {

	public static final int MAX_AGE = 100;
	private final double[] malePopulation;
	private final double totalMalePopulation;
	private final double[] femalePopulation;
	private final double totalFemalePopulation;
	private final double food;

	public DemographicState(double[] malePopulation, double[] femalePopulation, double food) {
		this.malePopulation = malePopulation;
		this.femalePopulation = femalePopulation;
		this.totalMalePopulation = sumArray(malePopulation);
		this.totalFemalePopulation = sumArray(femalePopulation);
		this.food = food;
	}

	public double[] getMalePopulation() {
		return malePopulation;
	}

	public double[] getFemalePopulation() {
		return femalePopulation;
	}

	private double sumArray(double[] array) {
		double sum = 0.0;
		for (double elem : array) {
			sum += elem;
		}
		return sum;
	}

	public double getTotalMalePopulation() {
		return totalMalePopulation;
	}

	public double getTotalFemalePopulation() {
		return totalFemalePopulation;
	}

	public double getFood() {
		return food;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("totalMalePopulation", totalMalePopulation)
				.add("totalFemalePopulation", totalFemalePopulation)
				.add("food", food)
				.add("malePopulation", Arrays.toString(malePopulation))
				.add("femalePopulation", Arrays.toString(femalePopulation))
				.toString();
	}
}
