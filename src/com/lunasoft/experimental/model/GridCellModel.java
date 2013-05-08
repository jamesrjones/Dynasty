package com.lunasoft.experimental.model;

import com.google.common.base.Preconditions;

public abstract class GridCellModel<S extends CellState> {

	protected final int width;
	protected final int height;
	protected S[][] currentState;

	public GridCellModel(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void initialize(int i, int j, S state) {
		Preconditions.checkArgument(i >= 0);
		Preconditions.checkArgument(j >= 0);
		Preconditions.checkArgument(i < width);
		Preconditions.checkArgument(j < height);
		currentState[i][j] = state;
	}

	public S[][] getCurrentState() {
		return currentState;
	}

	public abstract void step();
}
