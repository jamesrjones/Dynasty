package com.lunasoft.dynasty;

/**
 * A Region is a set of {@link Location}s that form a single unit. There is a
 * hierarchy of Regions; TBD there may be multiple such hierarchies (e.g. one
 * secular one religious).
 */
public interface Region {

	public boolean containsLocation(Location location);

	/** Returns the name of this Region. */
	public String getName();

	/** Returns the parent of this Region. Null if there isn't one. */
	public Region getParent();
}
