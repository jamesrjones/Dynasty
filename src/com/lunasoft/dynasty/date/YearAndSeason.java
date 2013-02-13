package com.lunasoft.dynasty.date;

import com.google.common.base.Objects;

public class YearAndSeason {

	private final int year;
	private final Season season;

	public YearAndSeason(int year, Season season) {
		this.year = year;
		this.season = season;
	}

	public int getYear() {
		return year;
	}

	public Season getSeason() {
		return season;
	}

	public int getCanonicalTurnNumber() {
		return Season.values().length * year + season.getIndex();
	}

	public static YearAndSeason fromCanonicalTurnNumber(int canonical) {
		int year = canonical / 4;
		Season season = Season.fromIndex(canonical % 4);
		return new YearAndSeason(year, season);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(year, season);
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
		YearAndSeason other = (YearAndSeason) obj;
		return Objects.equal(year, other.year)
				&& Objects.equal(season, other.season);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("year", year)
				.add("season", season)
				.toString();
	}
}
