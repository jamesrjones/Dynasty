package com.lunasoft.dynasty.date;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class YearAndSeasonTest {

	@Test
	public void testGetCanonicalTurnNumber() {
		assertEquals(0, new YearAndSeason(0, Season.fromIndex(0)).getCanonicalTurnNumber());
		assertEquals(1, new YearAndSeason(0, Season.fromIndex(1)).getCanonicalTurnNumber());
		assertEquals(1000 * Season.values().length,
				new YearAndSeason(1000, Season.fromIndex(0)).getCanonicalTurnNumber());
	}

	@Test
	public void testFromCanonicalTurnNumber() {
		assertEquals(new YearAndSeason(0, Season.fromIndex(0)),
				YearAndSeason.fromCanonicalTurnNumber(0));
		assertEquals(new YearAndSeason(0, Season.fromIndex(1)),
				YearAndSeason.fromCanonicalTurnNumber(1));
	}
}
