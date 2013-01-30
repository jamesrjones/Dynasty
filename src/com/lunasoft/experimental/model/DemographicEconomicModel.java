package com.lunasoft.experimental.model;

import java.util.Random;

public class DemographicEconomicModel extends SingleCellModel<DemographicState> {

	private static final double SEX_RATIO = (105.0) / (105.0 + 100.0);
	private static final Random RANDOM = new Random();

	@Override
	public void step() {
		double totalPopulation = currentState.getTotalFemalePopulation()
				+ currentState.getTotalMalePopulation();
		double producedFood = foodProduction(currentState);
		double newFood = currentState.getFood() - totalPopulation + producedFood;
		double maxFood = 1.2 * totalPopulation;
		if (newFood < 0.0) {
			newFood = 0.0;
		} else if (newFood > maxFood) {
			newFood = maxFood;
		}

		// handle mortality
		double[] newMalePopulation = new double[DemographicState.MAX_AGE];
		for (int i = 0; i < currentState.getMalePopulation().length - 1; ++i) {
			newMalePopulation[i + 1] = currentState.getMalePopulation()[i]
					* maleSurvival(i, totalPopulation, newFood);
		}
		double[] newFemalePopulation = new double[DemographicState.MAX_AGE];
		for (int i = 0; i < currentState.getFemalePopulation().length - 1; ++i) {
			newFemalePopulation[i + 1] = currentState.getFemalePopulation()[i]
					* femaleSurvival(i, totalPopulation, newFood);
		}

		// handle fertility
		double totalBirths = 0.0;
		for (int i = 1; i < currentState.getFemalePopulation().length; ++i) {
			totalBirths += currentState.getFemalePopulation()[i - 1]
					* fertility(i - 1, totalPopulation, newFood);
		}
		newMalePopulation[0] = SEX_RATIO * totalBirths;
		newFemalePopulation[0] = (1 - SEX_RATIO) * totalBirths;

		currentState = new DemographicState(newMalePopulation, newFemalePopulation, newFood);
	}

	private double maleSurvival(int age, double population, double food) {
		// Multipliers derived from http://paa2012.princeton.edu/papers/122198
		double baseMaleSurvival = baseMaleSurvival(age, population);
		double mortalityMultiplier = 1.0;
		double ratio = food / population;
		if (ratio < 0.5) {
			mortalityMultiplier = 6.0;
		} else if (ratio < 1.0) {
			mortalityMultiplier = 1.1;
		}
		return 1 - mortalityMultiplier * (1 - baseMaleSurvival);
	}

	private double femaleSurvival(int age, double population, double food) {
		// Multipliers derived from http://paa2012.princeton.edu/papers/122198
		double baseFemaleSurvival = baseFemaleSurvival(age, population);
		double mortalityMultiplier = 1.0;
		double ratio = food / population;
		if (ratio < 0.5) {
			mortalityMultiplier = 5.0;
		} else if (ratio < 1.0) {
			mortalityMultiplier = 1.1;
		}
		return 1 - mortalityMultiplier * (1 - baseFemaleSurvival);
	}

	private double baseMaleSurvival(int age, double population) {
		// Data picked from http://www.infoplease.com/ipa/A0005140.html
		// Converted to ASDR by the formula:
	  // ASDR[t] = (e[t] / (10 + e[t+1])) ^ (1/10)
		// Has no particular relevance to Dynasty.
		if (age < 1) {
			return .7;
		} else if (age < 10) {
			return .9935;
		} else if (age < 20) {
			return .9957;
		} else if (age < 30) {
			return .9908;
		} else if (age < 40) {
			return .9892;
		} else if (age < 50) {
			return .9876;
		} else if (age < 60) {
			return .9832;
		} else if (age < 70) {
			return .9745;
		} else if (age < 80) {
			return .9566;
		} else if (age < DemographicState.MAX_AGE){
			return .78;
		}
		return 0.0;
	}

	private double baseFemaleSurvival(int age, double population) {
		// Data picked from http://www.infoplease.com/ipa/A0005140.html
		// Converted to ASDR by the formula:
	  // ASDR[t] = (e[t] / (10 + e[t+1])) ^ (1/10)
		// Has no particular relevance to Dynasty.
		if (age < 1) {
			return .75;
		} else if (age < 10) {
			return .9936;
		} else if (age < 20) {
			return .9939;
		} else if (age < 30) {
			return .9879;
		} else if (age < 40) {
			return .9884;
		} else if (age < 50) {
			return .9884;
		} else if (age < 60) {
			return .9862;
		} else if (age < 70) {
			return .9777;
		} else if (age < 80) {
			return .9634;
		} else if (age < DemographicState.MAX_AGE){
			return .8;
		}
		return 0.0;
	}

	private double fertility(int age, double population, double food) {
		// Data picked from http://www.cpc.unc.edu/measure/prh/rh_indicators/specific/fertility/age-specific-fertility-rates
		// Has no particular relevance to Dynasty.
		if (age < 15) {
			return 0.0;
		} else if (age < 20) {
			return .051;
		} else if (age < 25) {
			return .196;
		} else if (age < 30) {
			return .208;
		} else if (age < 35) {
			return .147;
		} else if (age < 40) {
			return .075;
		} else if (age < 45) {
			return .024;
		} else if (age < 50) {
			return .004;
		} else {
			return 0.0;
		}
	}

	private double foodProduction(DemographicState state) {
		final double alpha = 2.7;
		final double beta = 0.001;
		final double sigma = 0.2;
		double p = state.getTotalFemalePopulation() + state.getTotalMalePopulation()
				- getPopulationUnderAgeN(state, 12);
		double rand = Math.exp(sigma * RANDOM.nextGaussian());
		return rand * p * alpha * Math.exp(-beta * p);
	}

	private double getPopulationUnderAgeN(DemographicState state, int cutoffAge) {
		double sum = 0.0;
		for (int i = 0; i < cutoffAge && i < state.getFemalePopulation().length; i++) {
			sum += state.getFemalePopulation()[i];
		}
		for (int i = 0; i < cutoffAge && i < state.getMalePopulation().length; i++) {
			sum += state.getMalePopulation()[i];
		}
		return sum;
	}	
}
