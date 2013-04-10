package com.lunasoft.dynasty.tools.mapeditor;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.graphics.Rectangle;
import org.junit.Test;

import com.lunasoft.dynasty.tools.mapeditor.Rectangles.ScaledRectangle;

public class RectanglesTest {

	@Test
	public void testSizeToFit_sameOrigin() {
		Rectangle original = new Rectangle(0, 0, 100, 100);
		Rectangle bounds = new Rectangle(0, 0, 100, 50);
		ScaledRectangle scaled = Rectangles.sizeToFit(original, bounds);
		Rectangle rect = scaled.getRectangle();
		assertEquals(rect, bounds.intersection(rect));
		assertEquals(scaled.getScale(), 0.5, 0.001);
	}

	@Test
	public void testSizeToFit_differentOrigin() {
		Rectangle original = new Rectangle(0, 0, 100, 100);
		Rectangle bounds = new Rectangle(10, 10, 100, 50);
		ScaledRectangle scaled = Rectangles.sizeToFit(original, bounds);
		Rectangle rect = scaled.getRectangle();
		assertEquals(rect, bounds.intersection(rect));
		assertEquals(scaled.getScale(), 0.5, 0.001);
	}
}
