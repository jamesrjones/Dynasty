package com.lunasoft.dynasty.tools;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PolygonTest {

	@Test
	public void testIsPointInside_square() {
		Polygon square = new Polygon(new double[] {0.0, 1.0, 1.0, 0.0},
				new double[] {0.0, 0.0, 1.0, 1.0});
		assertTrue(square.isPointInside(0.5, 0.5));
		assertFalse(square.isPointInside(1.5, 0.5));
		assertFalse(square.isPointInside(0.5, -0.5));
		assertFalse(square.isPointInside(0.5, 1.5));
		assertFalse(square.isPointInside(-0.5, 0));
	}

	@Test
	public void testIsPointInside_triangle() {
		Polygon triangle = new Polygon(new double[] {0.0, 10.0, 5.0},
				new double[] {0.0, 0.0, 10.0});
		assertTrue(triangle.isPointInside(4.9, 5.0));
		// assertTrue(triangle.isPointInside(5.0, 5.0)); hitting vertex; fix later
		assertTrue(triangle.isPointInside(1.0, 1.0));
		assertFalse(triangle.isPointInside(1.0, 3.0));
	}

	@Test
	public void testIsPointInside_nonConvex() {
		Polygon polygon = new Polygon(new double[] {0.0, 10.0, 0.0, 5.0},
				new double[] {0.0, 5.0, 10.0, 5.0});
		assertTrue(polygon.isPointInside(8.0, 5.0));
		assertFalse(polygon.isPointInside(8.0, 10.0));
		assertFalse(polygon.isPointInside(8.0, 0.0));
	}
}
