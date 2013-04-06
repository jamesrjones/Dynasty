package com.lunasoft.dynasty.tools;

import com.google.common.base.Preconditions;

/**
 * Represents a simple closed polygon, not necessarily convex, with the vertices
 * winding clockwise, i.e. the interior of the polygon will always be on the right
 * of an observer traversing the vertices in order.
 */
public class Polygon {

	private final double[] xs;
	private final double[] ys;
	private final double xmin;
	private final double ymin;
	private final double xmax;
	private final double ymax;

	public Polygon(double[] xs, double[] ys) {
		Preconditions.checkArgument(xs.length == ys.length);
		Preconditions.checkArgument(xs.length >= 3);
		this.xs = xs;
		this.ys = ys;
		double xmin_tmp = xs[0];
		double xmax_tmp = xs[0];
		double ymin_tmp = ys[0];
		double ymax_tmp = ys[0];
		for (int i = 1; i < xs.length; i++) {
			if (xs[i] < xmin_tmp) {
				xmin_tmp = xs[i];
			}
			if (xs[i] > xmax_tmp) {
				xmax_tmp = xs[i];
			}
			if (ys[i] < ymin_tmp) {
				ymin_tmp = ys[i];
			}
			if (ys[i] > ymax_tmp) {
				ymax_tmp = ys[i];
			}
		}
		this.xmin = xmin_tmp;
		this.xmax = xmax_tmp;
		this.ymin = ymin_tmp;
		this.ymax = ymax_tmp;
	}

	public boolean isPointInside(double x, double y) {
		if (x < xmin || x > xmax || y < ymin || y > ymax) {
			// outside of bounding box
			return false;
		}
		// draw ray from (x,y) to (x,+infinity) and see how many crossing there are
		// be naive for now: linear search for intersecting segments and don't
		// worry about vertex/degeneracy messiness...
		int numIntersections = 0;
		for (int i = 0; i < xs.length; i++) {
			int inext = i + 1;
			if (inext >= xs.length) {
				inext = 0;
			}
			if (!isBetween(x, xs[i], xs[inext])) {
				continue;
			}
			// test following two segments for intersection:
			//   A. segment from (xs[i], ys[i]) to (xs[inext], ys[inext])
			// formula is y-y1 = (y2-y1)/(x2-x1) (x-x1)
			//   B. segment from (x, y) to (x, ymax + 1.0)
			// plug in x, see if resulting y is in correct range

			double slope = (ys[inext] - ys[i]) / (xs[inext] - xs[i]);
			double ytest = slope * (x - xs[i]) + ys[i];
			if (ytest > y) {
				numIntersections++;
			}
		}
		return numIntersections % 2 == 1;
	}

	/**
	 * Returns true if d1 <= x <= d2 or d2 <= x <= d1.
	 */
	private boolean isBetween(double x, double d1, double d2) {
		if (x <= d1) {
			return d2 <= x;
		}
		return x <= d2;
	}
}
