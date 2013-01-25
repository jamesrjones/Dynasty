package com.lunasoft.common.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NorthSouthNoWrapHexMapTest extends BaseNorthSouthHexMapTest {

	@Override
	protected HexMap<Void> getHexMap() {
		return new HexMap.Builder<Void>().build();
	}

	@Test
	public void testGetNeighbor_fromOrigin() {
		HexMap<Void> hexMap = getHexMap();
		Location location = new SimpleLocation(0, 0);
		assertNull(hexMap.getNeighbor(location, Direction.NORTH));
		assertEquals(new SimpleLocation(1, 0), hexMap.getNeighbor(location, Direction.NORTHEAST));
		assertNull(hexMap.getNeighbor(location, Direction.NORTHWEST));
		assertEquals(new SimpleLocation(0, 1), hexMap.getNeighbor(location, Direction.SOUTH));
		assertEquals(new SimpleLocation(1, 1), hexMap.getNeighbor(location, Direction.SOUTHEAST));
		assertNull(hexMap.getNeighbor(location, Direction.SOUTHWEST));
	}
}
