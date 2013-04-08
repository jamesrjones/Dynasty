package com.lunasoft.dynasty.tools.mapeditor;

class HexDimensions {
	private final double inRadius;
	private final double circumRadius; // also equal to side length

	private static final double IN_TO_CIRCUM_RATIO = Math.sqrt(3.0) / 2.0;

	private HexDimensions(double inRadius, double circumRadius) {
		this.inRadius = inRadius;
		this.circumRadius = circumRadius;
	}

	static HexDimensions withInRadius(double inRadius) {
		HexDimensions hd = new HexDimensions(inRadius, inRadius / IN_TO_CIRCUM_RATIO);
		return hd;
	}

	static HexDimensions withCircumRadius(double circumRadius) {
		HexDimensions hd = new HexDimensions(circumRadius * IN_TO_CIRCUM_RATIO, circumRadius);
		return hd;
	}

	public double getInRadius() {
		return inRadius;
	}

	public double getCircumRadius() {
		return circumRadius;
	}
}