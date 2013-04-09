package com.lunasoft.dynasty;

public class MainLoop {

	// the current game state
	private final GameState gameState;

	// stepper that creates a new game state from an existing one
	private final Stepper stepper;

	MainLoop(GameState initialGameState, Stepper stepper) {
		this.gameState = initialGameState;
		this.stepper = stepper;
	}

	public void step() {
		MutableState mutableState = stepper.step(gameState);
		gameState.updateMutableState(mutableState);
		gameState.getPlans().clear();
	}
}
