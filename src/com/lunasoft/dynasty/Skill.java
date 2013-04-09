package com.lunasoft.dynasty;

public interface Skill {

	public enum Type {
		ADMINISTRATION,
		COMBAT,
		DIPLOMACY,
		LEADERSHIP,
		PERSUASION,
		TACTICS;
	}

	public Type getType();
	public double getValue();
}
