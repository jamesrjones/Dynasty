package com.lunasoft.dynasty.tools.mapeditor;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.google.common.base.Preconditions;

/**
 * Utility methods for {@link Rectangle}s.
 *
 * <p>Will almost certainly be promoted to a common package later on.
 */
public class Rectangles {

	public static ScaledRectangle sizeToFit(Rectangle original, Rectangle bounds) {
		Preconditions.checkArgument(!original.isEmpty());
		Preconditions.checkArgument(!bounds.isEmpty());
		double scaleX = bounds.width / (double) original.width;
		double scaleY = bounds.height / (double) original.height;
		double scale = Math.min(scaleX, scaleY);
		Rectangle scaled = new Rectangle(bounds.x, bounds.y,
				(int) (original.width * scale), (int) (original.height * scale));
		return new ScaledRectangle(scaled, scale);
	}

	public static Point getOrigin(Rectangle rect) {
		return new Point(rect.x, rect.y);
	}

	public static class ScaledRectangle {
		private final Rectangle rectangle;
		private final double scale;

		public ScaledRectangle(Rectangle rectangle, double scale) {
			this.rectangle = rectangle;
			this.scale = scale;
		}

		public Rectangle getRectangle() {
			return rectangle;
		}

		public double getScale() {
			return scale;
		}
	}
}
