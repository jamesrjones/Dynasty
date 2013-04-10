package com.lunasoft.dynasty.tools.mapeditor;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DemHexifierTest {

	@Test
	public void testDemHexifier() {
		DemHexifier hexifier = new DemHexifier(new short[100][100],
				HexDimensions.withCircumRadius(10));
		GameMap gameMap = hexifier.hexify();
		assertTrue(gameMap.getWidth() >= 5);
		assertTrue(gameMap.getHeight() >= 5);
	}
}
