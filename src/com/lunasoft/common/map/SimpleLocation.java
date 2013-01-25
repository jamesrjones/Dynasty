package com.lunasoft.common.map;

import com.google.common.base.Objects;

public class SimpleLocation implements Location {

	private final int x;
	private final int y;

	SimpleLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SimpleLocation other = (SimpleLocation) obj;
		if (x != other.x || y != other.y) {
			return false;
		}
		return true;
	}

}
