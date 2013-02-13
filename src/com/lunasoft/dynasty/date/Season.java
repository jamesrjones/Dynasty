package com.lunasoft.dynasty.date;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

public enum Season {

	SPRING(0),
	SUMMER(1),
	FALL(2),
	WINTER(3);

	private final int index;
	private static final Map<Integer, Season> INDEX_TO_SEASON_MAP;

	static {
		ImmutableMap.Builder<Integer, Season> builder = ImmutableMap.<Integer, Season>builder();
		for (Season season : Season.values()) {
			builder.put(season.getIndex(), season);
		}
		INDEX_TO_SEASON_MAP = builder.build();
	}

	Season(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public Season previous() {
		int prevIndex = index - 1;
		if (prevIndex < 0) {
			prevIndex = Season.values().length - 1;
		}
		return Season.fromIndex(prevIndex);
	}

	public Season next() {
		int nextIndex = index + 1;
		if (nextIndex > Season.values().length - 1) {
			nextIndex = 0;
		}
		return Season.fromIndex(nextIndex);
	}

	public static Season fromIndex(int index) {
		Preconditions.checkArgument(index >= 0 && index < Season.values().length);
		return INDEX_TO_SEASON_MAP.get(index);
	}
}
