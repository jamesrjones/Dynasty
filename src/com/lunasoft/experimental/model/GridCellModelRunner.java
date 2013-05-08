package com.lunasoft.experimental.model;

public class GridCellModelRunner<S extends CellState, T extends GridCellModel<S>> {

	private final T gridCellModel;

	public GridCellModelRunner(T gridCellModel) {
		this.gridCellModel = gridCellModel;
	}

	protected int getMaxIterations() {
		return 10;
	}

	public void run() {
		int numIterations = 0;
		System.out.println("**** INITIAL STATE ****");
		System.out.println(gridCellModel.getCurrentState());

		while (numIterations < getMaxIterations()) {
			gridCellModel.step();
			++numIterations;
			System.out.println("STATE at TIME = " + numIterations);
			System.out.println(gridCellModel.getCurrentState());
		}
	}
}
