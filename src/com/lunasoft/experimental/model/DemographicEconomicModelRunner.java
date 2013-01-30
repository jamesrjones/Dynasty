package com.lunasoft.experimental.model;

public class DemographicEconomicModelRunner extends SingleCellModelRunner<DemographicState, DemographicEconomicModel> {

	private DemographicEconomicModelRunner(DemographicEconomicModel model) {
		super(model);
	}

	@Override
	protected int getMaxIterations() {
		return 20;
	}

	public static void main(String[] args) {
		DemographicState state = new DemographicState(
				new double[] {100.0, 0.0},
				new double[] {100.0, 0.0},
				100.0
				);
		DemographicEconomicModel model = new DemographicEconomicModel();
		model.initialize(state);
		DemographicEconomicModelRunner runner = new DemographicEconomicModelRunner(model);
		runner.run();
	}
}
