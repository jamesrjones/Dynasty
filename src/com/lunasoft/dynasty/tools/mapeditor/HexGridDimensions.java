package com.lunasoft.dynasty.tools.mapeditor;

import org.eclipse.swt.graphics.Point;

public class HexGridDimensions {

	private final int width;
	private final int height;
	private final HexDimensions hexDimensions;

	private static final double DIAGONAL_SLOPE = 1 / Math.sqrt(3.0);

	public HexGridDimensions(int width, int height, HexDimensions hexDimensions) {
		this.width = width;
		this.height = height;
		this.hexDimensions = hexDimensions;
	}

	public double[] getCenter(int i, int j) {
		double cx = (2 * i + 1) * hexDimensions.getInRadius();
		double cy = (1.5 * j + 1) * hexDimensions.getCircumRadius();
		if (j % 2 == 1) {
			cx += hexDimensions.getInRadius();
		}
		return new double[] {cx, cy};
	}

	public double getTotalWidth() {
		return (2 * width + 1) * hexDimensions.getInRadius();
	}

	public double getTotalHeight() {
		return (1.5 * height + 0.5) * hexDimensions.getCircumRadius();
	}

	public HexDimensions getHexDimensions() {
		return hexDimensions;
	}

	public static double getDiagonalSlope() {
		return DIAGONAL_SLOPE;
	}

	public Point getHexContaining(double x, double y) {
		double gridWidth = hexDimensions.getInRadius();
		double gridHeight = hexDimensions.getCircumRadius() / 2;
		int iGrid = (int) (x / gridWidth);
		int jGrid = (int) (y / gridHeight);
		double iGridX = x - iGrid * gridWidth;
		double iGridY = y - jGrid * gridHeight;
		int i = -1;
		int j = -1;
		if (jGrid % 3 == 0) {
			int jMod6 = jGrid % 6;
			int iMod2 = iGrid % 2;
			boolean isBelow = false;
			if ((jMod6 == 0 && iMod2 == 0) || (jMod6 == 3 && iMod2 == 1)) {
				// indicates a diagonal like this: /
				isBelow = iGridY > gridHeight - iGridX * HexGridDimensions.getDiagonalSlope();
			} else {
				// indicates a diagonal like this: \
				isBelow = iGridY > iGridX * HexGridDimensions.getDiagonalSlope();
			}
			if (isBelow) {
				j = jGrid / 3;
			} else {
				j = jGrid / 3 - 1;
			}
		} else {
			j = jGrid / 3;
		}
		if (j % 2 == 0) {
			i = iGrid / 2;
		} else {
			i = (iGrid - 1) / 2;
		}

		if (i < 0 || i >= width || j < 0 || j >= height) {
			return null;
		}
		return new Point(i, j);
	}
}
