package com.lunasoft.dynasty;

/**
 * A relationship between exactly two people.
 */
public interface Relationship {
	public Person getFirst();
	public Person getSecond();
	public Type getType();

	public interface Type {}
}
