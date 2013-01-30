package com.lunasoft.experimental.model;

public abstract class SingleCellModel<S extends CellState> {

	protected S currentState;

	public void initialize(S state) {
		currentState = state;
	}
	public S getCurrentState() {
		return currentState;
	}
	public abstract void step();
}
