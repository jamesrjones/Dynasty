package com.lunasoft.dynasty.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.lunasoft.dynasty.ImmutableState;
import com.lunasoft.dynasty.date.Season;
import com.lunasoft.dynasty.date.YearAndSeason;

public class ImmutableStateImplTest {

	@Test
	public void testDateConversions() {
		testRoundTrip(new YearAndSeason(1000, Season.SPRING), 0, new LocalDate(1000, 3, 21),
				new LocalDate(1000, 3, 22));
	}

	private void testRoundTrip(YearAndSeason start, int turn, LocalDate dateAtStartOfTurn,
			LocalDate otherDate) {
		assertTrue(otherDate.compareTo(dateAtStartOfTurn) >= 0);
		ImmutableState state = new ImmutableStateImpl(start);
		LocalDate testDate = state.getDateAtStartOfTurn(turn);
		assertTrue(testDate.equals(dateAtStartOfTurn));
		assertEquals(Integer.valueOf(turn), state.getTurnNumberOnDate(dateAtStartOfTurn));
		assertEquals(Integer.valueOf(turn), state.getTurnNumberOnDate(otherDate));
	}
}
