package com.lunasoft.dynasty.tools.mapeditor.icons;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import com.lunasoft.common.math.geometry.AffineTransformation2D;

public class Forest extends BaseIcon {

	@Override
	public void draw(GC gc, double x, double y, double scale) {
		gc.setForeground(new Color(gc.getDevice(), 50, 50, 50));
		gc.setBackground(new Color(gc.getDevice(), 0, 200, 0));
		AffineTransformation2D transform = new AffineTransformation2D(scale, x, y);
		for (int i = 0; i < 10; i++) {
			double xrand = Math.random();
			double yrand = Math.random();
			double x0 = transform.transformX(xrand);
					//(int) (xrand * scale + x);
			double x1 = transform.transformX(xrand + 0.2);
			double y0 = transform.transformY(yrand);
			double y1 = transform.transformY(yrand + 0.2);
			double y2 = transform.transformY(yrand + 0.3);
			double cx = (x0 + x1) / 2;
			gc.drawLine((int) cx, (int) y1, (int) cx, (int) y2);
			gc.fillOval((int) x0, (int) y0, (int) x1 - (int) x0, (int) y1 - (int) y0);
		}
	}

}
