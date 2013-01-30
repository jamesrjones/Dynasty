package com.lunasoft.experimental.model;

public class SimpleEconomicModelRunner extends SingleCellModelRunner<SimpleState, SimpleEconomicModel> {

	private SimpleEconomicModelRunner(SimpleEconomicModel model) {
		super(model);
	}

	public static void main(String[] args) {
		SimpleState state = new SimpleState(1000, 100);
		SimpleEconomicModel model = new SimpleEconomicModel();
		model.initialize(state);
		SimpleEconomicModelRunner runner = new SimpleEconomicModelRunner(model);
		runner.run();
	}
}
