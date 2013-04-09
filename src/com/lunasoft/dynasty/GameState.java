package com.lunasoft.dynasty;

public class GameState {

	private final ImmutableState immutableState;
	private MutableState mutableState;
	private final Plans plans;

	public GameState(ImmutableState immutableState, MutableState mutableState, Plans plans) {
		this.immutableState = immutableState;
		this.mutableState = mutableState;
		this.plans = plans;
	}

	public ImmutableState getImmutableState() {
		return immutableState;
	}

	public MutableState getMutableState() {
		return mutableState;
	}

	public void updateMutableState(MutableState mutableState) {
		this.mutableState = mutableState;
	}

	public Plans getPlans() {
		return plans;
	}
}