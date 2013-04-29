package com.lunasoft.dynasty.tools.mapeditor.icons;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

public class Grassland extends BaseIcon {

	@Override
	public void draw(GC gc, double x, double y, double scale) {
		Color color = new Color(gc.getDevice(), 0, 200, 0);
		gc.setForeground(color);
		for (int i = 0; i < 50; i++) {
			double xrand = Math.random();
			double yrand = Math.random();
			int x0 = (int) (xrand * scale + x);
			int y0 = (int) (yrand * scale + y);
			int x1 = (int) ((xrand + 0.02) * scale + x);
			int y1 = (int) ((yrand - 0.1) * scale + y);
			gc.drawLine(x0, y0, x1, y1);
		}
	}

}
