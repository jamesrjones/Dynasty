package com.lunasoft.dynasty;

public interface Attribute {

	public enum Type {
		STRENGTH,
		HEALTH,
		INTELLIGENCE,
		APPEARANCE,
		CHARISMA;
	}

	public Type getType();
	public double getValue();
}
