package com.lunasoft.experimental.model;

public class SingleCellModelRunner<S extends CellState, T extends SingleCellModel<S>> {

	private final T singleCellModel;

	public SingleCellModelRunner(T singleCellModel) {
		this.singleCellModel = singleCellModel;
	}

	protected int getMaxIterations() {
		return 100;
	}

	public void run() {
		int numIterations = 0;
		System.out.println("**** INITIAL STATE ****");
		System.out.println(singleCellModel.getCurrentState());

		while (numIterations < getMaxIterations()) {
			singleCellModel.step();
			++numIterations;
			System.out.println("STATE at TIME = " + numIterations);
			System.out.println(singleCellModel.getCurrentState());
		}
	}
}
