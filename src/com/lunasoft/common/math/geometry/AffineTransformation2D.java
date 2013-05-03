package com.lunasoft.common.math.geometry;

// TODO: could this be better expressed as a Function<Point, Point>?
public class AffineTransformation2D {

	private final double scale;
	private final double translateX;
	private final double translateY;

	public AffineTransformation2D(double scale, double translateX, double translateY) {
		this.scale = scale;
		this.translateX = translateX;
		this.translateY = translateY;
	}

	public double transformX(double input) {
		return scale * input + translateX;
	}

	public double transformY(double input) {
		return scale * input + translateY;
	}
}
