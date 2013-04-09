package com.lunasoft.dynasty;

public enum Condition {

	BLINDNESS,
	POOR_SIGHT,
	DEAFNESS,
	POOR_HEARING,
	SPEECH_IMPEDIMENT,
	LOST_ARM(2),
	LOST_LEG(2);

	private final int maxMultiplicity;
	private Condition() {
		this(1);
	}
	private Condition(int maxMultiplicity) {
		this.maxMultiplicity = maxMultiplicity;
	}
}
