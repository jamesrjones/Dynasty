package com.lunasoft.experimental.model;

public class PopulationOnlyEconomicModelRunner extends SingleCellModelRunner<PopulationOnlyState,
		PopulationOnlyEconomicModel> {

	private PopulationOnlyEconomicModelRunner(PopulationOnlyEconomicModel model) {
		super(model);
	}

	public static void main(String[] args) {
		PopulationOnlyState state = new PopulationOnlyState(500);
		PopulationOnlyEconomicModel model = new PopulationOnlyEconomicModel();
		model.initialize(state);
		PopulationOnlyEconomicModelRunner runner = new PopulationOnlyEconomicModelRunner(model);
		runner.run();
	}
}
