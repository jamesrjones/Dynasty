package com.lunasoft.dynasty;

public interface IdGenerator {

	public long getNextIdForType(Type type);

	public enum Type {
		PERSON;
	}
}
