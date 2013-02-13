package com.lunasoft.dynasty.impl;

import java.util.Map;

import org.joda.time.LocalDate;
import org.joda.time.MonthDay;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.lunasoft.dynasty.ImmutableState;
import com.lunasoft.dynasty.date.Season;
import com.lunasoft.dynasty.date.YearAndSeason;

/**
 * Implementation of ImmutableState for European settings.
 */
public class ImmutableStateImpl implements ImmutableState {

	private static final Map<Season, MonthDay> FIRST_DATE_OF_SEASON_MAP
			= ImmutableMap.<Season, MonthDay>builder()
			.put(Season.SPRING, new MonthDay(3, 21))
			.put(Season.SUMMER, new MonthDay(6, 21))
			.put(Season.FALL, new MonthDay(9, 21))
			.put(Season.WINTER, new MonthDay(12, 21))
			.build();
	private final int startCanonicalTurnNumber;

	ImmutableStateImpl(YearAndSeason startYearAndSeason) {
		this.startCanonicalTurnNumber = startYearAndSeason.getCanonicalTurnNumber();
	}

	@Override
	public LocalDate getDateAtStartOfTurn(int turn) {
		Preconditions.checkArgument(turn >= 0);
		YearAndSeason yearAndSeason = YearAndSeason.fromCanonicalTurnNumber(
				startCanonicalTurnNumber + turn);
		return FIRST_DATE_OF_SEASON_MAP.get(yearAndSeason.getSeason()).toLocalDate(
				yearAndSeason.getYear());
	}

	@Override
	public Integer getTurnNumberOnDate(LocalDate date) {
		MonthDay monthDay = new MonthDay(date.getMonthOfYear(), date.getDayOfMonth());
		Season theSeason = null;
		int yearAdjustment = 0;
		for (Season season : FIRST_DATE_OF_SEASON_MAP.keySet()) {
			if (monthDay.compareTo(FIRST_DATE_OF_SEASON_MAP.get(season)) < 0) {
				theSeason = season.previous();
				if (season == Season.SPRING) {
					yearAdjustment = -1;
				}
				break;
			}
		}
		Preconditions.checkNotNull(theSeason);
		int canonical = new YearAndSeason(date.getYear() + yearAdjustment, theSeason)
				.getCanonicalTurnNumber();
		int actual = canonical - startCanonicalTurnNumber;
		if (actual < 0) {
			return null;
		}
		return actual;
	}
}
