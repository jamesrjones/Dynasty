package com.lunasoft.experimental.model;

import java.util.Random;

import com.google.common.base.Preconditions;
import com.lunasoft.common.map.HexMap;
import com.lunasoft.common.map.Location;
import com.lunasoft.common.map.Path;
import com.lunasoft.common.map.SimpleLocation;

public class PopulationGridModel extends GridCellModel<PopulationOnlyState> {

	private static final Random RANDOM = new Random();

	/** The ratio of pop to max pop below which population growth is considered "unconstrainted." */
	private static final double UNCONSTRAINED_GROWTH_MAX_RATIO = 0.7;

	/** Mean annual growth rate when unconstrained. */
	private static final double UNCONSTRAINED_MEAN_GROWTH_RATE = 0.02;

	/** Sigma of annual growth rate. (Taken so that p(N(MEAN, SIGMA) < -0.15) ~ 1/200.) */
	private static final double SIGMA_GROWTH_RATE = 0.055;

	/**
	 * When a neighboring cell is closer to limit that the current one, population migrates
	 * from the neighbor to current. The migration pressure factor is the amount of migration
	 * that occurs. If it equals 1.0, then sufficient population migrates to bring the ratios
	 * to equality.
	 */
	private static final double MIGRATION_PRESSURE_FACTOR = 0.2;

	private final HexMap<Void> hexMap;

	public PopulationGridModel(HexMap<Void> hexMap) {
		super(hexMap.getWidth(), hexMap.getHeight());
		this.hexMap = hexMap;
	}

	@Override
	public void step() {
		double[][] newPopulation = new double[width][height];
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				newPopulation[i][j] = currentState[i][j].getPopulation()
						* Math.exp(growthRate(currentState[i][j]));
			}
		}
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				double thisRatio = currentState[i][j].getPopulation()
						/ currentState[i][j].getPopulationLimit();
				Path<Location> neighbors = hexMap.getNeighbors(new SimpleLocation(i, j));
				for (Location neighbor : neighbors) {
					// is there migration FROM the neighbor TO this cell? (migration the other way is
					// determined what the neighbor is visited)
					int ii = neighbor.getX();
					int jj = neighbor.getY();
					double thatPop = currentState[ii][jj].getPopulation();
					double thatLimit = currentState[ii][jj].getPopulationLimit();
					double thatRatio = thatPop / thatLimit;
					if (thatRatio > thisRatio) {
						double limitRatio = currentState[i][j].getPopulationLimit()	/ thatLimit; 
						double migration = currentState[i][j].getPopulation() * (1 - thisRatio / thatRatio)
								/ (1 + limitRatio) * MIGRATION_PRESSURE_FACTOR;
						Preconditions.checkArgument(migration > 0.0);
						Preconditions.checkArgument(migration < thatPop);
						newPopulation[i][j] += migration;
						newPopulation[ii][jj] -= migration;
					}
				}
			}
		}
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				currentState[i][j] = new PopulationOnlyState(newPopulation[i][j]);
			}
		}
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
