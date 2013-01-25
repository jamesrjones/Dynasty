package com.lunasoft.common.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public abstract class BaseNorthSouthHexMapTest extends BaseHexMapTest {

	@Test
	public void testGetNeighbor_fromMiddleEvenColumn() {
		HexMap<Void> hexMap = getHexMap();

		assertTrue(hexMap.getWidth() > 12);
		assertTrue(hexMap.getHeight() > 6);

		Location location = new SimpleLocation(10, 5);
		assertEquals(new SimpleLocation(10, 4), hexMap.getNeighbor(location, Direction.NORTH));
		assertEquals(new SimpleLocation(11, 5), hexMap.getNeighbor(location, Direction.NORTHEAST));
		assertEquals(new SimpleLocation(9, 5), hexMap.getNeighbor(location, Direction.NORTHWEST));
		assertEquals(new SimpleLocation(10, 6), hexMap.getNeighbor(location, Direction.SOUTH));
		assertEquals(new SimpleLocation(11, 6), hexMap.getNeighbor(location, Direction.SOUTHEAST));
		assertEquals(new SimpleLocation(9, 6), hexMap.getNeighbor(location, Direction.SOUTHWEST));
	}

	@Test
	public void testGetNeighbor_fromMiddleOddColumn() {
		HexMap<Void> hexMap = getHexMap();

		assertTrue(hexMap.getWidth() > 12);
		assertTrue(hexMap.getHeight() > 6);

		Location location = new SimpleLocation(9, 5);
		assertEquals(new SimpleLocation(9, 4), hexMap.getNeighbor(location, Direction.NORTH));
		assertEquals(new SimpleLocation(10, 4), hexMap.getNeighbor(location, Direction.NORTHEAST));
		assertEquals(new SimpleLocation(8, 4), hexMap.getNeighbor(location, Direction.NORTHWEST));
		assertEquals(new SimpleLocation(9, 6), hexMap.getNeighbor(location, Direction.SOUTH));
		assertEquals(new SimpleLocation(10, 5), hexMap.getNeighbor(location, Direction.SOUTHEAST));
		assertEquals(new SimpleLocation(8, 5), hexMap.getNeighbor(location, Direction.SOUTHWEST));
	}
}
