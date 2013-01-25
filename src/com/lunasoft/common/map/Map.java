package com.lunasoft.common.map;

public interface Map<T> {

	T get(Location location);
	Location getNeighbor(Location location, Direction direction);
	Path<Location> getNeighbors(Location location);
	boolean isWrapping();
	int getWidth();
	int getHeight();
}
