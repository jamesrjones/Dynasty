package com.lunasoft.common.map;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class HexMap<T> implements Map<T> {
	public static enum Orientation {
		NORTH_SOUTH(Direction.NORTH, Direction.NORTHEAST, Direction.NORTHWEST,
				Direction.SOUTH, Direction.SOUTHWEST, Direction.SOUTHEAST),
		EAST_WEST(Direction.EAST, Direction.SOUTHEAST, Direction.SOUTHWEST,
				Direction.WEST, Direction.NORTHWEST, Direction.NORTHEAST);

		private final EnumSet<Direction> validDirections;

		private Orientation(Direction... validDirections) {
			this.validDirections = EnumSet.copyOf(Arrays.asList(validDirections));
		}

		public EnumSet<Direction> getValidDirections() {
			return validDirections;
		}
		public LocationCalculator buildLocationCalculator(int maxX, int maxY, boolean isWrapping) {
			//TODO: ugly, find a better way
			switch (this) {
			case NORTH_SOUTH:
				return new NorthSouthLocationCalculator(maxX, maxY, isWrapping);
			case EAST_WEST:
				return new EastWestLocationCalculator(maxX, maxY, isWrapping);
			}
			return null;
		}
	}

	private final Orientation orientation;
	private final boolean isWrapping;
	private final int width;
	private final int height;
	private final LocationCalculator locationCalculator;

	private HexMap(int width, int height, boolean isWrapping, Orientation orientation) {
		this.width = width;
		this.height = height;
		this.isWrapping = isWrapping;
		this.orientation = orientation;
		this.locationCalculator = orientation.buildLocationCalculator(width, height, isWrapping);
	}

	@Override
	public T get(Location location) {
		return null;
	}

	@Override
	public Location getNeighbor(Location location, Direction direction) {
		Preconditions.checkArgument(orientation.getValidDirections().contains(direction),
				String.format("Invalid direction %s for map orientation %s",
				direction, orientation));
		return locationCalculator.getNeighbor(location, direction);
	}

	@Override
	public Path<Location> getNeighbors(Location location) {
		List<Location> locations = Lists.newArrayList();
		for (Direction direction : orientation.getValidDirections()) {
			locations.add(getNeighbor(location, direction));
		}
		return new ListPath<Location>(locations);
	}

	@Override
	public boolean isWrapping() {
		return isWrapping;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	private static abstract class LocationCalculator {
		final int maxX;
		final int maxY;
		final boolean isWrapping;

		LocationCalculator(int maxX, int maxY, boolean isWrapping) {
			this.maxX = maxX;
			this.maxY = maxY;
			this.isWrapping = isWrapping;
		}

		abstract java.util.Map<Direction, Offsets> getOffsetsAt(Location location);

		Location getNeighbor(Location location, Direction direction) {
			Offsets offsets = getOffsetsAt(location).get(direction);
			int newX = location.getX() + offsets.x;
			int newY = location.getY() + offsets.y;
			if (newY < 0 || newY >= maxY) {
				return null;
			}
			if (newX < 0) {
				if (!isWrapping) {
					return null;
				} else {
					newX += maxX;
				}
			} else if (newX >= maxX) {
				if (!isWrapping) {
					return null;
				} else {
					newX -= maxX;
				}
			}
			return new SimpleLocation(newX, newY);
		}
	}

	private static final class NorthSouthLocationCalculator extends LocationCalculator {
		private static java.util.Map<Direction, Offsets> EVEN_COLUMN_OFFSETS
				= ImmutableMap.<Direction, Offsets>builder()
						.put(Direction.NORTH, new Offsets(0, -1))
						.put(Direction.NORTHEAST, new Offsets(1, 0))
						.put(Direction.SOUTHEAST, new Offsets(1, 1))
						.put(Direction.SOUTH, new Offsets(0, 1))
						.put(Direction.SOUTHWEST, new Offsets(-1, 1))
						.put(Direction.NORTHWEST, new Offsets(-1, 0))
						.build();
		private static java.util.Map<Direction, Offsets> ODD_COLUMN_OFFSETS
				= ImmutableMap.<Direction, Offsets>builder()
						.put(Direction.NORTH, new Offsets(0, -1))
						.put(Direction.NORTHEAST, new Offsets(1, -1))
						.put(Direction.SOUTHEAST, new Offsets(1, 0))
						.put(Direction.SOUTH, new Offsets(0, 1))
						.put(Direction.SOUTHWEST, new Offsets(-1, 0))
						.put(Direction.NORTHWEST, new Offsets(-1, -1))
						.build();

		NorthSouthLocationCalculator(int maxX, int maxY, boolean isWrapping) {
			super(maxX, maxY, isWrapping);
		}

		java.util.Map<Direction, Offsets> getOffsetsAt(Location location) {
			if ((location.getX() & 1) == 0) {
				return EVEN_COLUMN_OFFSETS;
			} else {
				return ODD_COLUMN_OFFSETS;
			}
		}
	}

	private static final class EastWestLocationCalculator extends LocationCalculator {
		private static java.util.Map<Direction, Offsets> EVEN_ROW_OFFSETS
				= ImmutableMap.<Direction, Offsets>builder()
						.put(Direction.NORTHEAST, new Offsets(1, -1))
						.put(Direction.EAST, new Offsets(1, 0))
						.put(Direction.SOUTHEAST, new Offsets(1, 1))
						.put(Direction.SOUTHWEST, new Offsets(0, 1))
						.put(Direction.WEST, new Offsets(-1, 0))
						.put(Direction.NORTHWEST, new Offsets(0, -1))
						.build();
		private static java.util.Map<Direction, Offsets> ODD_ROW_OFFSETS
				= ImmutableMap.<Direction, Offsets>builder()
						.put(Direction.NORTHEAST, new Offsets(0, -1))
						.put(Direction.EAST, new Offsets(1, 0))
						.put(Direction.SOUTHEAST, new Offsets(0, 1))
						.put(Direction.SOUTHWEST, new Offsets(-1, 1))
						.put(Direction.WEST, new Offsets(-1, 0))
						.put(Direction.NORTHWEST, new Offsets(-1, -1))
						.build();

		EastWestLocationCalculator(int maxX, int maxY, boolean isWrapping) {
			super(maxX, maxY, isWrapping);
		}

		java.util.Map<Direction, Offsets> getOffsetsAt(Location location) {
			if ((location.getY() & 1) == 0) {
				return EVEN_ROW_OFFSETS;
			} else {
				return ODD_ROW_OFFSETS;
			}
		}
	}

	private static class Offsets {
		final int x;
		final int y;
		Offsets(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public static class Builder<T> {
		private Orientation orientation = Orientation.NORTH_SOUTH;
		private boolean isWrapping = false;
		private int width = 20;
		private int height = 10;

		public Builder() {
		}

		public Builder<T> withWrapping() {
			isWrapping = true;
			return this;
		}

		public Builder<T> setWidth(int width) {
			this.width = width;
			return this;
		}

		public Builder<T> setHeight(int height) {
			this.height = height;
			return this;
		}

		public Builder<T> setOrientation(Orientation orientation) {
			this.orientation = orientation;
			return this;
		}

		public HexMap<T> build() {
			return new HexMap<T>(width, height, isWrapping, orientation);
		}
	}
}
